package com.hackerli.retrofit.module.showgirl;

import android.support.annotation.Nullable;

import com.hackerli.retrofit.BasePresenter;
import com.hackerli.retrofit.BaseView;
import com.hackerli.retrofit.module.showgank.GankContract;

import java.util.List;

/**
 * Created by CoXier on 2016/5/7.
 */
public interface GirlContract {
    interface View extends BaseView<GirlContract.Presenter>{
        public void showMore(@Nullable List list);

        public void finishRefresh();

        public void showSnackBar();
    }

    interface Presenter extends BasePresenter{

    }
}
