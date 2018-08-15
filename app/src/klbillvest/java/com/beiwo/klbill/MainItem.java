package com.beiwo.klbill;

public class MainItem {
    private City city;
    private int weatherInfo;

    public MainItem() {
        super();
    }

    public MainItem(City city, int weatherInfo) {
        super();
        this.city = city;
        this.weatherInfo = weatherInfo;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(int weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

}
