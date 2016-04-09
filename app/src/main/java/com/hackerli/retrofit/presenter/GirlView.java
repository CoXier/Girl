package com.hackerli.retrofit.presenter;

import android.support.annotation.Nullable;

import com.hackerli.retrofit.data.entity.Girl;

import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public interface GirlView {
    public void loadMore(@Nullable List<Girl> girls);

    public void finishRefresh();

    public void showSnackBar();
}
