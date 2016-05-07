package com.hackerli.retrofit.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hackerli.retrofit.R;
import com.hackerli.retrofit.api.GitHubService;
import com.hackerli.retrofit.data.entity.Android;
import com.hackerli.retrofit.data.entity.AndroidWrapper;
import com.hackerli.retrofit.data.entity.GitUser;
import com.hackerli.retrofit.ui.listener.GankOnClickListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.gujun.android.taggroup.TagGroup;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by CoXier on 2016/5/2.
 */
public class AndroidAdapter extends RecyclerView.Adapter<AndroidAdapter.GankHolder> {
    private List<AndroidWrapper> mAndroidWrappers;
    private Fragment mFragment;
    private Retrofit retroGithub;
    private GankOnClickListener gankListener;

    private String clientID = "b78af009a1b1cfe46317";
    private String clientSecret = "6d96f809338d479ed86614dd09983195119d338c";

    public AndroidAdapter(Fragment mFragment, List<AndroidWrapper> mAndroidWrappers) {
        this.mFragment = mFragment;
        this.mAndroidWrappers = mAndroidWrappers;
        retroGithub = new Retrofit.Builder().baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        gankListener = (GankOnClickListener) mFragment;

    }

    @Override
    public GankHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gank_layout, parent, false);
        GankHolder gankHolder = new GankHolder(v);
        return gankHolder;
    }

    @Override
    public void onBindViewHolder(GankHolder holder, int position) {
        Android android = mAndroidWrappers.get(position).getAndroid();
        String author = android.getWho() == null ? "无名好汉" : android.getWho();
        holder.author.setText(author);
        holder.title.setText(android.getDesc());

        setOnClickListener(holder.itemView,android.getUrl());
        // 给每篇干货 设置标签
        setTag(holder.tagGroup, android.getDesc(), android.getUrl());

        // 作者的头像
        setAvatar(holder, mAndroidWrappers.get(position));

    }

    private void setOnClickListener(View itemView, final String url) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gankListener!=null) gankListener.viewGankOnWebView(url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAndroidWrappers.size();
    }

    private void setAvatar(final GankHolder holder, final AndroidWrapper wrapper) {
        holder.imageView.setImageResource(android.R.color.transparent);
        if (wrapper.getAvatar() == null) {
            final String url = wrapper.getAndroid().getUrl();
            if (url.contains("https://github.com/")) {
                setGitHubAvatar(holder, wrapper, url);
            } else if (url.contains("http://blog.csdn.net/")) {
                setCSDNAvatar(holder, wrapper, url);
            } else if (url.contains("http://www.jianshu.com")) {
                setJianShuAvatar(holder, wrapper, url);
            } else if (url.contains("https://www.sdk.cn/")) {
                holder.imageView.setImageResource(R.drawable.sdkcn_logo);
            } else if (url.contains("http://android.jobbole.com")) {
                setBoleAvatar(holder, wrapper, url);
            }else {
                holder.imageView.setImageResource(R.drawable.ic_person_black_24dp);
            }
        } else {
            Glide.with(mFragment).load(wrapper.getAvatar()).into(holder.imageView);
        }

    }

    // avatar form github
    private void setGitHubAvatar(final GankHolder holder, final AndroidWrapper wrapper, String url) {
        GitHubService gitHubService = retroGithub.create(GitHubService.class);
        int start = url.indexOf("/", 19);
        String author = url.substring(19, start);
        holder.author.setText(author);
        wrapper.getAndroid().setWho(author);
        gitHubService.getAvatar(author, clientID, clientSecret)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GitUser>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", e.toString());
                    }

                    @Override
                    public void onNext(GitUser gitUser) {
                        Glide.with(mFragment).load(gitUser.getImageUrl()).into(holder.imageView);
                        wrapper.setAvatar(gitUser.getImageUrl());
                    }
                });
    }

    // avatar from csdn
    private void setCSDNAvatar(final GankHolder holder, final AndroidWrapper wrapper, final String url) {
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Document document = Jsoup.connect(url)
                            .userAgent("Mozilla")
                            .timeout(8000)
                            .get();
                    Element author = document.getElementById("blog_title");
                    Element avatar = document.getElementById("blog_userface");
                    wrapper.getAndroid().setWho(author.select("a[href]").text().toString());
                    String imgSrc = avatar.select("[src]").toString();
                    int end = imgSrc.indexOf("\"", 10);
                    String avatarUrl = imgSrc.substring(10, end);
                    wrapper.setAvatar(avatarUrl);
                    subscriber.onNext(avatarUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Glide.with(mFragment).load(s).into(holder.imageView);
                holder.author.setText(wrapper.getAndroid().getWho());
            }
        });
    }

    // avatar from jianshu
    private void setJianShuAvatar(final GankHolder holder, final AndroidWrapper wrapper, final String url) {
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Document document = Jsoup.connect(url)
                            .userAgent("Mozilla")
                            .timeout(8000)
                            .get();
                    Element container = document.getElementsByClass("container").get(0);
                    Element avatar = container.getElementsByClass("avatar").get(0);
                    String imgSrc = avatar.select("[src]").toString();
                    int end = imgSrc.indexOf("\"", 10);
                    String avatarUrl = imgSrc.substring(10, end);
                    wrapper.setAvatar(avatarUrl);
                    wrapper.getAndroid().setWho(container.select("a[class^=author-name blue-link]").text());
                    subscriber.onNext(avatarUrl);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Glide.with(mFragment).load(s).into(holder.imageView);
                holder.author.setText(wrapper.getAndroid().getWho());
            }
        });
    }

    // avatar from bole
    private void setBoleAvatar(final GankHolder holder, final AndroidWrapper wrapper, final String url) {
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Document document = Jsoup.connect(url)
                            .userAgent("Mozilla")
                            .timeout(8000)
                            .get();
                    Element container = document.getElementsByClass("copyright-area").get(0);
                    String author = container.select("a[href]").get(1).text();
                    wrapper.getAndroid().setWho(author);
                    subscriber.onNext(author);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Action1<String>() {
            @Override
            public void call(final String s) {
              Observable avatarObsevable =  Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String baseUrl = "http://www.jobbole.com/members/";
                        try {
                            Document document = Jsoup.connect(baseUrl + s)
                                    .userAgent("Mozilla")
                                    .timeout(8000)
                                    .get();
                            Element profileImg = document.getElementsByClass("profile-img").get(0);
                            String avatarUrl = profileImg.select("[src]").get(0).toString();
                            avatarUrl = avatarUrl.substring(51,avatarUrl.length()-2);
                            wrapper.setAvatar(avatarUrl);
                            subscriber.onNext(avatarUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

                avatarObsevable.subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Glide.with(mFragment).load(s).into(holder.imageView);
                        holder.author.setText(wrapper.getAndroid().getWho());
                    }
                });

            }
        });
    }


    public void setTag(TagGroup tag, String title, String url) {

        if (title.contains("源码解析") || title.contains("分析源代码") || title.contains("源代码分析")) {
            tag.setTags(new String("源码解析"));
        } else if (url.contains("https://github.com/") && title.contains("项目")) {
            tag.setTags(new String("开源项目"));
        } else if (url.contains("https://github.com/")) {
            tag.setTags(new String("开源库"));
        } else if (url.contains("https://zhuanlan.zhihu.com/")) {
            tag.setTags(new String("知乎专栏"));
        } else {
            tag.setTags(new String("干货"));
        }


    }

    public class GankHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_author)
        CircleImageView imageView;
        @Bind(R.id.tv_author)
        TextView author;
        @Bind(R.id.tv_title)
        TextView title;
        @Bind(R.id.tag_group)
        TagGroup tagGroup;
        View itemView;

        public GankHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }

}
