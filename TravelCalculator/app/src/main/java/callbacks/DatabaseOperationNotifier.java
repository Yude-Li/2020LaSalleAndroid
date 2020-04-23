package callbacks;

import java.util.List;

import Model.ExpenseModel;
import Model.TripInfoModel;

public interface DatabaseOperationNotifier {

    void onSavePerformed(boolean isCompletedSuccessfully);
    void onCurrencyRetrivePerformed(boolean isSuccess);
    void onDB_BootCompleted(boolean isSuccess);
    void onDeletePerformed(boolean isSuccess );
    void onUpdatePerformed(boolean isSuccess);

    void getTrips_INFO(List<TripInfoModel> mAllTrips, int mCount, TripInfoModel mTrip );
    void getExpenses_INFO(List<ExpenseModel> mAllExpense, int mCount, ExpenseModel mExpense );

}
