package Model;

public class TripInfoModel  {


//TripPreferenceModel


    private int Id;
    private String tripName;
    private int travelCountry; // country Id
    private String startDate = "8/11/2020";
    private String endDate = "8/11/2020";
    private int tax;
    private int breakfastTip;
    private int lunchTip;
    private int dinnerTip;

    public TripInfoModel() {
    }

    public TripInfoModel(int id, String tripName, int travelCountry, String startDate, String endDate, int tax, int breakfastTip, int lunchTip, int dinnerTip) {
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
        //return "18/11/2020";
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
       // return "22/11/2020";
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public int getBreakfastTip() {
        return breakfastTip;
    }

    public void setBreakfastTip(int breakfastTip) {
        this.breakfastTip = breakfastTip;
    }

    public int getLunchTip() {
        return lunchTip;
    }

    public void setLunchTip(int lunchTip) {
        this.lunchTip = lunchTip;
    }

    public int getDinnerTip() {
        return dinnerTip;
    }

    public void setDinnerTip(int dinnerTip) {
        this.dinnerTip = dinnerTip;
    }
}
