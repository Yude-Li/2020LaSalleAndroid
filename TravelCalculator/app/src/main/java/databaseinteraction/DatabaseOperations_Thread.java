package databaseinteraction;

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
    private Constants.DATABASE_OPERATION mOperation_Name;
    private Constants.TABLE mTableName;
    private DatabaseOperationNotifier mDatabaseOperationNotifier;
    private List<TripInfoModel> mAllTrips;
    private List<ExpenseModel> mAllExpenses;
    private ProgressBar_YAM mProgressDialog;
    private TripInfoModel mTripInfoModel;
    private ExpenseModel mExpenseModel;
    private int rowID;


    public DatabaseOperations_Thread(Context mContext) {

    }

    public DatabaseOperations_Thread(Context mContext, Constants.TABLE mTableName,
                                     Constants.DATABASE_OPERATION mOperation_Name,
                                     DatabaseOperationNotifier mDatabaseOperationNotifier) {
        this.mContext = mContext;
        this.mOperation_Name = mOperation_Name;
        this.mDatabaseOperationNotifier = mDatabaseOperationNotifier;
        this.mTableName=mTableName;



    }

    public DatabaseOperations_Thread(AppCompatActivity mActivity, Constants.TABLE mTableName,
                                     Constants.DATABASE_OPERATION mOperation_Name,
                                     DatabaseOperationNotifier mDatabaseOperationNotifier) {
        this.mActivity = mActivity;
        this.mOperation_Name = mOperation_Name;
        this.mDatabaseOperationNotifier = mDatabaseOperationNotifier;
        this.mTableName=mTableName;



    }

    public DatabaseOperations_Thread(AppCompatActivity mActivity, Constants.TABLE mTableName,
                                     Constants.DATABASE_OPERATION mOperation_Name,
                                     DatabaseOperationNotifier mDatabaseOperationNotifier, TripInfoModel tripInfoModel, ExpenseModel expenseModel) {
        this.mActivity = mActivity;
        this.mOperation_Name = mOperation_Name;
        this.mDatabaseOperationNotifier = mDatabaseOperationNotifier;
        this.mTripInfoModel=tripInfoModel;
        this.mExpenseModel= expenseModel;
        this.mTableName=mTableName;


    }


    public DatabaseOperations_Thread(AppCompatActivity mActivity, Constants.TABLE mTableName,
                                     Constants.DATABASE_OPERATION mOperation_Name,
                                     DatabaseOperationNotifier mDatabaseOperationNotifier,  int rowID) {
        this.mActivity = mActivity;
        this.mOperation_Name = mOperation_Name;
        this.mDatabaseOperationNotifier = mDatabaseOperationNotifier;
        this.rowID=rowID;
        this.mTableName=mTableName;


    }
    @Override
    protected Object doInBackground(Object[] objects) {

        DBOperations dbOperations = new DBOperations(mActivity);
        dbOperations.open();
        switch (mOperation_Name) {

            case DB_CREATE:


                dbOperations.close();

                break;


            case FETCH_ALL:

                if (mTableName == Constants.TABLE.TRIPINFO) {

                    mAllTrips = dbOperations.getAllTrips();

                } else if (mTableName == Constants.TABLE.EXPENSE) {
                    mAllExpenses = dbOperations.getAllExpenses();
                }

                break;
            case FETCH_SELECTED:

                if (mTableName == Constants.TABLE.TRIPINFO) {

                    mTripInfoModel = dbOperations.getTripPreferenceInfoByID(rowID);

                } else if (mTableName == Constants.TABLE.EXPENSE) {
                    mExpenseModel = dbOperations.getExpenseDetailsByID(rowID);
                }

                break;
            case DELETE_RECORD:
                if (mTableName == Constants.TABLE.TRIPINFO) {

                    dbOperations.deleteTrip(mTripInfoModel);

                }else if (mTableName == Constants.TABLE.EXPENSE) {
                    dbOperations.deleteExpense(mExpenseModel);
                }
                break;
            case ADD_RECORD:
                if (mTableName == Constants.TABLE.TRIPINFO) {

                    dbOperations.addTrip(mTripInfoModel);

                }else if (mTableName == Constants.TABLE.EXPENSE) {
                    dbOperations.addExpense(mExpenseModel);
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

        if(mProgressDialog!=null){

            mProgressDialog.cancel();
        }

        switch (mOperation_Name) {

            case FETCH_ALL:

                if (mTableName == Constants.TABLE.TRIPINFO) {

                    mDatabaseOperationNotifier.getTrips_INFO(mAllTrips, 0, null);

                } else if (mTableName == Constants.TABLE.EXPENSE) {
                    mDatabaseOperationNotifier.getExpenses_INFO(mAllExpenses, 0, null);
                }
                break;


            case DB_CREATE:
                mDatabaseOperationNotifier.onDB_BootCompleted(true);
                break;

            case DELETE_RECORD:
                if (mTableName == Constants.TABLE.TRIPINFO) {
                mDatabaseOperationNotifier.onDeletePerformed(true);
                } else if (mTableName == Constants.TABLE.EXPENSE) {
                    mDatabaseOperationNotifier.onDeletePerformed(true);
                }



            case FETCH_SELECTED:

                if (mTableName == Constants.TABLE.TRIPINFO) {
                    mDatabaseOperationNotifier.getTrips_INFO(null,0,mTripInfoModel);


                } else if (mTableName == Constants.TABLE.EXPENSE) {
                    mDatabaseOperationNotifier.getExpenses_INFO(mAllExpenses, 0, mExpenseModel);
                }


                break;

            case ADD_RECORD:
                if (mTableName == Constants.TABLE.TRIPINFO) {

                    mDatabaseOperationNotifier.onSavePerformed(true);

                }else if (mTableName == Constants.TABLE.EXPENSE) {
                    mDatabaseOperationNotifier.onSavePerformed(true);
                }


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
