![](https://github.com/lz233/MeizuGravity/raw/master/Gravity/banner.png)



# <p align="center">「让这块屏幕干它该干的事。」</p>

### 部署

1. 下载项目中的 ./gravity/A8Speaker5.apk  和 [Release APK](https://www.coolapk.com/apk/moe.lz233.meizugravity)。

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

### 配置

1. 新建 「settings.json」，填入下列字段。请注意在填入时去掉注释。

   ```json
   {
   	"autoSleep": true, //是否自动开启仪表盘
   	"overtime": 300000, //开启仪表盘的时间
   	"brightness": 0.2, //仪表盘亮度
   	"location": "120.9230,30.7672", //经度与纬度（用于仪表盘获取天气）
   	"coolapkUid": "798985" //酷安用户 ID（用于仪表获取用户信息）
   }
   ```

2. 使用 ADB 将文件推送到 /sdcard/Android/data/moe.lz233.meizugravity/files

   ``` bash
   > adb push ./settings.json /sdcard/Android/data/moe.lz233.meizugravity/files
   # settings.json: 1 file pushed, 0 skipped. x.x MB/s (xxx bytes in x.xxxs)
   ```

### 还原

1. 卸载 Meizu Gravity + 和 Speaker。

   ```bash
   > adb uninstall com.meizu.speaker
   # Success
   > adb uninstall moe.lz233.meizugravity
   # Success
   ```
   

### 编译

1. Clone with HTTPS/SSH or download zip.

2. Open it in Android Studio and build it.

- You need to pay attention to the [license](https://github.com/lz233/MeizuGravity/blob/master/LICENSE)  of this project.

