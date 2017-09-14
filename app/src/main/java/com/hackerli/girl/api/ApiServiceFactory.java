package com.hackerli.girl.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CoXier on 2016/5/24.
 */
public class ApiServiceFactory {
    private static GankIoService sGankIoService;
    private static GitHubService sGitHubService;

    private static String sGankBaseURL = "http://gank.io/api/";
    private static String sGithubBaseURL = "https://api.github.com/";

    public static GankIoService buildGankioService() {
        if (sGankIoService == null) {
            Retrofit retfGank = new Retrofit.Builder()
                    .baseUrl(sGankBaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            sGankIoService = retfGank.create(GankIoService.class);
        }
        return sGankIoService;
    }

    public static GitHubService buildGitHubService() {
        if (sGitHubService == null) {
            Retrofit retroGitHub = new Retrofit.Builder().baseUrl(sGithubBaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            sGitHubService = retroGitHub.create(GitHubService.class);
        }
        return sGitHubService;
    }

}
