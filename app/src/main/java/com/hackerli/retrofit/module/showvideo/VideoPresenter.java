package com.hackerli.retrofit.module.showvideo;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.hackerli.retrofit.data.entity.Video;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;

/**
 * Created by CoXier on 16-12-24.
 */

public class VideoPresenter implements VideoContract.Presenter {

    private List<String> mHeads;
    private List<String> mHeadUrlList;
    private List<Video> mVideos;
    private List<LoadYoukuVideosTask> mTasks;

    private VideoContract.View mView;

    public VideoPresenter(VideoContract.View view) {
        this.mView = view;
    }

    @Override
    public void loadYoukuVideos() {
        mHeads = new ArrayList<>();
        mVideos = new ArrayList<>();
        mHeadUrlList = new ArrayList<>();
        mTasks = new ArrayList<>();

        LoadYoukuChannelTask channelTask = new LoadYoukuChannelTask();
        channelTask.execute("http://fun.youku.com/?spm=a2hfu.20023297.topNav.5~1~3!21~A");
    }

    @Override
    public String extractRealUrl(String url) {
        return null;
    }

    class LoadYoukuVideosTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                Document document = Jsoup.connect(params[0])
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                        .header("Cookie", "__ysuid=14798669834357C8; ysestep=1; yseidcount=2; ystep=2; juid=01b4najpfp6mt; __aysid=1482546604362PFc; __ayspstp=2; __ali=14825466062231u8; __aliCount=1; cna=jPa7ECWH9FUCAd4UA4/SyKVz; ypvid=1482586265845XiGc1r; yseid=1482586265845sT67La; yseidtimeout=1482593465846; ycid=0; seid=01b4oge67o2no6; referhost=; seidtimeout=1482588065849; __ayft=1482586266347; __arpvid=1482586266347P6epCh-1482586266354; __arycid=cms-00-904-23747-0; __ayscnt=1; __arcms=cms-00-904-23747-0; __aypstp=1; ykss=9a785e58f70e26df5e2a41fd")
                        .timeout(7000)
                        .get();
                Elements elements = document.getElementsByClass("v-link");
                for (Element element : elements) {
                    String title, videoUrl;
                    Pattern pTitle = Pattern.compile("(?<=title=\")[^\"]*");
                    Pattern pVideoUrl = Pattern.compile("(?<=<a href=\")[^\"]*");

                    Matcher matcher = pTitle.matcher(element.child(0).toString());
                    matcher.find();
                    title = matcher.group(0);
                    matcher = pVideoUrl.matcher(element.child(0).toString());
                    matcher.find();
                    videoUrl = matcher.group(0);
                    videoUrl = "http://v.rpsofts.com/i.php?url=" + videoUrl;

                    Video video = new Video();
                    video.setVideoTitle(title);
                    video.setVideoUrl(videoUrl);
                    mVideos.add(video);
                }
                Elements photoElements = document.getElementsByClass("v-thumb");


                int i = 0;
                for (Element element : photoElements) {
                    Pattern pPhotoUrl = Pattern.compile("(?<=src=\")[^\"]*");
                    Matcher matcher = pPhotoUrl.matcher(element.child(0).toString());
                    matcher.find();
                    String photoUrl = matcher.group(0).contains("https") ? matcher.group(0) : "https:" + matcher.group(0);
                    mVideos.get(i++).setVideoPhotoUrl(photoUrl);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mView.showVideos(mVideos,mHeads.get(0));
            mHeads.remove(0);
            mTasks.remove(0);
            mHeadUrlList.remove(0);

            if (!mTasks.isEmpty()){
                mVideos.clear();
                mTasks.get(0).execute(mHeadUrlList.get(0));
            }
        }
    }

    class LoadYoukuChannelTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                Document document = Jsoup.connect(params[0])
                        .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0")
                        .header("Cookie", "__ysuid=1479897238517wnV; ysestep=5; yseidcount=2; ystep=6; juid=01b4najpfp6mt; __aysid=1482546604362PFc; __ayspstp=6; __ali=14825466062231u8; __aliCount=1; cna=jPa7ECWH9FUCAd4UA4/SyKVz")
                        .timeout(7000)
                        .get();
                Elements elements = document.getElementsByClass("yk-content");
                Element element = elements.get(1).child(0);
                Elements children = element.children();
                children.remove(0);
                for (Element child : children) {
                    mHeads.add(child.child(0).text());
                    Pattern pattern = Pattern.compile("(?<=<a href=\")[^\"]*");
                    Matcher matcher = pattern.matcher(child.child(0).toString());
                    matcher.find();
                    String url = matcher.group(0).contains("http:") ? matcher.group(0) : "http:" + matcher.group(0);
                    mHeadUrlList.add(url);
                    LoadYoukuVideosTask loadYoukuVideosTask = new LoadYoukuVideosTask();
                    mTasks.add(loadYoukuVideosTask);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mTasks.get(0).execute(mHeadUrlList.get(0));
        }
    }
}
