package com.hackerli.retrofit.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by CoXier on 2016/5/21.
 */
public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    private static int started = 0;
    private static int stopped = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public static boolean isBackground(){
        if (stopped == started)
            return true;
        return false;
    }
}
