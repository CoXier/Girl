package com.hackerli.retrofit.module.showgirl.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hackerli.retrofit.R;
import com.hackerli.retrofit.data.entity.Girl;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoXier on 2016/3/20.
 */
public class GirlAdapter extends RecyclerView.Adapter<GirlAdapter.GirlHolder> {
    private Fragment mFragment;
    private List<Girl> mList;
    private static int widthPixels;


    private GirlOnClickListener mOnClickListener;


    public GirlAdapter(Fragment fragment, List<Girl> mList) {
        this.mFragment = fragment;
        this.mList = mList;
        mOnClickListener = (GirlOnClickListener) mFragment;
        widthPixels = mFragment.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public GirlHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gril_layout, parent, false);
        GirlHolder girlHolder = new GirlHolder(v);
        return girlHolder;
    }

    @Override
    public void onBindViewHolder(GirlHolder holder, int position) {
        Girl girl = mList.get(position);
        Glide.with(mFragment)
                .load(girl.getUrl())
                .centerCrop()
                .into(holder.imageView);
        String desc = girl.getDesc();
        holder.textView.setText(desc);
        setOnClickListener(holder, girl);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class GirlHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_girl)
        ImageView imageView;
        @Bind(R.id.tv_date)
        TextView textView;

        public GirlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            float scale = (float) (Math.random() + 1);
            while(scale>1.6||scale<1.1){
                scale = (float) (Math.random() + 1);
            }
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.height = (int) (widthPixels * scale * 0.448);
            imageView.setLayoutParams(params);
        }

    }

    @Override
    public void onViewRecycled(GirlHolder holder) {
        super.onViewRecycled(holder);
    }

    public void setOnClickListener(GirlHolder girlHolder, final Girl girl) {
        girlHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.viewGirlPhoto(girl);
            }
        });

    }


}
