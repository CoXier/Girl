package com.hackerli.girl.app;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by CoXier on 2016/5/21.
 */
public class GirlApp extends Application {
    private static final String APP_ID = "OLWHHM4o06lSRnqfHF8K5n94-gzGzoHsz";
    private static final String APP_KEY = "1aaz4nwvMpglAwY29wIUsYKY";

    @Override
    public void onCreate() {
        LifecycleHandler lifecycleHandler = new LifecycleHandler();
        registerActivityLifecycleCallbacks(lifecycleHandler);
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).openDatabasesOnInit(true).build());
        AVOSCloud.initialize(this, APP_ID, APP_KEY);
    }

    public static boolean isBackground() {
        return LifecycleHandler.isBackground();
    }
}
