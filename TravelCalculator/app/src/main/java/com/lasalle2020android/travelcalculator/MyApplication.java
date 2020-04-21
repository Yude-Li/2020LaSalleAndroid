package com.lasalle2020android.travelcalculator;

import android.app.Application;
import android.util.Log;

import DataConfig.CountryConfigAccess;
import DataConfig.SettingConfigAccess;

public class MyApplication extends Application {

    SettingConfigAccess settingConfig;
    CountryConfigAccess countryConfig;

    public boolean isActivityRunning=true;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Travel", "MyApplication onCreate");
        settingConfig = new SettingConfigAccess(getApplicationContext());
        settingConfig.initConfigData();

        countryConfig = new CountryConfigAccess(getApplicationContext());
        boolean fisrtTime = settingConfig.getIsFirstTime();
        countryConfig.initConfigData(fisrtTime);
        if (fisrtTime) {
            settingConfig.setIsFirstTime(false);

            // get all currency from api
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
