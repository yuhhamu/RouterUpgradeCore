# Router Upgrade Core

> This mod was developed with the help of Anthropic's AI assistant "Claude".

## What does this mod do?

Router Upgrade Core is a framework mod that lets a vanilla ModularRouters Router switch into special transfer modes (such as handling fluids or gases) without needing a dedicated custom block. Instead, a companion addon mod's marker Upgrade item is inserted into an existing Router to activate its mode. By itself, this mod adds no visible items, blocks, or behavior — it exists purely as a required dependency for addon mods that register their own transfer modes against it.

## Requirements

- Minecraft: 1.20.1
- ModLoader: Forge 47.4.6+
- Dependencies: Modular Routers (required)

## Installation

1. Install Forge.
2. Place the dependency mods above into your `mods` folder.
3. Place this mod's jar file into your `mods` folder.

## Known Limitations

- This mod alone has no visible effect. At least one addon mod (such as Fluid Router Upgrade) must be installed alongside it.
- Only one registered mode can be active on a given Router at a time.

## Credits

- Developed by yuuhamu

## License

This mod is licensed under MIT. See [LICENSE](https://github.com/yuhhamu/RouterUpgradeCore/blob/main/LICENSE).

Copyright (c) 2026 yuuhamu

---

# Router Upgrade Core

> 本MODの開発にはAnthropicのAIアシスタント「Claude」を活用しています。

## これは何をするMODですか?

Router Upgrade Coreは、VanillaのModularRoutersのRouterブロックが、専用のブロックを追加することなく特殊な転送モード(液体・気体の転送など)へ切り替えられるようにするフレームワークMODです。姉妹アドオンMODが提供するマーカーUpgradeアイテムを既存のRouterへ挿入するだけでモードが有効化されます。本MOD単体では見た目上のアイテム・ブロック・挙動の変化は一切なく、あくまで、独自の転送モードを登録する姉妹アドオンMODのための必須の共通基盤です。

## 必要なもの

- Minecraft: 1.20.1
- ModLoader: Forge 47.4.6以降
- 依存Mod: Modular Routers(必須)

## 導入方法

1. Forgeを導入する。
2. 上記の依存Modを`mods`フォルダに配置する。
3. 本MODのjarファイルを`mods`フォルダに配置する。

## 既知の制限

- 本MOD単体で導入しても見た目上の変化はありません。少なくとも1つの姉妹アドオンMOD(Fluid Router Upgrade等)を併せて導入する必要があります。
- 1つのRouterに対して同時に有効化できる転送モードは1つのみです。

## クレジット

- 開発: yuuhamu

## ライセンス

本MODは MIT ライセンスの下で公開されています。詳細は [LICENSE](https://github.com/yuhhamu/RouterUpgradeCore/blob/main/LICENSE) を参照してください。

Copyright (c) 2026 yuuhamu
