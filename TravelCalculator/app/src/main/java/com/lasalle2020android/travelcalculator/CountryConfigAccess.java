package com.lasalle2020android.travelcalculator;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import Model.CountryModel;

public class CountryConfigAccess {

    private Context context;
    static String COUNTRY_CONF = "travelcalc_country.config";
    private static String[] _files = new String[1];

    public static MyCountryConfig config = new MyCountryConfig();

    public enum ConfigName
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

    public static class MyCountryConfig
    {
        List<CountryModel> countryList;
    }

    public void initConfigData() {
        _files[0] = ConfigName.COUNTRY_INFO_CONFIG.toString();

        config.countryList = new ArrayList<>();
        ReadSiteConfig(ConfigName.COUNTRY_INFO_CONFIG);
    }

    private void SaveSiteConfig(ConfigName confName)
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

    public void ReadSiteConfig(ConfigName confName)
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

    private void ReadSiteProcess(ConfigName confName, BufferedReader fis) {
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
                        else if (Data[0].compareTo("DisplayName") == 0 && Data.length > 1) {
                            country.setDisplayName(Data[1]);
                        }
                        else if (Data[0].compareTo("CurrencyCode") == 0 && Data.length > 1) {
                            country.setCurrencyCode(Data[1]);
                        }
                        else if (Data[0].compareTo("Currency") == 0 && Data.length > 1) {
                            country.setCurrency(Float.valueOf(Data[1]));
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

    private void WriteSiteProcess(ConfigName confName, BufferedWriter fos)
    {
        try {
            switch(confName)
            {
                case COUNTRY_INFO_CONFIG:

                    for (int i = 0; i < config.countryList.size() ; i++) {
                        CountryModel country = config.countryList.get(i);
                        fos.write("Id=" + country.getId() + '\n');
                        fos.write("DisplayName=" + country.getDisplayName() + '\n');
                        fos.write("CurrencyCode=" + country.getCurrencyCode() + '\n');
                        fos.write("Currency=" + country.getCurrency() + '\n');
                    }

                    _files[0] = ConfigName.COUNTRY_INFO_CONFIG.toString();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
