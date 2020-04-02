package currencies;

/**
 * Created by Bugalia on 24/03/2020.
 */

public interface CurrencyPickerListener {
    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID);
}
