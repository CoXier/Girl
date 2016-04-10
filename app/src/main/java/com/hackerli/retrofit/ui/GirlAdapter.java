package com.hackerli.retrofit.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hackerli.retrofit.R;
import com.hackerli.retrofit.data.entity.Girl;
import com.hackerli.retrofit.widget.RatioImageview;

import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/20.
 */
public class GirlAdapter extends RecyclerView.Adapter<GirlAdapter.ViewHolder> {
    private Context mContext;
    private List<Girl> mList;


    private GirlOnClickListener mOnClickListenr;


    public GirlAdapter(Context mContext, List<Girl> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mOnClickListenr = (GirlOnClickListener) mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gril_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Girl girl = mList.get(position);

        Glide.with(mContext)
                .load(girl.getUrl())
                .centerCrop()
                .into(holder.imageView);
        String desc = girl.getDesc();
        String title = desc.length() > 40 ? desc.substring(0, 40) + "......" : desc;
        holder.textView.setText(title);

        setOnClickListener(holder, girl);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_girl)
        RatioImageview imageView;
        @Bind(R.id.tv_title)
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Random random = new Random();
            int originalHeight = random.nextInt(10) + 50;
            imageView.setOriginalSize(50, originalHeight);
        }


    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public void setOnClickListener(ViewHolder viewHolder, final Girl girl) {
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListenr.viewGirlPhoto(girl);
            }
        });

        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListenr.viewCSMaterial();
            }
        });
    }


}
