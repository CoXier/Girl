package com.hackerli.retrofit.presenter;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public interface BaseView {
    public void showMore(@Nullable List list);

    public void finishRefresh();

    public void showSnackBar();
}
