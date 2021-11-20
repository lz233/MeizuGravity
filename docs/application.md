---
lang: zh-CN title: 应用
---

# Meizu Gravity +

Meizu Gravity + 可以在你使用 DLNA 串流时显示歌词。另外也包含一些小功能。

## 部署

1. 下载项目中的 ./gravity/A8Speaker5.apk 和 [Release APK](https://github.com/lz233/MeizuGravity/releases)。

2. 通过 TCP/IP 连接到 Meizu Gravity，端口号 7788。

   ```bash
   > adb connect 192.168.xx.xxx:7788
   # connected to 192.168.xx.xxx:7788
   > adb devices
   # List of devices attached
   # 192.168.xx.xxx:7788     device
   ```

3. 安装 A8Speaker5.apk 和 Release APK。

   ```bash
   > adb install -r A8Speaker5.apk
   #         pkg: /sdcard/A8Speaker5.apk
   # Success
   > adb install app-release.apk
   #         pkg: /sdcard/app-release.apk
   # Success
   ```

5. 在首页长按「下一首」按键，进入「Meizu Gravity +」。

## 配置

1. 参见应用内的设置。

## 还原

1. 卸载 Meizu Gravity + 和 Speaker。

   ```bash
   > adb uninstall com.meizu.speaker
   # Success
   > adb uninstall moe.lz233.meizugravity
   # Success
   ```

# Meizu Gravity Controller

Meizu Gravity Controller 可以替代官方~~鸡肋~~客户端的一些功能，并进行一些扩展，例如触摸板、远程命令。

<a href='https://play.google.com/store/apps/details?id=moe.lz233.meizugravity.controller&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png' style="zoom:35%;" /></a>

# Netease Cloud Music  For Gravity

Netease Cloud Music For Gravity 是一个专门为 Gravity 适配的网易云第三方客户端。

## 部署

安装方法参见 [Meizu Gravity +](#部署)，此处不再赘述。

## 截图

![](https://raw.githubusercontent.com/lz233/MeizuGravity/master/Gravity/cloudmusic-1.png)

![](https://raw.githubusercontent.com/lz233/MeizuGravity/master/Gravity/cloudmusic-2.png)

![](https://raw.githubusercontent.com/lz233/MeizuGravity/master/Gravity/cloudmusic-3.png)