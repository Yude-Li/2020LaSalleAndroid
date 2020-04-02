package com.lasalle2020android.travelcalculator;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserSettingActivity extends AppCompatActivity {

    TextView textView_RealtimeUpdate_Info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings_layout);

        textView_RealtimeUpdate_Info = findViewById(R.id.textView_RealtimeUpdate_Info);

        String updateInfo = "On: the currency is updated only while using wifi.\n" +
                            "Off: the currency is updated whenever there is a connection.";

        textView_RealtimeUpdate_Info.setText(updateInfo);
    }
}
