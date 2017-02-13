package com.hackerli.retrofit.module.showvideo.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hackerli.retrofit.R;
import com.hackerli.retrofit.data.VideoData;
import com.hackerli.retrofit.data.entity.Video;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoXier on 2016/5/7.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private Fragment mFragment;
    private VideoOnClickListener mClickListener;
    private ArrayList<VideoData> mVideoDatas;


    private static int VIDEO_NORMAL = 0;
    private static int VIDEO_HEAD = 1;
    private static int VIDEO_FOOT = 2;


    public VideoAdapter(Fragment mFragment) {
        this.mFragment = mFragment;
        mClickListener = (VideoOnClickListener) mFragment;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIDEO_NORMAL) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        } else if (viewType == VIDEO_HEAD) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_head, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_foot, parent, false);
        }
        VideoHolder videoHolder = new VideoHolder(view, viewType);
        return videoHolder;
    }

    @Override
    public void onBindViewHolder(final VideoHolder holder, int position) {
        int type = getItemViewType(position);
        final int index = position / 6;
        if (type == VIDEO_NORMAL) {
            Video video = mVideoDatas.get(index).getVideos().get(position - index * 6 - 1);
            holder.tvTitle.setText(video.getVideoTitle());
            Glide.with(mFragment).load(video.getVideoPhotoUrl()).into(holder.imageView);

            setUpCardViewListener(holder.cardView, video);
        } else if (type == VIDEO_HEAD) {
            holder.tvHead.setText(mVideoDatas.get(index).getChannel());
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    refreshCategory(index, holder.footImageView, holder.footTextView);

                }
            });
        }

    }

    private void refreshCategory(final int index, View imageView, final TextView textView) {
        RotateAnimation anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        //Setup anim with desired properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(600); //Put desired duration per anim cycle here, in milliseconds

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                textView.setText("嘿咻嘿咻～");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mClickListener.refreshCategory(index);
                textView.setText("换一波推荐");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Start animation
        imageView.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return mVideoDatas.size() * 6;
    }

    @Override
    public int getItemViewType(int position) {
        int index = position % 6;
        if (index == 0)
            return VIDEO_HEAD;
        else if (index == 5)
            return VIDEO_FOOT;
        return VIDEO_NORMAL;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == VIDEO_HEAD || getItemViewType(position) == VIDEO_FOOT)
                        return gridLayoutManager.getSpanCount();
                    return 1;
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

    public void setVideoDatas(ArrayList<VideoData> videoDatas) {
        this.mVideoDatas = videoDatas;
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.card)
        CardView cardView;
        @Bind(R.id.iv_video)
        ImageView imageView;
        @Bind(R.id.tv_video_title)
        TextView tvTitle;

        TextView tvHead;

        ImageView footImageView;
        TextView footTextView;

        VideoHolder(View itemView, int type) {
            super(itemView);
            if (type == VIDEO_NORMAL)
                ButterKnife.bind(this, itemView);
            else if (type == VIDEO_HEAD) {
                tvHead = (TextView) itemView.findViewById(R.id.tv_video_head);
            } else {
                footImageView = (ImageView) itemView.findViewById(R.id.iv_refresh_category);
                footTextView = (TextView) itemView.findViewById(R.id.tv_refresh_category);
            }
        }
    }


}
