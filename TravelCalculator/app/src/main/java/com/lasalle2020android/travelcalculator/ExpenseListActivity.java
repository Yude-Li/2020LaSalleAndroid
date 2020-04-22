package com.lasalle2020android.travelcalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import ViewUsage.MyDividerItemDecoration;
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

        Intent intent = getIntent();
        mTripId = intent.getIntExtra("tripId", -1);

        // get trip info from db
        if (mTripId != -1) {
            new DatabaseOperations_Thread(ExpenseListActivity.this, Constants.TABLE.TRIPINFO,
                    Constants.DATABASE_OPERATION.FETCH_SELECTED, this, mTripId).execute();
        }

        expenseListView = findViewById(R.id.expenselist_listview);

        PrepareListView();
        GetAllExpenseListFromDb();

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
                intent.putExtra("expenseId", -1);
                intent.putExtra("tripId", mTripId);
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
        List<ExpenseModel> filteredExpenseList = new ArrayList<>();

        for (ExpenseModel e : mExpenseList) {
            // get the list which correspond trip id
            if (e.getTripId() == mTripId) {
                filteredExpenseList.add(e);
            }
        }

        mExpenseList.clear();
        mExpenseList.addAll(filteredExpenseList);

        adapter.notifyDataSetChanged();
    }

    private void PrepareListView(){
        mExpenseList = new ArrayList<>();
        adapter = new RecycleViewAdapter(mExpenseList, ExpenseListActivity.this);

        expenseListView.setAdapter(adapter);
        expenseListView.setLayoutManager(new LinearLayoutManager(this));

        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.recycler_divider);
        expenseListView.addItemDecoration(new MyDividerItemDecoration(this, dividerDrawable));

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
                ExpenseModel expense = mExpenseList.get(position);

                if (which == 0) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), ExpenseRecordEditActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("expenseId", expense.getId()); // + 1
                    intent.putExtra("tripId", mTripId);
                    startActivity(intent);
                    finish();
                }
                else {
                    showDeleteDialog(position); // + 1
                }
            }
        });

        builder.show();
    }
    
    private void showDeleteDialog(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.notice_deleteConfirmTitle);
        alertDialog.setMessage(R.string.notice_deleteExpenseConfirm);

        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteExpense(position);
                dialog.cancel();
            }
        });

        alertDialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
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

    }

    @Override
    public void getExpenses_INFO(List<ExpenseModel> mAllExpense, int mCount, ExpenseModel mExpense) {
        if (mAllExpense != null){
            mExpenseList.clear();
            mExpenseList.addAll(mAllExpense);

            adapter.notifyDataSetChanged();

            GetCorrectExpenseList();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), TripListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        ExpenseListActivity.super.onBackPressed();
    }
}
