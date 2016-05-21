package com.kressx_genesis.saifullah.wysiwyg.presenter;

import android.content.Context;

import com.kressx_genesis.saifullah.wysiwyg.model.WeatherResult;
import com.kressx_genesis.saifullah.wysiwyg.model.gen.WeatherData;
import com.kressx_genesis.saifullah.wysiwyg.services.WeatherServices;
import com.kressx_genesis.saifullah.wysiwyg.util.UserPreference;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saifullah on 21/05/2016.
 */
public class WeatherPresenterImpl implements IWeatherPresenter {

    private IWeatherView weatherView;
    private Context context;

    private WeatherServices weatherServices;
    private UserPreference userPreference;

    public WeatherPresenterImpl(Context context, IWeatherView weatherView){
        this.context = context;
        this.weatherView = weatherView;

        userPreference = new UserPreference(context);
        weatherServices = new WeatherServices(context);
    }

    @Override
    public void onCreate() {
        getCurrentWeather(null);
        //TODO start worker
        //if(userPreference.isAutoRefresh())
        //    startWorker();
    }

    @Override
    public void onDestroy() {
        //TODO stop worker
        //stopWorker();
    }

    @Override
    public void getCurrentWeather(String city){
        if(city==null || city.length()==0)
            city = userPreference.getCurrentCity();
        else
            userPreference.setCurrentCity(city);

        weatherView.stopLoading();
        weatherView.showLoading();
        Call<WeatherData> call = weatherServices.getCurrentWeather(city);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String city = response.body().getName();
                        String country = response.body().getSys().country;
                        String status = response.body().getWeather().get(0).description;
                        double temp = response.body().getMain().temp;
                        double tempMin = response.body().getMain().tempMin;
                        double tempMax = response.body().getMain().tempMax;

                        String humidity = response.body().getMain().humidity.toString();
                        String pressure = response.body().getMain().pressure.toString();

                        String sTemp;
                        String sTempMin;
                        String sTempMax;
                        DecimalFormat format = new DecimalFormat("#.0");
                        if (userPreference.getDegrees().equalsIgnoreCase("F")) {
                            temp = (temp * 9 / 5) - 459.67;
                            tempMin = (tempMin * 9 / 5) - 459.67;
                            tempMax = (tempMax * 9 / 5) - 459.67;
                            sTemp = format.format(temp) + "\u2109";
                            sTempMin = format.format(tempMin) + "\u2109";
                            sTempMax = format.format(tempMax) + "\u2109";
                        } else {
                            temp = temp - 273.15;
                            tempMin = tempMin - 273.15;
                            tempMax = tempMax - 273.15;
                            sTemp = format.format(temp) + "\u2103";
                            sTempMin = format.format(tempMin) + "\u2103";
                            sTempMax = format.format(tempMax) + "\u2103";
                        }

                        long dt = response.body().getDt() * 1000L;
                        String sLastUpdt = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date(dt));
                        String imgUrl = WeatherServices.WEATHER_IMG_URL + response.body().getWeather().get(0).icon +".png";

                        WeatherResult result = new WeatherResult();
                        result.setCity(city);
                        result.setCountry(country);
                        result.setStatus(status);
                        result.setTemp(sTemp);
                        result.setTempMin(sTempMin);
                        result.setTempMax(sTempMax);
                        result.setHumidity(humidity + " %");
                        result.setPressure(pressure + " hpa");
                        result.setLastUpdate("Last update: " + sLastUpdt);
                        result.setImgUrl(imgUrl);

                        onWeatherUpdatedSuccess(result);

                    } else if (response.errorBody() != null) {
                        onWeatherUpdatedFailed(response.errorBody().string());
                    } else
                    {
                        onWeatherUpdatedFailed("Request Failed!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    onWeatherUpdatedFailed(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                onWeatherUpdatedFailed(t.getMessage());
            }

        });

    }

    private void onWeatherUpdatedSuccess(final WeatherResult data)
    {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                weatherView.stopLoading();
                weatherView.onWeatherUpdateSuccess(data);
            }
        };
        mainHandler.post(myRunnable);
    }

    private void onWeatherUpdatedFailed(final String msg)
    {
        android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                weatherView.stopLoading();
                weatherView.onWeatherUpdateFailed(msg);
            }
        };
        mainHandler.post(myRunnable);
    }
}
