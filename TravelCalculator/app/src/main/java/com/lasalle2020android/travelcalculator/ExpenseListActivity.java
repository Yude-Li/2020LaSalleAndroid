package com.lasalle2020android.travelcalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Model.ExpenseModel;
import Model.TripInfoModel;
import ViewUsage.RecycleViewAdapter;
import ViewUsage.RecyclerTouchListener;
import callbacks.DatabaseOperationNotifier;
import commonutilities.Constants;
import databaseinteraction.DatabaseOperations_Thread;

public class ExpenseListActivity extends AppCompatActivity implements DatabaseOperationNotifier{

    private RecyclerView expenseListView;
    
    private RecycleViewAdapter adapter;
    private List<ExpenseModel> mExpenseList;

    private int mTripId = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_list_layout);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_expenselist);
        mTopToolbar.setTitle(R.string.toolBarTitle_expenseList);
        setSupportActionBar(mTopToolbar);

        int dataIndex = -1;
        Intent intent = getIntent();
        dataIndex = intent.getIntExtra("DataIndex", -1);

        // get trip info from db
        if (dataIndex != -1) {
            new DatabaseOperations_Thread(ExpenseListActivity.this, Constants.TABLE.TRIPINFO,
                    Constants.DATABASE_OPERATION.FETCH_SELECTED, this, dataIndex).execute();
        }

        expenseListView = findViewById(R.id.expenselist_listview);

        mExpenseList = new ArrayList<>();

        GetAllExpenseListFromDb();

        GetCorrectExpenseList();

        PrepareListView(mExpenseList);

        SearchBarHandler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Set the toolBar layout from menu
        getMenuInflater().inflate(R.menu.expenselist_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Button action for actionBar(toolBar)
        int id = item.getItemId();

        switch (id) {
            case R.id.action_addExpense:
            {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ExpenseRecordEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("DataIndex", -1);
                startActivity(intent);
                finish();
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }
    
    private void GetAllExpenseListFromDb() {
        // get expense list from db
        new DatabaseOperations_Thread(ExpenseListActivity.this, Constants.TABLE.EXPENSE,
                Constants.DATABASE_OPERATION.FETCH_ALL, this).execute();
    }

    private void GetCorrectExpenseList() {
        // compare trip id to filter the expense list
        if (mExpenseList != null) {
            List<ExpenseModel> filteredExpenseList = new ArrayList<>();

            for (ExpenseModel e: mExpenseList) {
                // get the list which correspond trip id
                if (e.getTripId() == mTripId) {
                    filteredExpenseList.add(e);
                }
            }

            if (filteredExpenseList != null) {
                mExpenseList.clear();
                mExpenseList = filteredExpenseList;
            }
        }
    }

    private void PrepareListView(List<ExpenseModel> expenseList){
        adapter = new RecycleViewAdapter(expenseList, 0);

        expenseListView.setAdapter(adapter);
        expenseListView.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewOnTouchListener();

        adapter.notifyDataSetChanged();
    }

    public void recyclerViewOnTouchListener() {
        expenseListView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), expenseListView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }
    
    private void showActionsDialog(final int position){
        CharSequence actions[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Option");

        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), ExpenseRecordEditActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("DataIndex", position);
                    startActivity(intent);
                    finish();
                }
                else {
                    showDeleteDialog(position);
                }
            }
        });

        builder.show();
    }
    
    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage("Are you sure to delete this expense?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteExpense(position);
                dialog.cancel();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteExpense(int position) {
        adapter.remove(position);
        mExpenseList.remove(position);
        new DatabaseOperations_Thread(ExpenseListActivity.this, Constants.TABLE.EXPENSE,
                Constants.DATABASE_OPERATION.DELETE_RECORD, this, position).execute();
    }

    private void SearchBarHandler() {
        EditText searchBar = findViewById(R.id.expenselist_edittext_searchExpense);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchExpense(s.toString());
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void searchExpense(String text) {
        List<ExpenseModel> mSearchedExpenseList = new ArrayList<>();

        for (ExpenseModel e: mExpenseList){
            if (e.getExpenseName().toLowerCase(Locale.ENGLISH).contains(text.toLowerCase())){
                mSearchedExpenseList.add(e);
            }
        }

        mExpenseList.clear();
        mExpenseList = mSearchedExpenseList;

        adapter.notifyDataSetChanged();
    }

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
            mTripId = mTrip.getId();
        }
        else {
            Toast.makeText(ExpenseListActivity.this, getString(R.string.fetchDataFail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getExpenses_INFO(List<ExpenseModel> mAllExpense, int mCount, ExpenseModel mExpense) {
        if (mAllExpense != null){
            mExpenseList.clear();
            mExpenseList.addAll(mAllExpense);

            adapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(ExpenseListActivity.this, getString(R.string.fetchDataFail), Toast.LENGTH_LONG).show();
        }
    }
}
