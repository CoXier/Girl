package com.hackerli.retrofit.api;

import com.hackerli.retrofit.data.VideoData;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;

/**
 * Created by CoXier on 2016/5/7.
 */
public interface VideoApiService {
    @Headers({
            "X-Bmob-Application-Id: 82c75d3435a1a68a58df1574ba7e235a",
            "X-Bmob-REST-API-Key: f72a1c058aee798da27a3e39085e4155",
            "Content-Type: application/json"
    })
    @GET("Video")
    Observable<VideoData> getVideoList();


}
