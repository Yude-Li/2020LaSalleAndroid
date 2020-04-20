package databaseinteraction;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import commonutilities.Constants;

 class DatabaseHelper  extends SQLiteOpenHelper {


    private String TRIPINFO_CREATE, EXPENSE_CREATE;



    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);


        this.TRIPINFO_CREATE = "create table "
                + Constants.TABLE_NAME_TRIPINFO
                + " (_id integer primary key autoincrement, "+
                Constants.COLUMN_TRIPNAME+" text not null, "+
                Constants.COLUMN_TRAVELCOUNTRY+" text not null, "+
                Constants.COLUMN_STARTDATE+" text not null, "+
                Constants.COLUMN_ENDDATE+" text not null, " +
                Constants.COLUMN_TAX+" text not null, "+
                Constants.COLUMN_BREAKFAST_TIP+" text not null, "+
                Constants.COLUMN_LUNCH_TIP+" text not null, "+
                //Amount
                // Total amount.
                Constants.COLUMN_DINNER_TIP+" text not null, "+
                "UNIQUE("+Constants.COLUMN_TRIPNAME+", "+
                Constants.COLUMN_TRAVELCOUNTRY+") ON CONFLICT REPLACE );";

        this.EXPENSE_CREATE = "create table "
                + Constants.TABLE_NAME_EXPENSE
                + " (_id integer primary key autoincrement, "+
                Constants.COLUMN_EXPENSE_NAME+" text not null, "+
                Constants.COLUMN_EXPENSE_DATE+" text not null, "+
                Constants.COLUMN_EXPENSE_SPEND_AMOUNT+" text not null, "+
                Constants.COLUMN_EXPENSE_CONVERTED_AMOUNT+" text not null, " +
                Constants.COLUMN_EXPENSE_DESCRIPTION+" text not null, "+
                Constants.COLUMN_TRIPNAME +" text not null, "+


                "UNIQUE("+Constants.COLUMN_EXPENSE_NAME+", "+
                Constants.COLUMN_EXPENSE_DATE+") ON CONFLICT REPLACE );";

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table

try {
    db.execSQL(TRIPINFO_CREATE);
    db.execSQL(EXPENSE_CREATE);
}catch (Exception e){

    // do firebase logging here.

    e.printStackTrace();

}
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        // Create tables again
       // onCreate(db);
    }





}