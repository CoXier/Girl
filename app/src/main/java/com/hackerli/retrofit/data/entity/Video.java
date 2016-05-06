package com.hackerli.retrofit.data.entity;

/**
 * Created by CoXier on 2016/5/6.
 */
public class Video {
    private String videoUrl;
    private String photoUrl;

    public Video(String photoUrl, String videoUrl) {
        this.photoUrl = photoUrl;
        this.videoUrl = videoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
