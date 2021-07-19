# Meizu Gravity +

Meizu Gravity + 可以在你使用 DLNA 串流时显示歌词。另外也包含一些小功能。

## 部署

1. 下载项目中的 ./gravity/A8Speaker5.apk  和 [Release APK](https://github.com/lz233/MeizuGravity/releases)。

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