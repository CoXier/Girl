package com.hackerli.girl.module.showvideo.adapter;

import com.hackerli.girl.data.entity.Video;

/**
 * Created by CoXier on 2016/5/7.
 */
public interface VideoOnClickListener {
    void playVideo(Video video);

    void refreshCategory(int index);
}
