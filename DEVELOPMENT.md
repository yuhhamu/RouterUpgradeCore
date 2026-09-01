# DEVELOPMENT.md

Router Upgrade Coreの内部設計と、各Mixin/APIクラスの役割をまとめた実装ノートです。ソースコード中に設計判断の経緯コメントを置かない方針(コメント除去・コピー元明記ポリシー)のため、代わりに本ファイルへ集約しています。

## 全体設計

Vanilla `ModularRouterBlockEntity`は`AttachCapabilitiesEvent`では後付けのcapabilityを一切公開できない(`getCapability()`が常に自前のバケツアダプタで無条件に上書きするため)ことが実デコンパイルで確認済みのため、Mixin注入による直接介入が必須。

`com.yuuhamu.routerupgradecore.api.RouterModeProvider`が姉妹アドオンMOD向けの公開インターフェースで、`RouterUpgradeCore.registerMode(markerUpgradeItem, provider, imageColor)`で登録する。`ModeRegistry`(internal)がマーカーUpgradeアイテムとproviderの対応を保持し、Routerの`upgradesHandler`に登録済みマーカーが1つでも入っていれば該当providerを「アクティブ」として扱う。

## APIクラス

- `RouterModeProvider` — capability公開・NBTセーブ&ロード・ターゲット判定緩和・モジュール実行処理をフックするためのインターフェース。
- `RouterUpgradeCore` — `registerMode`/`getImageColor`の公開エントリポイント。
- `RouterVisualBlock` — Router本体の見た目色を提供するための補助インターフェース。
- `ModuleKind` — PULLER/SENDER/DISTRIBUTOR/VOIDの列挙。
- `ModuleTargeting` — Vanilla`CompiledModule`のprivateな`getTarget`/`getTargets`へ、`CompiledModuleAccessor`(Mixin `@Invoker`)経由でアクセスするためのヘルパー。

## Mixinクラス

- `ModularRouterBlockEntityMixin` — `getCapability`等へ注入し、アクティブなproviderがあればそちらへ処理を委譲する。
- `ModularRouterBlockEntityVanillaOverrideMixin` — `load`/`saveAdditional`等のNBTセーブ&ロードへ注入。
- `TargetedModuleMixin` — `isValidTarget`のHEADへ注入し、アクティブなproviderが緩和を許可した場合はブロック存在判定のみに差し替える。
- `ModuleExecutionMixin` — Vanilla実シングルトンアイテム(PullerModule等)のcompiled module実行を、アクティブなproviderがあればそちらの処理へリダイレクトする。
- `UpgradeSlotValidationMixin` — `ModularRouterBlockEntity$UpgradeHandler#isItemValid`へ注入し、登録済みマーカーUpgradeの重複挿入を防ぐ。
- `ModularRouterScreenMixin` — Router本体のGUI(`ModularRouterScreen`)にモード切り替えUIを追加する。
- `ModularRouterBERMixin` / `ModularRouterCamouflageAccessor` — カモフラージュブロックのハイライト表示のみ実際のカモフラージュを参照させるための補助。
- `BufferHandlerMixin` — バッファスロットのアイテム受け入れ判定を補助。
- `CompiledModuleAccessor` — Vanilla`CompiledModule`のprivateメソッドへの`@Invoker`アクセサ。

## 依存関係

- Modular Routers 12.1.1(Modrinth配布)を`fg.deobf`経由で依存。
- FluidRouterUpgrade/ChemicalRouterUpgradeは、`.localRepo`(maven-publish)経由ではなく、本プロジェクトの通常ビルド成果物(`build/libs/routerupgradecore-${version}.jar`)を直接ファイル参照する方式で依存する。

## ビーム継続管理(BeamContinuityRegistry)

`internal.BeamContinuityRegistry`は、Routerの`executeModules()`呼び出し1回(稼働タイミング1回分)ごとに、視覚効果(ビーム等)を表す任意のキーが輸送成功として継続報告されたかを追跡する汎用レジストリ。Fluid/Chemical等の具体的な種別には関知しない。`ModuleExecutionMixin`が`executeModules`のHEAD/TAILから`beginTick`/`endTick`を呼び、その間に各`RouterModeProvider`実装が輸送成功のたびに`RouterUpgradeCore.reportBeamActive(router, beamKey, startAction, stopAction)`を呼ぶ。

- 直前の稼働タイミングまでそのキーが報告されていなかった場合のみ`startAction`を1回呼ぶ(新規開始)。
- 継続中は何もしない(表示をそのまま維持)。
- 直前まで継続していたキーが、次の稼働タイミングで報告されなかった時点(=輸送が行われなかった)で、自動的に`stopAction`を1回だけ呼ぶ。
- 状態はRouterの実体(`ModularRouterBlockEntity`)に対する`WeakHashMap`で保持するため、Router自体が破棄されればGCされる。ただし接続断・再接続をまたいでは持続するため、再接続直後のクライアントへ開始イベントが再送されない問題が起こりうる(FluidRouterUpgrade側の対策はそちらのDEVELOPMENT.md参照)。

## 中心ビーム点滅の無効化(BeamPulseRegistry)

Vanilla本体の`ModularRouterBER`は、ビームの種別を区別せず全ての`BeamData`に同じ1秒周期のアルファ点滅(`getGameTime()`基準のsin波、alpha 32〜160)を適用する。`RouterUpgradeCore.markBeamNoPulse(BeamData)`で登録したビームに限り、`ModularRouterBERMixin`が`renderBeamLine`の該当ローカル変数をModifyVariableで160(最大輝度)に固定し、点滅を無効化する。register対象は`BeamData`の参照そのものをキーにした`WeakHashMap`(`BeamPulseRegistry`)で管理し、Vanilla側のビームリストから失効・GCされれば自動的にエントリも消える。点滅演出自体は各具体実装側の周辺エフェクト(例: FluidRouterUpgradeのハローライン)に持たせる方針。

## ModeRegistryのマーカーアイテム検出とNBLロード直後のタイミング問題

`ModeRegistry.scanMarkerItem()`は`router.getUpgradeCount()`(Vanilla本体の`compileUpgrades()`が構築するキャッシュに依存)を使わず、`router.getUpgrades()`が返す生の`IItemHandler`のスロットを直接走査する。`compileUpgrades()`自体はNBTロード直後ではなく次のtickで初めて実行される(`recompileNeeded`フラグによる遅延コンパイル方式)ため、`load()`直後に`getUpgradeCount()`を参照すると常に0が返り、実際にはUpgradeが挿入済みでもアクティブなProviderが見つからず、リログイン直後にタンク等の状態復元が一切行われない不具合があった。生スロット走査にすることでこのタイミング依存を回避している。

## mode_upgrade_coreアイテムの追加(2026-09-01)

`ModItems.MODE_UPGRADE_CORE`(`mode_upgrade_core`)は、現時点では機能を持たないプレースホルダーアイテムとして追加した。`RouterUpgradeCore`自体はFluid/Chemical等の具体的なモードロジックを持たないフレームワーク本体であるため、`FluidRouterUpgrade`の`FluidModeUpgrade`のように`RouterUpgradeCore.registerMode()`へ紐づく実装は無く、`registry.ModItems`への登録・テクスチャ・モデル・lang整備のみを行っている。CreativeModeTabへの登録も行っていない(入手は`/give`等による)。将来的に用途が定まった場合はこのアイテムに機能を追加するか、別アイテムへ置き換える。
