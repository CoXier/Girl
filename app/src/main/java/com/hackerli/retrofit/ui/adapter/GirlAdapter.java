package com.hackerli.retrofit.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hackerli.retrofit.R;
import com.hackerli.retrofit.data.entity.Girl;
import com.hackerli.retrofit.ui.listener.GirlOnClickListener;
import com.hackerli.retrofit.widget.RatioImageview;

import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoXier on 2016/3/20.
 */
public class GirlAdapter extends RecyclerView.Adapter<GirlAdapter.GirlHolder> {
    private Fragment mFragment;
    private List<Girl> mList;


    private GirlOnClickListener mOnClickListenr;


    public GirlAdapter(Fragment fragment, List<Girl> mList) {
        this.mFragment = fragment;
        this.mList = mList;
        mOnClickListenr = (GirlOnClickListener) mFragment;
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
        RatioImageview imageView;
        @Bind(R.id.tv_date)
        TextView textView;

        public GirlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Random random = new Random();
            int originalHeight = random.nextInt(10) + 50;
            imageView.setOriginalSize(50, originalHeight);
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
                mOnClickListenr.viewGirlPhoto(girl);
            }
        });

    }


}
