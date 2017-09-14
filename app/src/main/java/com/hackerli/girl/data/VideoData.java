package com.hackerli.girl.data;

import com.hackerli.girl.data.entity.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoXier on 16-12-25.
 */

public class VideoData {
    private List<Video> mVideos;
    private String mChannel;

    public VideoData(List<Video> videos, String channel) {
        this.mVideos = videos;
        this.mChannel = channel;
    }

    public List<Video> getVideos() {
        return mVideos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.mVideos = videos;
    }

    public String getChannel() {
        return mChannel;
    }

    public void setChannel(String channel) {
        this.mChannel = channel;
    }
}
