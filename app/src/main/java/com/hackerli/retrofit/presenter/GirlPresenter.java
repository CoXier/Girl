package com.hackerli.retrofit.presenter;

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
public class GirlPresenter extends BasePresenter{

    private GirlData girlData = null;
    private BaseView baseView;

    public GirlPresenter(BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void loadMore(int page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GankioService gankioService = retrofit.create(GankioService.class);
        final Call<GirlData> girlDataCall = gankioService.getGirls(page);
        girlDataCall.enqueue(new Callback<GirlData>() {
            @Override
            public void onResponse(Call<GirlData> call, Response<GirlData> response) {
                girlData = response.body();
                List<Girl> newGirls = girlData.getGirls();
                baseView.showMore(newGirls);
                baseView.finishRefresh();
            }

            @Override
            public void onFailure(Call<GirlData> call, Throwable t) {
                baseView.finishRefresh();
                baseView.showSnackBar();
            }
        });

    }
}
