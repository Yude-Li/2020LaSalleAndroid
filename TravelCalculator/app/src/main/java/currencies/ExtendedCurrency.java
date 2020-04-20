package currencies;

import android.content.Context;

import com.lasalle2020android.travelcalculator.R;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Bugalia on 24/03/2020.
 */
public class ExtendedCurrency {
    public static final ExtendedCurrency[] CURRENCIES = {
            new ExtendedCurrency("EUR", "Euro", "€", R.drawable.flag_eur),
            new ExtendedCurrency("USD", "United States Dollar", "$", R.drawable.flag_us),
            new ExtendedCurrency("GBP", "British Pound", "£", R.drawable.flag_gg),
            new ExtendedCurrency("CZK", "Czech Koruna", "Kč", R.drawable.flag_cz),
            new ExtendedCurrency("TRY", "Turkish Lira", "₺", R.drawable.flag_tr),
            new ExtendedCurrency("AED", "Emirati Dirham", "د.إ", R.drawable.flag_ae),
            new ExtendedCurrency("AFN", "Afghanistan Afghani", "؋", R.drawable.flag_af),
            new ExtendedCurrency("ARS", "Argentine Peso", "$", R.drawable.flag_ar),
            new ExtendedCurrency("AUD", "Australian Dollar", "$", R.drawable.flag_au),
            new ExtendedCurrency("BBD", "Barbados Dollar", "$", R.drawable.flag_bb),
            new ExtendedCurrency("BDT", "Bangladeshi Taka", "Tk", R.drawable.flag_bd),
            new ExtendedCurrency("BGN", "Bulgarian Lev", "лв", R.drawable.flag_bg),
            new ExtendedCurrency("BHD", "Bahraini Dinar", "BD", R.drawable.flag_bh),
            new ExtendedCurrency("BMD", "Bermuda Dollar","$", R.drawable.flag_bm),
            new ExtendedCurrency("BND", "Brunei Darussalam Dollar","$", R.drawable.flag_bn),
            new ExtendedCurrency("BOB", "Bolivia Bolíviano","$b", R.drawable.flag_bo),
            new ExtendedCurrency("BRL", "Brazil Real","R$", R.drawable.flag_br),
            new ExtendedCurrency("BTN", "Bhutanese Ngultrum","Nu.", R.drawable.flag_bt),
            new ExtendedCurrency("BZD", "Belize Dollar","BZ$", R.drawable.flag_bz),
            new ExtendedCurrency("CAD", "Canada Dollar","$", R.drawable.flag_ca),
            new ExtendedCurrency("CHF", "Switzerland Franc","CHF", R.drawable.flag_li),
            new ExtendedCurrency("CLP", "Chile Peso","$", R.drawable.flag_cl),
            new ExtendedCurrency("CNY", "China Yuan Renminbi","¥", R.drawable.flag_cn),
            new ExtendedCurrency("COP", "Colombia Peso","$", R.drawable.flag_co),
            new ExtendedCurrency("CRC", "Costa Rica Colon","₡", R.drawable.flag_cr),
            new ExtendedCurrency("DKK", "Denmark Krone","kr", R.drawable.flag_dk),
            new ExtendedCurrency("DOP", "Dominican Republic Peso","RD$", R.drawable.flag_do),
            new ExtendedCurrency("EGP", "Egypt Pound","£", R.drawable.flag_eg),
            new ExtendedCurrency("ETB", "Ethiopian Birr","Br", R.drawable.flag_et),
            new ExtendedCurrency("GEL", "Georgian Lari","₾", R.drawable.flag_ge),
            new ExtendedCurrency("GHS", "Ghana Cedi","¢", R.drawable.flag_gh),
            new ExtendedCurrency("GMD", "Gambian dalasi","D", R.drawable.flag_gm),
            new ExtendedCurrency("GYD", "Guyana Dollar","$", R.drawable.flag_gy),
            new ExtendedCurrency("HKD", "Hong Kong Dollar","$", R.drawable.flag_hk),
            new ExtendedCurrency("HRK", "Croatia Kuna","kn", R.drawable.flag_hr),
            new ExtendedCurrency("HUF", "Hungary Forint","Ft", R.drawable.flag_hu),
            new ExtendedCurrency("IDR", "Indonesia Rupiah","Rp", R.drawable.flag_id),
            new ExtendedCurrency("ILS", "Israel Shekel","₪", R.drawable.flag_il),
            new ExtendedCurrency("INR", "Indian Rupee","₹", R.drawable.flag_in),
            new ExtendedCurrency("ISK", "Iceland Krona","kr", R.drawable.flag_is),
            new ExtendedCurrency("JMD", "Jamaica Dollar","J$", R.drawable.flag_jm),
            new ExtendedCurrency("JPY", "Japanese Yen","¥", R.drawable.flag_jp),
            new ExtendedCurrency("KES", "Kenyan Shilling","KSh", R.drawable.flag_ke),
            new ExtendedCurrency("KRW", "Korea (South) Won","₩", R.drawable.flag_kr),
            new ExtendedCurrency("KWD", "Kuwaiti Dinar","د.ك", R.drawable.flag_kw),
            new ExtendedCurrency("KYD", "Cayman Islands Dollar","$", R.drawable.flag_ky),
            new ExtendedCurrency("KZT", "Kazakhstan Tenge","лв", R.drawable.flag_kz),
            new ExtendedCurrency("LAK", "Laos Kip","₭", R.drawable.flag_la),
            new ExtendedCurrency("LKR", "Sri Lanka Rupee","₨", R.drawable.flag_lk),
            new ExtendedCurrency("LRD", "Liberia Dollar","$", R.drawable.flag_lr),
            new ExtendedCurrency("LTL", "Lithuanian Litas","Lt", R.drawable.flag_lt),
            new ExtendedCurrency("MAD", "Moroccan Dirham","MAD", R.drawable.flag_ma),
            new ExtendedCurrency("MDL", "Moldovan Leu","MDL", R.drawable.flag_md),
            new ExtendedCurrency("MKD", "Macedonia Denar","ден", R.drawable.flag_mk),
            new ExtendedCurrency("MNT", "Mongolia Tughrik","₮", R.drawable.flag_mn),
            new ExtendedCurrency("MUR", "Mauritius Rupee","₨", R.drawable.flag_mu),
            new ExtendedCurrency("MWK", "Malawian Kwacha","MK", R.drawable.flag_mw),
            new ExtendedCurrency("MXN", "Mexico Peso","$", R.drawable.flag_mx),
            new ExtendedCurrency("MYR", "Malaysia Ringgit","RM", R.drawable.flag_my),
            new ExtendedCurrency("MZN", "Mozambique Metical","MT", R.drawable.flag_mz),
            new ExtendedCurrency("NAD", "Namibia Dollar","$", R.drawable.flag_na),
            new ExtendedCurrency("NGN", "Nigeria Naira","₦", R.drawable.flag_ng),
            new ExtendedCurrency("NIO", "Nicaragua Cordoba","C$", R.drawable.flag_ni),
            new ExtendedCurrency("NOK", "Norway Krone","kr", R.drawable.flag_no),
            new ExtendedCurrency("NPR", "Nepal Rupee","₨", R.drawable.flag_np),
            new ExtendedCurrency("NZD", "New Zealand Dollar","$", R.drawable.flag_nz),
            new ExtendedCurrency("OMR", "Oman Rial","﷼", R.drawable.flag_om),
            new ExtendedCurrency("PEN", "Peru Sol","S/.", R.drawable.flag_pe),
            new ExtendedCurrency("PGK", "Papua New Guinean Kina","K", R.drawable.flag_pg),
            new ExtendedCurrency("PHP", "Philippines Peso","₱", R.drawable.flag_ph),
            new ExtendedCurrency("PKR", "Pakistan Rupee","₨", R.drawable.flag_pk),
            new ExtendedCurrency("PLN", "Poland Zloty","zł", R.drawable.flag_pl),
            new ExtendedCurrency("PYG", "Paraguay Guarani","Gs", R.drawable.flag_py),
            new ExtendedCurrency("QAR", "Qatar Riyal","﷼", R.drawable.flag_qa),
            new ExtendedCurrency("RON", "Romania Leu","lei", R.drawable.flag_ro),
            new ExtendedCurrency("RSD", "Serbia Dinar","Дин.", R.drawable.flag_rs),
            new ExtendedCurrency("RUB", "Russia Ruble","₽", R.drawable.flag_ru),
            new ExtendedCurrency("SAR", "Saudi Arabia Riyal","﷼", R.drawable.flag_sa),
            new ExtendedCurrency("SEK", "Sweden Krona","kr", R.drawable.flag_se),
            new ExtendedCurrency("SGD", "Singapore Dollar","$", R.drawable.flag_sg),
            new ExtendedCurrency("SOS", "Somalia Shilling","S", R.drawable.flag_so),
            new ExtendedCurrency("SRD", "Suriname Dollar","$", R.drawable.flag_sr),
            new ExtendedCurrency("THB", "Thailand Baht","฿", R.drawable.flag_th),
            new ExtendedCurrency("TTD", "Trinidad and Tobago Dollar","TT$", R.drawable.flag_tt),
            new ExtendedCurrency("TWD", "Taiwan New Dollar","NT$", R.drawable.flag_tw),
            new ExtendedCurrency("TZS", "Tanzanian Shilling","TSh", R.drawable.flag_tz),
            new ExtendedCurrency("UAH", "Ukraine Hryvnia","₴", R.drawable.flag_ua),
            new ExtendedCurrency("UGX", "Ugandan Shilling","USh", R.drawable.flag_ug),
            new ExtendedCurrency("UYU", "Uruguay Peso","$U", R.drawable.flag_uy),
            new ExtendedCurrency("VEF", "Venezuela Bolívar","Bs", R.drawable.flag_ve),
            new ExtendedCurrency("VND", "Viet Nam Dong","₫", R.drawable.flag_vn),
            new ExtendedCurrency("YER", "Yemen Rial","﷼", R.drawable.flag_ye),
            new ExtendedCurrency("ZAR", "South Africa Rand","R", R.drawable.flag_za)
    };
    private String code;
    private String name;
    private String symbol;
    private int flag = -1;

