package Model;

import android.content.Context;

public class CountryModel {

    private int Id;
    private int countryNum;
    private String countryCode;
    private String displayName;
    private String currencyCode;
    private float currency;
    private String currencyName;
    private String currencySymbol;
    private String flagId;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCountryNum() {
        return countryNum;
    }

    public void setCountryNum(int countryNum) {
        this.countryNum = countryNum;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public int getFlagId(Context context) {
        int flag = context.getResources().getIdentifier(this.flagId, "drawable", context.getPackageName());
        return flag;
    }

    public void setFlagId(String flagId) {
        this.flagId = flagId;
    }
}
