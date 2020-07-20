![](https://github.com/lz233/MeizuGravity/raw/master/Gravity/banner.png)



# <p align="center">「让这块屏幕干它该干的事。」</p>

### 部署

1. 下载项目中的 ./gravity/A8Speaker4.apk  和 Release 中的 APK 文件。

2. 通过 TCP/IP 连接到 Meizu Gravity，端口号 7788。

   ```bash
   > adb connect 192.168.xx.xxx:7788
   # connected to 192.168.xx.xxx:7788
   > adb devices
   # List of devices attached
   # 192.168.xx.xxx:7788     device
   ```

3. 安装 A8Speaker4.apk 和 Release 中的 APK 文件。

   ```bash
   > adb install -r A8Speaker4.apk
   #         pkg: /sdcard/A8Speaker3.apk
   # Success
   > adb install app-release.apk
   #         pkg: /sdcard/app-release.apk
   # Success
   ```

5. 在首页长按「下一首」按键，进入「Meizu Gravity +」。

### 还原

2. 卸载 Speaker。

   ```bash
   > adb uninstall com.meizu.speaker
   # Success
   ```
   

### 编译

1. Clone with HTTPS/SSH or download zip.

2. Open it in Android Studio and build it.

- You need to pay attention to the [license](https://github.com/lz233/MeizuGravity/blob/master/LICENSE)  of this project.

