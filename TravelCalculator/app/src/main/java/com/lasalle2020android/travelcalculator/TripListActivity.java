package com.lasalle2020android.travelcalculator;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Model.CountryModel;
import Model.ExpenseModel;
import Model.TripInfoModel;
import ViewUsage.MyDividerItemDecoration;
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

        PrepareListView();
        GetListFromDb();
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
                intent.putExtra("tripId", -1);
                startActivity(intent);
                this.finish();
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
                TripInfoModel trip = searchedTripList.get(position);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ExpenseListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("tripId", trip.getId());
                startActivity(intent);
                finish();
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

//        for (int i = 0 ; i < 15 ; i ++) {
//            TripInfoModel trip = new TripInfoModel();
//            trip.setTripName("Demo" + String.valueOf(i));
//            trip.setTravelCountry(136);
//
//            searchedTripList.add(trip);
//        }
    }

    private void PrepareListView()
    {
        tripList = new ArrayList<>();
        searchedTripList = new ArrayList<>();
        adapter = new RecycleViewAdapter(searchedTripList, TripListActivity.this, 1);
        tripListView.setAdapter(adapter);
        tripListView.setLayoutManager(new LinearLayoutManager(this));
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.recycler_divider);
        tripListView.addItemDecoration(new MyDividerItemDecoration(this, dividerDrawable));
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
                TripInfoModel trip = searchedTripList.get(position);
                if (which == 0) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), TripInfoEditActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("tripId", trip.getId());
                    startActivity(intent);
                    finish();
                } else {
                    showDeleteDialog(trip.getId());
                }
            }
        });
        builder.show();
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.notice_deleteConfirmTitle);
        alertDialog.setMessage(R.string.notice_deleteTripConfirm);

        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                deleteTrip(position);
                dialog.cancel();
            }
        });
        alertDialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void deleteTrip(int position) {
        adapter.remove(position);
        tripList.remove(position);
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
    public void onUpdatePerformed(boolean isSuccess) {

    }

    @Override
    public void getTrips_INFO(List<TripInfoModel> mAllTrips, int mCount, TripInfoModel mTrip) {
        if (mAllTrips != null && mAllTrips.size()>1) {

            tripList.clear();
            tripList.addAll(mAllTrips);
            searchedTripList.clear();
            searchedTripList.addAll(tripList);

            for (int i = 0; i < tripList.size(); i++) {
                if (tripList.get(i).getId() == 9999)
                {
                    tripList.remove(i);
                    searchedTripList.remove(i);
                    break;
                }
            }

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getExpenses_INFO(List<ExpenseModel> mAllExpense, int mCount, ExpenseModel mExpense) {

    }
    // endregion
}
