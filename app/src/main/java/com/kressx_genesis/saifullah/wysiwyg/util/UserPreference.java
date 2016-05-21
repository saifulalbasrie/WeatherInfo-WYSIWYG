package com.kressx_genesis.saifullah.wysiwyg.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.kressx_genesis.saifullah.wysiwyg.R;

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
}
