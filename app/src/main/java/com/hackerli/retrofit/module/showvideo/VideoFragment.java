package com.hackerli.retrofit.module.showvideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackerli.retrofit.R;
import com.hackerli.retrofit.data.VideoData;
import com.hackerli.retrofit.data.entity.Video;
import com.hackerli.retrofit.module.showvideo.adapter.VideoAdapter;
import com.hackerli.retrofit.module.showvideo.adapter.VideoOnClickListener;
import com.hackerli.retrofit.web.WebActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoXier on 2016/5/2.
 */
public class VideoFragment extends Fragment implements VideoOnClickListener,VideoContract.View {


    @Bind(R.id.recycle_video)
    RecyclerView recycleView;

    private ArrayList<VideoData> mVideoList;

    private VideoAdapter mVideoAdapter;

    private VideoContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        initRecycleView();
        return view;
    }

    private void initRecycleView() {
        mVideoAdapter = new VideoAdapter(this);
        mVideoList = new ArrayList<>();
        mVideoAdapter.setVideoDatas(mVideoList);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),2);
        recycleView.setLayoutManager(manager);
        recycleView.setAdapter(mVideoAdapter);

        mPresenter = new VideoPresenter(this);
        mPresenter.loadYoukuVideos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void playVideo(Video video) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("url", video.getVideoUrl());
        startActivity(intent);
    }

    @Override
    public void refreshCategory(int index) {
        long time = System.nanoTime();
        Collections.shuffle(mVideoList.get(index).getVideos(),new Random(time));
        mVideoAdapter.notifyDataSetChanged();
    }

    @Override
    public void showVideos(List<Video> videos,String head) {
        ArrayList<Video> videoArrayList = new ArrayList<>();
        videoArrayList.addAll(videos);
        VideoData videoData = new VideoData(videoArrayList,head);
        mVideoList.add(videoData);
        mVideoAdapter.setVideoDatas(mVideoList);
        mVideoAdapter.notifyDataSetChanged();
    }
}
