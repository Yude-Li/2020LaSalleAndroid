package threads;
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
import Model.ApiResponseModel;
import callbacks.ServerResponseNotifier;
import commonutilities.Constants;

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

    Constants.ActionMode actionMode;

    private static List<ApiResponseModel> rateList = new ArrayList<>();



//    public HttpServiceThread(MyApplication componentInfo, Context ctx, ServerResponseNotifier mHandler, String body, int requestNo) {
//        this.componentInfo = componentInfo;
//
//        this.ctx = ctx;
//        //this.mHandler = mHandler;
//        this.serverResponseNotifier = mHandler;
//        this.bodyData = body;
//
//        this.requestNo = requestNo;
//    }

    public HttpServiceThread(Context ctx, MyApplication componentInfo, Constants.ActionMode actionMode,String body,ServerResponseNotifier mHandler) {
        this.ctx = ctx;
        this.componentInfo = componentInfo;
        this.actionMode = actionMode;
        this.bodyData = body;
        this.serverResponseNotifier = mHandler;
    }

    public HttpServiceThread(Context context, Constants.ActionMode actionMode) {
        this.ctx = ctx;
        this.actionMode = actionMode;
    }



    public void setConversionCurrencyBase(String newCurrency) {
        // Update currency base on the original country
        SettingConfigAccess settingConfigAccess = new SettingConfigAccess(this.ctx);
        CountryConfigAccess countryConfigAccess = new CountryConfigAccess(this.ctx);

        this.originalCurrencyName = countryConfigAccess.getCountryById(settingConfigAccess.getOriginalCountryId()).getCurrencyCode();
    }

    public void setConversionCurrency(String currCurrency, String newCurrency) {
        this.travelCurrencyName = newCurrency;
        this.originalCurrencyName = currCurrency;
    }

    private void postMessage(ApiResponseModel webResponse, int requestNo) {
        if (componentInfo.isActivityRunning) {
//            Message msg = new Message();
//            msg.obj = webResponse;
//            msg.arg1 = requestNo;
//            mHandler.sendMessage(msg);
            // serverResponse.onServerResponse(message, requestNo);

            serverResponseNotifier.onServerResponseRecieved(webResponse, 200, true);
        }
    }


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
                    URL url = new URL(" https://api.exchangeratesapi.io/latest?base=" + this.originalCurrencyName + "&symbols=" + this.bodyData);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(60000);
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream input = connection.getInputStream();
                        String response = streamToString(input);
                        Log.d("GetCurrency", response);
                        JSONObject object = new JSONObject(response);
                        String[] country= bodyData.split("\\,");
                        ApiResponseModel model = new ApiResponseModel(object.getString("base"), object.getJSONObject("rates").getDouble(country[0]),
                                object.getJSONObject("rates").getDouble(country[1]));


                        postMessage(model, responseCode);
                    }
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
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
            List<ApiResponseModel> rateList = new ArrayList<>();
            while(keysItr.hasNext()) {
                String key = keysItr.next();
                Double value = Double.valueOf(jsonRate.get(key).toString());
                ApiResponseModel apiCurrencyModel = new ApiResponseModel(key, value);
                this.rateList.add(apiCurrencyModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseConvertCurrencyJson(String jsonString) {
        // {"rates":{"GBP":0.809054937},"base":"USD","date":"2020-04-22"}
        try {
            JSONObject jsonGet = new JSONObject(jsonString);
            String rates = jsonGet.getString("rates");
            String base = jsonGet.getString("base");

            JSONObject jsonRate = new JSONObject(rates);
            Iterator<String> keysItr = jsonRate.keys();

            String currency = keysItr.next();
            Double value = Double.valueOf(jsonRate.get(currency).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public List<ApiResponseModel> GetAllCurrencyList() {
        return this.rateList;
    }
}



