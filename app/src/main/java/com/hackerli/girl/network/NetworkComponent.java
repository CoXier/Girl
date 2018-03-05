package com.hackerli.girl.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by lijianxin on 2018/3/5.
 */

public class NetworkComponent {
    private static OkHttpClient sOkHttpClient;
    private static volatile NetworkComponent sInstance;

    private NetworkComponent() {
        sOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static NetworkComponent getInstance() {
        if (sInstance == null) {
            synchronized (NetworkComponent.class) {
                if (sInstance == null) {
                    sInstance = new NetworkComponent();
                }
            }
        }
        return sInstance;
    }

    public OkHttpClient getClient() {
        return sOkHttpClient;
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
