package com.lasalle2020android.travelcalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
        mTopToolbar.setTitle(R.string.toolBarTitle_tripInfoEdit);
        setSupportActionBar(mTopToolbar);

        Intent intent = getIntent();
        dataIndex = intent.getIntExtra("DataIndex", -1);

        countryConfig = new CountryConfigAccess(TripInfoEditActivity.this);

        mCountryPicker = CountryPicker.newInstance("Select Country", TripInfoEditActivity.this);
        mCountryPicker.setCountryList(countryConfig.getCountriesList());
        mCountryPicker.setListener(this);

        if (dataIndex != -1) { // For edit
            // Get data from db
            new DatabaseOperations_Thread(TripInfoEditActivity.this, Constants.TABLE.TRIPINFO,
                    Constants.DATABASE_OPERATION.FETCH_SELECTED, this, dataIndex).execute();
        }
        else { // For create
            initialViewObjects();
        }
    }

    @Override
    public void onBackPressed() {
        if (fieldCheck()) {
            new AlertDialog.Builder(TripInfoEditActivity.this)
            .setTitle(R.string.backWithoutSave_NoticeTitle)
            .setMessage(R.string.backWithoutSave_NoticeContent)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    TripInfoEditActivity.super.onBackPressed();
                }
            })
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
        }
        else {
            super.onBackPressed();
        }
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
                            Constants.DATABASE_OPERATION.UPDATE_RECORD, this, dataIndex).execute();
                }
                else { // For create, add db
                    if (inputCheck()) {
                        new DatabaseOperations_Thread(TripInfoEditActivity.this, Constants.TABLE.TRIPINFO,
                                Constants.DATABASE_OPERATION.ADD_RECORD, this, tripInfo, null).execute();
                        showSaveSuccessDialog();
                    }
                }
            }
            break;
        }
        return super.onOptionsItemSelected(item);
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

    private void clearAllFields() {
        tripNameField.setText("");
        countryTaxField.setText("");
        bfTipField.setText("");
        lnTipField.setText("");
        DnTipField.setText("");
        travelCountry.setText("");
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

    private boolean inputCheck() {
        boolean completeInput = false;

        if (fieldCheck()) {
            completeInput = true;
            tripInfo = new TripInfoModel();
            tripInfo.setTripName(tripNameField.getText().toString());
            tripInfo.setTravelCountry(selectedCountry.getId());
            tripInfo.setTax(Float.valueOf(countryTaxField.getText().toString()));
            tripInfo.setBreakfastTip(Float.valueOf(bfTipField.getText().toString()));
            tripInfo.setLunchTip(Float.valueOf(lnTipField.getText().toString()));
            tripInfo.setDinnerTip(Float.valueOf(DnTipField.getText().toString()));
        }
        else {
            Toast.makeText(TripInfoEditActivity.this, R.string.notice_FillupFields, Toast.LENGTH_LONG).show();
        }
        return completeInput;
    }

    private boolean fieldCheck() {
        boolean isFilled = false;
        if (!tripNameField.getText().equals("") && !countryTaxField.getText().equals("") && !bfTipField.getText().equals("")
                && !bfTipField.getText().equals("") && !lnTipField.getText().equals("") && !DnTipField.getText().equals("") && !travelCountry.getText().equals("")) {
            isFilled = true;
        }
        return isFilled;
    }

    private void showSaveSuccessDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.notice_saveSuccessTitle);
        alertDialog.setMessage(R.string.notice_saveSuccessContent);

        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    // region Database callback function
    @Override
    public void onSavePerformed(boolean isCompletedSuccessfully) {
        clearAllFields();
    }

    @Override
    public void onCurrencyRetrivePerformed(boolean isSuccess) {
        clearAllFields();
    }

    @Override
    public void onDB_BootCompleted(boolean isSuccess) {
        clearAllFields();
    }

    @Override
    public void onDeletePerformed(boolean isSuccess) {
        clearAllFields();
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

        travelCountry.setText(displayName);

        mCountryPicker.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        clearAllFields();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        clearAllFields();
    }
    // endregion
}
