package com.hackerli.girl.data;

import com.google.gson.annotations.SerializedName;
import com.hackerli.girl.data.entity.Girl;

import java.util.List;

/**
 * Created by Administrator on 2016/3/19.
 */
public class GirlData {


    /**
     * error : false
     * results : [{"_id":"56eb5db867765933d9b0a8fc","_ns":"ganhuo","createdAt":"2016-03-18T09:45:28.259Z","desc":"3.18","publishedAt":"2016-03-18T12:18:39.928Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/7a8aed7bjw1f20ruz456sj20go0p0wi3.jpg","used":true,"who":"张涵宇"},{"_id":"56e8d0bb67765933d8be90be","_ns":"ganhuo","createdAt":"2016-03-16T11:19:23.692Z","desc":"3.16","publishedAt":"2016-03-17T11:14:16.306Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/7a8aed7bjw1f1yjc38i9oj20hs0qoq6k.jpg","used":true,"who":"张涵宇"},{"_id":"56e8ce3967765933d8be90bd","_ns":"ganhuo","createdAt":"2016-03-16T11:08:41.957Z","desc":"3.16","publishedAt":"2016-03-16T11:24:01.505Z","source":"chrome","type":"福利","url":"http://ww3.sinaimg.cn/large/610dc034gw1f1yj0vc3ntj20e60jc0ua.jpg","used":true,"who":"代码家"},{"_id":"56e764116776592d80511280","_ns":"ganhuo","createdAt":"2016-03-15T09:23:29.580Z","desc":"3.15","publishedAt":"2016-03-15T11:45:57.350Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/7a8aed7bjw1f1xad7meu2j20dw0ku0vj.jpg","used":true,"who":"张涵宇"},{"_id":"56e619a46776591744cf05c0","_ns":"ganhuo","createdAt":"2016-03-14T09:53:40.126Z","desc":"3.14","publishedAt":"2016-03-14T11:55:19.66Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/7a8aed7bjw1f1w5m7c9knj20go0p0ae4.jpg","used":true,"who":"张涵宇"},{"_id":"56e220ca67765966681b3a23","_ns":"ganhuo","createdAt":"2016-03-11T09:35:06.879Z","desc":"3.11--一周年快乐！！！","publishedAt":"2016-03-11T12:37:20.4Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/7a8aed7bjw1f1so7l2u60j20zk1cy7g9.jpg","used":true,"who":"张涵宇"},{"_id":"56e0f0e86776596669cc2511","_ns":"ganhuo","createdAt":"2016-03-10T11:58:32.298Z","desc":"3.10","publishedAt":"2016-03-10T12:54:31.68Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/7a8aed7bjw1f1rmqzruylj20hs0qon14.jpg","used":true,"who":"张涵宇"},{"_id":"56df891167765947765e2ad1","_ns":"ganhuo","createdAt":"2016-03-09T10:23:13.778Z","desc":"3.9","publishedAt":"2016-03-09T12:06:26.401Z","source":"chrome","type":"福利","url":"http://ww2.sinaimg.cn/large/7a8aed7bjw1f1qed6rs61j20ss0zkgrt.jpg","used":true,"who":"张涵宇"},{"_id":"56de2b1b6776592b6192bf46","_ns":"ganhuo","createdAt":"2016-03-08T09:30:03.578Z","desc":"3.8","publishedAt":"2016-03-08T12:55:59.161Z","source":"chrome","type":"福利","url":"http://ww3.sinaimg.cn/large/7a8aed7bjw1f1p77v97xpj20k00zkgpw.jpg","used":true,"who":"张涵宇"},{"_id":"56dd06b56776592b6246e979","_ns":"ganhuo","createdAt":"2016-03-07T12:42:29.664Z","desc":"3.7","publishedAt":"2016-03-07T12:49:24.470Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/7a8aed7bjw1f1o75j517xj20u018iqnf.jpg","used":true,"who":"张涵宇"}]
     */

    private boolean error;


    @SerializedName("results")
    private List<Girl> girls;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Girl> getGirls() {
        return girls;
    }

    public void setGirls(List<Girl> girls) {
        this.girls = girls;
    }


}