    public ExtendedCurrency(String code, String name, String symbol, int flag) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.flag = flag;
    }

    public ExtendedCurrency() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void loadFlagByCode(Context context) {
        if (this.flag != -1)
            return;

        try {
            this.flag = context.getResources()
                    .getIdentifier("flag_" + this.code.toLowerCase(), "drawable",
                            context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            this.flag = -1;
        }
    }

    /*
     *      GENERIC STATIC FUNCTIONS
     */

    private static List<ExtendedCurrency> allCurrenciesList;

    public static List<ExtendedCurrency> getAllCurrencies() {
        if (allCurrenciesList == null) {
            allCurrenciesList = Arrays.asList(CURRENCIES);
        }
        return allCurrenciesList;
    }

    public static ExtendedCurrency getCurrencyByISO(String currencyIsoCode) {
        // Because the data we have is sorted by ISO codes and not by names, we must check all
        // currencies one by one

        for (ExtendedCurrency c : CURRENCIES) {
            if (currencyIsoCode.equals(c.getCode())) {
                return c;
            }
        }
        return null;
    }

    public static ExtendedCurrency getCurrencyByName(String currencyName) {
        // Because the data we have is sorted by ISO codes and not by names, we must check all
        // currencies one by one

        for (ExtendedCurrency c : CURRENCIES) {
            if (currencyName.equals(c.getName())) {
                return c;
            }
        }
        return null;
    }

    /*
     * COMPARATORS
     */

    public static class ISOCodeComparator implements Comparator<ExtendedCurrency> {
        @Override
        public int compare(ExtendedCurrency currency, ExtendedCurrency t1) {
            return currency.code.compareTo(t1.code);
        }
    }


    public static class NameComparator implements Comparator<ExtendedCurrency> {
        @Override
        public int compare(ExtendedCurrency currency, ExtendedCurrency t1) {
            return currency.name.compareTo(t1.name);
        }
    }
}
