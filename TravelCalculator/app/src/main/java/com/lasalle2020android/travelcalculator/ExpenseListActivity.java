package com.lasalle2020android.travelcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ExpenseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_list_layout);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_expenselist);
        setSupportActionBar(mTopToolbar);
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
}
