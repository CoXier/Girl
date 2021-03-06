package com.hackerli.girl.network.api;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CoXier on 2016/5/24.
 */
public class ApiServiceFactory {
    private static GankIoService sGankIoService;
    private static GitHubService sGitHubService;

    public static GankIoService buildGankioService() {
        if (sGankIoService == null) {
            sGankIoService = buildService(GankIoService.class);
        }
        return sGankIoService;
    }

    public static GitHubService buildGitHubService() {
        if (sGitHubService == null) {
            sGitHubService = buildService(GitHubService.class);
        }
        return sGitHubService;
    }

    private static <T> T buildService(Class<T> clazz) {
        String baseUrl = getBaseUrl(clazz);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
        return retrofit.create(clazz);
    }

    private static String getBaseUrl(Class clazz) {
        if (clazz.isAnnotationPresent(BaseUrl.class)) {
            BaseUrl baseUrl = (BaseUrl) clazz.getAnnotation(BaseUrl.class);
            return baseUrl.value();
        }
        throw new IllegalArgumentException(clazz.getSimpleName()
                + " doesn't have @BaseUrl annotation");
    }

}
