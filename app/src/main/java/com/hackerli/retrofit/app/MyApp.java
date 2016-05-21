package com.hackerli.retrofit.app;

import android.app.Application;

/**
 * Created by CoXier on 2016/5/21.
 */
public class MyApp extends Application {
    private MyLifecycleHandler myLifecycleHandler;
    @Override
    public void onCreate() {
        myLifecycleHandler = new MyLifecycleHandler();
        registerActivityLifecycleCallbacks(myLifecycleHandler);
        super.onCreate();
    }

    public static boolean isBackground(){
        return MyLifecycleHandler.isBackground();
    }
}
