package com.lasalle2020android.travelcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import Countries.CountryPicker;
import Countries.CountryPickerLietener;
import DataConfig.CountryConfigAccess;
import Model.CountryModel;
import Model.ExpenseModel;
import Model.TripInfoModel;
import callbacks.DatabaseOperationNotifier;
import commonutilities.Constants;
import databaseinteraction.DatabaseOperations_Thread;

public class TripInfoEditActivity extends AppCompatActivity  implements DatabaseOperationNotifier, CountryPickerLietener, AdapterView.OnItemSelectedListener {

    // View objects
    EditText tripNameField;
    EditText countryTaxField;
    EditText bfTipField;
    EditText lnTipField;
    EditText DnTipField;

    TextView travelCountry;

    // Variables
    int dataIndex = -1;
    TripInfoModel tripInfo = null;
    CountryModel selectedCountry = null;
    CountryConfigAccess countryConfig;
    private CountryPicker mCountryPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_info_layout);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_tripinfo);
        setSupportActionBar(mTopToolbar);

        Intent intent = getIntent();
        dataIndex = intent.getIntExtra("DataIndex", -1);

        countryConfig = new CountryConfigAccess(TripInfoEditActivity.this);

        mCountryPicker = CountryPicker.newInstance("Select Country", TripInfoEditActivity.this);
        mCountryPicker.setCountryList(countryConfig.getCountriesList());
        // mCurrencyPicker.setCurrenciesList(mSharedPrefrences.getStringSet("selectedCurrencies", new HashSet<String>()));

        mCountryPicker.setListener(this);

        if (dataIndex != -1) { // For edit
            // Get data from db
            new DatabaseOperations_Thread(TripInfoEditActivity.this, Constants.TABLE.TRIPINFO,
                    Constants.DATABSE_OPERATION.FETCH_SELECTED, this, dataIndex).execute();
        }
        else { // For create
            initialViewObjects();
        }
    }

    private void initialViewObjects() {
        tripNameField = findViewById(R.id.edittext_tripinfo_tripname);
        countryTaxField = findViewById(R.id.editText_tripinfo_travelcountry_tax);
        bfTipField = findViewById(R.id.editText_tripinfo_bf_tip);
        lnTipField = findViewById(R.id.editText_tripinfo_lunch_tip);
        DnTipField = findViewById(R.id.editText_tripinfo_dinner_tip);

        travelCountry = findViewById(R.id.tripinfo_countrylist);
        SelectTravelCountry();
    }

    private void initialViewObjectsWithValue(TripInfoModel mTrip) {
        // Get data from db and set to view objects
        tripNameField = findViewById(R.id.edittext_tripinfo_tripname);
        countryTaxField = findViewById(R.id.editText_tripinfo_travelcountry_tax);
        bfTipField = findViewById(R.id.editText_tripinfo_bf_tip);
        lnTipField = findViewById(R.id.editText_tripinfo_lunch_tip);
        DnTipField = findViewById(R.id.editText_tripinfo_dinner_tip);

        tripNameField.setText(mTrip.getTripName());
        countryTaxField.setText(String.valueOf(mTrip.getTax()));
        bfTipField.setText(String.valueOf(mTrip.getBreakfastTip()));
        lnTipField.setText(String.valueOf(mTrip.getLunchTip()));
        DnTipField.setText(String.valueOf(mTrip.getDinnerTip()));

        CountryModel country = countryConfig.getCountryById(mTrip.getTravelCountry());

        travelCountry = findViewById(R.id.tripinfo_countrylist);
        travelCountry.setText(country.getDisplayName());
        SelectTravelCountry();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tripinfo_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_saveTripInfo:
            {
                if (dataIndex != -1) { // For edit, update db
                    new DatabaseOperations_Thread(TripInfoEditActivity.this, Constants.TABLE.TRIPINFO,
                            Constants.DATABSE_OPERATION.UPDATE_RECORD, this, dataIndex).execute();
                }
                else { // For create, add db

                }
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SelectTravelCountry() {
        travelCountry.setOnClickListener(new TextView.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // User can not edit travel country
                if (dataIndex != -1) {  return; }

                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });
    }

    // region Database callback function
    @Override
    public void onSavePerformed(boolean isCompletedSuccessfully) {

    }

    @Override
    public void onCurrencyRetrivePerformed(boolean isSuccess) {

    }

    @Override
    public void onDB_BootCompleted(boolean isSuccess) {

    }

    @Override
    public void onDeletePerformed(boolean isSuccess) {

    }

    @Override
    public void getTrips_INFO(List<TripInfoModel> mAllTrips, int mCount, TripInfoModel mTrip) {
        if (mTrip != null) {
            initialViewObjectsWithValue(mTrip);
        }
        else {
            Toast.makeText(TripInfoEditActivity.this, getString(R.string.fetchDataFail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getExpenses_INFO(List<ExpenseModel> mAllExpense, int mCount, ExpenseModel mExpense) {

    }
    // endregion

    // region Dialog Fragment callback
    @Override
    public void onSelectCountry(int Id, String displayName, int countryNum, String countryCode, String currencyCode, float currency, String currencyName, String currencySymbol, int flagDrawableResID) {
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

        mCountryPicker.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    // endregion
}
