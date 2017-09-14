package com.hackerli.girl.data.entity;

/**
 * Created by CoXier on 2016/5/4.
 */
public class AndroidWrapper {
    private Android mAndroid;
    private String mAvatarUrl;

    public AndroidWrapper(Android android, String avatarUrl) {
        this.mAndroid = android;
        this.mAvatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }

    public Android getAndroid() {
        return mAndroid;
    }

    public void setAndroid(Android android) {
        this.mAndroid = android;
    }
}
