package com.lasalle2020android.travelcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import Countries.CountryPicker;
import Countries.CountryPickerLietener;
import DataConfig.CountryConfigAccess;
import DataConfig.SettingConfigAccess;
import Model.CountryModel;

public class UserSettingActivity extends AppCompatActivity implements CountryPickerLietener {

    private Switch switchUpdateRealTime;
    private TextView originalCountryTextView;

    private SettingConfigAccess settingConfig;
    private CountryModel selectedCountry = null;
    private CountryConfigAccess countryConfig;
    private CountryPicker mCountryPicker;

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
                    settingConfig.setUpdateWiFiOnly(true);
                }
                else {
                    // User update whenever is a connection
                    settingConfig.setUpdateWiFiOnly(false);
                }
            }
        });

        countryConfig = new CountryConfigAccess(UserSettingActivity.this);

        mCountryPicker = CountryPicker.newInstance("Select Country", UserSettingActivity.this);
        mCountryPicker.setCountryList(countryConfig.getCountriesList());
        mCountryPicker.setListener(this);

        settingConfig = new SettingConfigAccess(getApplicationContext());
        originalCountryTextView = findViewById(R.id.usersetting_textview_countrylist);

        for (CountryModel c: countryConfig.getCountriesList()) {
            if (c.getId() == settingConfig.getOriginalCountryId()) {
                originalCountryTextView.setText(c.getDisplayName());
                break;
            }
        }

        SelectTravelCountry();
    }

    private void SelectTravelCountry() {
        originalCountryTextView.setOnClickListener(new TextView.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });
    }

    @Override
    public void onSelectCountry(int Id, String displayName, int countryNum, String countryCode, String currencyCode, double currency, String currencyName, String currencySymbol, int flagDrawableResID) {
        selectedCountry = new CountryModel();
        selectedCountry.setId(Id);
        selectedCountry.setDisplayName(displayName);
        selectedCountry.setCountryNum(countryNum);
        selectedCountry.setCountryCode(countryCode);
        selectedCountry.setCurrencyCode(currencyCode);
        selectedCountry.setCurrency(currency);
        selectedCountry.setCurrencyName(currencyName);
        selectedCountry.setCurrencySymbol(currencySymbol);
        selectedCountry.setFlagId(String.valueOf(flagDrawableResID));

        originalCountryTextView.setText(displayName);

        settingConfig.setOriginalCountryId(Id);

        mCountryPicker.dismiss();
    }
}
