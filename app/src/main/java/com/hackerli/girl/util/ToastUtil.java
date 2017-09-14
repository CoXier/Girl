package com.hackerli.girl.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by CoXier on 2016/5/18.
 */
public class ToastUtil {
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
