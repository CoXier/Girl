package com.hackerli.girl.network.api;

import com.hackerli.girl.data.entity.GitUser;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by CoXier on 2016/5/4.
 */
@BaseUrl(value = "https://api.github.com/")
public interface GitHubService {

    String CLIENT_ID = "b78af009a1b1cfe46317";
    String CLIENT_SECRET = "6d96f809338d479ed86614dd09983195119d338c";

    @GET("users/{user}?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET)
    Observable<GitUser> getAvatar(@Path("user") String user);
}
