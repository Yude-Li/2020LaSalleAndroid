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

public class ExpenseRecordEditActivity extends AppCompatActivity implements DatabaseOperationNotifier, TextWatcher {

    // View Objects
    private ImageView travelCountryImg, originalCountryImg;
    private EditText expenseNameEditText, spendAmountEditText, expenseDescEditText, dateEditText;
    private TextView tripNameTextView, convertedAmountTextView;
    //private Spinner tripListSpinner;

    private CountryConfigAccess countryConfig;
    private SettingConfigAccess settingConfig;

    private ExpenseModel mExpenseInfo = new ExpenseModel();
    private TripInfoModel mTripInfo = new TripInfoModel();

    //private List<TripInfoModel> mTripList;
    //private ArrayAdapter<String> tripInfoAdapter;
    //private ArrayList<String> listOfTripName;

    // expense list position, get from expense list activity
    private int mExpenseId = -1, mTripId = -1;

    // get from main activity
    private int mTripIdFromMain = -1;
    private String mSpendAmount = "", mConvertedAmount = "";
    private  Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_record_layout);
mBundle=getIntent().getBundleExtra("bundle");

        Toolbar mTopToolbar = findViewById(R.id.toolbar_expenserecord);
        mTopToolbar.setTitle(R.string.toolBarTitle_expenseRecordEdit);
        setSupportActionBar(mTopToolbar);

        countryConfig = new CountryConfigAccess(ExpenseRecordEditActivity.this);
        settingConfig = new SettingConfigAccess(ExpenseRecordEditActivity.this);

        receiveDataFromAnotherActivity();
        GetTripFromDb();
    }

    private void receiveDataFromAnotherActivity() {

        Intent intent = getIntent();
        Bundle mBundle= intent.getBundleExtra("bundle");
        mExpenseId = mBundle.getInt("expenseId", -1);
        mTripId = mBundle.getInt("tripId", -1);
        mTripIdFromMain = mBundle.getInt("tripIdFromMain", -1);

        if (mExpenseId != -1) { // edit
            new DatabaseOperations_Thread(ExpenseRecordEditActivity.this, Constants.TABLE.EXPENSE,
                    Constants.DATABASE_OPERATION.FETCH_SELECTED, this, mExpenseId).execute();
        }
        else if (mTripIdFromMain != -1){ // save expense from main activity
            mSpendAmount = mBundle.getString("spendAmount");
            mConvertedAmount = mBundle.getString("convertedAmount");
        }
    }

    private void initialize() {
        expenseNameEditText = findViewById(R.id.expenserecord_edittext_expensename);
        expenseDescEditText = findViewById(R.id.expenserecord_edittext_desc);
        spendAmountEditText = findViewById(R.id.expenserecord_edittext_spendamount);
        spendAmountEditText.addTextChangedListener(this);
        convertedAmountTextView = findViewById(R.id.expenserecord_textview_convertedamount);

        // Date maybe will be a pop-up dialog
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy/MM/dd");
        dateEditText = findViewById(R.id.expenserecord_edittext_date);
        dateEditText.setText(ft.format(dNow));

        tripNameTextView = findViewById(R.id.expenserecord_textview_tripname);
        tripNameTextView.setText(mTripInfo.getTripName());

        travelCountryImg = findViewById(R.id.expenserecord_imageview_travelcountry);
        originalCountryImg = findViewById(R.id.expenserecord_imageview_originalcountry);

        travelCountryImg.setImageResource(GetFlagId(mTripInfo, -1));
        originalCountryImg.setImageResource(GetFlagId(null, settingConfig.getOriginalCountryId()));

        if (mExpenseId != -1 ){ // edit
            initialViewObjectsWithValue(mExpenseInfo);
        }
        else if (mTripIdFromMain != -1){ // save
            initialViewObjectsWithValue(null);
        }
        else { // create
            initialViewObjectsWithoutValue();
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

    private void GetTripFromDb() {
        new DatabaseOperations_Thread(ExpenseRecordEditActivity.this, Constants.TABLE.TRIPINFO,
                Constants.DATABASE_OPERATION.FETCH_SELECTED, this, mTripId).execute();
    }

    private void initialViewObjectsWithValue(ExpenseModel expenseInfo) {

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
        spendAmountEditText.setEnabled(true);
        convertedAmountTextView.setEnabled(true);
    }

    private boolean inputCheck() {
        if (fieldCheck()) {
            mExpenseInfo = new ExpenseModel();
            mExpenseInfo.setExpenseName(expenseNameEditText.getText().toString());
            mExpenseInfo.setTripId(mTripId);
            mExpenseInfo.setSpendAmount(spendAmountEditText.getText().toString());
            mExpenseInfo.setConvertedAmount(convertedAmountTextView.getText().toString());
            mExpenseInfo.setDate(dateEditText.getText().toString());
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
           )
        {
            return true;
        }

        return false;
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
                intent.putExtra("tripId", mTripId);
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
                if (mExpenseId != -1) { // edit the expense, update to db
                    // ************ inputCheck();

                    new DatabaseOperations_Thread(ExpenseRecordEditActivity.this, Constants.TABLE.EXPENSE,
                            Constants.DATABASE_OPERATION.UPDATE_RECORD, this, mExpenseId).execute();

                }
                else if (mTripIdFromMain != -1) { // save from Main Activity, add to db
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
    public void onUpdatePerformed(boolean isSuccess) {

    }

    @Override
    public void getTrips_INFO(List<TripInfoModel> mAllTrips, int mCount, TripInfoModel mTrip) {
        if (mTrip != null) {
            mTripInfo = mTrip;
            initialize();
        }
        else {
            Toast.makeText(ExpenseRecordEditActivity.this, getString(R.string.fetchDataFail), Toast.LENGTH_SHORT).show();
        }
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
                            intent.putExtra("tripId", mTripId);
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
            intent.putExtra("tripId", mTripId);
            startActivity(intent);
            finish();
            super.onBackPressed();
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
        double spendAmount = 0.0, convertedAmount = 0.0;

        if (!spendAmountEditText.getText().toString().equals("")){
            spendAmount = Double.valueOf(spendAmountEditText.getText().toString());
        }

        convertedAmount = spendAmount;
        // convert spendAmount to convertedAmount

        convertedAmountTextView.setText(String.valueOf(convertedAmount));
    }
}
