package com.hackerli.retrofit.module.showgirl;

import android.support.annotation.Nullable;

import com.hackerli.retrofit.base.BasePresenter;
import com.hackerli.retrofit.base.BaseView;

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
