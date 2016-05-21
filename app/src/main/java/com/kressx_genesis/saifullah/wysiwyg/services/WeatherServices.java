package com.kressx_genesis.saifullah.wysiwyg.services;

import android.content.Context;

import com.kressx_genesis.saifullah.wysiwyg.model.gen.WeatherData;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saifullah on 18/05/2016.
 */
public class WeatherServices {

    public static final String API_HOST = "http://api.openweathermap.org";
    public static final String API_KEY = "3e418f0e1f5c32ec0b6546f60c61ee95";

    public static final String WEATHER_IMG_URL = "http://openweathermap.org/img/w/";

    private IWeatherService service;
    private Context context;
    public WeatherServices(Context context)
    {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IWeatherService.class);
    }

    public Call<WeatherData> getCurrentWeather(String cityName){
        return service.getCurrentWeather(API_KEY, cityName);
    }

}
