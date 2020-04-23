package Model;

public class ApiResponseModel {

    private String currencyBaseCode;
    private String currencyCode;
    private double currencyRateBase;
    private double currencyRateConvert;

    public ApiResponseModel(String currencyBaseCode, double currencyRateBase, double currencyRateConvert) {
        this.currencyBaseCode = currencyBaseCode;
        this.currencyCode = currencyCode;
        this.currencyRateBase = currencyRateBase;
        this.currencyRateConvert = currencyRateConvert;
    }

    public ApiResponseModel(String currencyCode, double currencyRateConvert) {
        this.currencyCode = currencyCode;
        this.currencyRateConvert = currencyRateConvert;
    }

    public String getCurrencyBaseCode() {
        return currencyBaseCode;
    }

    public void setCurrencyBaseCode(String currencyBaseCode) {
        this.currencyBaseCode = currencyBaseCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getCurrencyRateBase() {
        return currencyRateBase;
    }

    public void setCurrencyRateBase(double currencyRateBase) {
        this.currencyRateBase = currencyRateBase;
    }

    public double getCurrencyRateConvert() {
        return currencyRateConvert;
    }

    public void setCurrencyRateConvert(double currencyRateConvert) {
        this.currencyRateConvert = currencyRateConvert;
    }
}
