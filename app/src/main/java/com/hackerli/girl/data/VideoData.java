package com.hackerli.girl.data;

import com.hackerli.girl.data.entity.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoXier on 16-12-25.
 */

public class VideoData {
    private List<Video> videos;
    private String channel;

    public VideoData(List<Video> videos, String channel) {
        this.videos = videos;
        this.channel = channel;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
