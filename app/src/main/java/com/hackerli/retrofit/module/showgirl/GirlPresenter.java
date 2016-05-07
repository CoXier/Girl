package com.hackerli.retrofit.module.showgirl;

import com.hackerli.retrofit.api.GankioService;
import com.hackerli.retrofit.data.GirlData;
import com.hackerli.retrofit.data.entity.Girl;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by XoCier on 2016/3/20.
 */
public class GirlPresenter implements GirlContract.Presenter {

    private GirlData mGirlData = null;
    private GirlContract.View mView;
    private Retrofit mRetrofit;
    private GankioService mService;

    public GirlPresenter(GirlContract.View baseView) {
        this.mView = baseView;
        init();
    }

    private void init() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = mRetrofit.create(GankioService.class);
        mView.setPresenter(this);
    }

    @Override
    public void loadMore(int page) {
        final Call<GirlData> girlDataCall = mService.getGirls(page);
        girlDataCall.enqueue(new Callback<GirlData>() {
            @Override
            public void onResponse(Call<GirlData> call, Response<GirlData> response) {
                mGirlData = response.body();
                List<Girl> newGirls = mGirlData.getGirls();
                mView.showMore(newGirls);
                mView.finishRefresh();
            }

            @Override
            public void onFailure(Call<GirlData> call, Throwable t) {
                mView.finishRefresh();
                mView.showSnackBar();
            }
        });

    }
}
