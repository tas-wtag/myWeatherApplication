package com.weatherupdate.glare.models;

public class UpcomingWeatherData {

    private final String tempOfUpcomingWeather;
    private final String upcomingWeatherSituation;
    private final String upcomingWeatherImage;
    private final String dateInExpectedFormat;

    public UpcomingWeatherData(String tempOfUpcomingWeather, String upcomingWeatherSituation, String upcomingWeatherImage, String dateInExpectedFormat) {
        this.tempOfUpcomingWeather = tempOfUpcomingWeather;
        this.upcomingWeatherSituation = upcomingWeatherSituation;
        this.upcomingWeatherImage = upcomingWeatherImage;
        this.dateInExpectedFormat = dateInExpectedFormat;
    }

    public String getDateInExpectedFormat() {
        return dateInExpectedFormat;
    }

    public String getTempOfUpcomingWeather() {
        return tempOfUpcomingWeather;
    }

    public String getUpcomingWeatherSituation() {
        return upcomingWeatherSituation;
    }

    public String getUpcomingWeatherImage() {
        return upcomingWeatherImage;
    }
}