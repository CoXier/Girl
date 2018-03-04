package com.hackerli.girl.api;


import com.hackerli.girl.data.AndroidData;
import com.hackerli.girl.data.GirlData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


/**
 * Created by Administrator on 2016/3/19.
 */
public interface GankIoService {
    String BASE_URL = "http://gank.io/api/";

    @GET("data/福利/10/{page}")
    Observable<GirlData> getGirls(@Path("page") int page);

    @GET("data/Android/10/{page}")
    Observable<AndroidData> getAndroidData(@Path("page") int page);
}
