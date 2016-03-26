package com.hackerli.retrofit.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2016/3/25.
 */
public class ScreenUtils {
    public static int getScreenWidth(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        return w_screen;
    }
}
