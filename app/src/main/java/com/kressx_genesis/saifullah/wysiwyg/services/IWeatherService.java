package com.kressx_genesis.saifullah.wysiwyg.services;

import com.kressx_genesis.saifullah.wysiwyg.model.gen.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Saifullah on 19/05/2016.
 */
public interface IWeatherService {

    @GET("/data/2.5/weather")
    Call<WeatherData> getCurrentWeather(@Query("appid") String api_key, @Query("q") String query);

}
