package com.lasalle2020android.travelcalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import Model.TripInfoModel;

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
        adapter = new RecycleViewAdapter(tripList);
        tripListView.setAdapter(adapter);
        tripListView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOnTouchListener();

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
        adapter.notifyDataSetChanged();
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (which == 0) {
//                    showNoteDialog(true, notesList.get(position), position);
//                } else {
//                    deleteNote(position);
//                }
            }
        });
        builder.show();
    }
}
