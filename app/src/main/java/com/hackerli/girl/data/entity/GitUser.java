package com.hackerli.girl.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CoXier on 2016/5/3.
 */
public class GitUser {

    @SerializedName("avatar_url")
    private String mImageUrl;

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }
}
