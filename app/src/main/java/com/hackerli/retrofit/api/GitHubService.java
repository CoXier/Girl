package com.hackerli.retrofit.api;

import com.hackerli.retrofit.ui.author.GitUser;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by CoXier on 2016/5/4.
 */
public interface GitHubService {

    @GET("users/{user}")
    public Observable<GitUser> getAvatar(@Path("user")String user, @Query("client_id")String id,@Query("client_secret")String secret);
}
