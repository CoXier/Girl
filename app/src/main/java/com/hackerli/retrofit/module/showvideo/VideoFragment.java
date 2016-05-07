package com.hackerli.retrofit.module.showvideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackerli.retrofit.R;
import com.hackerli.retrofit.web.WebActivity;
import com.hackerli.retrofit.api.VideoApiService;
import com.hackerli.retrofit.data.VideoData;
import com.hackerli.retrofit.data.entity.Video;
import com.hackerli.retrofit.ui.adapter.VideoAdapter;
import com.hackerli.retrofit.ui.listener.VideoOnClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoXier on 2016/5/2.
 */
public class VideoFragment extends Fragment implements VideoOnClickListener{


    @Bind(R.id.recycle_video)
    RecyclerView recycleView;

    private VideoAdapter videoAdapter;
    private List<Video> mVideoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        setUpRecycleView();
        loadVideoData();
        return view;
    }

    private void setUpRecycleView() {
        videoAdapter = new VideoAdapter(this, mVideoList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recycleView.setAdapter(videoAdapter);
        recycleView.setLayoutManager(manager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void loadVideoData() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.bmob.cn/1/classes/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        VideoApiService apiService = retrofit.create(VideoApiService.class);
        Observable observable = apiService.getVideoList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Subscriber<VideoData>() {
            @Override
            public void onCompleted() {
                recycleView.requestLayout();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(VideoData videoData) {
                List<Video> videos = videoData.getVideoList();
                Log.d("TAG",videos.size()+"");
                for (Video video:videos){
                    mVideoList.add(video);
                }
            }
        });
    }

    @Override
    public void playVideo(Video video) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("url",video.getVideoUrl());
        startActivity(intent);
    }
}
