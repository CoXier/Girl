package com.hackerli.girl.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lijianxin on 2017/9/28.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(int position);
}
