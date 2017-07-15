package com.hackerli.girl.module.showvideo;

import android.os.AsyncTask;

import com.hackerli.girl.data.entity.Video;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CoXier on 16-12-24.
 */

public class VideoPresenter implements VideoContract.Presenter {

    private List<String> mHeads;
    private List<String> mHeadUrlList;
    private List<Video> mVideos;
    private List<LoadYoukuVideosTask> mTasks;

    private VideoContract.View mView;

    private boolean mNetworkBroken = false;

    private final static String sYoukuChannel = "http://fun.youku.com/?spm=a2hfu.20023297.topNav.5~1~3!21~A";

    private final static String sUserAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0";

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
        channelTask.execute(sYoukuChannel);
    }

    private class LoadYoukuVideosTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                mNetworkBroken = false;
                Document document = Jsoup.connect(params[0])
                        .userAgent(sUserAgent)
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
                mNetworkBroken = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mVideos.size() != 0)
                mView.showVideos(mVideos, mHeads.get(0));
            mHeads.remove(0);
            mTasks.remove(0);
            mHeadUrlList.remove(0);

            if (!mTasks.isEmpty()) {
                mVideos.clear();
                mTasks.get(0).execute(mHeadUrlList.get(0));
            }
        }
    }

    private class LoadYoukuChannelTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                mNetworkBroken = false;
                Document document = Jsoup.connect(params[0])
                        .userAgent(sUserAgent)
                        .timeout(7000)
                        .get();
                Elements elements = document.getElementsByClass("g-content");
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
                mNetworkBroken = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!mNetworkBroken) {
                mTasks.get(0).execute(mHeadUrlList.get(0));
            }
        }
    }
}
