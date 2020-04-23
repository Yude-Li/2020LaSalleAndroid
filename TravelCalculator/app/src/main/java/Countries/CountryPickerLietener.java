package Countries;

public interface CountryPickerLietener {
    void onSelectCountry(int Id, String displayName, int countryNum, String countryCode, String currencyCode, double currency, String currencyName, String currencySymbol, int flagDrawableResID);
}
