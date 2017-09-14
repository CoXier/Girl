package com.hackerli.girl.module.showvideo;

import com.hackerli.girl.data.entity.Video;

import java.util.List;

/**
 * Created by CoXier on 16-12-24.
 */

public interface VideoContract {
    interface View {
        void showVideos(List<Video> videos, String head);
    }

    interface Presenter {
        void loadYoukuVideos();
    }


}
