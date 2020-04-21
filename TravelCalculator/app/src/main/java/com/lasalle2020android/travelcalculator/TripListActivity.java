package com.lasalle2020android.travelcalculator;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Model.CountryModel;
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
    List<TripInfoModel> searchedTripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_list_layout);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_triplist);
        mTopToolbar.setTitle(R.string.toolBarTitle_tripList);
        setSupportActionBar(mTopToolbar);

        tripListView = findViewById(R.id.triplist_listview);

        GetListFromDb();
        PrepareListView();
        SearchBarHandler();
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
                intent.putExtra("DataIndex", -1);
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
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ExpenseListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("DataIndex", position);
                startActivity(intent);
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
        searchedTripList = new ArrayList<>();
        adapter = new RecycleViewAdapter(searchedTripList);
        tripListView.setAdapter(adapter);
        tripListView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOnTouchListener();
        adapter.notifyDataSetChanged();
    }

    private void SearchBarHandler() {
        EditText searchBar = findViewById(R.id.editText_searchTrip);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchTrip(s.toString());
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void searchTrip(String text) {
        searchedTripList.clear();
        for (TripInfoModel trip : tripList) {
            if (trip.getTripName().toLowerCase(Locale.ENGLISH).contains(text.toLowerCase())) {
                searchedTripList.add(trip);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showActionsDialog(final int position) {
        CharSequence actions[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), TripInfoEditActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("DataIndex", position);
                    startActivity(intent);
                } else {
                    showDeleteDialog(position);
                }
            }
        });
        builder.show();
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
        builder1.setTitle(R.string.notice_deleteConfirmTitle);
        builder1.setMessage(R.string.notice_deleteTripConfirm);
        builder1.setCancelable(true);
        builder1.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteTrip(position);
                dialog.cancel();
            }
        });

        builder1.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void deleteTrip(int position) {
        adapter.remove(position);
        tripList.remove(position);
        searchedTripList.remove(position);
        new DatabaseOperations_Thread(TripListActivity.this, Constants.TABLE.TRIPINFO,
                Constants.DATABASE_OPERATION.DELETE_RECORD, this, position).execute();
    }

    // region Database callback function
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
        Toast.makeText(TripListActivity.this, getString(R.string.deleteSuccess), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getTrips_INFO(List<TripInfoModel> mAllTrips, int mCount, TripInfoModel mTrip) {
        tripList.clear();
        tripList.addAll(mAllTrips);
        searchedTripList.clear();
        searchedTripList.addAll(tripList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getExpenses_INFO(List<ExpenseModel> mAllExpense, int mCount, ExpenseModel mExpense) {

    }
    // endregion
}
