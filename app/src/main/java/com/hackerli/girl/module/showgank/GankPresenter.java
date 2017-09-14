package com.hackerli.girl.module.showgank;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hackerli.girl.api.ApiServiceFactory;
import com.hackerli.girl.api.GankIoService;
import com.hackerli.girl.api.GitHubService;
import com.hackerli.girl.data.AndroidData;
import com.hackerli.girl.data.entity.Android;
import com.hackerli.girl.data.entity.AndroidWrapper;
import com.hackerli.girl.data.entity.GitUser;

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

    private GankIoService mGankioService;
    private GitHubService mGitHubService;

    private GankContract.View mGankView;
    private AndroidData mAndroidData;


    GankPresenter(@NonNull GankContract.View baseView) {
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
            if (url == null) continue;
            if (url.contains("https://github.com/")) {
                setGitHubAvatar(url, android, wrapperList, size);
            } else if (url.contains("http://www.jianshu.com")) {
                setJianShuAvatar(url, android, wrapperList, size);
            } else {
                AndroidWrapper wrapper = new AndroidWrapper(android, null);
                wrapperList.add(wrapper);
            }
        }

    }

    private void setJianShuAvatar(final String url, final Android android, final List<AndroidWrapper> wrapperList, final int size) {
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
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

    private void setGitHubAvatar(String url, final Android android, final List<AndroidWrapper> wrapperList, final int size) {
        String author;
        int start = url.indexOf("/", 19);
        if (start == -1) {
            author = url.substring(19);
        } else {
            author = url.substring(19, start);
        }
        android.setWho(author);
        mGitHubService.getAvatar(author)
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
