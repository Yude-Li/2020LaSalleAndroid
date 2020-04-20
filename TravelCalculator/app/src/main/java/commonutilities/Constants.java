package commonutilities;

public class Constants {
    public static final String DATABASE_NAME="travel_history_db";
    public static final int DATABASE_VERSION = 1;

    //Common COlumns
    public static final String COLUMN_ID = "Id";


    /// TripInfoModel Table

    public static final String TABLE_NAME_TRIPINFO = "trips";


    public static final String COLUMN_TRIPNAME = "tripName";
    public static final String COLUMN_TRAVELCOUNTRY = "travelCountry";
    public static final String COLUMN_STARTDATE = "startDate";
    public static final String COLUMN_ENDDATE = "endDate";
    public static final String COLUMN_TAX = "tax";
    public static final String COLUMN_BREAKFAST_TIP = "breakfastTip";
    public static final String COLUMN_LUNCH_TIP = "lunchTip";
    public static final String COLUMN_DINNER_TIP = "dinnerTip";


    /// Expense Table


    public static final String TABLE_NAME_EXPENSE = "expenses";


    public static final String COLUMN_EXPENSE_DATE = "expenseDate";
    public static final String COLUMN_EXPENSE_NAME = "expenseName";
    public static final String COLUMN_EXPENSE_SPEND_AMOUNT = "expenseSpendAmount";
    public static final String COLUMN_EXPENSE_CONVERTED_AMOUNT = "expenseConvertedAmount"; //converted spend amount to home currency
    public static final String COLUMN_EXPENSE_DESCRIPTION = "expenseDescription";








    // Tables Enum

    public   enum  TABLE{

        TRIPINFO,
        EXPENSE,
        ALL

    }



    // Database Operation ENUM


    public enum DATABSE_OPERATION{


        ADD_RECORD,
        UPDATE_RECORD,
        DELETE_RECORD,
        FETCH_ALL,
        FETCH_SELECTED,
        FETCH_LAST_FIVE,
        FETCH_LAST,
        DB_CREATE,
        DB_DELETE


    }


//

    public  enum ConfigName
    {
        COUNTRY_INFO_CONFIG("countryinfo.config"),
        SETTING_CONFIG("setting.config");


        @SuppressWarnings("unused")
        private final String name;

        ConfigName(String s) {
            name = s;
        }
    }





}
