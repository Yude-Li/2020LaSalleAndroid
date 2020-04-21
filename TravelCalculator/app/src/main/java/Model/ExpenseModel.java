package Model;

public class ExpenseModel {

    private int id;
    private String expenseName;
    private String spendAmount;
    private String convertedAmount;
    private String date;
    private int tripId;
    private String expenseDesc;

    public ExpenseModel() {
    }

    public ExpenseModel(int id, String expenseName, String spendAmount, String convertedAmount, String date, int tripId, String expenseDesc) {
        this.id = id;
        this.expenseName = expenseName;
        this.spendAmount = spendAmount;
        this.convertedAmount = convertedAmount;
        this.date = date;
        this.tripId = tripId;
        this.expenseDesc = expenseDesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getExpenseDesc() {
        return expenseDesc;
    }

    public void setExpenseDesc(String expenseDesc) {
        this.expenseDesc = expenseDesc;
    }
}
