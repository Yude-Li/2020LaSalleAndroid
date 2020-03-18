package com.lasalle2020android.travelcalculator;

/*
To get new currency by http or api
API Key : c38d9184caab9be299cde8caedb13181
Get all currency: http://data.fixer.io/api/latest?access_key=c38d9184caab9be299cde8caedb13181
https://fixer.io/documentation#ssl

Convert currency:
API Key: a307308d10611c002ab8
Example usage (Convert currency):
https://free.currconv.com/api/v7/convert?q=USD_PHP&compact=ultra&apiKey=a307308d10611c002ab8

Get all currency: https://free.currconv.com/api/v7/currencies?apiKey=a307308d10611c002ab8
https://www.currencyconverterapi.com/docs
*/

import android.content.Context;

public class HttpServiceThread extends Thread {

    Context myContext;

    public enum ActionMode{
        UPDATE_CURRENCY,
        CONVERT_CURRENCY
    }

    public void HttpSetContext(Context context)
    {
        myContext = context;
    }

    public void setConversionCurrency(String newCurrency) {

    }

    public void setConversionCurrency(String currCurrency, String newCurrency) {

    }

    public void run()
    {

    }
}
