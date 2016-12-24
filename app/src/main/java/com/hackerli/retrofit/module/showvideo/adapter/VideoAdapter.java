package com.hackerli.retrofit.module.showvideo.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hackerli.retrofit.R;
import com.hackerli.retrofit.data.entity.Video;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoXier on 2016/5/7.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private Fragment mFragment;
    private List<Video> mVideoList;
    private List<String> mHeads;
    private VideoOnClickListener mClickListener;

    private static int VIDEO_NORMAL = 0;
    private static int VIDEO_HEAD = 1;


    public VideoAdapter(Fragment mFragment, List<Video> videoList) {
        this.mFragment = mFragment;
        this.mVideoList = videoList;
        mClickListener = (VideoOnClickListener) mFragment;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        if (viewType == VIDEO_NORMAL) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_head,parent,false);
        }
        VideoHolder videoHolder = new VideoHolder(view,viewType);
        return videoHolder;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == VIDEO_NORMAL){
            int index = position/4 + 1;
            Video video = mVideoList.get(position - index );
//            Log.d("TAG",video.getVideoTitle()+"  "+video.getVideoUrl());
            holder.tvTitle.setText(video.getVideoTitle());
            Glide.with(mFragment).load(video.getVideoPhotoUrl()).into(holder.imageView);

            setUpCardViewListener(holder.cardView, video);
        }else {
            holder.tvHead.setText("HH");
        }

    }

    @Override
    public int getItemCount() {
        return mVideoList.size()/3 + mVideoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 4 == 0)
            return VIDEO_HEAD;
        return VIDEO_NORMAL;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position)==VIDEO_HEAD?gridLayoutManager.getSpanCount():1;
                }
            });
        }
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void setUpCardViewListener(CardView cardView, final Video video) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) mClickListener.playVideo(video);
            }
        });
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.card)
        CardView cardView;
        @Bind(R.id.iv_video)
        ImageView imageView;
        @Bind(R.id.tv_video_title)
        TextView tvTitle;

        TextView tvHead;

        VideoHolder(View itemView, int type) {
            super(itemView);
            if (type == VIDEO_NORMAL)
                ButterKnife.bind(this, itemView);
            else{
                tvHead = (TextView) itemView.findViewById(R.id.tv_video_head);
            }
        }
    }


}
