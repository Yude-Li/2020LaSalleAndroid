package com.lasalle2020android.travelcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class UserSettingActivity extends AppCompatActivity {

    Switch switchUpdateRealTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting_layout);

        switchUpdateRealTime = findViewById(R.id.switch_updateRealtime);
        switchUpdateRealTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // User only update by wifi
                }
                else {
                    // User update whenever is a connection
                }
            }
        });
    }
}
