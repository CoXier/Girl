package com.hackerli.girl.module.showgank;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.hackerli.girl.data.entity.Android;
import com.hackerli.girl.network.api.ApiServiceFactory;
import com.hackerli.girl.network.api.GankIoService;
import com.hackerli.girl.data.AndroidData;
import com.hackerli.girl.data.entity.AndroidWrapper;
import com.hackerli.girl.util.RegexUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;


/**
 * Created by CoXier on 2016/5/2.
 */
public class GankPresenter implements GankContract.Presenter {
    private static final String TAG = "GankPresenter";

    private GankIoService mGankioService;

    private GankContract.View mGankView;
    private Disposable mDisposable;


    GankPresenter(@NonNull GankContract.View baseView) {
        mGankView = baseView;
        init();
    }

    private void init() {
        mGankioService = ApiServiceFactory.buildGankioService();
        mGankView.setPresenter(this);
    }

    @Override
    public void loadMore(int page) {
        mGankioService.getAndroidData(page)
                .map(AndroidData::getData)
                .flatMapSingle(new AndroidList2WrapperList())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        androidWrappers -> {
                            Log.d(TAG, "onNext");
                            mGankView.showMore(androidWrappers);
                            mGankView.finishRefresh();
                        },
                        throwable -> {
                            mGankView.finishRefresh();
                            mGankView.showSnackBar();
                        },
                        () -> {
                        },
                        disposable -> mDisposable = disposable);
    }

    @Override
    public void unSubscribe() {
        if (mDisposable != null) {
            Log.d(TAG, "dispose");
            mDisposable.dispose();
        }
    }

    private static class AndroidList2WrapperList implements
            Function<List<Android>, SingleSource<? extends List<AndroidWrapper>>> {

        @Override
        public SingleSource<? extends List<AndroidWrapper>> apply(List<Android> androids) throws Exception {
            return Observable.fromIterable(androids)
                    .concatMap(android -> {
                        String url = android.getUrl();
                        Log.d(TAG, android.getDesc());
                        if (!TextUtils.isEmpty(url) && url.contains("https://github.com/")) {
                            String author = RegexUtil.parse("github.com/([^/]+)/", url, 1);
                            android.setWho(author);
                            return ApiServiceFactory.buildGitHubService().getAvatar(author)
                                    .map(gitUser -> new AndroidWrapper(android, gitUser.getImageUrl()));
                        } else {
                            return Observable.just(new AndroidWrapper(android, null)).hide();
                        }
                    }, 1)
                    .toList();
        }
    }

}
