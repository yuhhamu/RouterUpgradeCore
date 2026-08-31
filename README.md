# Router Upgrade Core

> 本MODの開発にはAnthropicのAIアシスタント「Claude」を活用しています。

## これは何をするMODですか?

Router Upgrade Coreは、ModularRoutersのRouterブロックへ特殊な動作モードを後付けするための基盤MODです。専用のブロックを追加するのではなく、既存のRouterへ専用のUpgradeアイテムを挿入するだけで、そのRouterを特殊な転送モード(液体・気体の転送など)へ切り替えられるようにします。

本MOD単体では、目に見えるアイテムやブロック、挙動の変化は一切ありません。あくまで、具体的な転送モードを実装する姉妹アドオンMOD(下記参照)が動作するために必要な、共通基盤としての位置づけです。

## 必要なもの

- Minecraft: 1.20.1
- ModLoader: Forge 47.4.6以降
- 依存Mod:
  - Modular Routers — 必須

## 導入方法

1. Forgeを導入する。
2. Modular Routersを`mods`フォルダに配置する。
3. 本MODのjarファイルを`mods`フォルダに配置する。
4. 対応する姉妹アドオンMOD(下記参照)を少なくとも1つ`mods`フォルダに配置する(無いと見た目上何も変化しません)。

## 使い方

本MOD自体には操作画面や設定項目はありません。姉妹アドオンMODが提供するUpgradeアイテムをRouterへ挿入することで、そのRouterの動作モードが切り替わります。具体的な操作方法は各アドオンMODの説明をご覧ください。

## 対応アドオンMOD

- Fluid Router Upgrade — Forge Fluid(バケツ等)の転送に対応します。
- Chemical Router Upgrade — Mekanism Chemical(Gas)の転送に対応予定です(開発中)。

## 開発者向け

公開APIの詳細は [DEVELOPMENT.md](./DEVELOPMENT.md) を参照してください。

## 既知の制限

- 本MOD単体で導入しても見た目上の変化はありません。姉妹アドオンMODを併せて導入する必要があります。
- 1つのRouterに対して同時に有効化できる転送モードは1つのみです。

## クレジット

- 開発: yuuhamu

## ライセンス

本MODは MIT ライセンスの下で公開されています。詳細は [LICENSE](./LICENSE) を参照してください。

Copyright (c) 2026 yuuhamu
