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

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import commonutilities.ComponentInfo;

public class HttpServiceThread extends Thread {

    private Context ctx;
    private ComponentInfo componentInfo;

    private HttpURLConnection connection;
    int requestNo;
    private Handler mHandler;
    private boolean interupt = false;
    private String webResponse = "", bodyData = "", apiName = "", baseUrl = "";

    public enum ActionMode {
        UPDATE_CURRENCY,
        CONVERT_CURRENCY
    }

    public HttpServiceThread(ComponentInfo componentInfo, Context ctx, Handler mHandler, String body, int requestNo) {
        this.componentInfo = componentInfo;

        this.ctx = ctx;
        this.mHandler = mHandler;

        this.requestNo = requestNo;
    }

    public void setConversionCurrency(String newCurrency) {

    }

    public void setConversionCurrency(String currCurrency, String newCurrency) {

    }

    private void postMessage(String webResponse, int requestNo) {
        if (componentInfo.isActivityRunning) {
            Message msg = new Message();
            msg.obj = webResponse;
            msg.arg1 = requestNo;
            mHandler.sendMessage(msg);
            // serverResponse.onServerResponse(message, requestNo);
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

                    Thread.sleep(2000);
                    System.setProperty("http.keepAlive", "false");
                    java.net.URL url = new URL(baseUrl + apiName);

                    try {
                        connection = (HttpURLConnection) url.openConnection();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    connection.setDoOutput(true);          //    setDoOutput(true) is used with POST to allow sending a body via the connection
                    connection.setDoInput(true);           //    doInput flag to true indicates that the application intends to read data from the URL  Coonection,,,setDoOutput(true) is used for POST and PUT requests. If it is false then it is for using GET requests
                    connection.setUseCaches(false);        //   the connection is allowed to use whatever caches it can. If false, caches are to be ignored
                    connection.setConnectTimeout(60000);
                    connection.setRequestMethod("POST");

                    //   if ( Build.VERSION.SDK_INT > 13) {
                    //   connection.setRequestProperty("Connection", "close"); }

                    connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    // connection.setRequestProperty("auth_token", Token);
                    // conn.setRequestProperty("Content-Type",
                    // "application/x-www-form-urlencoded;charset=UTF-8");
                    OutputStream out = null;
                    out = connection.getOutputStream();
                    String body = bodyData;
                    out.write(body.getBytes());
                    out.close();
                    connection.connect();
                    InputStream inputStream = null;
                    inputStream = connection.getInputStream();
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

