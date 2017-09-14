package com.hackerli.girl.base;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by CoXier on 2016/5/2.
 */
public abstract class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public abstract void setRecyclerView();

    public abstract void setSwipeRefreshLayout();
}
