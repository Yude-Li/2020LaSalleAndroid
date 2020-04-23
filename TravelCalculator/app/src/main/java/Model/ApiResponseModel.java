package Model;

public class ApiResponseModel {

    private String currencyBaseCode;
//    private String currencyConvertCode;
    private double currencyRateBase;
    private double currencyRateConvert;

    public ApiResponseModel(String currencyBaseCode, double currencyRateBase,double currencyRateConvert) {
        this.currencyBaseCode = currencyBaseCode;
        this.currencyRateBase = currencyRateBase;

        this.currencyRateConvert = currencyRateConvert;
    }

    public String getCurrencyBaseCode() {
        return currencyBaseCode;
    }

    public double getCurrencyRateBase() {
        return currencyRateBase;
    }

    public double getCurrencyRateConvert() {
        return currencyRateConvert;
    }
}
