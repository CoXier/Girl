package com.hackerli.retrofit.data.entity;

/**
 * Created by CoXier on 2016/5/6.
 */
public class Video {

    private String VideoTitle;
    private String videoPhotoUrl;
    private String videoUrl;

    public Video(){

    }

    public Video(String videoUrl, String videoTitle, String videoPhotoUrl) {
        this.videoUrl = videoUrl;
        VideoTitle = videoTitle;
        this.videoPhotoUrl = videoPhotoUrl;
    }

    public String getVideoTitle() {
        return VideoTitle;
    }

    public void setVideoTitle(String VideoTitle) {
        this.VideoTitle = VideoTitle;
    }

    public String getVideoPhotoUrl() {
        return videoPhotoUrl;
    }

    public void setVideoPhotoUrl(String videoPhotoUrl) {
        this.videoPhotoUrl = videoPhotoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
