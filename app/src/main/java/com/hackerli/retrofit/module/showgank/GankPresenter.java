package com.hackerli.retrofit.module.showgank;

import android.support.annotation.NonNull;

import com.hackerli.retrofit.api.GankioService;
import com.hackerli.retrofit.data.AndroidData;
import com.hackerli.retrofit.data.entity.Android;
import com.hackerli.retrofit.data.entity.AndroidWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CoXier on 2016/5/2.
 */
public class GankPresenter implements GankContract.Presenter {

    // 从gank.io获取干货
    Retrofit retfGank;
    GankioService gankioService;

    private GankContract.View mGankView;
    private AndroidData androidData;


    public GankPresenter(@NonNull GankContract.View baseView) {
        this.mGankView = baseView;
        init();
    }

    private void init() {
        retfGank = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gankioService = retfGank.create(GankioService.class);
        mGankView.setPresenter(this);
    }

    @Override
    public void loadMore(int page) {
        init();
        final Call<AndroidData> androidDataCall = gankioService.getAndroidData(page);
        androidDataCall.enqueue(new Callback<AndroidData>() {
            @Override
            public void onResponse(Call<AndroidData> call, Response<AndroidData> response) {
                androidData = response.body();
                int size = androidData.getData().size();
                List<AndroidWrapper> wrapperList = new ArrayList<AndroidWrapper>(size);
                for (Android android : androidData.getData()) {
                    AndroidWrapper wrapper = new AndroidWrapper(android, null);
                    wrapperList.add(wrapper);
                }
                mGankView.showMore(wrapperList);
                mGankView.finishRefresh();
            }

            @Override
            public void onFailure(Call<AndroidData> call, Throwable t) {
                mGankView.finishRefresh();
                mGankView.showSnackBar();
            }
        });
    }

}
