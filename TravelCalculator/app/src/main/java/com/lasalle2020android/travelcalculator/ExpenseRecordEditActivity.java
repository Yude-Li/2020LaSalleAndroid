package com.lasalle2020android.travelcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DataConfig.CountryConfigAccess;
import DataConfig.SettingConfigAccess;
import Model.CountryModel;
import Model.ExpenseModel;
import Model.TripInfoModel;
import callbacks.DatabaseOperationNotifier;
import commonutilities.Constants;
import databaseinteraction.DatabaseOperations_Thread;

public class ExpenseRecordEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        DatabaseOperationNotifier {

    // View Objects
    private ImageView travelCountryImg, originalCountryImg;
    private EditText expenseNameEditText, spendAmountEditText, expenseDescEditText, dateEditText;
    private TextView convertedAmountTextView;
    private Spinner tripListSpinner;

    private CountryConfigAccess countryConfig;
    private SettingConfigAccess settingConfig;

    private ExpenseModel mExpenseInfo = null;

    // Variables dealing with spinner
    private TripInfoModel mTripInfo = null;
    private List<TripInfoModel> mTripList;
    private ArrayAdapter<String> tripInfoAdapter;
    private ArrayList<String> listOfTripName;

    // expense list position, get from expense list activity
    private int dataIndex = -1;

