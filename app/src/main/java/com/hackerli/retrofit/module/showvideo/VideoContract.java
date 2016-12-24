package com.hackerli.retrofit.module.showvideo;

import com.hackerli.retrofit.data.entity.Video;

import java.util.List;

/**
 * Created by CoXier on 16-12-24.
 */

public interface VideoContract {
    interface View{
        void showVideos(List<Video> videos);
    }

    interface Presenter{
        void loadYoukuVideos();

        String extractRealUrl(String url);
    }


}
