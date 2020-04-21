package com.lasalle2020android.travelcalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Model.ExpenseModel;
import Model.TripInfoModel;
import ViewUsage.RecycleViewAdapter;
import ViewUsage.RecyclerTouchListener;
import callbacks.DatabaseOperationNotifier;
import commonutilities.Constants;
import databaseinteraction.DatabaseOperations_Thread;

public class TripListActivity extends AppCompatActivity implements DatabaseOperationNotifier {

    // Object from view
    RecyclerView tripListView;

    // Variable for this activity
    RecycleViewAdapter adapter;
    List<TripInfoModel> tripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_list_layout);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_triplist);
        setSupportActionBar(mTopToolbar);

        tripListView = findViewById(R.id.triplist_listview);

        GetListFromDb();
        PrepareListView();
    }

    // Toolbar btns action
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Set the toolBar layout from menu
        getMenuInflater().inflate(R.menu.triplist_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Button action for actionBar(toolBar)
        int id = item.getItemId(); 

        switch (id) {
            case R.id.action_addTrip:
            {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), TripInfoEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    // RecyclerView item touch listener
    public void recyclerViewOnTouchListener() {
        tripListView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), tripListView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {


            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    private void GetListFromDb()
    {
        new DatabaseOperations_Thread(TripListActivity.this, Constants.TABLE.TRIPINFO,
                Constants.DATABASE_OPERATION.FETCH_ALL, this).execute();
    }

    private void PrepareListView()
    {
        tripList = new ArrayList<>();
        adapter = new RecycleViewAdapter(tripList);
        tripListView.setAdapter(adapter);
        tripListView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOnTouchListener();
        adapter.notifyDataSetChanged();
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //showNoteDialog(true, tripList.get(position), position);
                } else {
                    showDeleteDialog(position);
                }
            }
        });
        builder.show();
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
        builder1.setMessage("Are you sure you want to delete this trip?");
        builder1.setCancelable(true);

        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                adapter.remove(position);
                tripList.remove(position);
                dialog.cancel();
            }
        });

        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

        AlertDialog alert11 = builder1.create();
        alert11.show();
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
        tripList = mAllTrips;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getExpenses_INFO(List<ExpenseModel> mAllExpense, int mCount, ExpenseModel mExpense) {

    }
}
