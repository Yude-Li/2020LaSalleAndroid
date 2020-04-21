package threads;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkCheckStatus extends Thread {

    Context myContext;
    CheckType checkType;

    static boolean isWiFi = false;
    static boolean isConnected = false;

    public enum CheckType{
        CHECK_CONNECTION_AVAILABLE,
        CHECK_CONNECTIONTYPE_WIFI,
        CHECK_ALL
    }

    public void setContext(Context context)
    {
        myContext = context;
    }

    public void setCheckType(CheckType checkType)
    {
        this.checkType = checkType;
    }

    public void run()
    {
        switch (checkType) {
            case CHECK_CONNECTION_AVAILABLE: {
                ConnectivityManager cm = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                isConnected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            }
                break;
            case CHECK_CONNECTIONTYPE_WIFI: {
                ConnectivityManager cm = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                isWiFi = nInfo.getType() == ConnectivityManager.TYPE_WIFI;
            }
                break;
            case CHECK_ALL: {
                ConnectivityManager cm = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                isConnected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                isWiFi = nInfo.getType() == ConnectivityManager.TYPE_WIFI;
            }
            break;
        }
    }

    public boolean IsConnectionAvailable() {
        return isConnected;
    }

    public boolean IsConnectionWifi() {
        return isWiFi;
    }

}
