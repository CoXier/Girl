package com.hackerli.retrofit.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CoXier on 2016/5/24.
 */
public class ApiServiceFactory {
    private static GankioService mGankioService;
    private static GitHubService mGitHubService;
    private static VideoApiService mVideoApiService;

    public static GankioService buildGankioService() {
        if (mGankioService == null) {
            Retrofit retfGank = new Retrofit.Builder()
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mGankioService = retfGank.create(GankioService.class);
        }
        return mGankioService;
    }

    public static GitHubService buildGitHubService() {
        if (mGitHubService == null) {
            Retrofit retroGithub = new Retrofit.Builder().baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mGitHubService = retroGithub.create(GitHubService.class);
        }
        return mGitHubService;
    }

    public static VideoApiService buildVideoApiService() {
        if (mVideoApiService == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.bmob.cn/1/classes/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mVideoApiService = retrofit.create(VideoApiService.class);
        }
        return mVideoApiService;
    }
}
