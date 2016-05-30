
# 源代码在 GPL v3 协议下发布, 使用前, 请确保你了解这个协议!

# 感谢

* [drakeet](https://github.com/drakeet)的开源项目

* [daimajia](https://github.com/daimajia)提供的干货和妹子
* [hujiaweibujidao](https://github.com/hujiaweibujidao/)提供的搜索接口

## Girl
Girl是我的第一个开源项目，声明[LiJianxinChina](https://github.com/LiJianxinChina)是我之前的一个账号，由于名字太.....我就换了现在的帐号。

这个APP是用来学习Retrofit和RxJava的，学习的过程中也尝试使用了MVP模式，在解析html时使用了jsoup。

### Girl特色
应用截图：

<img src="/screenshots/s0.png" alt="screenshot" title="screenshot" width="250" height="436" />   <img src="/screenshots/s1.png" alt="screenshot" title="screenshot" width="250" height="436" />

<img src="/screenshots/s2.png" alt="screenshot" title="screenshot" width="250" height="436" />   <img src="/screenshots/s3.png" alt="screenshot" title="screenshot" width="250" height="436" />

<img src="/screenshots/s4.png" alt="screenshot" title="screenshot" width="250" height="436" />

特点：
* 个人觉得Girl还是小而美的，增加的特色：在干货页面，利用RxJava+Jsoup解析html，获取干货的作者姓名和作者头像。由于能力有限，我只展示了github，简书，csdn，伯乐上的作者姓名和头像。
* 在展示干货的UI上使用了Tag，可以正确的匹配一下干货的类型，比如：开源库，开源项目，知乎专栏。
* 播放视频方面，我花了挺多时间的，由于api获取的只是网页能播放的视频，所以只好用webview，这样就使得看视频不是那么纯粹，毕竟网页里面多少会有一些广告和其他的东西。另外其中由于代码家的api里面的休息视频信息不是很够，所以我根据信息自己构造了RestApi来补充信息，补充的信息主要包括：视频的简介，视频的截图，视频在优酷上的地址。
* 提供干货在线搜索功能，可以方便地根据关键词查找资源
* 可以本地上传妹子的照片

## 项目的使用的开源库：
```bash

   compile 'com.squareup.retrofit2:retrofit:2.0.0'
   compile 'com.google.code.gson:gson:2.6.2'
   compile 'com.squareup.retrofit2:converter-gson:2.0.0'
   compile 'com.squareup.okhttp3:okhttp:3.2.0'
   compile 'com.squareup.retrofit2:adapter-rxjava:2.0.0'
   compile 'com.jakewharton:butterknife:7.0.1'
   compile 'com.github.bumptech.glide:glide:3.7.0'
   compile 'com.commit451:PhotoView:1.2.5'
   compile 'com.squareup.picasso:picasso:2.5.2'
   compile 'io.reactivex:rxandroid:1.1.0'
   compile 'io.reactivex:rxjava:1.1.0'
   compile 'me.gujun.android.taggroup:library:1.4@aar'
   compile 'de.hdodenhof:circleimageview:2.0.0'
   compile 'com.devbrackets.android:exomedia:2.5.6'
   compile files('libs/jsoup-1.9.1.jar')

```
各个库具体的功能可以在github查一查，比较实用。

## 关于我
我是华科的在读大学生，今年大二，现在在学习Android和Java的相关知识，喜欢开源，欢迎小伙伴们和我交流，我的邮箱：TmWorkCC@yahoo.com
