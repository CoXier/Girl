package com.hackerli.retrofit.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CoXier on 2016/5/24.
 */
public class ApiServiceFactory {
    private static GankIoService mGankIoService;
    private static GitHubService mGitHubService;

    private static String GANK_BASE_URL = "http://gank.io/api/";
    private static String GITHUB_BASE_URL = "https://api.github.com/";

    public static GankIoService buildGankioService() {
        if (mGankIoService == null) {
            Retrofit retfGank = new Retrofit.Builder()
                    .baseUrl(GANK_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mGankIoService = retfGank.create(GankIoService.class);
        }
        return mGankIoService;
    }

    public static GitHubService buildGitHubService() {
        if (mGitHubService == null) {
            Retrofit retroGitHub = new Retrofit.Builder().baseUrl(GITHUB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mGitHubService = retroGitHub.create(GitHubService.class);
        }
        return mGitHubService;
    }

}
