package com.hackerli.retrofit.data.entity;

/**
 * Created by CoXier on 2016/5/4.
 */
public class AndroidWrapper {
    Android android;
    String avatar;

    public AndroidWrapper(Android android, String avatar) {
        this.android = android;
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Android getAndroid() {
        return android;
    }

    public void setAndroid(Android android) {
        this.android = android;
    }
}
