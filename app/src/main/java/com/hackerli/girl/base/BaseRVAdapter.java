package com.hackerli.girl.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by CoXier on 17-3-7.
 */

public abstract class BaseRVAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {
    private boolean mIsScrolling;

    protected abstract void setOnItemClickListener(View view, T data);

    protected boolean isScrolling() {
        return mIsScrolling;
    }

    public void setScrollState(boolean isScrolling) {
        mIsScrolling = isScrolling;
    }
}
