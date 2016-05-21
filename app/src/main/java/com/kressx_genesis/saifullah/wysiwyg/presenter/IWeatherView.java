package com.kressx_genesis.saifullah.wysiwyg.presenter;

import com.kressx_genesis.saifullah.wysiwyg.model.WeatherResult;

/**
 * Created by Saifullah on 21/05/2016.
 */
public interface IWeatherView {

    void showLoading();

    void stopLoading();

    void onWeatherUpdateSuccess(WeatherResult data);

    void onWeatherUpdateFailed(String msg);
}
