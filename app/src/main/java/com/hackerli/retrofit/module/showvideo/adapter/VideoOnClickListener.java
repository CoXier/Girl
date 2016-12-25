package com.hackerli.retrofit.module.showvideo.adapter;

import com.hackerli.retrofit.data.entity.Video;

/**
 * Created by CoXier on 2016/5/7.
 */
public interface VideoOnClickListener {
    void playVideo(Video video);

    void refreshCategory(int index);
}
