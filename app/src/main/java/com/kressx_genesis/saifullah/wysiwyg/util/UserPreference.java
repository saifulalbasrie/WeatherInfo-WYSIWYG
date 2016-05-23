package com.kressx_genesis.saifullah.wysiwyg.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kressx_genesis.saifullah.wysiwyg.R;
import com.kressx_genesis.saifullah.wysiwyg.model.WeatherResult;

/**
 * Created by Saifullah on 18/05/2016.
 */
public class UserPreference {

    private SharedPreferences prefs;

    public UserPreference(Context context){
        prefs = context.getSharedPreferences("WeatherWYSIWYG", Activity.MODE_PRIVATE);
    }

    public String getCurrentCity(){
        return prefs.getString("city", "Jakarta, ID");
    }

    public void setCurrentCity(String city){
        prefs.edit().putString("city", city).commit();
    }

    public String getDegrees(){
        return prefs.getString("degrees", "C");
    }

    public void setDegrees(String dg){
        prefs.edit().putString("degrees", dg).commit();
    }

    public boolean isAutoRefresh(){
        return prefs.getBoolean("auto_refresh", true);
    }

    public void setIsAutoRefresh(boolean auto){
        prefs.edit().putBoolean("auto_refresh", auto).commit();
    }

    public int getRefreshInterval(){
        return prefs.getInt("interval", 10); //10 minutes
    }

    public void setCurrentCity(int interval){
        prefs.edit().putInt("interval", interval).commit();
    }

    public WeatherResult getCurrentWeather(String city){
        String sJosn = prefs.getString("current_weather", "");
        Gson gson = new Gson();
        WeatherResult obj = gson.fromJson(sJosn, WeatherResult.class);
        if(obj!=null && obj.getCity()!=null && obj.getCity().equalsIgnoreCase(city))
            return obj;

        return null;
    }

    public void setCurrentWeather(WeatherResult obj){
        Gson gson = new Gson();
        String sJson = gson.toJson(obj);
        prefs.edit().putString("current_weather", sJson).commit();
    }
}
