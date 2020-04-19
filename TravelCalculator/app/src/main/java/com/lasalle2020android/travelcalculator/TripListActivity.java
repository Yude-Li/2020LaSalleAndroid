package com.lasalle2020android.travelcalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import Model.TripInfoModel;
import ViewUsage.MyDividerItemDecoration;
import ViewUsage.RecycleViewAdapter;
import ViewUsage.RecyclerTouchListener;

public class TripListActivity extends AppCompatActivity {

    RecyclerView tripListView;
    public SimpleAdapter listAdapter;

    RecycleViewAdapter adapter;
    ArrayList<TripInfoModel> tripList;
    private ArrayList<HashMap<String,Object>> tripDisplayList = new ArrayList<HashMap<String,Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_list_layout);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_triplist);
        setSupportActionBar(mTopToolbar);

        tripListView = findViewById(R.id.triplist_listview);

        tripList = new ArrayList<>();
        // Get trip list from Db
        //tripList = TripInfoModel.createContactsList(20);

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
        for (int i=0 ; i < 5; i++) {
            TripInfoModel trip = new TripInfoModel();

            trip.setId(i);
            trip.setTripName("Test" + String.valueOf(i));
            trip.setStartDate("20200315");
            trip.setEndDate("20200318");
            trip.setTax(15);
            trip.setBreakfastTip(15);
            trip.setLunchTip(18);
            trip.setDinnerTip(20);

            tripList.add(trip);
        }
    }

    private void PrepareListView()
    {
//        listAdapter = new SimpleAdapter(this, tripDisplayList, R.layout.listview_row_double_img
//                , new String[] {"icon", "tripName", "tripDate"} , new int[] {R.id.doublerow_icon, R.id.doublerow_title, R.id.doublerow_tripDate});
//        tripListView.setAdapter(listAdapter);
//        tripListView.setOnItemClickListener(
//            new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//                }
//            }
//        );
        adapter = new RecycleViewAdapter(tripList);
        tripListView.setAdapter(adapter);
        tripListView.setLayoutManager(new LinearLayoutManager(this));

        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.recycler_divider);
        tripListView.addItemDecoration(new MyDividerItemDecoration(this, dividerDrawable));

        recyclerViewOnTouchListener();
        adapter.notifyDataSetChanged();
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{getString(R.string.text_edit), getString(R.string.text_delete)};

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
}
