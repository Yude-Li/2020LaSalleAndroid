package Model;

public class ExpenseModel {

    //
private int id;
    private String expenseName="", date="", spendAmount="", convertedAmount="", tripId="", tripExpenseDesc="";


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ExpenseModel() {
    }

    public ExpenseModel(int id, String expenseName, String date, String spendAmount, String convertedAmount, String tripId, String tripDesc) {
        this.expenseName = expenseName;
        this.date = date;
        this.spendAmount = spendAmount;
        this.convertedAmount = convertedAmount;
        this.tripId = tripId;
        this.tripExpenseDesc= tripDesc;
        this.id=id;

    }

    public String getTripExpenseDesc() {
        return tripExpenseDesc;
    }

    public void setTripExpenseDesc(String tripExpenseDesc) {
        this.tripExpenseDesc = tripExpenseDesc;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSpendAmount() {
        return spendAmount;
    }

    public void setSpendAmount(String spendAmount) {
        this.spendAmount = spendAmount;
    }

    public String getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(String convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }


}
