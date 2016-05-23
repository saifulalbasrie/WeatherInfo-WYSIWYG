package com.kressx_genesis.saifullah.wysiwyg.presenter;

import android.content.Context;

import com.kressx_genesis.saifullah.wysiwyg.model.WeatherResult;
import com.kressx_genesis.saifullah.wysiwyg.model.gen.WeatherData;
import com.kressx_genesis.saifullah.wysiwyg.services.WeatherServices;
import com.kressx_genesis.saifullah.wysiwyg.util.UserPreference;
import com.kressx_genesis.saifullah.wysiwyg.util.Utility;

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
    public void onSettingChanged() {
        WeatherResult result = userPreference.getCurrentWeather(userPreference.getCurrentCity());
        if(result!=null)
        {
            double temp = result.getTempKelvin();
            double tempMin = result.getTempMinKelvin();
            double tempMax = result.getTempMaxKelvin();
            String sTemp;
            String sTempMin;
            String sTempMax;
            DecimalFormat format = new DecimalFormat("#.0");
            if (userPreference.getDegrees().equalsIgnoreCase("F")) {
                temp = Utility.getFarenheitValue(temp);
                tempMin = Utility.getFarenheitValue(tempMin);
                tempMax = Utility.getFarenheitValue(tempMax);
                sTemp = format.format(temp) + "\u2109";
                sTempMin = format.format(tempMin) + "\u2109";
                sTempMax = format.format(tempMax) + "\u2109";
            } else {
                temp = Utility.getCelciusValue(temp);
                tempMin = Utility.getCelciusValue(tempMin);
                tempMax = Utility.getCelciusValue(tempMax);
                sTemp = format.format(temp) + "\u2103";
                sTempMin = format.format(tempMin) + "\u2103";
                sTempMax = format.format(tempMax) + "\u2103";
            }

            result.setTemp(sTemp);
            result.setTempMin(sTempMin);
            result.setTempMax(sTempMax);
            onWeatherUpdatedSuccess(result);
        }
        else
        {
            getCurrentWeather(null);
        }

        //TODO start or stop or rest timer worker based on config
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
                        double tempKelvin = response.body().getMain().temp;
                        double tempMinKelvin = response.body().getMain().tempMin;
                        double tempMaxKelvin = response.body().getMain().tempMax;

                        String humidity = response.body().getMain().humidity.toString();
                        String pressure = response.body().getMain().pressure.toString();

                        double temp = tempKelvin;
                        double tempMin = tempMinKelvin;
                        double tempMax = tempMaxKelvin;
                        String sTemp;
                        String sTempMin;
                        String sTempMax;
                        DecimalFormat format = new DecimalFormat("#.0");
                        if (userPreference.getDegrees().equalsIgnoreCase("F")) {
                            temp = Utility.getFarenheitValue(tempKelvin); //temp * 9 / 5) - 459.67;
                            tempMin = Utility.getFarenheitValue(tempMinKelvin);// tempMin * 9 / 5) - 459.67;
                            tempMax = Utility.getFarenheitValue(tempMaxKelvin); // tempMax * 9 / 5) - 459.67;
                            sTemp = format.format(temp) + "\u2109";
                            sTempMin = format.format(tempMin) + "\u2109";
                            sTempMax = format.format(tempMax) + "\u2109";
                        } else {
                            temp = Utility.getCelciusValue(tempKelvin);// temp - 273.15;
                            tempMin = Utility.getCelciusValue(tempMinKelvin); //tempMin - 273.15;
                            tempMax = Utility.getCelciusValue(tempMaxKelvin); //tempMax - 273.15;
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
                        result.setTempKelvin(tempKelvin);
                        result.setTempMinKelvin(tempMinKelvin);
                        result.setTempMaxKelvin(tempMaxKelvin);
                        result.setTemp(sTemp);
                        result.setTempMin(sTempMin);
                        result.setTempMax(sTempMax);
                        result.setHumidity(humidity + " %");
                        result.setPressure(pressure + " hPa");
                        result.setLastUpdate(sLastUpdt);
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
        userPreference.setCurrentWeather(data);
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
