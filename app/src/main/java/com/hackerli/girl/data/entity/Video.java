package com.hackerli.girl.data.entity;

/**
 * Created by CoXier on 2016/5/6.
 */
public class Video {

    private String mVideoTitle;
    private String mVideoPhotoUrl;
    private String mVideoUrl;

    public Video() {

    }

    public Video(String videoUrl, String videoTitle, String videoPhotoUrl) {
        this.mVideoUrl = videoUrl;
        this.mVideoTitle = videoTitle;
        this.mVideoPhotoUrl = videoPhotoUrl;
    }

    public String getVideoTitle() {
        return mVideoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.mVideoTitle = videoTitle;
    }

    public String getVideoPhotoUrl() {
        return mVideoPhotoUrl;
    }

    public void setVideoPhotoUrl(String videoPhotoUrl) {
        this.mVideoPhotoUrl = videoPhotoUrl;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.mVideoUrl = videoUrl;
    }
}
