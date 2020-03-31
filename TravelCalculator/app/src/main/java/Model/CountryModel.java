package Model;

public class CountryModel {

    private int Id;
    private int countryNum;
    private int countryCode;
    private String displayName;
    private String currencyCode;
    private float currency;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCountryNumber() {
        return countryNum;
    }

    public void setCountryNumber(int countryNumber) {
        this.countryNum = countryNumber;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int code) {
        this.countryCode = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public float getCurrency() {
        return currency;
    }

    public void setCurrency(float currency) {
        this.currency = currency;
    }
}
