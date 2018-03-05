package com.hackerli.girl.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lijianxin on 2018/3/5.
 */

public class NetworkComponent {
    private static NetworkComponent sInstance;

    private NetworkComponent() {

    }

    public static NetworkComponent getInstance() {
        if (sInstance == null) {
            synchronized (sInstance) {
                if (sInstance == null) {
                    sInstance = new NetworkComponent();
                }
            }
        }
        return sInstance;
    }

    public static boolean isNetConnected(Context context) {
        if (context == null) return false;
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (manager != null) {
            networkInfo = manager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}
