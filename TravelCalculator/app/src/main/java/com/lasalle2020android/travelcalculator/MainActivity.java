package com.lasalle2020android.travelcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import DataConfig.CountryConfigAccess;
import Model.ApiResponseModel;
import Model.CountryModel;
import Model.ExpenseModel;
import Model.TripInfoModel;
import callbacks.DatabaseOperationNotifier;
import callbacks.ServerResponseNotifier;
import commonutilities.Constants;
import commonutilities.SpinnerAdapter;
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


    private MyApplication mComponentInfo;


    private TripInfoModel mTripInfoModel;
    private TripInfoModel[] tripInfoModels;
    private String countryOneString = "", countryTwoString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_layout);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_calculator);
        setSupportActionBar(mTopToolbar);

        mComponentInfo = (MyApplication) getApplicationContext();


//        HttpServiceThread httpServiceThread = new HttpServiceThread(mComponentInfo,MainActivity.this,MainActivity.this,"USD,GBP",00);
//        httpServiceThread.start();


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
            case R.id.action_toSetting: {

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), UserSettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void performDB_Create() {


        new DatabaseOperations_Thread(MainActivity.this, Constants.TABLE.ALL,
                Constants.DATABASE_OPERATION.DB_CREATE, this).execute();


    }

    private void updateData() {
        CountryModel model = mComponentInfo.countryConfig.getCountryById(mComponentInfo.settingConfig.getOriginalCountryId());
        countryOneString = model.getCurrencyCode();
        countryTwoString = model.getCurrencyCode();
        mCurrencySelectionImage_One.setImageResource(model.getFlagId(MainActivity.this));
        mCurrencySelectionImage_Second.setImageResource(model.getFlagId(MainActivity.this));
        mInputTextView_One.setText("");
        mInputTextView_Second.setText("");
        mEnteredString="";
        processInput('0');
        performDB_Create();
        instantiateData();
    }


    private void instantiateViews() {

        mSpinnerTripList = findViewById(R.id.spinner_triplist);
        mSpinnerTripList.setOnItemSelectedListener(this);

        mCurrencySelectionImage_One = findViewById(R.id.img_firstCountry);
        mCurrencySelectionImage_One.setOnClickListener(this);
        mCurrencySelectionImage_Second = findViewById(R.id.img_secondCountry);
        mCurrencySelectionImage_Second.setOnClickListener(this);


        mInputTextView_One = findViewById(R.id.textView_firstAmount);
        // processInput('1');
        // processInput('0');
        mInputTextView_Second = findViewById(R.id.textView_secondC);
        processInput('0');
        mInputTextView_One.setText(""); //change from two
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


        ArrayList<CountryModel> nc = new ArrayList<>();
        for (CountryModel c : mComponentInfo.countryConfig.getCountriesList()) {
            //if (c.getSymbol().endsWith("0")) {
            nc.add(c);
            //}
        }


        mSharedPrefrences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        mSharedPrefrences.registerOnSharedPreferenceChangeListener(this);

        mCurrencyPicker = CurrencyPicker.newInstance("Select Currency", nc, MainActivity.this);
        mCurrencyPicker.setCurrenciesList(nc);
        // mCurrencyPicker.setCurrenciesList(mSharedPrefrences.getStringSet("selectedCurrencies", new HashSet<String>()));

        mCurrencyPicker.setListener(this);


//        HttpServiceThread httpServiceThread= new HttpServiceThread(mComponentInfo, MainActivity.this,
//                MainActivity.this, "USD,INR",101);
//        httpServiceThread.start();
    }


    private void instantiateData() {

//        CountryConfigAccess countryConfigAccess= new CountryConfigAccess(MainActivity.this);
//        countryConfigAccess.getCountryById(00);

        new DatabaseOperations_Thread(MainActivity.this, Constants.TABLE.TRIPINFO,
                Constants.DATABASE_OPERATION.FETCH_ALL, this).execute();

    }

    private void setInactive() {

    }

    private void setMaxEnteredLength() {
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxEnteredLength);
        mInputTextView_Second.setFilters(fArray); //change

    }

    private void setUI_TripSelectionChange(boolean isSelected) {

        if (isSelected) {

            mCustomiseTip_Btn.setEnabled(true);
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
    public void onServerResponseRecieved(ApiResponseModel response, int resultCode, boolean useResponseDirectly) {

        if (useResponseDirectly) {
            switch (resultCode) {

                case 200:

                    calculateFinal();
                    mInputTextView_One.setText(Double.parseDouble(mEnteredString) * response.getCurrencyRateConvert() + ""); //changed from two


                    break;


            }
        } else {


        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (view != null) {
            switch (parent.getId()) {

                case R.id.spinner_triplist:


                    mTripInfoModel = tripInfoModels[position];

                    if (mTripInfoModel.getTravelCountry() == 9999) {
                        //  HttpServiceThread httpServiceThread = new HttpServiceThread(mComponentInfo,MainActivity.this,MainActivity.this,"USD,GBP",00);httpServiceThread.start();
                        setUI_TripSelectionChange(false);

                    } else {

                        mCurrencySelectionImage_Second.setImageResource(mComponentInfo.countryConfig.getCountryById(mTripInfoModel.getTravelCountry()).getFlagId(MainActivity.this));

                        setUI_TripSelectionChange(true);

                    }

                    break;


            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void processInput(char inputChar) {


        if (!isLastCharEnteredSymbol) {

            mEnteredString += inputChar;
            mInputTextView_Second.setText(mEnteredString); //change


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
            case '%':
                isLastCharEnteredSymbol = true;
                break;

            default:
                isLastCharEnteredSymbol = false;
                break;


        }
    }

    private void calculateFinal() {

        if (mEnteredString.contains("+") || mEnteredString.contains("-") || mEnteredString.contains("/") || mEnteredString.contains("*") || mEnteredString.contains(".") || mEnteredString.contains("%")) {
            BigDecimal result = null;
            mEnteredString = mEnteredString;
            Expression expression = new Expression(mEnteredString);
            result = expression.eval();
            result = result.setScale(2, RoundingMode.CEILING);
            mEnteredString = result + "";
        }

        mInputTextView_Second.setText(mEnteredString);// change
    }

    private String calculateFinal(float tip, String amount, float tax, CountryModel model) {

        String ret = "";
        BigDecimal result = null;
        double total = 0.0;
        Expression expression;
        if (tip > 0) {
            expression = new Expression(amount + "*" + tip + "/" + "100");
            result = expression.eval();
            total = total + Double.parseDouble(result + "");
        }
        expression = new Expression(amount + "*" + tax + "/" + "100");
        result = expression.eval();
        total = total + Double.parseDouble(result + "");


        total = total + Double.parseDouble(amount + "");
        if (model.getCurrency() > 0) {
            expression = new Expression(total + "/" + model.getCurrency());
            result = expression.eval();
            result = result.setScale(2, RoundingMode.CEILING);

            return ret + result;
        } else {

            return ret + total;
        }


    }

    private String calculateFinal(float tip, String amount, float tax) {

        String ret = "";
        BigDecimal result = null;
        double total = 0.0;
        Expression expression;
        if (tip > 0) {
            expression = new Expression(amount + "*" + tip + "/" + "100");
            result = expression.eval();
            total = total + Double.parseDouble(result + "");
        }
        expression = new Expression(amount + "*" + tax + "/" + "100");
        result = expression.eval();
        total = total + Double.parseDouble(result + "");


        total = total + Double.parseDouble(amount + "");


        return ret + total;


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
                Toast.makeText(MainActivity.this, "Comming Soon", Toast.LENGTH_LONG).show();

                break;

            case R.id.btn_percentage:
                processInput('%');


                break;
            case R.id.btn_breakfast:

                if (validateTotalAmount()) {
                    calculateFinal();
                    mInputTextView_One.setText(calculateFinal(mTripInfoModel.getBreakfastTip(),
                            mEnteredString, mTripInfoModel.getTax(), mComponentInfo.countryConfig.getCountryById(mTripInfoModel.getTravelCountry()))); //change from two
                    mInputTextView_Second.setText(calculateFinal(mTripInfoModel.getBreakfastTip(),
                            mEnteredString, mTripInfoModel.getTax()));


                } else {

                    Toast.makeText(MainActivity.this, "Please enter calculate amount or enter greater than 0", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_lunch:
                calculateFinal();

                if (validateTotalAmount()) {
                    calculateFinal();
                    mInputTextView_One.setText(calculateFinal(mTripInfoModel.getLunchTip(),
                            mEnteredString, mTripInfoModel.getTax(), mComponentInfo.countryConfig.getCountryById(mTripInfoModel.getTravelCountry()))); //change from two
                    mInputTextView_Second.setText(calculateFinal(mTripInfoModel.getLunchTip(),
                            mEnteredString, mTripInfoModel.getTax()));


                } else {

                    Toast.makeText(MainActivity.this, "Please enter calculate amount or enter greater than 0", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_dinner:

                if (validateTotalAmount()) {
                    calculateFinal();
                    mInputTextView_One.setText(calculateFinal(mTripInfoModel.getDinnerTip(),
                            mEnteredString, mTripInfoModel.getTax(), mComponentInfo.countryConfig.getCountryById(mTripInfoModel.getTravelCountry()))); //change from two
                    mInputTextView_Second.setText(calculateFinal(mTripInfoModel.getDinnerTip(),
                            mEnteredString, mTripInfoModel.getTax()));


                } else {

                    Toast.makeText(MainActivity.this, "Please enter calculate amount or enter greater than 0", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btn_normal:

                if (validateTotalAmount()) {
                    calculateFinal();
                    mInputTextView_One.setText(calculateFinal(0,
                            mEnteredString, mTripInfoModel.getTax(), mComponentInfo.countryConfig.getCountryById(mTripInfoModel.getTravelCountry()))); //change from two
                    mInputTextView_Second.setText(calculateFinal(0,
                            mEnteredString, mTripInfoModel.getTax()));


                } else {

                    Toast.makeText(MainActivity.this, "Please enter calculate amount or enter greater than 0", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btn_save:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ExpenseRecordEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                calculateFinal();
                Bundle bundle = new Bundle();
                CountryModel model = mComponentInfo.countryConfig.getCountryById(mComponentInfo.settingConfig.getOriginalCountryId());

                bundle.putString("baseCurrency", model.getCurrencyCode());
                bundle.putInt("baseCountryId", mTripInfoModel.getTravelCountry());

                bundle.putInt("destinationCountryId", model.getCountryNum());


                bundle.putString("destinationCurrency", countryTwoString);


                bundle.putString("spendAmount", mEnteredString);
                bundle.putString("convertedAmount", mInputTextView_One.getText().toString()); //change from two
                bundle.putString("currentDate", "");
                bundle.putInt("tripId", mTripInfoModel.getId());
                bundle.putInt("expenseId", -1);
                bundle.putInt("tripIdFromMain", 1);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                break;

            case R.id.btn_equal:
                calculateFinal();
                break;


        }


    }


    private boolean validateTotalAmount() {
        boolean ret = false;

        try {
            double amt = Double.parseDouble(mEnteredString);
            if (amt >= 0) {
                ret = true;


            }

        } catch (Exception e) {

        }

        return ret;


    }

    private void performBackSpace() {

        if (mEnteredString.length() > 0) {


            if ((mEnteredString.charAt(mEnteredString.length() - 1) + "").equalsIgnoreCase(".")) {

                maxEnteredLength = 15;
                setMaxEnteredLength();

            }
            mEnteredString = mEnteredString.substring(0, mEnteredString.length() - 1);
            mInputTextView_Second.setText(mEnteredString); ////change
        }

    }

    private void performOperations() {


    }

    @Override
    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {

        if (mCurrencySelectedFirst) {
            mCurrencySelectionImage_One.setImageResource(flagDrawableResID);
            countryOneString = code;

        } else {
            mCurrencySelectionImage_Second.setImageResource(flagDrawableResID);
            countryTwoString = code;


            HttpServiceThread httpServiceThread = new HttpServiceThread(MainActivity.this, mComponentInfo, Constants.ActionMode.CONVERT_CURRENCY, countryOneString + "," + countryTwoString, MainActivity.this);
            httpServiceThread.setConversionCurrencyBase(countryOneString);

            httpServiceThread.start();


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
            // mCurrencyPicker.setCurrenciesList(mSharedPrefrences.getStringSet("selectedCurrencies", new HashSet<String>()));
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

        if (isSuccess) {

            new DatabaseOperations_Thread(MainActivity.this, Constants.TABLE.TRIPINFO,
                    Constants.DATABASE_OPERATION.FETCH_ALL, this).execute();
        }

    }

    @Override
    public void onDeletePerformed(boolean isSuccess) {

    }

    @Override
    public void onUpdatePerformed(boolean isSuccess) {

    }

    @Override
    public void getTrips_INFO(List<TripInfoModel> mAllTrips, int mCount, TripInfoModel mTrip) {


        if (mAllTrips != null) {
            if (mAllTrips.size() == 1) {
                setUI_TripSelectionChange(false);
            }
            tripInfoModels = mAllTrips.toArray(new TripInfoModel[mAllTrips.size()]);

            Log.e("sac", "sks" + mAllTrips);


            SpinnerAdapter spinnerArrayAdapter = new SpinnerAdapter(this,
                    android.R.layout.simple_spinner_item, tripInfoModels);


            mSpinnerTripList.setAdapter(spinnerArrayAdapter);
            //  mCurrencySelectionImage_Second.setImageResource( mComponentInfo.countryConfig.getCountryById(((TripInfoModel)mSpinnerTripList.getSelectedItem()).getTravelCountry()).getFlagId(MainActivity.this));

            //  mCurrencySelectionImage_Second.setEnabled(false);
            //mCurrencySelectionImage_One.setEnabled(false);

        } else {


            setUI_TripSelectionChange(false);
        }


    }

    @Override
    public void getExpenses_INFO(List<ExpenseModel> mAllExpense, int mCount, ExpenseModel mExpense) {


    }
}
