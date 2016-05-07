package com.hackerli.retrofit.data;

import com.google.gson.annotations.SerializedName;
import com.hackerli.retrofit.data.entity.Video;

import java.util.List;

/**
 * Created by CoXier on 2016/5/6.
 */
public class VideoData {


    /**
     * VideoTitle : 《super star》
     * createdAt : 2016-05-07 09:56:38
     * objectId : JBwHTTTU
     * updatedAt : 2016-05-07 11:38:30
     * videoDesc : 在交到男朋友之后，估计这段珍贵的视频就要被删除了
     * videoPhotoUrl : http://7xra01.com1.z0.glb.clouddn.com/%E5%8F%91%E7%96%AF%E7%9A%84%E5%A6%B9%E5%AD%90.PNG
     * videoUrl : http://v.rpsofts.com/i.php?url=http://v.youku.com/v_show/id_XMTU1NzY3MjA2OA==.html?from=s1.8-1-1.2
     */

    @SerializedName("results")
    private List<Video> videoList;

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

}
