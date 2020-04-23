package threads;

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



/*
The api provided by Eurpean Central Bank

GET https://api.exchangeratesapi.io/latest?symbols=USD,GBP



 */


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lasalle2020android.travelcalculator.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import DataConfig.CountryConfigAccess;
import DataConfig.SettingConfigAccess;
import callbacks.ServerResponseNotifier;

public class HttpServiceThread extends Thread {

    private Context ctx;
    private MyApplication componentInfo;
    private ServerResponseNotifier serverResponseNotifier;

    private HttpURLConnection connection;
    int requestNo;
    private Handler mHandler;
    private boolean interupt = false;
    private String  bodyData = "", apiName = "", baseUrl = "https://api.exchangeratesapi.io/latest?symbols=";

    private String travelCurrencyName = "";
    private String originalCurrencyName = "";

    ActionMode actionMode;
    public enum ActionMode {
        UPDATE_CURRENCY,
        CONVERT_CURRENCY
    }

    public HttpServiceThread(MyApplication componentInfo, Context ctx, ServerResponseNotifier mHandler, String body, int requestNo) {
        this.componentInfo = componentInfo;

        this.ctx = ctx;
        //this.mHandler = mHandler;
        this.serverResponseNotifier = mHandler;
        this.bodyData = body;

        this.requestNo = requestNo;
    }

    public HttpServiceThread(Context ctx, MyApplication componentInfo, ActionMode actionMode) {
        this.ctx = ctx;
        this.componentInfo = componentInfo;
        this.actionMode = actionMode;
    }

    public void setConversionCurrencyBase(String newCurrency) {
        // Update currency base on the original country
        SettingConfigAccess settingConfigAccess = new SettingConfigAccess(this.ctx);
        CountryConfigAccess countryConfigAccess = new CountryConfigAccess(this.ctx);

        this.originalCurrencyName = countryConfigAccess.getCountryById(settingConfigAccess.getOriginalCountryId()).getCurrencyName();
    }

    public void setConversionCurrency(String currCurrency, String newCurrency) {
        this.travelCurrencyName = newCurrency;
        this.originalCurrencyName = currCurrency;
    }

    private void postMessage(String webResponse, int requestNo) {
        if (componentInfo.isActivityRunning) {
//            Message msg = new Message();
//            msg.obj = webResponse;
//            msg.arg1 = requestNo;
//            mHandler.sendMessage(msg);
            // serverResponse.onServerResponse(message, requestNo);
            serverResponseNotifier.onServerResponseRecieved(null, 200, false);
        }
    }

//    @Override
//    public void run() {
//        if (interupt == false) {
//
//            String URL = "";
//
//            if (!interupt) {
//
//                try {
//
//                    Log.e("", "=========== HTTP REQUEST===========");
//                    Log.e("", "Request Name  - " + apiName);
//                    Log.e("", "Request Data  - " + bodyData);
//                    Log.e("", "====================================");
//
//
//                    java.net.URL url = new URL(baseUrl + bodyData);
//
//                    try {
//                        connection = (HttpURLConnection) url.openConnection();
//                    } catch (IOException e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//                    connection.setConnectTimeout(60000);
//
//                    connection.connect();
//                    InputStream inputStream = null;
//                    inputStream = connection.getInputStream();
//                    //readStream(inputStream);
//
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                    String str;
//                    StringBuilder stringBuilder = new StringBuilder();
//
//                    while ((str = reader.readLine()) != null) {
//                        stringBuilder.append(str);
//                        webResponse = stringBuilder.toString();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Log.i("http end-" + requestNo, "Resquest Json --> " + bodyData);
//                // Log.e("http end-" + requestNo, "Response Json  - image -> " + webResponse);
//
//                Log.i("", "=========== HTTP REQUEST===========");
//                Log.i("", "Request Name  -   " + apiName);
//                Log.i("", "Request Data  -   " + bodyData);
//                Log.i("", "Response Data -   " + webResponse);
//                Log.i("", "====================================");
//                // Log.e("webResponse=========",webResponse);
//
//                connection.disconnect();
//                connection = null;
//
//
//            }
//
//            Log.i("" + requestNo, webResponse);
//            postMessage(webResponse, requestNo);
//        }
//    }

    public void run()
    {
        switch (actionMode) {
            case UPDATE_CURRENCY: {
                try {
                    URL url = new URL("https://api.exchangeratesapi.io/latest?base=" + this.originalCurrencyName);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(60000);
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream input = connection.getInputStream();
                        String response = streamToString(input);
                        Log.d("GetCurrency", response);
                        parseAllCurrencyJson(response);
                    }
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            break;
            case CONVERT_CURRENCY: {
                try {
                    //https://api.exchangeratesapi.io/latest?base=USD&symbols=GBP
                    URL url = new URL(" https://api.exchangeratesapi.io/latest?base=" + this.originalCurrencyName + "&symbols=" + this.travelCurrencyName);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(60000);
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream input = connection.getInputStream();
                        String response = streamToString(input);
                        Log.d("GetCurrency", response);
                        postMessage(response, responseCode);
                    }
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            break;
        }
    }


    public void parseAllCurrencyJson(String jsonString) {
        try {
            JSONObject jsonGet = new JSONObject(jsonString);
            String rates = jsonGet.getString("rates");
            JSONObject jsonRate = new JSONObject(rates);
            Iterator<String> keysItr = jsonRate.keys();
            List<ApiCurrencyModel> rateList = new ArrayList<>();
            while(keysItr.hasNext()) {
                String key = keysItr.next();
                Double value = Double.valueOf(jsonRate.get(key).toString());
                ApiCurrencyModel apiCurrencyModel = new ApiCurrencyModel(key, value);
                rateList.add(apiCurrencyModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseConvertCurrencyJson(String jsonString) {

    }

    public static String streamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}

class ApiCurrencyModel {
    private String currencyCode;
    private double currencyRate;

    public ApiCurrencyModel(String currencyCode, double currencyRate) {
        this.currencyCode = currencyCode;
        this.currencyRate = currencyRate;
    }
}

