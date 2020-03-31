package com.lasalle2020android.travelcalculator;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

public class MyLifeCycleHandler implements ActivityLifecycleCallbacks {
    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
    public static int resumed;
    public static int paused;
    public static int started;
    public static int stopped;
    public static String activityName;

    public interface ApplicationStatusListener {
        public void applicationStatusCallback();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        activity.getLocalClassName();
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;

    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
    }

    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }

}
