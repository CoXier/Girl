package com.hackerli.retrofit.module.showvideo;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
    private List<Video> videoList;
    private VideoOnClickListener clickListener;

    public VideoAdapter(Fragment mFragment, List<Video> videoList) {
        this.mFragment = mFragment;
        this.videoList = videoList;
        clickListener = (VideoOnClickListener) mFragment;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layout,parent,false);
        VideoHolder videoHolder = new VideoHolder(view);
        return videoHolder;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        Video video = videoList.get(position);
        holder.tvTitle.setText(video.getVideoTitle());
        holder.tvDesc.setText(video.getVideoDesc());
        Glide.with(mFragment).load(video.getVideoPhotoUrl()).into(holder.imageView);

        setUpCardViewListener(holder.cardView,video);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void setUpCardViewListener(CardView cardView, final Video video) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener!=null)  clickListener.playVideo(video);
                }
            });
    }

    class VideoHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.card)
        CardView cardView;
        @Bind(R.id.iv_video)
        ImageView imageView;
        @Bind(R.id.tv_video_title)
        TextView tvTitle;
        @Bind(R.id.tv_video_desc)
        TextView tvDesc;

        public VideoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
