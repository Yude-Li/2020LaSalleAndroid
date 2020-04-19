package com.lasalle2020android.travelcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import Model.TripInfoModel;
import commonutilities.Constants;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "travel_history_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        //db.execSQL();
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long addTrip(TripInfoModel trip) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Constants.COLUMN_TRIPNAME, trip.getTripName());
        values.put(Constants.COLUMN_TRAVELCOUNTRY, trip.getTravelCountry());
        values.put(Constants.COLUMN_STARTDATE, trip.getStartDate());
        values.put(Constants.COLUMN_ENDDATE, trip.getEndDate());
        values.put(Constants.COLUMN_TAX, trip.getTax());
        values.put(Constants.COLUMN_BREAKFAST_TIP, trip.getBreakfastTip());
        values.put(Constants.COLUMN_LUNCH_TIP, trip.getLunchTip());
        values.put(Constants.COLUMN_DINNER_TIP, trip.getDinnerTip());

        // insert row
        long id = db.insert(Constants.TABLE_NAME_TRIPINFO, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public TripInfoModel getTripInfo(int id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME_TRIPINFO,
                new String[]{Constants.COLUMN_ID
                           , Constants.COLUMN_TRIPNAME
                           , Constants.COLUMN_TRAVELCOUNTRY
                           , Constants.COLUMN_STARTDATE
                           , Constants.COLUMN_ENDDATE
                           , Constants.COLUMN_TAX
                           , Constants.COLUMN_BREAKFAST_TIP
                           , Constants.COLUMN_LUNCH_TIP
                           , Constants.COLUMN_DINNER_TIP},
                Constants.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        TripInfoModel trip = new TripInfoModel(
                                cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_TRIPNAME)),
                                cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_TRAVELCOUNTRY)),
                                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_STARTDATE)),
                                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_ENDDATE)),
                                cursor.getFloat(cursor.getColumnIndex(Constants.COLUMN_TAX)),
                                cursor.getFloat(cursor.getColumnIndex(Constants.COLUMN_BREAKFAST_TIP)),
                                cursor.getFloat(cursor.getColumnIndex(Constants.COLUMN_LUNCH_TIP)),
                                cursor.getFloat(cursor.getColumnIndex(Constants.COLUMN_DINNER_TIP)));

        // close the db connection
        cursor.close();

        return trip;
    }

    public List<TripInfoModel> getAllTrips() {
        List<TripInfoModel> trips = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Constants.TABLE_NAME_TRIPINFO + " ORDER BY " +
                Constants.COLUMN_STARTDATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TripInfoModel trip = new TripInfoModel();
                trip.setId(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_ID)));
                trip.setTripName(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_TRIPNAME)));
                trip.setTravelCountry(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_TRAVELCOUNTRY)));
                trip.setStartDate(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_STARTDATE)));
                trip.setEndDate(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_ENDDATE)));
                trip.setTax(cursor.getFloat(cursor.getColumnIndex(Constants.COLUMN_TAX)));
                trip.setBreakfastTip(cursor.getFloat(cursor.getColumnIndex(Constants.COLUMN_BREAKFAST_TIP)));
                trip.setLunchTip(cursor.getFloat(cursor.getColumnIndex(Constants.COLUMN_LUNCH_TIP)));
                trip.setDinnerTip(cursor.getFloat(cursor.getColumnIndex(Constants.COLUMN_DINNER_TIP)));

                trips.add(trip);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return trips;
    }

    public int getTripsCount() {
        String countQuery = "SELECT  * FROM " + Constants.TABLE_NAME_TRIPINFO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteTrip(TripInfoModel trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME_TRIPINFO, Constants.COLUMN_ID + " = ?",
                new String[]{String.valueOf(trip.getId())});
        db.close();
    }
}