package com.hackerli.retrofit.api;

import com.hackerli.retrofit.data.entity.GitUser;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by CoXier on 2016/5/4.
 */
public interface GitHubService {
    String clientID = "b78af009a1b1cfe46317";
    String clientSecret = "6d96f809338d479ed86614dd09983195119d338c";

    @GET("users/{user}")
    Observable<GitUser> getAvatar(@Path("user")String user, @Query("client_id")String id,@Query("client_secret")String secret);
}
