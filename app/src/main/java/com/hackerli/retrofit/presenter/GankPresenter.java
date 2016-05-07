package com.hackerli.retrofit.presenter;

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
public class GankPresenter extends BasePresenter {

    // 从gank.io获取干货
    Retrofit retfGank ;

    private BaseView baseView;
    private AndroidData androidData;

    public GankPresenter(BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void loadMore(int page) {
        retfGank = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GankioService gankioService = retfGank.create(GankioService.class);
        final Call<AndroidData> androidDataCall = gankioService.getAndroidData(page);
        androidDataCall.enqueue(new Callback<AndroidData>() {
            @Override
            public void onResponse(Call<AndroidData> call, Response<AndroidData> response) {
                androidData = response.body();
                int size = androidData.getData().size();
                List<AndroidWrapper> wrapperList = new ArrayList<AndroidWrapper>(size);
                for (Android android:androidData.getData()){
                    AndroidWrapper wrapper = new AndroidWrapper(android,null);
                    wrapperList.add(wrapper);
                }
                baseView.showMore(wrapperList);
                baseView.finishRefresh();
            }

            @Override
            public void onFailure(Call<AndroidData> call, Throwable t) {
                baseView.finishRefresh();
                baseView.showSnackBar();
            }
        });
    }

}
