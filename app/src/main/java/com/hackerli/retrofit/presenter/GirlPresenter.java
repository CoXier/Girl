package com.hackerli.retrofit.presenter;

import android.util.Log;

import com.hackerli.retrofit.api.GirlsApi;
import com.hackerli.retrofit.data.GirlData;
import com.hackerli.retrofit.data.entity.Girl;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/3/20.
 */
public class GirlPresenter {

    private GirlData girlData = null;
    private GirlView girlView;

    public GirlPresenter(GirlView girlView) {
        this.girlView = girlView;
    }

    public void showMoreGirls(int page){
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
                girlView.loadMore(newGirls);
                girlView.finishRefresh();
            }

            @Override
            public void onFailure(Call<GirlData> call, Throwable t) {
                Log.e("TAG","failure");
                girlView.finishRefresh();
                girlView.showSnackBar();
            }
        });

    }
}
