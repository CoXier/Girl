package com.hackerli.retrofit.data.entity;

/**
 * Created by CoXier on 2016/5/6.
 */
public class Video {

    /**
     * VideoTitle : 励志滑板短片[梦想]
     * createdAt : 2016-05-07 11:58:25
     * objectId : 1Eeg0007
     * updatedAt : 2016-05-07 12:13:23
     * videoDesc : 梦想，人人都有。但并非每一个人都为其而活，也并非每一个人都敢大胆地去追寻属于自己的梦。别人只会看到你的成功，却看不见背后你为了成功而摔了多少次。From @SkeiCheung
     * videoPhotoUrl : http://7xra01.com1.z0.glb.clouddn.com/%E6%BB%91%E6%9D%BF%E6%A2%A6%E6%83%B3.PNG
     * videoUrl : http://v.rpsofts.com/i.php?url=http://v.youku.com/v_show/id_XMTU0NTYwMjIyNA==.html?from=s1.8-1-1.2NA==.html?from=s1.8-1-1.2
     */

    private String VideoTitle;
    private String videoDesc;
    private String videoPhotoUrl;
    private String videoUrl;

    public String getVideoTitle() {
        return VideoTitle;
    }

    public void setVideoTitle(String VideoTitle) {
        this.VideoTitle = VideoTitle;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
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
