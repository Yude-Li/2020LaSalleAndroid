package Model;

public class TripInfoModel {

    public static final String TABLE_NAME = "trips";

    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_TRIPNAME = "tripName";
    public static final String COLUMN_TRAVELCOUNTRY = "travelCountry";
    public static final String COLUMN_STARTDATE = "startDate";
    public static final String COLUMN_ENDDATE = "endDate";
    public static final String COLUMN_TAX = "tax";
    public static final String COLUMN_BREAKFAST_TIP = "breakfastTip";
    public static final String COLUMN_LUNCH_TIP = "lunchTip";
    public static final String COLUMN_DINNER_TIP = "dinnerTip";

    private int Id;
    private String tripName;
    private int travelCountry; // country Id
    private String startDate;
    private String endDate;

    private float tax;
    private float breakfastTip;
    private float lunchTip;
    private float dinnerTip;

    public TripInfoModel() {
    }

    public TripInfoModel(int id, String tripName, int travelCountry, String startDate, String endDate, float tax, float breakfastTip, float lunchTip, float dinnerTip) {
        Id = id;
        this.tripName = tripName;
        this.travelCountry = travelCountry;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tax = tax;
        this.breakfastTip = breakfastTip;
        this.lunchTip = lunchTip;
        this.dinnerTip = dinnerTip;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public int getTravelCountry() {
        return travelCountry;
    }

    public void setTravelCountry(int travelCountry) {
        this.travelCountry = travelCountry;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getBreakfastTip() {
        return breakfastTip;
    }

    public void setBreakfastTip(float breakfastTip) {
        this.breakfastTip = breakfastTip;
    }

    public float getLunchTip() {
        return lunchTip;
    }

    public void setLunchTip(float lunchTip) {
        this.lunchTip = lunchTip;
    }

    public float getDinnerTip() {
        return dinnerTip;
    }

    public void setDinnerTip(float dinnerTip) {
        this.dinnerTip = dinnerTip;
    }
}
