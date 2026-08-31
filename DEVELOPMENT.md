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
