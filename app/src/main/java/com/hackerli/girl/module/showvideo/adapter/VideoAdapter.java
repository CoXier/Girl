package com.hackerli.girl.module.showvideo.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hackerli.girl.R;
import com.hackerli.girl.data.VideoData;
import com.hackerli.girl.data.entity.Video;

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


    private static int sVideoNormal = 0;
    private static int sVideoHead = 1;
    private static int sVideoFoot = 2;


    public VideoAdapter(Fragment mFragment) {
        this.mFragment = mFragment;
        mClickListener = (VideoOnClickListener) mFragment;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == sVideoNormal) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        } else if (viewType == sVideoHead) {
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
        if (type == sVideoNormal) {
            Video video = mVideoDatas.get(index).getVideos().get(position - index * 6 - 1);
            holder.mTVTitle.setText(video.getVideoTitle());
            Glide.with(mFragment).load(video.getVideoPhotoUrl()).into(holder.mImageView);

            setUpCardViewListener(holder.mCardView, video);
        } else if (type == sVideoHead) {
            holder.mTVHead.setText(mVideoDatas.get(index).getChannel());
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    refreshCategory(index, holder.mFootImageView, holder.mFootTextView);

                }
            });
        }

    }

    private void refreshCategory(final int index, View imageView, final TextView textView) {
        RotateAnimation anim =
                new RotateAnimation(0, 360,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);

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
            return sVideoHead;
        else if (index == 5)
            return sVideoFoot;
        return sVideoNormal;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == sVideoHead || getItemViewType(position) == sVideoFoot)
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
        CardView mCardView;
        @Bind(R.id.iv_video)
        ImageView mImageView;
        @Bind(R.id.tv_video_title)
        TextView mTVTitle;

        TextView mTVHead;

        ImageView mFootImageView;
        TextView mFootTextView;

        VideoHolder(View itemView, int type) {
            super(itemView);
            if (type == sVideoNormal)
                ButterKnife.bind(this, itemView);
            else if (type == sVideoHead) {
                mTVHead = (TextView) itemView.findViewById(R.id.tv_video_head);
            } else {
                mFootImageView = (ImageView) itemView.findViewById(R.id.iv_refresh_category);
                mFootTextView = (TextView) itemView.findViewById(R.id.tv_refresh_category);
            }
        }
    }


}
