package com.hackerli.retrofit.module.showgank.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hackerli.retrofit.R;
import com.hackerli.retrofit.data.entity.Android;
import com.hackerli.retrofit.data.entity.AndroidWrapper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.gujun.android.taggroup.TagGroup;

/**
 * Created by CoXier on 2016/5/2.
 */
public class AndroidAdapter extends RecyclerView.Adapter<AndroidAdapter.GankHolder> {
    private List<AndroidWrapper> mAndroidWrappers;
    private Fragment mFragment;
    private GankOnClickListener gankListener;

    public AndroidAdapter(Fragment mFragment, List<AndroidWrapper> mAndroidWrappers) {
        this.mFragment = mFragment;
        this.mAndroidWrappers = mAndroidWrappers;
        gankListener = (GankOnClickListener) mFragment;

    }

    @Override
    public GankHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gank_layout, parent, false);
        GankHolder gankHolder = new GankHolder(v);
        return gankHolder;
    }

    @Override
    public void onBindViewHolder(GankHolder holder, int position) {
        Android android = mAndroidWrappers.get(position).getAndroid();
        String author = android.getWho() == null ? "无名好汉" : android.getWho();
        holder.author.setText(author);
        holder.title.setText(android.getDesc());

        setOnClickListener(holder.itemView, android.getUrl());
        // 给每篇干货 设置标签
        setTag(holder.tagGroup, android.getDesc(), android.getUrl());

        // 作者的头像
        setAvatar(holder, mAndroidWrappers.get(position));

    }

    @Override
    public int getItemCount() {
        return mAndroidWrappers.size();
    }

    private void setOnClickListener(View itemView, final String url) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gankListener != null) gankListener.viewGankOnWebView(url);
            }
        });
    }


    private void setAvatar(final GankHolder holder, final AndroidWrapper wrapper) {
        holder.imageView.setImageResource(android.R.color.transparent);
        if (wrapper.getAvatarUrl() == null) {
            final String url = wrapper.getAndroid().getUrl();
            if (url.contains("https://www.sdk.cn/")) {
                holder.imageView.setImageResource(R.drawable.sdkcn_logo);
            } else {
                holder.imageView.setImageResource(R.drawable.ic_person_black_24dp);
            }
        } else {
            Glide.with(mFragment).load(wrapper.getAvatarUrl()).into(holder.imageView);
        }

    }

    public void setTag(TagGroup tag, String title, String url) {

        if (title.contains("源码解析") || title.contains("分析源代码") || title.contains("源代码分析")) {
            tag.setTags(new String("源码解析"));
        } else if (url.contains("https://github.com/") && title.contains("项目")) {
            tag.setTags(new String("开源项目"));
        } else if (url.contains("https://github.com/")) {
            tag.setTags(new String("开源库"));
        } else if (url.contains("https://zhuanlan.zhihu.com/")) {
            tag.setTags(new String("知乎专栏"));
        } else {
            tag.setTags(new String("干货"));
        }


    }

    public class GankHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_author)
        CircleImageView imageView;
        @Bind(R.id.tv_author)
        TextView author;
        @Bind(R.id.tv_title)
        TextView title;
        @Bind(R.id.tag_group)
        TagGroup tagGroup;
        View itemView;

        public GankHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }

}
