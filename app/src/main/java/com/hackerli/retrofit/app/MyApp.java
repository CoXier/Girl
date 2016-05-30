package com.hackerli.retrofit.app;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by CoXier on 2016/5/21.
 */
public class MyApp extends Application {
    private MyLifecycleHandler myLifecycleHandler;
    private static final  String APP_ID = "OLWHHM4o06lSRnqfHF8K5n94-gzGzoHsz";
    private static final  String APP_KEY = "1aaz4nwvMpglAwY29wIUsYKY";

    @Override
    public void onCreate() {
        myLifecycleHandler = new MyLifecycleHandler();
        registerActivityLifecycleCallbacks(myLifecycleHandler);
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).openDatabasesOnInit(true).build());
        AVOSCloud.initialize(this,APP_ID,APP_KEY);
    }

    public static boolean isBackground(){
        return MyLifecycleHandler.isBackground();
    }
}
