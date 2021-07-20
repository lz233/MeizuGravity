# Netease Cloud Music  For Gravity

Netease Cloud Music  For Gravity 是一个专门为 Gravity 适配的网易云第三方客户端，目前实现了如下功能：

- [x] 签到
- [x] 播放控制页
- [x] 每日推荐
- [x] 我的歌单

## 部署

安装方法参见 [Meizu Gravity +](GRAVITY_PLUS.md)，此处不再赘述。

服务器地址：Fork [NeteaseCloudMusicApi](https://github.com/lz233/NeteaseCloudMusicApi) （不要 fork 原版）并按照 Readme 中的[「Vercel 部署」](https://github.com/lz233/NeteaseCloudMusicApi#vercel-%E9%83%A8%E7%BD%B2)节操作。完成后，转到 Settings->Domains 复制**网址**并通过 Adb（adb shell input text xxx）填入 Dialog 内。

## 截图

![](https://raw.githubusercontent.com/lz233/MeizuGravity/master/Gravity/cloudmusic-1.png)

![](https://raw.githubusercontent.com/lz233/MeizuGravity/master/Gravity/cloudmusic-2.png)

![](https://raw.githubusercontent.com/lz233/MeizuGravity/master/Gravity/cloudmusic-3.png)
