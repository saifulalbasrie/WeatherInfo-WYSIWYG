package com.kressx_genesis.saifullah.wysiwyg.presenter;

/**
 * Created by Saifullah on 21/05/2016.
 */
public interface IWeatherPresenter {
    void onCreate();

    void onDestroy();

    void getCurrentWeather(String city);

}
