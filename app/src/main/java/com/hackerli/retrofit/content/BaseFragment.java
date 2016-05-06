package com.hackerli.retrofit.content;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.hackerli.retrofit.presenter.BaseView;

/**
 * Created by CoXier on 2016/5/2.
 */
public abstract class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,BaseView {
    public abstract void setRecyclerView();
    public abstract void setSwipeRefreshLayout();
}
