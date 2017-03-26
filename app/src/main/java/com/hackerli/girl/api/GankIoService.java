package com.hackerli.girl.api;


import com.hackerli.girl.data.AndroidData;
import com.hackerli.girl.data.GirlData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2016/3/19.
 */
public interface GankIoService {
    @GET("data/福利/10/{page}")
    Call<GirlData> getGirls(@Path("page")int page);

    @GET("data/Android/10/{page}")
    Call<AndroidData> getAndroidData(@Path("page")int page);
}
