package com.lasalle2020android.travelcalculator;

import android.app.Application;
import android.content.IntentFilter;
import android.util.Log;

import DataConfig.CountryConfigAccess;
import DataConfig.SettingConfigAccess;
import threads.HttpServiceThread;

public class MyApplication extends Application {

    SettingConfigAccess settingConfig;
    CountryConfigAccess countryConfig;

    public static boolean isActivityRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Travel", "MyApplication onCreate");
        isActivityRunning = true;
        settingConfig = new SettingConfigAccess(getApplicationContext());
        settingConfig.initConfigData();

        countryConfig = new CountryConfigAccess(getApplicationContext());
        boolean fisrtTime = settingConfig.getIsFirstTime();
        countryConfig.initConfigData(fisrtTime);

        if (fisrtTime) {
            settingConfig.setIsFirstTime(false);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static boolean isActivityVisible() {
        return isActivityRunning; // return true or false
    }

    public static void activityResumed() {
        isActivityRunning = true;// this will set true when activity resumed

    }

    public static void activityPaused() {
        isActivityRunning = false;// this will set false when activity paused

    }
}
