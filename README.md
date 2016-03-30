## 学习Meizhi的历程
还在看，还在学，还没写完......



### 为啥学Meizhi
这个小demo涉及到:
* [Retrofit](https://github.com/square/retrofit):一个开源的API请求库，简化了网络请求操作，很利于封装
* [Glide](https://github.com/bumptech/glide):一个图片加载库,其实和[Piccso](https://github.com/square/picasso)很像
* butterknife：一个很适合偷懒的库（其实我用来就是初始化一些资源）
* [GsonFormat](https://github.com/zzz40500/GsonFormat):一个非常方便的插件，可以根据JSON格式的数据转成一个Java Class
* [PhotoView](https://github.com/chrisbanes/PhotoView):一个浏览图片的库，支持很多手势，例如双击放缩，滑动，但是没有像QQ那样的旋转，但是感觉够用了。

感觉浑浑噩噩的在大二上学期学了一点Android,在无意之间看到了[drakeet](https://github.com/drakeet)大神的博客，然后就看到了Meizhi这个开源项目，第一次用这个APP的时候觉得做的好漂亮啊，不仅仅是妹子漂亮哦！同时我觉得这个项目应该值得一学。但在学习他的Meizhi之前，我先仔细看了看看他的[验证码助手](https://github.com/drakeet/SmsCodeHelper),熟练了一下Android的四大组件。感觉大神写的真好，考虑的也很全面。

大概是间断的看着Meizhi,只怪我的能力太弱，所以最开始很多都不懂，就想着最好把最简单的主界面的瀑布流加载给整出来。大概花了4天时间，接触retrofit,然后学着封装API请求，再学用recycleview实现瀑布流，SwipeRefreshLayout实现下拉刷新，recycleview上滑加载更多。学的过程中确实遇到了很多问题，现在记下来。

### Retrofit的简单使用
最开始在网上一搜，这样的东西确实很多，看了很多之后对我这个新手来说感觉还是半生半熟。不得不说软件这个行业的实践性要求特别高，我就试着利用[daimajia](https://github.com/daimajia)提供的API试试。在这里推荐一个AS的插件GsonFormat,特别好用。

```java
public class Girl {
    /**
     * _id : 56e8d0bb67765933d8be90be
     * _ns : ganhuo
     * createdAt : 2016-03-16T11:19:23.692Z
     * desc : 3.16
     * publishedAt : 2016-03-17T11:14:16.306Z
     * source : chrome
     * type : 福利
     * url : http://ww4.sinaimg.cn/large/7a8aed7bjw1f1yjc38i9oj20hs0qoq6k.jpg
     * used : true
     * who : 张涵宇
     */
    @SerializedName("_id")
    private String id;
    @SerializedName("_ns")
    private String ns;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;
  }
```
上面的代码就是用GsonFormat生成的，仔细看注释部分很容易可以发现改变名字（和JSON里面的名字不同），需要加上@SerializedName("xxx")；这个类就是实体类（entity）Girl(妹子)。
由于获取的JSON包括了很多Girl,所以新建了另一个类GirlData.
```java
public class GirlData {
    private boolean error;
    @SerializedName("results")
    private List<Girl> girls;
}

```
接下来是最关键的API接口了：
```java
public interface GirlsApi {
    @GET("data/福利/10/{page}")
    Call<GirlData> getGrils(@Path("page")int page);
}
```
为什么说Retrofit方便呢？我的理解是，利用注解添加参数，使接口具有了扩展性。
@GET 指明了HTTP请求方式是get。参数是data/福利/10/page，这个page是个变量（需要与@Path结合使用）
不同的是在函数返回值这里有一个call。

有了API，那就要有对象来用。--GirlModle
```java
public void getGirs(int page, final RecyclerView recyclerView, final List<Girl> girlList){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GirlsApi girlsApi = retrofit.create(GirlsApi.class);
        final Call<GirlData> girlDataCall = girlsApi.getGrils(page);
        girlDataCall.enqueue(new Callback<GirlData>() {
            @Override
            public void onResponse(Call<GirlData> call, Response<GirlData> response) {
                girlData = response.body();
                List<Girl> newGirls = girlData.getGirls();
                for (Girl girl:newGirls){
                    girlList.add(girl);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<GirlData> call, Throwable t) {
                Log.e("TAG","failure");
            }
        });

```
第一步是创建一个Retrofit对象retrofit,在创建的时候可以选择使用哪一个库来转化JSON，我选择的是Gson.
第二步是获取接口对象；
第三步使用这个对象 的某一个HTTP行为，（之前写的GET方法）。
第四步网络请求排队：Call<Class> object.enqueue。需要的数据可以从response的body中取出。

### 如何加载图片呢？
在晚上搜过，轻便好用的有Glide和Picasso。两者的区别有：
* Glide默认的Bitmap的格式是RGB_565,Picasso的默认格式是ARGB_8888,这样不难发现Glide的内存开销要小一些，虽然结果是Picasso的质量较高，但是这样的区别不太明显，或者说看不出来。
* Glide可以自动计算出要加载的Imageview的尺寸大小，Picasso加载的是默认的是原图大小，resize方法可修改显示的大小,但是加载的还是整个图，从这个角度来说，GLide使用起来更灵活
* Disk Cache方面来说，Picasson缓存的是整个图，而Glide缓存的是实际大小。凡是都有两面性,Picasso缓存了整个图之后，就不会再去加载以适应缩剪等操作了，就像你为了考微积分，完全过了一遍，啥都不怕了，而GLide就像只学了几个单元。

### 实现瀑布流
MainActivity中瀑布流用的是GLide，尺寸小，加载速度快。RecyclerView实现瀑布流比较简单，上代码：
```java
StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
```
在gridLayoutManager中初始有两列。当时写瀑布流的时候有个BUG，语言不太好表达出来，反正原因是RecyclerView的xml布局里的height要写成wrap_content。

为了搞出瀑布流（那种高度不一的感觉），drakeet大神采取的方式自定义一个RatioImageView,重写onMeasure()方法，计算出每个RatioImageView实际大小尺寸之后，直接调用** setMeasuredDimension(width,height) **，但是我觉得他并没有做到就是高度的不一，他的技巧是靠RatioImageView下方Textview的title这个字符串。由于我并没有获取这个title，所以采取的是随机生成高度，当然要维持width:height的一个比例，我的比例范围是（1，1.2）。

### 实现下拉刷新
之前一直没有接触过SwipeRefreshLayout，这次用了一下。主要的方法：
```java
    @Override
    public void onRefresh() {
        page++;
        swipeRefreshLayout.setRefreshing(true);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
        girlModle.getGirs(page, recyclerView, girls);
    }

```
思路简单：拉一下加载是个数据。
```java
 swipeRefreshLayout.setColorSchemeResources(R.color.refresh_process1, R.color.refresh_process2, R.color.refresh_process3);
```
这个方法设置加载是的动画颜色。做到这里又有了一个小问题，我想实现一个效果就是第一次进入 app的时候，这个加载动画就会出现，但是如果直接在MainActivity的onCreate()设置  swipeRefreshLayout.setRefreshing(true);这个是没有用的，上网搜了一下，原因是setRefreshing在onMeasure方法之前调用了，这自然无效。我解决的策略是：
在onCreate()中添加 swipeRefreshLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);

### 实现上滑刷新
当下很多APP都有就是在用户上滑（向下浏览）的时候会加载数据。
![](http://7xra01.com1.z0.glb.clouddn.com/Scroll.PNG)
这个思路顺理成章，当滑到与加载的itemCount相差一定值的时候，就需要重新加载数据，这里就直接调用onRefresh,代码不重复。

### 使用PhotoView
PhotoView使用比较简单，当涉及到与网络请求的照片时：
```xml
<uk.co.senab.photoview.PhotoView
       android:id="@+id/iv_fr_girl"
       android:layout_width="match_parent"
       android:layout_height="match_parent" />
```
使用photoview而不是Imageview，但drakeet大神使用的是Imageview，也做到了。
```java
Picasso.with(getActivity())
       .load(getArguments().getString("photoUrl")
       .into(photoView);
```
然后接下来我用了两种方式来展示大图片（也就是用户对小图片有兴趣了，点了看看）。一种是DialogFragment,一个是Activity。DialogFragment这个之前没怎么用过，记一下这个细节：
![](http://7xra01.com1.z0.glb.clouddn.com/fragment.PNG)，传参数这个地方需要注意，不能写成内部变量，使用Bundle.
做到这里我又有问题了，怎么把这个DialogFragment设置成full screen呢？google之后找到可行之法：
Step1:

```java
    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

```
Step2:在res/values/styles.xml加入这个：

```java
<style name="Dialog.FullScreen" parent="Theme.AppCompat.Dialog">
    <item name="android:padding">0dp</item>
    <item name="android:windowBackground">@android:color/black</item>
</style>
```

Step3:在启动fragment时
```java
  GirlPhotoFragment fragment = GirlPhotoFragment.newInstance(photoUrl);
  fragment.show(fragmentManager,"fragment_girl_photo");
  fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
```
### 接下来要解决的是：学习RxJava，然后用他来保存照片到本地。
