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

import com.lasalle2020android.travelcalculator.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import callbacks.ServerResponseNotifier;

public class HttpServiceThread extends Thread {

    private Context ctx;
    private MyApplication componentInfo;
    private ServerResponseNotifier serverResponseNotifier;

    private HttpURLConnection connection;
    int requestNo;
    private Handler mHandler;
    private boolean interupt = false;
    private String webResponse = "", bodyData = "", apiName = "", baseUrl = "https://api.exchangeratesapi.io/latest?symbols=";

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

    public void setConversionCurrency(String newCurrency) {

    }

    public void setConversionCurrency(String currCurrency, String newCurrency) {

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

    @Override
    public void run() {
        if (interupt == false) {

            String URL = "";

            if (!interupt) {

                try {

                    Log.e("", "=========== HTTP REQUEST===========");
                    Log.e("", "Request Name  - " + apiName);
                    Log.e("", "Request Data  - " + bodyData);
                    Log.e("", "====================================");


                    java.net.URL url = new URL(baseUrl + bodyData);

                    try {
                        connection = (HttpURLConnection) url.openConnection();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    connection.setConnectTimeout(60000);

                    connection.connect();
                    InputStream inputStream = null;
                    inputStream = connection.getInputStream();
                    //readStream(inputStream);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String str;
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((str = reader.readLine()) != null) {
                        stringBuilder.append(str);
                        webResponse = stringBuilder.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("http end-" + requestNo, "Resquest Json --> " + bodyData);
                // Log.e("http end-" + requestNo, "Response Json  - image -> " + webResponse);

                Log.i("", "=========== HTTP REQUEST===========");
                Log.i("", "Request Name  -   " + apiName);
                Log.i("", "Request Data  -   " + bodyData);
                Log.i("", "Response Data -   " + webResponse);
                Log.i("", "====================================");
                // Log.e("webResponse=========",webResponse);

                connection.disconnect();
                connection = null;


            }

            Log.i("" + requestNo, webResponse);
            postMessage(webResponse, requestNo);
        }
    }
}

