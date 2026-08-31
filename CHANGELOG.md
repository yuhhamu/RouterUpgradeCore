# Changelog

このプロジェクトの変更点は [Keep a Changelog](https://keepachangelog.com/ja/1.0.0/) 形式で記録し、
[Semantic Versioning](https://semver.org/lang/ja/) (バージョニングルール参照) に従う。

## [Unreleased]

### Added

### Changed

### Fixed

- `addItemBeam`で追加されるビームの色を、アクティブなモードの`registerMode()`登録時`imageColor`へ無条件に上書きしていた挙動を削除。各`RouterModeProvider`実装がビームごとに指定した色がそのまま尊重されるようにした(FluidRouterUpgrade側で液体転送の方向ごとに個別の色を設定しても、常に単色になってしまう不具合の原因だった)。

## [0.1.0] - 2026-08-29

### Added

- `RouterModeProvider`登録APIを追加。姉妹アドオンMODがRouterのcapability公開・セーブ&ロード・ターゲット判定・モジュール実行処理へフックできるようにした。
- Vanilla `ModularRouterBlockEntity`への一連のMixin(capability・save/load・compileUpgrades等)を追加し、登録済みモードが無い場合はVanilla本来の動作を維持するようにした。
- `TargetedModule`のターゲット判定を緩和するフックを追加。
