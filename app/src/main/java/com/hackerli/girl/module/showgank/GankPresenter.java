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
import com.hackerli.girl.util.RegexUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
        mGankioService.getAndroidData(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AndroidData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mGankView.finishRefresh();
                        mGankView.showSnackBar();
                    }

                    @Override
                    public void onNext(AndroidData androidData) {
                        setAvatarUrlAndShow(androidData);
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
            } else {
                AndroidWrapper wrapper = new AndroidWrapper(android, null);
                wrapperList.add(wrapper);
            }
        }

    }

    private void setGitHubAvatar(String url, final Android android, final List<AndroidWrapper> wrapperList, final int size) {
        String author = RegexUtil.parse("github.com/([^/]+)/", url, 1);
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
