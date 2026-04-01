# LC-HAX

Developer: NotAlley
License: GPL-3.0

---

## 中文说明

### 项目简介
LC-HAX 是一个基于 WeaveMC 的 1.8.9 客户端模组项目，当前已实现模块管理、命令系统、按键绑定、配置档保存与基础 HUD/BedWars 实用功能。

### 当前功能模块
- `prefix`：聊天前缀开关与文本设置
- `autoclicker`：自动左键（`minCPS` / `maxCPS` / `breakBlocks`）
- `rightclicker`：自动右键（`minCPS` / `maxCPS` / `startDelay`）
- `antibot`：反假人（`Hypixel` / `Universal`）
- `eagle`：边缘自动潜行（`blocksOnly` / `pitchCheck` / `pitchThreshold`）
- `nohitdelay`：移除攻击点击间隔
- `bedtracker`：床位追踪、敌人接近告警、HUD 显示
- `clickgui`：点击 GUI 模块入口

### 命令系统
命令默认以 `.` 开头（聊天输入后会被拦截执行，不会发到服务器）。

常用命令：
- `.help` / `.?`：查看帮助
- `.list`：查看模块状态
- `.t <module> [on/off]`：切换模块
- `.prefix [text...]`：设置前缀文本（前缀开关使用 `.t prefix`）
- `.<module> [property] [value...]`：直接查看/修改模块属性
- `.bind <module> <key> [-f]`：绑定按键（`-f` 为仅长按激活）
- `.bind -l`：查看所有绑定
- `.bind -u <module>`：取消模块绑定
- `.c save|load|list|delete [profile]`：管理配置档

### 配置与按键
- 旧版配置文件：`run/lchax.json`
- 新版配置目录：`run/.weave/mods/lchax`
- 按键绑定文件：`run/.weave/mods/lchax/keymaps/*.json`
- 首次启动若未绑定 `clickgui`，会默认绑定到 `RSHIFT`

### 构建与测试
要求：JDK 8（项目使用 Java Toolchain 8）

```bash
./gradlew build
./gradlew test
```

构建产物位于：

```text
build/libs/*.jar
```

---

## English

### Overview
LC-HAX is a WeaveMC 1.8.9 mod project with a working module system, in-chat command system, key binding manager, profile/config persistence, and utility features including HUD and BedWars tracking.

### Implemented Modules
- `prefix`: chat prefix toggle and text setting
- `autoclicker`: auto left-click (`minCPS` / `maxCPS` / `breakBlocks`)
- `rightclicker`: auto right-click (`minCPS` / `maxCPS` / `startDelay`)
- `antibot`: bot filtering (`Hypixel` / `Universal`)
- `eagle`: edge sneak assist (`blocksOnly` / `pitchCheck` / `pitchThreshold`)
- `nohitdelay`: removes attack click delay
- `bedtracker`: bed tracking, nearby enemy alerts, HUD output
- `clickgui`: Click GUI entry module

### Command System
Commands use `.` prefix by default and are intercepted client-side (not sent to server chat).

Common commands:
- `.help` / `.?`: show help
- `.list`: list module states
- `.t <module> [on/off]`: toggle module
- `.prefix [text...]`: set prefix text (toggle with `.t prefix`)
- `.<module> [property] [value...]`: inspect/update module properties directly
- `.bind <module> <key> [-f]`: bind key (`-f` means hold-to-enable)
- `.bind -l`: list all binds
- `.bind -u <module>`: unbind module
- `.c save|load|list|delete [profile]`: manage config profiles

### Config and Keymap Paths
- Legacy config file: `run/lchax.json`
- New config directory: `run/.weave/mods/lchax`
- Keymap files: `run/.weave/mods/lchax/keymaps/*.json`
- If no bind exists at first launch, `clickgui` is auto-bound to `RSHIFT`

### Build and Test
Requirement: JDK 8 (Java Toolchain 8)

```bash
./gradlew build
./gradlew test
```

Build artifacts:

```text
build/libs/*.jar
```
