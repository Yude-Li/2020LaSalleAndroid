package databaseinteraction;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import Model.ExpenseModel;
import Model.TripInfoModel;
import callbacks.DatabaseOperationNotifier;
import commonutilities.Constants;
import commonutilities.ProgressBar_YAM;

public class DatabaseOperations_Thread extends AsyncTask {


    private Context mContext;
    private AppCompatActivity mActivity;
    private Constants.DATABSE_OPERATION mOperation_Name;
    private Constants.TABLE mTableName;
    private DatabaseOperationNotifier mDatabaseOperationNotifier;
    private List<TripInfoModel> mAllTrips;
    private List<ExpenseModel> mAllExpenses;
    private ProgressBar_YAM mProgressDialog;


    public DatabaseOperations_Thread(Context mContext) {

    }

    public DatabaseOperations_Thread(Context mContext, Constants.TABLE mTableName,
                                     Constants.DATABSE_OPERATION mOperation_Name,
                                     DatabaseOperationNotifier mDatabaseOperationNotifier) {
        this.mContext = mContext;
        this.mOperation_Name = mOperation_Name;
        this.mDatabaseOperationNotifier = mDatabaseOperationNotifier;


    }

    public DatabaseOperations_Thread(AppCompatActivity mActivity, Constants.TABLE mTableName,
                                     Constants.DATABSE_OPERATION mOperation_Name,
                                     DatabaseOperationNotifier mDatabaseOperationNotifier) {
        this.mActivity = mActivity;
        this.mOperation_Name = mOperation_Name;
        this.mDatabaseOperationNotifier = mDatabaseOperationNotifier;


    }

    @Override
    protected Object doInBackground(Object[] objects) {

        DBOperations dbOperations = new DBOperations(mActivity);

        switch (mOperation_Name) {

            case DB_CREATE:

                dbOperations.open();
                dbOperations.close();

                break;


            case FETCH_ALL:

                if (mTableName == Constants.TABLE.TRIPINFO) {

                    mAllTrips = dbOperations.getAllTrips();

                } else if (mTableName == Constants.TABLE.EXPENSE) {
                    mAllExpenses = dbOperations.getAllExpenses();
                }

                break;


        }


        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // All the progress spinner showing goes here
        mProgressDialog = new ProgressBar_YAM();
        mProgressDialog.setCancelable(false);
        mProgressDialog.show(mActivity.getSupportFragmentManager(), "UI");

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        switch (mOperation_Name) {

            case FETCH_ALL:

                if (mTableName == Constants.TABLE.TRIPINFO) {

                    mDatabaseOperationNotifier.getTrips_INFO(mAllTrips, 0, null);

                } else if (mTableName == Constants.TABLE.EXPENSE) {
                    mDatabaseOperationNotifier.getExpenses_INFO(mAllExpenses, 0, null);
                }
                break;

            case ADD_RECORD:

                if (mTableName == Constants.TABLE.TRIPINFO) {


                } else if (mTableName == Constants.TABLE.EXPENSE) {

                }

                break;

        }


    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
