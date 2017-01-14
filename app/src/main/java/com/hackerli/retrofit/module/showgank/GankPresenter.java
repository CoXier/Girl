package com.hackerli.retrofit.module.showgank;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hackerli.retrofit.api.ApiServiceFactory;
import com.hackerli.retrofit.api.GankioService;
import com.hackerli.retrofit.api.GitHubService;
import com.hackerli.retrofit.data.AndroidData;
import com.hackerli.retrofit.data.entity.Android;
import com.hackerli.retrofit.data.entity.AndroidWrapper;
import com.hackerli.retrofit.data.entity.GitUser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by CoXier on 2016/5/2.
 */
public class GankPresenter implements GankContract.Presenter {

    private GankioService mGankioService;
    private GitHubService mGitHubService;

    private GankContract.View mGankView;
    private AndroidData mAndroidData;

    private Object CSDNAvatar;

    public GankPresenter(@NonNull GankContract.View baseView) {
        mGankView = baseView;
        init();
    }

    private void init() {
        mGankioService = ApiServiceFactory.buildGankioService();
        mGitHubService = ApiServiceFactory.buildGitHubService();
        mGankView.setPresenter(this);
    }

    @Override
    public void loadMore(int page) {
        init();
        final Call<AndroidData> androidDataCall = mGankioService.getAndroidData(page);
        androidDataCall.enqueue(new Callback<AndroidData>() {
            @Override
            public void onResponse(Call<AndroidData> call, Response<AndroidData> response) {
                mAndroidData = response.body();
                setAvatarUrlAndShow(mAndroidData);
            }

            @Override
            public void onFailure(Call<AndroidData> call, Throwable t) {
                mGankView.finishRefresh();
                mGankView.showSnackBar();
            }
        });
    }

    private void setAvatarUrlAndShow(AndroidData mAndroidData) {
        List<Android> androidList = mAndroidData.getData();
        final int size = androidList.size();
        final List<AndroidWrapper> wrapperList = new ArrayList<AndroidWrapper>(size);
        for (int i = 0; i < size; i++) {
            final Android android = androidList.get(i);
            String url = android.getUrl();
            if (url.contains("https://github.com/")) {
                setGitHubAvatar(url, android, wrapperList, size);
            }  else if (url.contains("http://www.jianshu.com")) {
                setJianShuAvatar(url, android, wrapperList, size);
            } else if (url.contains("http://android.jobbole.com")) {
                setJobboleAvatar(url, android, wrapperList, size);
            } else {
                AndroidWrapper wrapper = new AndroidWrapper(android, null);
                wrapperList.add(wrapper);
            }
        }

    }

    private void setJobboleAvatar(final String url, final Android android, final List<AndroidWrapper> wrapperList, final int size) {

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
                    android.setWho(author);
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
                Observable avatarObsevable = Observable.create(new Observable.OnSubscribe<String>() {
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
                            avatarUrl = avatarUrl.substring(51, avatarUrl.length() - 2);
                            AndroidWrapper androidWrapper = new AndroidWrapper(android, avatarUrl);
                            subscriber.onNext(avatarUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

                avatarObsevable.subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (wrapperList.size() == size) {
                            mGankView.showMore(wrapperList);
                            mGankView.finishRefresh();
                        }
                    }
                });

            }
        });
    }

    private void setJianShuAvatar(final String url, final Android android, final List<AndroidWrapper> wrapperList, final int size) {
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Log.d("TAG",url);
                    Document document = Jsoup.connect(url)
                            .userAgent("Mozilla")
                            .timeout(8000)
                            .get();
                    Element container = document.getElementsByClass("author").get(0);
                    Element avatar = container.getElementsByClass("avatar").get(0);
                    String imgSrc = avatar.select("[src]").toString();
                    int end = imgSrc.indexOf("\"", 10);
                    String avatarUrl = imgSrc.substring(10, end);
                    android.setWho(container.select("a[class^=author-name blue-link]").text());
                    AndroidWrapper androidWrapper = new AndroidWrapper(android, avatarUrl);
                    wrapperList.add(androidWrapper);
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
                if (wrapperList.size() == size) {
                    mGankView.showMore(wrapperList);
                    mGankView.finishRefresh();
                }
            }
        });
    }

    private void setCSDNAvatar(final String url, final Android android, final List<AndroidWrapper> wrapperList, final int size) {
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {

                    if (!url.contains("http://blog.csdn.net/qq_22329521/")) {
                        Document document = Jsoup.connect(url)
                                .userAgent("Mozilla")
                                .timeout(8000)
                                .get();
                        Element author = document.getElementById("blog_title");
                        Element avatar = document.getElementById("blog_userface");
                        android.setWho(author.select("a[href]").text().toString());
                        String imgSrc = avatar.select("[src]").toString();
                        int end = imgSrc.indexOf("\"", 10);
                        String avatarUrl = imgSrc.substring(10, end);
                        AndroidWrapper androidWrapper = new AndroidWrapper(android, avatarUrl);
                        wrapperList.add(androidWrapper);
                        subscriber.onNext(avatarUrl);
                    }
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
                if (wrapperList.size() == size) {
                    mGankView.showMore(wrapperList);
                    mGankView.finishRefresh();
                }
            }
        });
    }

    private void setGitHubAvatar(String url, final Android android, final List<AndroidWrapper> wrapperList, final int size) {
        String author;
        int start = url.indexOf("/", 19);
        if (start == -1){
            author = url.substring(19);
        }else {
            author = url.substring(19, start);
        }
        android.setWho(author);
        mGitHubService.getAvatar(author, GitHubService.clientID, GitHubService.clientSecret)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GitUser>() {
                    @Override
                    public void onCompleted() {
                        if (wrapperList.size() == size) {
                            mGankView.showMore(wrapperList);
                            mGankView.finishRefresh();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", e.toString());
                    }

                    @Override
                    public void onNext(GitUser gitUser) {
                        AndroidWrapper wrapper = new AndroidWrapper(android, gitUser.getImageUrl());
                        wrapperList.add(wrapper);
                    }
                });
    }

}
