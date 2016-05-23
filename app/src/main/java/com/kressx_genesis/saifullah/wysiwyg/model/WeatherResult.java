package com.kressx_genesis.saifullah.wysiwyg.model;

/**
 * Created by Saifullah on 21/05/2016.
 */
public class WeatherResult {

    private String city;
    private String country;
    private String status;
    private double tempKelvin;
    private double tempMinKelvin;
    private double tempMaxKelvin;
    private String temp;
    private String tempMin;
    private String tempMax;
    private String humidity;
    private String pressure;
    private String imgUrl;
    private String lastUpdate;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTempKelvin() {
        return tempKelvin;
    }

    public void setTempKelvin(double tempKelvin) {
        this.tempKelvin = tempKelvin;
    }

    public double getTempMinKelvin() {
        return tempMinKelvin;
    }

    public void setTempMinKelvin(double tempMinKelvin) {
        this.tempMinKelvin = tempMinKelvin;
    }

    public double getTempMaxKelvin() {
        return tempMaxKelvin;
    }

    public void setTempMaxKelvin(double tempMaxKelvin) {
        this.tempMaxKelvin = tempMaxKelvin;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
