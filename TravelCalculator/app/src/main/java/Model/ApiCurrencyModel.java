package Model;

public class ApiCurrencyModel {

    private String currencyCode;
    private double currencyRate;

    public ApiCurrencyModel(String currencyCode, double currencyRate) {
        this.currencyCode = currencyCode;
        this.currencyRate = currencyRate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(double currencyRate) {
        this.currencyRate = currencyRate;
    }
}
