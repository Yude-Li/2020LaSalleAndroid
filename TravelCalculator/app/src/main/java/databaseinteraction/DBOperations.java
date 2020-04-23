package databaseinteraction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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



// TRIP TABLE


    public List<TripInfoModel> getAllTrips() {
        List<TripInfoModel> trips = new ArrayList<>();
        TripInfoModel trip = new TripInfoModel();
        trip.setId(9999);
        trip.setTravelCountry(9999);
        trip.setTripName("Select Trip");
        trip.setStartDate("");
        trips.add(trip);


        // Select All Query
        String selectQuery = "SELECT  * FROM " + Constants.TABLE_NAME_TRIPINFO + " ORDER BY " +
                Constants.COLUMN_ID + " DESC";


        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount() > 0
                && cursor.moveToFirst()) {
            do {
                 trip = new TripInfoModel();
                int coiIndex= cursor.getColumnIndex(Constants.COLUMN_ID);
                trip.setId((int)cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID)));
                trip.setTripName(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_TRIPNAME)));
                trip.setTravelCountry(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_TRAVELCOUNTRY)));
                trip.setStartDate(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_STARTDATE)));
                trip.setEndDate(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_ENDDATE)));
                trip.setTax(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_TAX)));
                trip.setBreakfastTip(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_BREAKFAST_TIP)));
                trip.setLunchTip(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_LUNCH_TIP)));
                trip.setDinnerTip(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_DINNER_TIP)));

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

    public boolean deleteTrip(TripInfoModel trip) {

        long id=-1;
try {
   id=  database.delete(Constants.TABLE_NAME_TRIPINFO, Constants.COLUMN_ID + " = ?",
            new String[]{String.valueOf(trip.getId())});
}catch (Exception e){


}


     close();
        if(id==1){
            return true;
        }else {
            return false;
        }
    }



    public TripInfoModel getTripPreferenceInfoByID(int id) {
        // get readable database as we are not inserting anything
        String selection = Constants.COLUMN_ID+" = '"+ id+"'";
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
                selection, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        TripInfoModel trip = new TripInfoModel(
                cursor.getInt((int)cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_TRIPNAME)),
                cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_TRAVELCOUNTRY)),
                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_STARTDATE)),
                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_ENDDATE)),
                cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_TAX)),
                cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_BREAKFAST_TIP)),
                cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_LUNCH_TIP)),
                cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_DINNER_TIP)));

        // close the db connection
      close();

        return trip;
    }


private ContentValues getTripValues(TripInfoModel trip){

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

    return  values;
}

    public boolean addTrip(TripInfoModel trip) {
        // get writable database as we want to write data

boolean ret=false;


        // insert row
        long id = database.insert(Constants.TABLE_NAME_TRIPINFO, null, getTripValues(trip));

        // close db connection
        if(id==1){
            ret=true;
        }
        close();

        // return newly inserted row id
        return ret;
    }




    // Expense Table

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
                 trip.setSpendAmount(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_SPEND_AMOUNT)));
                 trip.setConvertedAmount(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_CONVERTED_AMOUNT)));
                 trip.setDate(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_DATE)));
                 trip.setTripId(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_TRIP_ID)));
                 trip.setExpenseDesc(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_DESCRIPTION)) + "");

                 trips.add(trip);
             } while (cursor.moveToNext());
         }

         // close db connection
         close();

         // return notes list
         return trips;
     }

     private ContentValues getExpenseValues(ExpenseModel mExpenseModel){

         ContentValues values = new ContentValues();
         // `id` and `timestamp` will be inserted automatically.
         // no need to add them
         values.put(Constants.COLUMN_EXPENSE_NAME, mExpenseModel.getExpenseName());
         values.put(Constants.COLUMN_EXPENSE_DATE, mExpenseModel.getDate());
         values.put(Constants.COLUMN_EXPENSE_SPEND_AMOUNT, mExpenseModel.getSpendAmount());
         values.put(Constants.COLUMN_EXPENSE_CONVERTED_AMOUNT, mExpenseModel.getConvertedAmount());
         values.put(Constants.COLUMN_EXPENSE_DESCRIPTION, mExpenseModel.getExpenseDesc());
         values.put(Constants.COLUMN_TRIP_ID, mExpenseModel.getTripId());

         return  values;
     }

     public boolean addExpense(ExpenseModel mExpenseModel) {
         // get writable database as we want to write data



         // insert row
         long id = database.insert(Constants.TABLE_NAME_EXPENSE, null, getExpenseValues(mExpenseModel));

         // close db connection
         close();

         // return newly inserted row id
         if(id==1){
             return true;
         }else {
             return false;
         }

     }



     public ExpenseModel getExpenseDetailsByID(int id) {


         Cursor cursor = database.query(Constants.TABLE_NAME_EXPENSE,
                 new String[]{Constants.COLUMN_ID
                         , Constants.COLUMN_EXPENSE_NAME
                         , Constants.COLUMN_EXPENSE_SPEND_AMOUNT
                         , Constants.COLUMN_EXPENSE_CONVERTED_AMOUNT
                         , Constants.COLUMN_EXPENSE_DATE
                         , Constants.COLUMN_TRIP_ID
                         , Constants.COLUMN_EXPENSE_DESCRIPTION},
                 Constants.COLUMN_ID + "=?",
                 new String[]{String.valueOf(id)}, null, null, null, null);

         if (cursor != null)
             cursor.moveToFirst();


         ExpenseModel mExpense = new ExpenseModel(
                 cursor.getInt(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_ID))),
                 cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_NAME)),
                 cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_SPEND_AMOUNT)),
                 cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_CONVERTED_AMOUNT)),
                 cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_DATE)),
                 cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_TRIP_ID)),
                 cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EXPENSE_DESCRIPTION)));

         // close the db connection
         close();

         return mExpense;
     }



     public int getExpenseCount() {
         String countQuery = "SELECT  * FROM " + Constants.TABLE_NAME_EXPENSE;

         Cursor cursor = database.rawQuery(countQuery, null);
         int count = cursor.getCount();
         cursor.close();
         return count;
     }

     public boolean deleteExpense(ExpenseModel expenseModel) {

        long id= database.delete(Constants.TABLE_NAME_EXPENSE, Constants.COLUMN_ID + " = ?",
                 new String[]{String.valueOf(expenseModel.getId())});


         close();
         if(id==1){
             return true;
         }else {
             return false;
         }
     }



     public boolean updateExpense(ExpenseModel expenseModel) {

        long id=  database.update(Constants.TABLE_NAME_EXPENSE, getExpenseValues(expenseModel),Constants.COLUMN_ID + " = ?",
                 new String[]{String.valueOf(expenseModel.getId())});
         close();

         if(id==1){
             return true;
         }else {
             return false;
         }
     }

     public boolean updateTrips(TripInfoModel tripInfoModel) {


//         String selection = "txnType = '"+ txnType+"'"
//              ;
      long id=   database.update(Constants.TABLE_NAME_TRIPINFO, getTripValues(tripInfoModel),Constants.COLUMN_ID + " = ?",
                 new String[]{String.valueOf(tripInfoModel.getId())});
         close();

         if(id==1){
             return true;
         }else {
             return false;
         }
     }

 }
