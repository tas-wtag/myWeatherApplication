package com.weatherupdate.glare;

public class MyWeatherData {
    private String country;
    private String temprature;
    private String img;
    private String date;
    private String sunset;
    private String sunrise;
    private String windSpeed;
    private String pressure;
    private double latitudeCurrentLocation;
    private double longitudecurrentLocation;
    private double latitude;
    private double longitude;
    private int humidity;
    private String findTimeZone;

    public String getFindLat() {
        return findLat;
    }

    public void setFindLat(String findLat) {
        this.findLat = findLat;
    }

    public String getFindLong() {
        return findLong;
    }

    public void setFindLong(String findLong) {
        this.findLong = findLong;
    }

    private String findLat;
    private String findLong;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemprature() {
        return temprature;
    }

    public void setTemprature(String temprature) {
        this.temprature = temprature;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getFindTimeZone() {
        return findTimeZone;
    }

    public void setFindTimeZone(String findTimeZone) {
        this.findTimeZone = findTimeZone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

   public double getLatitudeCurrentLocation() {
        return latitudeCurrentLocation;
    }

    public void setLatitudeCurrentLocation(double latitudeCurrentLocation) {
        this.latitudeCurrentLocation = latitudeCurrentLocation;
    }
    public double getLongitudecurrentLocation() {
        return longitudecurrentLocation;
    }

    public void setLongitudecurrentLocation(double longitudecurrentLocation) {
        this.longitudecurrentLocation = longitudecurrentLocation;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }



}