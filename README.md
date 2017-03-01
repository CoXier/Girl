## Girl

这个APP是用来学习Retrofit和RxJava的，学习的过程中也尝试使用了MVP模式，在解析html时使用了jsoup。

## 截图：

<img src="/screenshots/s1.png" alt="screenshot" title="screenshot" width="250" height="436" /> <img src="/screenshots/s2.png" alt="screenshot" title="screenshot" width="250" height="436" />   <img src="/screenshots/s3.png" alt="screenshot" title="screenshot" width="250" height="436" />

<img src="/screenshots/s4.png" alt="screenshot" title="screenshot" width="250" height="436" />

特点：
* 个人觉得Girl还是小而美的，增加的特色：在干货页面，利用RxJava+Jsoup解析html，获取干货的作者姓名和作者头像。由于能力有限，我只展示了github，简书的作者姓名和头像。
* 在展示干货的UI上使用了Tag，可以正确的匹配一下干货的类型，比如：开源库，开源项目，知乎专栏。
* 提供干货在线搜索功能，可以方便地根据关键词查找资源
* 可以上传本地妹子的照片

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
   compile 'io.reactivex:rxandroid:1.1.0'
   compile 'io.reactivex:rxjava:1.1.0'
   compile 'me.gujun.android.taggroup:library:1.4@aar'
   compile 'de.hdodenhof:circleimageview:2.0.0'
   compile 'com.devbrackets.android:exomedia:2.5.6'
   compile 'org.jsoup:jsoup:1.9.1'

```
各个库具体的功能可以在github查一查，比较实用。

## LICENSE

GLP V3
