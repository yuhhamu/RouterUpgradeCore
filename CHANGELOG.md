# Changelog

このプロジェクトの変更点は [Keep a Changelog](https://keepachangelog.com/ja/1.0.0/) 形式で記録し、
[Semantic Versioning](https://semver.org/lang/ja/) (バージョニングルール参照) に従う。

## [Unreleased]

### Added

- `mode_upgrade_core`アイテムを追加(現時点では機能を持たないプレースホルダー)。
- `RouterUpgradeCore.markBeamNoPulse(BeamData)`APIを新規追加。指定したBeamDataについて、Vanilla本体`ModularRouterBER`が中心ビーム(BEAM_LINE_THICK)に常時適用している1秒周期のアルファ点滅を無効化し、常に最大値で一定描画されるようにする。姉妹アドオンMOD(FluidRouterUpgrade等)が、独自の周辺エフェクトで転送演出を行いたい場合にVanilla本体の点滅と重ならないよう用意した。
- `RouterUpgradeCore.reportBeamActive(router, beamKey, startAction, stopAction)`APIを新規追加。「稼働タイミング(executeModules呼び出し)ごとに輸送が継続しているか」を追跡し、新規開始時のみstartActionを呼び、継続中は表示を維持し、輸送が行われなかった稼働タイミングが来た時点で自動的にstopActionを呼ぶ。姉妹アドオンMODが、duration(表示時間)による自動失効に頼らず、稼働タイミングと厳密に同期した視覚効果を実装できるようにするためのもの。

### Changed

### Fixed

- `addItemBeam`で追加されるビームの色を、アクティブなモードの`registerMode()`登録時`imageColor`へ無条件に上書きしていた挙動を削除。各`RouterModeProvider`実装がビームごとに指定した色がそのまま尊重されるようにした(FluidRouterUpgrade側で液体転送の方向ごとに個別の色を設定しても、常に単色になってしまう不具合の原因だった)。
- ワールド再読み込み・リログイン直後にアクティブな`RouterModeProvider`を検出できず、`provider.load()`が呼ばれないまま(=永続化データが復元されないまま)になる不具合を修正。原因は、Upgrade装着判定にVanilla本体の`getUpgradeCount()`(次のtickで初めて構築される遅延キャッシュ)を使用していたため、`load()`直後の時点では常に0を返していたこと。`getUpgrades()`で生のUpgradeインベントリを直接走査する方式に変更した。

## [0.1.0] - 2026-08-29

### Added

- `RouterModeProvider`登録APIを追加。姉妹アドオンMODがRouterのcapability公開・セーブ&ロード・ターゲット判定・モジュール実行処理へフックできるようにした。
- Vanilla `ModularRouterBlockEntity`への一連のMixin(capability・save/load・compileUpgrades等)を追加し、登録済みモードが無い場合はVanilla本来の動作を維持するようにした。
- `TargetedModule`のターゲット判定を緩和するフックを追加。
