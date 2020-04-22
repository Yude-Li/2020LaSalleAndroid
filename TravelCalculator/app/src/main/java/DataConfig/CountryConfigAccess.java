package DataConfig;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



import Model.CountryModel;
import commonutilities.Constants;

public class CountryConfigAccess {

    private Context context;
    static String COUNTRY_CONF = "travelcalc_country.config";
    private static String[] _files = new String[1];

    public static MyCountryConfig config = new MyCountryConfig();

    private enum ConfigName
    {
        COUNTRY_INFO_CONFIG("countryinfo.config");

        @SuppressWarnings("unused")
        private final String name;

        ConfigName(String s) {
            name = s;
        }
    }

    public CountryConfigAccess(Context currContext)
    {
        context = currContext;
    }

    private static class MyCountryConfig
    {
        List<CountryModel> countryList;
    }

    public void initConfigData(Boolean isFirstTime) {
        _files[0] = Constants.ConfigName.COUNTRY_INFO_CONFIG.toString();

        config.countryList = new ArrayList<>();
        ReadSiteConfig(Constants.ConfigName.COUNTRY_INFO_CONFIG);

        if (isFirstTime) {
            readJsontoList();
            SaveSiteConfig(Constants.ConfigName.COUNTRY_INFO_CONFIG);
        }
    }

    public void setCurrencyUpdate(List<CountryModel> updateList) {
        config.countryList = updateList;
        SaveSiteConfig(Constants.ConfigName.COUNTRY_INFO_CONFIG);
    }

    public List<CountryModel> getCountriesList() {
        return config.countryList;
    }

    public CountryModel getCountryById(int id) {

        return config.countryList.get(id-1);
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
            case COUNTRY_INFO_CONFIG:
                try {
                    boolean KeepReading = true;
                    if (config.countryList != null)
                        config.countryList.clear();
                    else {
                        config.countryList = new ArrayList<>();
                    }
                    CountryModel country = null;
                    do
                    {
                        String line = fis.readLine();

                        if(line == null)
                            break;

                        String[] Data;
                        Data = line.split("=");

                        if(Data[0].compareTo("Id") == 0 && Data.length > 1) {
                            country = new CountryModel();
                            country.setId(Integer.valueOf(Data[1]));
                        }
                        else if (Data[0].compareTo("CountryNum") == 0 && Data.length > 1) {
                            country.setCountryNum(Integer.valueOf(Data[1]));
                        }
                        else if (Data[0].compareTo("CountryCode") == 0 && Data.length > 1) {
                            country.setCurrencyCode(Data[1]);
                        }
                        else if (Data[0].compareTo("DisplayName") == 0 && Data.length > 1) {
                            country.setDisplayName(Data[1]);
                        }
                        else if (Data[0].compareTo("CurrencyCode") == 0 && Data.length > 1) {
                            country.setCurrencyCode(Data[1]);
                        }
                        else if (Data[0].compareTo("Currency") == 0 && Data.length > 1) {
                            country.setCurrency(Float.valueOf(Data[1]));
                        }
                        else if (Data[0].compareTo("CurrencyName") == 0 && Data.length > 1) {
                            country.setCurrencyName(Data[1]);
                        }
                        else if (Data[0].compareTo("CurrencySymbol") == 0 && Data.length > 1) {
                            country.setCurrencySymbol(Data[1]);
                        }
                        else if (Data[0].compareTo("FlagId") == 0 && Data.length > 1) {
                            country.setFlagId(Data[1]);
                            config.countryList.add(country);
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
                case COUNTRY_INFO_CONFIG:

                    for (int i = 0; i < config.countryList.size() ; i++) {
                        CountryModel country = config.countryList.get(i);
                        fos.write("Id=" + country.getId() + '\n');
                        fos.write("CountryNum=" + country.getCountryNum() + '\n');
                        fos.write("CountryCode=" + country.getCurrencyCode() + '\n');
                        fos.write("DisplayName=" + country.getDisplayName() + '\n');
                        fos.write("CurrencyCode=" + country.getCurrencyCode() + '\n');
                        fos.write("Currency=" + country.getCurrency() + '\n');
                        fos.write("CurrencyName=" + country.getCurrencyName() + '\n');
                        fos.write("CurrencySymbol=" + country.getCurrencySymbol() + '\n');
                        fos.write("FlagId=" + country.getFlagId(context) + '\n');
                    }

                    _files[0] = Constants.ConfigName.COUNTRY_INFO_CONFIG.toString();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("Countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void readJsontoList(){
        try {
            JSONObject objFile = new JSONObject(loadJSONFromAsset());
            JSONArray objAry = objFile.getJSONArray("Countries");
            config.countryList = new Gson().fromJson(objAry.toString(), new TypeToken<List<CountryModel>>(){}.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
