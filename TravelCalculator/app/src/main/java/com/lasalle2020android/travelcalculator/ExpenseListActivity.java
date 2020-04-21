package com.lasalle2020android.travelcalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import Model.ExpenseModel;
import Model.TripInfoModel;
import ViewUsage.RecycleViewAdapter;
import ViewUsage.RecyclerTouchListener;
import callbacks.DatabaseOperationNotifier;
import commonutilities.Constants;
import databaseinteraction.DatabaseOperations_Thread;

public class ExpenseListActivity extends AppCompatActivity implements DatabaseOperationNotifier {

    RecyclerView expenseListView;
    
    RecycleViewAdapter adapter;
    List<ExpenseModel> expenseList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_list_layout);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_expenselist);
        setSupportActionBar(mTopToolbar);
        
        expenseListView = findViewById(R.id.expenselist_listview);

        GetListFromDb();
        PrepareListView();
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
                startActivity(intent);
            }
            break;
        }

        return super.onOptionsItemSelected(item);
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
    
    private void GetListFromDb() {
        new DatabaseOperations_Thread(ExpenseListActivity.this, Constants.TABLE.EXPENSE,
                Constants.DATABASE_OPERATION.FETCH_ALL, this).execute();

    }
    
    private void PrepareListView(){
        expenseList = new ArrayList<>();

        adapter = new RecycleViewAdapter(expenseList, 0);
        expenseListView.setAdapter(adapter);
        expenseListView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOnTouchListener();
        adapter.notifyDataSetChanged();
    }
    
    private void showActionsDialog(final int position){
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // showNoteDialog(true, expenseList.get(position), position);
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
        builder.setMessage("Are you sure you want to delete this expense?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                adapter.remove(position);
                expenseList.remove(position);
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
        expenseList = mAllExpense;
        adapter.notifyDataSetChanged();
    }
}
