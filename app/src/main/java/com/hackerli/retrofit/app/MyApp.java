package com.hackerli.retrofit.app;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

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
        FlowManager.init(new FlowConfig.Builder(this).openDatabasesOnInit(true).build());
    }

    public static boolean isBackground(){
        return MyLifecycleHandler.isBackground();
    }
}
