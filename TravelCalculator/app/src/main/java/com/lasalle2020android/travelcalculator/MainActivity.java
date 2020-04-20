package com.lasalle2020android.travelcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import DataConfig.CountryConfigAccess;
import Model.ExpenseModel;
import Model.TripInfoModel;
import callbacks.DatabaseOperationNotifier;
import callbacks.ServerResponseNotifier;
import commonutilities.ComponentInfo;
import commonutilities.Constants;
import currencies.CurrencyPicker;
import currencies.CurrencyPickerListener;
import currencies.ExtendedCurrency;
import databaseinteraction.DatabaseOperations_Thread;
import threads.HttpServiceThread;

public class MainActivity extends AppCompatActivity implements ServerResponseNotifier,
        CurrencyPickerListener, AdapterView.OnItemSelectedListener, View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener, DatabaseOperationNotifier {


    //UI Views
    private Spinner mSpinnerTripList;
    private ImageView mCurrencySelectionImage_Second, mCurrencySelectionImage_One;
    private TextView mInputTextView_One, mInputTextView_Second;
    private Button mSave_Btn, mBreakfast_Btn, mLunch_Btn, mDinner_Btn, mCustomiseTip_Btn, mNormalTip_Btn;

    private String mEnteredString = "";
    private int maxEnteredLength;


    private CurrencyPicker mCurrencyPicker;
    private SharedPreferences mSharedPrefrences;

    private boolean mCurrencySelectedFirst = false, isLastCharEnteredSymbol = false;


    private ComponentInfo mComponentInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_layout);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_calculator);
        setSupportActionBar(mTopToolbar);

        mComponentInfo = (ComponentInfo) getApplicationContext();
        performDB_Create();
        instantiateViews();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calculator_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_toTripList: {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), TripListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void performDB_Create() {


        new DatabaseOperations_Thread(MainActivity.this, Constants.TABLE.ALL,
                Constants.DATABSE_OPERATION.DB_CREATE, this).execute();


    }

    private void instantiateViews() {

        mSpinnerTripList = findViewById(R.id.spinner_triplist);
        mSpinnerTripList.setOnItemSelectedListener(this);

        mCurrencySelectionImage_One = findViewById(R.id.img_firstCountry);
        mCurrencySelectionImage_One.setOnClickListener(this);
        mCurrencySelectionImage_Second = findViewById(R.id.img_secondCountry);
        mCurrencySelectionImage_Second.setOnClickListener(this);


        mInputTextView_One = findViewById(R.id.textView_firstAmount);
        mInputTextView_Second = findViewById(R.id.textView_secondC);


        mSave_Btn = findViewById(R.id.btn_save);
        mSave_Btn.setOnClickListener(this);
        mBreakfast_Btn = findViewById(R.id.btn_breakfast);
        mBreakfast_Btn.setOnClickListener(this);
        mLunch_Btn = findViewById(R.id.btn_lunch);
        mLunch_Btn.setOnClickListener(this);
        mDinner_Btn = findViewById(R.id.btn_dinner);
        mDinner_Btn.setOnClickListener(this);
        mCustomiseTip_Btn = findViewById(R.id.btn_customize_tip);
        mCustomiseTip_Btn.setOnClickListener(this);
        mNormalTip_Btn = findViewById(R.id.btn_normal);
        mNormalTip_Btn.setOnClickListener(this);


        ArrayList<ExtendedCurrency> nc = new ArrayList<>();
        for (ExtendedCurrency c : ExtendedCurrency.getAllCurrencies()) {
            //if (c.getSymbol().endsWith("0")) {
            nc.add(c);
            //}
        }


        mSharedPrefrences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        mSharedPrefrences.registerOnSharedPreferenceChangeListener(this);

        mCurrencyPicker = CurrencyPicker.newInstance("Select Currency");
        mCurrencyPicker.setCurrenciesList(nc);
        // mCurrencyPicker.setCurrenciesList(mSharedPrefrences.getStringSet("selectedCurrencies", new HashSet<String>()));

        mCurrencyPicker.setListener(this);


//        HttpServiceThread httpServiceThread= new HttpServiceThread(mComponentInfo, MainActivity.this,
//                MainActivity.this, "USD,INR",101);
//        httpServiceThread.start();
    }

    private void instantiateData(){

        CountryConfigAccess countryConfigAccess= new CountryConfigAccess(MainActivity.this);
        countryConfigAccess.getCountryById(00);
    }
    private void setInactive() {

    }

    private void setMaxEnteredLength() {
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxEnteredLength);
        mInputTextView_One.setFilters(fArray);

    }

    private void setUI_TripSelectionChange(boolean isSelected) {

        if (isSelected) {

            mCustomiseTip_Btn.setEnabled(false);
            mSpinnerTripList.setEnabled(true);
            mCurrencySelectionImage_One.setEnabled(false);
            mCurrencySelectionImage_Second.setEnabled(false);

            mSave_Btn.setEnabled(true);
            mBreakfast_Btn.setEnabled(true);
            mLunch_Btn.setEnabled(true);
            mDinner_Btn.setEnabled(true);
            mNormalTip_Btn.setEnabled(true);
            mCustomiseTip_Btn.setEnabled(true);


        } else {
            mCustomiseTip_Btn.setEnabled(true);
            mSpinnerTripList.setEnabled(true);
            mCurrencySelectionImage_One.setEnabled(true);
            mCurrencySelectionImage_Second.setEnabled(true);

            mSave_Btn.setEnabled(false);
            mBreakfast_Btn.setEnabled(false);
            mLunch_Btn.setEnabled(false);
            mDinner_Btn.setEnabled(false);
            mNormalTip_Btn.setEnabled(false);
            mCustomiseTip_Btn.setEnabled(false);
        }


    }

    @Override
    public void onServerResponseRecieved(String response, int resultCode, boolean useResponseDirectly) {

        if (useResponseDirectly) {

        } else {

            switch (resultCode) {

                case 200:

                    break;


            }

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (view.getId()) {

            case R.id.spinner_triplist:

                setUI_TripSelectionChange(position > 0 ? true : false);

                break;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void processInput(char inputChar) {


        if (!isLastCharEnteredSymbol) {

            mEnteredString += inputChar;
            mInputTextView_One.setText(mEnteredString);


        }

        switch (inputChar) {

            case '.':
                isLastCharEnteredSymbol = true;
                maxEnteredLength = mEnteredString.length() + 2;
                setMaxEnteredLength();
                break;
            case '+':
                isLastCharEnteredSymbol = true;
                break;
            case '-':
                isLastCharEnteredSymbol = true;
                break;
            case '/':
                isLastCharEnteredSymbol = true;
                break;
            case '*':
                isLastCharEnteredSymbol = true;
                break;

            default:
                isLastCharEnteredSymbol = false;
                break;


        }
    }

    private void calculateFinal() {


        BigDecimal result = null;

        Expression expression = new Expression(mEnteredString);
        result = expression.eval();
        mEnteredString = result + "";

        mInputTextView_One.setText(mEnteredString);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.btn_0:


                processInput('0');

                break;

            case R.id.btn_1:
                processInput('1');
                break;

            case R.id.btn_2:
                processInput('2');
                break;


            case R.id.btn_3:
                processInput('3');
                break;
            case R.id.btn_4:
                processInput('4');

                break;
            case R.id.btn_5:
                processInput('5');
                break;
            case R.id.btn_6:
                processInput('6');
                break;
            case R.id.btn_7:
                processInput('7');
                break;
            case R.id.btn_8:
                processInput('8');

                break;
            case R.id.btn_9:
                processInput('9');
                break;


            case R.id.btn_add:
                processInput('+');

                break;

            case R.id.btn_minus:
                processInput('-');

                break;
            case R.id.btn_divide:
                processInput('/');

                break;

            case R.id.btn_multiply:
                processInput('*');

                break;

            case R.id.btn_dot:


                processInput('.');

                break;

            case R.id.btn_del:
                performBackSpace();
                break;


            case R.id.img_firstCountry:
                mCurrencyPicker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
                mCurrencySelectedFirst = true;

                break;
            case R.id.img_secondCountry:
                mCurrencyPicker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
                mCurrencySelectedFirst = false;

                break;

            case R.id.btn_customize_tip:

                break;

            case R.id.btn_percentage:

                break;
            case R.id.btn_breakfast:

                break;
            case R.id.btn_lunch:

                break;

            case R.id.btn_normal:

                break;

            case R.id.btn_save:

                break;

            case R.id.btn_equal:
                calculateFinal();
                break;


        }


    }

    private void performBackSpace() {

        if (mEnteredString.length() > 0) {


            if ((mEnteredString.charAt(mEnteredString.length() - 1) + "").equalsIgnoreCase(".")) {

                maxEnteredLength = 15;
                setMaxEnteredLength();

            }
            mEnteredString = mEnteredString.substring(0, mEnteredString.length() - 1);
            mInputTextView_One.setText(mEnteredString);
        }

    }

    private void performOperations() {


    }

    @Override
    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {

        if (mCurrencySelectedFirst) {
            mCurrencySelectionImage_One.setImageResource(flagDrawableResID);

        } else {
            mCurrencySelectionImage_Second.setImageResource(flagDrawableResID);
        }

        mCurrencySelectedFirst = !mCurrencySelectedFirst;
        mCurrencyPicker.dismiss();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("selectedCurrency")) {
            // mTextView.setText(sharedPreferences.getString(key, ""));
        }
        if (key.equals("selectedCurrencies")) {
            mCurrencyPicker.setCurrenciesList(mSharedPrefrences.getStringSet("selectedCurrencies", new HashSet<String>()));
        }
    }

    @Override
    public void onSavePerformed(boolean isCompletedSuccessfully) {


    }

    @Override
    public void onCurrencyRetrivePerformed(boolean isSuccess) {


    }

    @Override
    public void onDB_BootCompleted(boolean isSuccess) {

        if(isSuccess){

             new DatabaseOperations_Thread(MainActivity.this, Constants.TABLE.TRIPINFO,
                    Constants.DATABSE_OPERATION.FETCH_ALL, this).execute();
        }

    }

    @Override
    public void onDeletePerformed(boolean isSuccess) {

    }

    @Override
    public void getTrips_INFO(List<TripInfoModel> mAllTrips, int mCount, TripInfoModel mTrip) {



    }

    @Override
    public void getExpenses_INFO(List<ExpenseModel> mAllExpense, int mCount, ExpenseModel mExpense) {


    }
}
