package com.hackerli.girl.module.showgank.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hackerli.girl.R;
import com.hackerli.girl.base.BaseRVAdapter;
import com.hackerli.girl.data.entity.Android;
import com.hackerli.girl.data.entity.AndroidWrapper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.gujun.android.taggroup.TagGroup;

/**
 * Created by CoXier on 2016/5/2.
 */
public class GankAdapter extends BaseRVAdapter<String, GankAdapter.GankHolder> {
    private List<AndroidWrapper> mAndroidWrappers;
    private Fragment mFragment;
    private GankOnClickListener mGankListener;

    public GankAdapter(Fragment mFragment, List<AndroidWrapper> mAndroidWrappers) {
        this.mFragment = mFragment;
        this.mAndroidWrappers = mAndroidWrappers;
        mGankListener = (GankOnClickListener) mFragment;

    }

    @Override
    public GankHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gank, parent, false);
        return new GankHolder(v);
    }

    @Override
    public void onBindViewHolder(GankHolder holder, int position) {
        Android android = mAndroidWrappers.get(position).getAndroid();
        String author = android.getWho() == null ? "无名好汉" : android.getWho();
        holder.mAuthor.setText(author);
        holder.mTitle.setText(android.getDesc());

        setOnItemClickListener(holder.mItemView, android.getUrl());
        // 给每篇干货 设置标签
        setTag(holder.mTagGroup, android.getDesc(), android.getUrl());

        // 作者的头像
        setAvatar(holder, mAndroidWrappers.get(position));

    }

    @Override
    public int getItemCount() {
        return mAndroidWrappers.size();
    }

    private void setAvatar(final GankHolder holder, final AndroidWrapper wrapper) {
        holder.mImageView.setImageResource(android.R.color.transparent);
        if (wrapper.getAvatarUrl() == null) {
            holder.mImageView.setImageResource(R.drawable.ic_person);
        } else {
            Glide.with(mFragment).load(wrapper.getAvatarUrl()).into(holder.mImageView);
        }

    }

    private void setTag(TagGroup tag, String title, String url) {

        if (title.contains("源码解析") || title.contains("分析源代码") || title.contains("源代码分析")) {
            tag.setTags("源码解析");
        } else if (url.contains("https://github.com/") && title.contains("项目")) {
            tag.setTags("开源项目");
        } else if (url.contains("https://github.com/")) {
            tag.setTags("开源库");
        } else if (url.contains("https://zhuanlan.zhihu.com/")) {
            tag.setTags("知乎专栏");
        } else {
            tag.setTags("干货");
        }


    }

    @Override
    protected void setOnItemClickListener(View view, final String data) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGankListener != null) mGankListener.viewGankOnWebView(data);
            }
        });
    }

    class GankHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_author)
        CircleImageView mImageView;
        @BindView(R.id.tv_author)
        TextView mAuthor;
        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.tag_group)
        TagGroup mTagGroup;
        View mItemView;

        GankHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mItemView = itemView;
        }
    }

}
