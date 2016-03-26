package com.hackerli.retrofit.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/3/20.
 */
public class Girl {
    /**
     * _id : 56e8d0bb67765933d8be90be
     * _ns : ganhuo
     * createdAt : 2016-03-16T11:19:23.692Z
     * desc : 3.16
     * publishedAt : 2016-03-17T11:14:16.306Z
     * source : chrome
     * type : 福利
     * url : http://ww4.sinaimg.cn/large/7a8aed7bjw1f1yjc38i9oj20hs0qoq6k.jpg
     * used : true
     * who : 张涵宇
     */

    @SerializedName("_id")
    private String id;
    @SerializedName("_ns")
    private String ns;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
