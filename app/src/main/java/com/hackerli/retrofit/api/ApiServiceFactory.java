package com.hackerli.retrofit.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CoXier on 2016/5/24.
 */
public class ApiServiceFactory {
    private static GankioService gankioService;
    private static GitHubService gitHubService;
    private static VideoApiService videoApiService;

    public static GankioService buildGankioService(){
        if (gankioService==null){
            Retrofit retfGank = new Retrofit.Builder()
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gankioService = retfGank.create(GankioService.class);
        }
        return gankioService;
    }

    public static GitHubService buildGitHubService(){
        if (gitHubService==null){
            Retrofit retroGithub = new Retrofit.Builder().baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            gitHubService = retroGithub.create(GitHubService.class);
        }
        return gitHubService;
    }

    public static VideoApiService buildVideoApiService(){
        if (videoApiService==null){
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.bmob.cn/1/classes/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            videoApiService = retrofit.create(VideoApiService.class);
        }
        return videoApiService;
    }

}
