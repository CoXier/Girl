## Retrofit
关于一个retrofit的小练习


### 为啥学Meizhi
这个小demo涉及到:
* [Retrofit](https://github.com/square/retrofit):一个开源的API请求库，简化了网络请求操作，很利于封装
* [Glide](https://github.com/bumptech/glide):一个图片加载库,其实和[Piccso](https://github.com/square/picasso)很像
* butterknife：一个很适合偷懒的库（其实我用来就是初始化一些资源）
* [GsonFormat](https://github.com/zzz40500/GsonFormat):一个非常方便的插件，可以根据JSON格式的数据转成一个Java Class

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
第二步是获取API；
第三步使用API 的某一个行为，（之前写的GET方法）。
第四步网络请求排队：Call<Class> object.enqueue。需要的数据可以从response的body中取出
