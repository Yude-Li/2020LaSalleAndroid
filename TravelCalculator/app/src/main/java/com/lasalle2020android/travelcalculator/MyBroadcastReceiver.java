package com.lasalle2020android.travelcalculator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import DataConfig.SettingConfigAccess;
import threads.HttpServiceThread;
import threads.NetworkCheckStatus;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Getting connection change
        //boolean isAvailable = MyApplication.isActivityVisible();
        Log.d("MyBroadcastReceiver", "MyBroadcastReceiver is called");
        // Get user setting for data usage from SettingConfigAccess
        SettingConfigAccess settingConfigAccess = new SettingConfigAccess(context);
        boolean updateOnlyWifi = settingConfigAccess.getUpdateWiFiOnly();

        // Get current connection status, is connect to network or not
        NetworkCheckStatus networkCheckStatus = new NetworkCheckStatus();
        networkCheckStatus.setContext(context);
        networkCheckStatus.setCheckType(NetworkCheckStatus.CheckType.CHECK_ALL);
        networkCheckStatus.start();
        try{
            networkCheckStatus.join();
        }catch(InterruptedException ie){}

        if (networkCheckStatus.IsConnectionAvailable()) { // Check if the device is connect to network
            if (updateOnlyWifi) {
                // User want to update currency only when using wifi
                // if using wifi, update currency
                if (networkCheckStatus.IsConnectionWifi()) {
                    updateCurrencyFromApi(context);
                }
                else { // not using wifi, do nothing
                    return;
                }
            }
            else { // User accept update no matter what kind of connection is using
                // Update the currency
                updateCurrencyFromApi(context);
            }
        }
    }

    private void updateCurrencyFromApi(Context context) {
        // Call api from http thread and update
        HttpServiceThread httpServiceThread = new HttpServiceThread(context, HttpServiceThread.ActionMode.UPDATE_CURRENCY);
        httpServiceThread.start();
        try{
            httpServiceThread.join();
        }catch(InterruptedException ie){}
    }
}
