package com.hackerli.retrofit.ui.author;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CoXier on 2016/5/3.
 */
public class GitUser {

    @SerializedName("avatar_url")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
