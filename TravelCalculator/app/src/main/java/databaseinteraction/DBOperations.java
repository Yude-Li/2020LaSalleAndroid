package databaseinteraction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Model.ExpenseModel;
import Model.TripInfoModel;
import commonutilities.Constants;

 class DBOperations {

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;




    public DBOperations(Context ctx) {
        this.context = ctx;
    }

    public DBOperations open() throws SQLException {
        dbHelper = new DatabaseHelper(context);

        try {


            //invoke the following in problems while development to start fresh/all over again.
//            if (context.getClass().getName().matches("MainActivity")) {
//                if (context.deleteDatabase(Constants.DATABASE_NAME)) {
//                    Toast.makeText(context, "deleted", Toast.LENGTH_LONG)
//                            .show();
//                }
//
//            }

            database = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return this;
    }

    public void close() {
        dbHelper.close();
    }






    public List<TripInfoModel> getAllTrips() {
        List<TripInfoModel> trips = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Constants.TABLE_NAME_TRIPINFO + " ORDER BY " +
                Constants.COLUMN_STARTDATE + " DESC";


        Cursor cursor = database.rawQuery(selectQuery, null);

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
       close();

        // return notes list
        return trips;
    }


     public List<ExpenseModel> getAllExpenses() {
         List<ExpenseModel> trips = new ArrayList<>();

         // Select All Query
         String selectQuery = "SELECT  * FROM " + Constants.TABLE_NAME_EXPENSE + " ORDER BY " +
                 Constants.COLUMN_EXPENSE_DATE + " DESC";


         Cursor cursor = database.rawQuery(selectQuery, null);

         // looping through all rows and adding to list
         if (cursor.moveToFirst()) {
             do {
                 ExpenseModel trip = new ExpenseModel();
                 trip.setId(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_ID)));
                 trip.setExpenseName(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_NAME)));
                 trip.setDate(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_DATE)));
                 trip.setSpendAmount(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_SPEND_AMOUNT)));
                 trip.setConvertedAmount(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_CONVERTED_AMOUNT)));
                 trip.setTripExpenseDesc(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_DESCRIPTION)));
                 trip.setTripId(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_TRIP_ID))+"");

                 trips.add(trip);
             } while (cursor.moveToNext());
         }

         // close db connection
         close();

         // return notes list
         return trips;
     }



     public int getTripsCount() {
        String countQuery = "SELECT  * FROM " + Constants.TABLE_NAME_TRIPINFO;

        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteTrip(TripInfoModel trip) {

        database.delete(Constants.TABLE_NAME_TRIPINFO, Constants.COLUMN_ID + " = ?",
                new String[]{String.valueOf(trip.getId())});
     close();
    }



    public TripInfoModel getTripInfo(int id) {
        // get readable database as we are not inserting anything

        Cursor cursor = database.query(Constants.TABLE_NAME_TRIPINFO,
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
      close();

        return trip;
    }




    public long addTrip(TripInfoModel trip) {
        // get writable database as we want to write data


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
        long id = database.insert(Constants.TABLE_NAME_TRIPINFO, null, values);

        // close db connection
        close();

        // return newly inserted row id
        return id;
    }
}
