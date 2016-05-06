package com.hackerli.retrofit.data;

import com.google.gson.annotations.SerializedName;
import com.hackerli.retrofit.data.entity.Android;

import java.util.List;

/**
 * Created by CoXier on 2016/5/2.
 */
public class AndroidData {

    /**
     * error : false
     * results : [{"_id":"5722c77567765974f5e27e53","createdAt":"2016-04-29T10:31:17.87Z","desc":"Android中Enum分析使用","publishedAt":"2016-04-29T11:36:42.906Z","source":"chrome","type":"Android","url":"http://www.jianshu.com/p/6052cd4ea9ae","used":true,"who":"AndWang"},{"_id":"5722c61767765974f5e27e51","createdAt":"2016-04-29T10:25:27.959Z","desc":"使用Swift为Android构建代码","publishedAt":"2016-04-29T11:36:42.906Z","source":"web","type":"Android","url":"https://www.sdk.cn/news/3268","used":true,"who":null},{"_id":"5722a88a67765974fbfcf9b8","createdAt":"2016-04-29T08:19:22.33Z","desc":"一个切割时间的表盘","publishedAt":"2016-04-29T11:36:42.906Z","source":"chrome","type":"Android","url":"https://github.com/DuanTzXavier/conciseclock","used":true,"who":"蒋朋"},{"_id":"572232ab67765974fca830b8","createdAt":"2016-04-28T23:56:27.136Z","desc":"SearchView源码解析","publishedAt":"2016-04-29T11:36:42.906Z","source":"chrome","type":"Android","url":"https://github.com/nukc/SearchViewAnalysis","used":true,"who":"Jason"},{"_id":"57222ded67765974fbfcf9b7","createdAt":"2016-04-28T23:36:13.245Z","desc":"这是一个专用于解决Android中网络请求及图片加载的缓存处理框架","publishedAt":"2016-04-29T11:36:42.906Z","source":"chrome","type":"Android","url":"https://github.com/LittleFriendsGroup/KakaCache","used":true,"who":"Jason"},{"_id":"57222d4f67765974fbfcf9b6","createdAt":"2016-04-28T23:33:35.223Z","desc":"FloatingActionButton变种的工具栏","publishedAt":"2016-04-29T11:36:42.906Z","source":"chrome","type":"Android","url":"https://github.com/rubensousa/FloatingToolbar","used":true,"who":"Jason"},{"_id":"57222abf67765974fbfcf9b5","createdAt":"2016-04-28T23:22:39.267Z","desc":"文本与图片混排","publishedAt":"2016-04-29T11:36:42.906Z","source":"chrome","type":"Android","url":"https://github.com/Bilibili/drawee-text-view","used":true,"who":"Jason"},{"_id":"5722172c67765974f5e27e4b","createdAt":"2016-04-28T21:59:08.826Z","desc":"Depth 终于开源啦～","publishedAt":"2016-04-29T11:36:42.906Z","source":"chrome","type":"Android","url":"https://github.com/danielzeller/Depth-LIB-Android-","used":true,"who":"mthli"},{"_id":"5721dab067765974fca830b4","createdAt":"2016-04-28T17:41:04.289Z","desc":"Linux下/proc目录简介","publishedAt":"2016-04-29T11:36:42.906Z","source":"chrome","type":"Android","url":"http://blog.csdn.net/zdwzzu2006/article/details/7747977","used":true,"who":"LHF"},{"_id":"5721916567765974fbfcf9ad","createdAt":"2016-04-28T12:28:21.470Z","desc":"依赖注入及dagger2的学习指南","publishedAt":"2016-04-28T13:07:51.7Z","source":"chrome","type":"Android","url":"https://lber19535.github.io/2016/04/22/%E4%BE%9D%E8%B5%96%E6%B3%A8%E5%85%A5%E5%AD%A6%E4%B9%A0%E6%8C%87%E5%8D%97/","used":true,"who":"bill"}]
     */

    private boolean error;

    @SerializedName("results")
    private List<Android> data;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Android> getData() {
        return data;
    }

    public void setData(List<Android> data) {
        this.data = data;
    }


}
