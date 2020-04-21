package DataConfig;

import android.content.Context;
import android.content.Intent;
import android.text.BoringLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import Model.CountryModel;
import commonutilities.Constants;

public class SettingConfigAccess {

    private Context context;
    static String SETTING_CONF = "setting.config";
    private static String[] _files = new String[1];

    public static SettingConfig config = new SettingConfig();

    private enum ConfigName
    {
        SETTING_CONFIG("setting.config");

        @SuppressWarnings("unused")
        private final String name;

        ConfigName(String s) {
            name = s;
        }
    }

    public SettingConfigAccess(Context currContext)
    {
        context = currContext;
    }

    private static class SettingConfig
    {
        boolean isFirstTime;
        boolean updateWiFiOnly;
        int oriCountryId;
//        CountryModel originalCountry;
    }

    public void initConfigData() {
        _files[0] = Constants.ConfigName.SETTING_CONFIG.toString();

        config.isFirstTime = true;
        config.updateWiFiOnly = true;
        config.oriCountryId = -1;
//        config.originalCountry = new CountryModel();

        ReadSiteConfig( Constants.ConfigName.SETTING_CONFIG);
    }

    public void setIsFirstTime(Boolean isFirstTime) {
        config.isFirstTime = isFirstTime;
        SaveSiteConfig(Constants.ConfigName.SETTING_CONFIG);
    }

    public void setUpdateWiFiOnly(Boolean updateWiFiOnly) {
        config.updateWiFiOnly = updateWiFiOnly;
        SaveSiteConfig(Constants.ConfigName.SETTING_CONFIG);
    }

    public void setOriginalCountryId(int Id) {
        config.oriCountryId = Id;
        SaveSiteConfig(Constants.ConfigName.SETTING_CONFIG);
    }

    public Boolean getIsFirstTime() {
        return config.isFirstTime;
    }

    public Boolean getUpdateWiFiOnly() {
        return config.updateWiFiOnly;
    }

    public int getOriginalCountryId() {
        return config.oriCountryId;
    }

    private void SaveSiteConfig(Constants.ConfigName confName)
    {
        try
        {
            BufferedWriter fos = new BufferedWriter (new OutputStreamWriter(context.openFileOutput(confName.toString(), Context.MODE_PRIVATE)));
            WriteSiteProcess(confName, fos);
            fos.flush();
            fos.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public void ReadSiteConfig(Constants.ConfigName confName)
    {
        try
        {
            BufferedReader fis = new BufferedReader (new InputStreamReader(context.openFileInput(confName.toString())));
            ReadSiteProcess(confName, fis);
            fis.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private void ReadSiteProcess(Constants.ConfigName confName, BufferedReader fis) {
        switch(confName)
        {
            case SETTING_CONFIG:
                try {
                    boolean KeepReading = true;
                    do
                    {
                        String line = fis.readLine();

                        if(line == null)
                            break;

                        String[] Data;
                        Data = line.split("=");

                        if(Data[0].compareTo("IsFirstTime") == 0 && Data.length > 1) {
                            config.isFirstTime = Boolean.valueOf(Data[1]);
                        }
                        else if (Data[0].compareTo("UpdateWiFiOnly") == 0 && Data.length > 1) {
                            config.updateWiFiOnly = Boolean.valueOf(Data[1]);
                        }
                        else if (Data[0].compareTo("OriginalCountryId") == 0 && Data.length > 1) {
                            config.oriCountryId = Integer.valueOf(Data[1]);
                        }
                    }while(KeepReading);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void WriteSiteProcess(Constants.ConfigName confName, BufferedWriter fos)
    {
        try {
            switch(confName)
            {
                case SETTING_CONFIG:
                    fos.write("IsFirstTime=" + config.isFirstTime + '\n');
                    fos.write("UpdateWiFiOnly=" + config.updateWiFiOnly + '\n');
                    fos.write("OriginalCountryId=" + config.oriCountryId + '\n');
                    _files[0] = Constants.ConfigName.SETTING_CONFIG.toString();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
