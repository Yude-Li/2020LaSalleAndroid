package com.lasalle2020android.travelcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import Model.ExpenseModel;
import Model.TripInfoModel;
import currencies.CurrencyPicker;
import currencies.CurrencyPickerListener;
import currencies.ExtendedCurrency;

public class ExpenseRecordEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, TextWatcher,
        CurrencyPickerListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private ImageView firstCountryImg, secondCountryImg;
    private EditText expenseNameEditText, spendAmountEditText, expenseDescEditText;
    private TextView convertedAmountTextView;
    private Spinner tripListSpinner;

    private CurrencyPicker mCurrencyPicker;
    private SharedPreferences mSharedPreferences;
    private boolean mCurrencySelectedFirst = false;

    ArrayList<TripInfoModel> listOfTrips;
    ArrayList<String> listOfTripNames;
    ArrayAdapter<String> tripInfoAdapter;


    private ExpenseModel mExpenseModel;
    // Date will be a pop-up dialog


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_record_layout);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_expenserecord);
        setSupportActionBar(mTopToolbar);

        initialize();
    //    receiveData();
    }

    private void initialize() {

        firstCountryImg = findViewById(R.id.expenserecord_imageview_firstcountry);
        firstCountryImg.setOnClickListener(this);
        secondCountryImg = findViewById(R.id.expenserecord_imageview_secondcountry);
        secondCountryImg.setOnClickListener(this);

        expenseNameEditText = findViewById(R.id.expenserecord_edittext_expensename);
        spendAmountEditText = findViewById(R.id.expenserecord_edittext_spendamount);
        spendAmountEditText.addTextChangedListener(this);
        expenseDescEditText = findViewById(R.id.expenserecord_edittext_desc);

        convertedAmountTextView = findViewById(R.id.expenserecord_textview_convertedamount);

        tripListSpinner = findViewById(R.id.expenserecord_spinner_tripname);
        tripListSpinner.setOnItemSelectedListener(this);

        listOfTrips = new ArrayList<TripInfoModel>();
        ////********** get data from MainActivity


        ArrayList<ExtendedCurrency> nc = new ArrayList<>();
        for (ExtendedCurrency c : ExtendedCurrency.getAllCurrencies()) {
            nc.add(c);
        }

        mCurrencyPicker = CurrencyPicker.newInstance("Select Currency");
        mCurrencyPicker.setCurrenciesList(nc);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ExpenseRecordEditActivity.this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        mCurrencyPicker.setListener(this);

    }

    private void receiveData() {
        // get All the data from MainActivity
        // 1. first & second currency amount
        // 2. first & second country id
        // 3. trip id from trip spinner

        // And also fetch all the trip list from database to spinner

        // this adapter display all the name of trips from database
        tripInfoAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listOfTripNames);
        tripInfoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripListSpinner.setAdapter(tripInfoAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.expenserecord_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_saveExpenseRecord:
            {
                // add to data base
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.expenserecord_spinner_tripname:
                setUI_TripSelectionChange(position > 0 ? true : false);
                /* handle if user select the spinner manually
                // 1. show the selected trip
                // 2. change the first & second country flag
                TripInfoModel trip = listOfTrips.get(position);
                */

                break;
        }
    }

    private void setUI_TripSelectionChange(boolean isSelected) {

        tripListSpinner.setEnabled(true);

        if (isSelected) {
            firstCountryImg.setEnabled(false);
            secondCountryImg.setEnabled(false);
        }
        else {
            firstCountryImg.setEnabled(true);
            secondCountryImg.setEnabled(true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.expenserecord_imageview_firstcountry:
                mCurrencyPicker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
                mCurrencySelectedFirst = true;
                break;
            case R.id.expenserecord_imageview_secondcountry:
                mCurrencyPicker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
                mCurrencySelectedFirst = false;
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String expenseName = "", expenseDesc = "";

        if (!expenseNameEditText.getText().toString().equals("")){
            expenseName = expenseNameEditText.getText().toString();
        }

        if (!expenseDescEditText.getText().toString().equals("")){
            expenseDesc = expenseDescEditText.getText().toString();
        }


        double spendAmount = 0.0;

        if (!spendAmountEditText.getText().toString().equals("")){
            spendAmount = Double.valueOf(spendAmountEditText.getText().toString());
        }

        double convertedAmount = 0.0;

        // convert spendAmount to convertedAmount

        // convertedAmountTextView.setText("" + String.valueOf(convertedAmount));
    }

    @Override
    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
        if (mCurrencySelectedFirst){
            firstCountryImg.setImageResource(flagDrawableResID);
        }
        else {
            secondCountryImg.setImageResource(flagDrawableResID);
        }

        mCurrencySelectedFirst = !mCurrencySelectedFirst;
        mCurrencyPicker.dismiss();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("selectedCurrency")){
            // mTextView.setText(sharedPreferences.getString(key, ""));
        }
        if (key.equals("selectedCurrencies")){
            mCurrencyPicker.setCurrenciesList(mSharedPreferences.getStringSet("selectedCurrencies", new HashSet<String>()));
        }
    }
}