    // get from main activity
    private int mTripId = -1;
    private String mSpendAmount = "", mConvertedAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_record_layout);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_expenserecord);
        mTopToolbar.setTitle(R.string.toolBarTitle_expenseRecordEdit);
        setSupportActionBar(mTopToolbar);

        receiveData();
        initialize();
        AmountHandler();
    }

    private void receiveData() {

        Intent intent = getIntent();
        dataIndex = intent.getIntExtra("DataIndex", -1);
        mTripId = intent.getIntExtra("tripId", -1);

        if (dataIndex != -1) { // edit
            // Get expense data from db
            new DatabaseOperations_Thread(ExpenseRecordEditActivity.this, Constants.TABLE.EXPENSE,
                    Constants.DATABASE_OPERATION.FETCH_SELECTED, this, dataIndex).execute();
        }
        else if (mTripId != -1){ // save expense from main activity
            mSpendAmount = intent.getStringExtra("spendAmount");
            mConvertedAmount = intent.getStringExtra("convertedAmount");
        }
//        else { // create a new expense
//
//        }
    }

    private void initialize() {

        travelCountryImg = findViewById(R.id.expenserecord_imageview_travelcountry);

        countryConfig = new CountryConfigAccess(ExpenseRecordEditActivity.this);
        settingConfig = new SettingConfigAccess(ExpenseRecordEditActivity.this);

        originalCountryImg = findViewById(R.id.expenserecord_imageview_originalcountry);
        originalCountryImg.setImageResource(GetFlagId(null, settingConfig.getOriginalCountryId()));

        expenseNameEditText = findViewById(R.id.expenserecord_edittext_expensename);
        expenseDescEditText = findViewById(R.id.expenserecord_edittext_desc);
        spendAmountEditText = findViewById(R.id.expenserecord_edittext_spendamount);
        convertedAmountTextView = findViewById(R.id.expenserecord_textview_convertedamount);

        tripListSpinner = findViewById(R.id.expenserecord_spinner_tripname);
        tripListSpinner.setOnItemSelectedListener(this);

        // Date maybe will be a pop-up dialog
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy/MM/dd");
        dateEditText = findViewById(R.id.expenserecord_edittext_date);
        dateEditText.setText(ft.format(dNow));

        mTripList = new ArrayList<>();
        GetTripListFromDb();

        if (dataIndex != -1 ){ // edit
            initialViewObjectsWithValue(mExpenseInfo);
            SetupTripSpinner(mExpenseInfo);
        }
        else if (mTripId != -1){ // save
            initialViewObjectsWithValue(null);
            SetupTripSpinner(null);
        }
        else { // create
            initialViewObjectsWithoutValue();
            SetupTripSpinner(null);
        }

    }

    private int GetFlagId(TripInfoModel trip, int originalCountryId){

        for (CountryModel c: countryConfig.getCountriesList()) {
            if (originalCountryId != -1) {
                if (c.getId() == originalCountryId) {
                    return c.getFlagId(ExpenseRecordEditActivity.this);
                }
            }
            else {
                if (c.getId() == trip.getTravelCountry()) {
                    return c.getFlagId(ExpenseRecordEditActivity.this);
                }
            }
        }

        return -1;
    }

    private void GetTripListFromDb() {
        // For Spinner
        new DatabaseOperations_Thread(ExpenseRecordEditActivity.this, Constants.TABLE.TRIPINFO,
                Constants.DATABASE_OPERATION.FETCH_ALL, this).execute();
    }

    private void initialViewObjectsWithValue(ExpenseModel expenseInfo) {
        tripListSpinner.setEnabled(false);

        if (expenseInfo != null) {
            // editing
            spendAmountEditText.setEnabled(true);
            convertedAmountTextView.setEnabled(true);

            expenseNameEditText.setText(expenseInfo.getExpenseName());
            spendAmountEditText.setText(expenseInfo.getSpendAmount());
            convertedAmountTextView.setText(expenseInfo.getConvertedAmount());
            expenseDescEditText.setText(expenseInfo.getExpenseDesc());
        }
        else {
            // saving
            spendAmountEditText.setEnabled(false);
            convertedAmountTextView.setEnabled(false);

            spendAmountEditText.setText(mSpendAmount);
            convertedAmountTextView.setText(mConvertedAmount);
        }
    }

    private void initialViewObjectsWithoutValue(){
        // creating
        tripListSpinner.setEnabled(true);
        spendAmountEditText.setEnabled(true);
        convertedAmountTextView.setEnabled(true);
    }

    private void SetupTripSpinner(ExpenseModel expenseInfo) {
        listOfTripName = new ArrayList<>();

        if (dataIndex != -1) { // edit
            mTripInfo = GetTrip(expenseInfo.getTripId(), -1);
            listOfTripName.add(mTripInfo.getTripName());

            // Get Travel Country Flag
            travelCountryImg.setImageResource(GetFlagId(mTripInfo, -1));
        }
        else if (mTripId != -1) { // save
            mTripInfo = GetTrip( mTripId, -1);
            listOfTripName.add(mTripInfo.getTripName());

            // Get Travel Country Flag
            travelCountryImg.setImageResource(GetFlagId(mTripInfo, -1));
        }
        else {
            for (TripInfoModel trip: mTripList) {
                listOfTripName.add(trip.getTripName());
            }
        }

        // this adapter display all the name of trips from database
        tripInfoAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listOfTripName);
        tripInfoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripListSpinner.setAdapter(tripInfoAdapter);
    }

    private TripInfoModel GetTrip(int tripId, int position) {
        String tripName = "";

        if (position != -1) {
            tripName = (String) tripListSpinner.getItemAtPosition(position);
            // or listOfTripName.get(position);
        }

        for (TripInfoModel trip: mTripList) {
            if (tripName != "" && trip.getTripName() == tripName) { // get from spinner
                return trip;
            }
            if (trip.getId() == tripId) { // get from other activity
                return trip;
            }
        }

        return null;
    }

    private boolean inputCheck() {
        if (fieldCheck()) {
            mExpenseInfo = new ExpenseModel();
            mExpenseInfo.setExpenseName(expenseNameEditText.getText().toString());
            mExpenseInfo.setTripId(mTripInfo.getId());
            mExpenseInfo.setSpendAmount(spendAmountEditText.getText().toString());
            mExpenseInfo.setConvertedAmount(convertedAmountTextView.getText().toString());
            mExpenseInfo.setDate(expenseDescEditText.getText().toString());
            mExpenseInfo.setExpenseDesc(expenseDescEditText.getText().toString());
            return true;
        }
        else {
            Toast.makeText(ExpenseRecordEditActivity.this, "Please fill up all data", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean fieldCheck() {
        if (  !expenseNameEditText.getText().equals("")
           && !spendAmountEditText.getText().equals("")
           && !convertedAmountTextView.getText().equals("")
           && !dateEditText.getText().equals("")
           && tripListSpinner.getSelectedItem() != null
           // && !expenseDescEditText.getText().equals("") no need to check
           )
        {
            return true;
        }
        return false;
    }

    private void AmountHandler() {
        spendAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double spendAmount = 0.0, convertedAmount = 0.0;

                if (!spendAmountEditText.getText().toString().equals("")){
                    spendAmount = Double.valueOf(spendAmountEditText.getText().toString());
                }

                // convert spendAmount to convertedAmount

                // convertedAmountTextView.setText(String.valueOf(convertedAmount));
            }
        });
    }

    private void showSaveSuccessDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.notice_saveSuccessTitle);
        alertDialog.setMessage(R.string.notice_expenseSaveSuccessContent);

        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.cancel();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ExpenseListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.show();
    }

    private void clearAllFields() {
        expenseNameEditText.setText("");
        spendAmountEditText.setText("");
        convertedAmountTextView.setText("");
        dateEditText.setText("");
        expenseDescEditText.setText("");
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
                if (dataIndex != -1) { // edit the expense, update to db
                    // ************ inputCheck();

                    new DatabaseOperations_Thread(ExpenseRecordEditActivity.this, Constants.TABLE.EXPENSE,
                            Constants.DATABASE_OPERATION.UPDATE_RECORD, this, dataIndex).execute();

                }
                else if (mTripId != -1) { // save from Main Activity, add to db
                    if (inputCheck()) {
                        new DatabaseOperations_Thread(ExpenseRecordEditActivity.this, Constants.TABLE.EXPENSE,
                                Constants.DATABASE_OPERATION.ADD_RECORD, this, null, mExpenseInfo).execute();
                        showSaveSuccessDialog();
                    }
                }
                else { // create a new expense, add to db
                    if (inputCheck()) {
                        new DatabaseOperations_Thread(ExpenseRecordEditActivity.this, Constants.TABLE.EXPENSE,
                                Constants.DATABASE_OPERATION.ADD_RECORD, this, null, mExpenseInfo).execute();
                        showSaveSuccessDialog();
                    }
               }
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    // AdapterView.OnItemSelectedListener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // only for creating
        switch (view.getId()){
            case R.id.expenserecord_spinner_tripname:
                mTripInfo = GetTrip(-1, position);
                travelCountryImg.setImageResource(GetFlagId(mTripInfo, -1));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    // DatabaseOperationNotifier
    @Override
    public void onSavePerformed(boolean isCompletedSuccessfully) {
        clearAllFields();
    }

    @Override
    public void onCurrencyRetrivePerformed(boolean isSuccess) {
        // ???
    }

    @Override
    public void onDB_BootCompleted(boolean isSuccess) {
        // maybe get expense db from here
    }

    @Override
    public void onDeletePerformed(boolean isSuccess) {
        clearAllFields();
    }

    @Override
    public void getTrips_INFO(List<TripInfoModel> mAllTrips, int mCount, TripInfoModel mTrip) {
        mTripList.clear();
        mTripList.addAll(mAllTrips);
    }

    @Override
    public void getExpenses_INFO(List<ExpenseModel> mAllExpense, int mCount, ExpenseModel mExpense) {
        if (mExpense != null) {
            mExpenseInfo = mExpense;
        }
        else {
            Toast.makeText(ExpenseRecordEditActivity.this, getString(R.string.fetchDataFail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (fieldCheck()){
            new AlertDialog.Builder(ExpenseRecordEditActivity.this)
                    .setTitle(R.string.backWithoutSave_NoticeTitle)
                    .setMessage(R.string.backWithoutSave_NoticeContent)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ExpenseRecordEditActivity.super.onBackPressed();

                            Intent intent = new Intent();
                            intent.setClass(getApplicationContext(), ExpenseListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), ExpenseListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
    }
}
