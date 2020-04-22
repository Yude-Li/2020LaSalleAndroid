package Model;

public class TripInfoModel  {


//TripPreferenceModel


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
        //return startDate;
        return "18/11/2020";
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        //return endDate;
        return "22/11/2020";
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
