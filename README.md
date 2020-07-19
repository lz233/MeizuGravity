# MeizuGravity +

<p align="center">「让这块屏幕实现它该干的事。」</p>

### 部署

1. 下载项目中的 ./gravity/A8Speaker3.apk  和 Release 中的 APK 文件。

2. 通过 TCP/IP 连接到 Meizu Gravity，端口号 7788。

   ```bash
   > adb connect 192.168.xx.xxx:7788
   # connected to 192.168.xx.xxx:7788
   > adb devices
   # List of devices attached
   # 192.168.xx.xxx:7788     device
   ```

3. 安装 A8Speaker3.apk 和 Release 中的 APK 文件。

   ```bash
   > adb install A8Speaker3.apk
   #         pkg: /sdcard/A8Speaker3.apk
   # Success
   > adb install app-release.apk
   #         pkg: /sdcard/A8Speaker3.apk
   ```

4. 强制停止 Speaker。

   ```bash
   > adb shell am force-stop com.meizu.speaker
   ```

5. 