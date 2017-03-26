package com.hackerli.girl.data.entity;

/**
 * Created by CoXier on 2016/5/4.
 */
public class AndroidWrapper {
    Android android;
    String avatarUrl;

    public AndroidWrapper(Android android, String avatarUrl) {
        this.android = android;
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Android getAndroid() {
        return android;
    }

    public void setAndroid(Android android) {
        this.android = android;
    }
}
