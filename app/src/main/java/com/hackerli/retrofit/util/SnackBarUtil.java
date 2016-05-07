package com.hackerli.retrofit.util;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.hackerli.retrofit.BaseFragment;

/**
 * Created by CoXier on 2016/5/2.
 */
public class SnackBarUtil {
    public static void showSnackBar(View view, final BaseFragment baseFragment){
        Snackbar.make(view, "加载失败，请重试", Snackbar.LENGTH_LONG)
                .setAction("重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        baseFragment.onRefresh();
                    }
                })
                .show();
    }
}
