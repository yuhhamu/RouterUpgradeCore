# Changelog

このプロジェクトの変更点は [Keep a Changelog](https://keepachangelog.com/ja/1.0.0/) 形式で記録し、
[Semantic Versioning](https://semver.org/lang/ja/) (バージョニングルール参照) に従う。

## [Unreleased]

### Added

### Changed

### Fixed

## [0.1.0] - 2026-08-29

### Added

- `RouterModeProvider`登録APIを追加。姉妹アドオンMODがRouterのcapability公開・セーブ&ロード・ターゲット判定・モジュール実行処理へフックできるようにした。
- Vanilla `ModularRouterBlockEntity`への一連のMixin(capability・save/load・compileUpgrades等)を追加し、登録済みモードが無い場合はVanilla本来の動作を維持するようにした。
- `TargetedModule`のターゲット判定を緩和するフックを追加。
