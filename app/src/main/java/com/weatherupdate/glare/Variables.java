package com.weatherupdate.glare;

import android.app.Application;

public class Variables extends Application {
    private String country_find;
    private String temp_find;
    private String img;
    private String date;
    private String sunset_find;
    private String sunrise_find;
    private String windSpeed_find;
    private String pressure_find;
    private double lati;
    private double longi;



    private String findLat;
    private String findLong;


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



    public long getDateInPause() {
        return dateInPause;
    }

    public void setDateInPause(long dateInPause) {
        this.dateInPause = dateInPause;
    }

    public long getDateInResume() {
        return dateInResume;
    }

    public void setDateInResume(long dateInResume) {
        this.dateInResume = dateInResume;
    }

    public long getDateInPause2() {
        return dateInPause2;
    }

    public void setDateInPause2(long dateInPause2) {
        this.dateInPause2 = dateInPause2;
    }

    public long dateInPause;
    public long dateInResume;
    public long dateInPause2;

    private String findTimeZone;
    private long date2;

    public String getTimePause() {
        return timePause;
    }

    public void setTimePause(String timePause) {
        this.timePause = timePause;
    }

    private String timePause;

    public String getCountry_find() {
        return country_find;
    }

    public void setCountry_find(String country_find) {
        this.country_find = country_find;
    }

    public String getTemp_find() {
        return temp_find;
    }

    public void setTemp_find(String temp_find) {
        this.temp_find = temp_find;
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

    public String getSunset_find() {
        return sunset_find;
    }

    public void setSunset_find(String sunset_find) {
        this.sunset_find = sunset_find;
    }

    public String getSunrise_find() {
        return sunrise_find;
    }

    public void setSunrise_find(String sunrise_find) {
        this.sunrise_find = sunrise_find;
    }

    public String getWindSpeed_find() {
        return windSpeed_find;
    }

    public void setWindSpeed_find(String windSpeed_find) {
        this.windSpeed_find = windSpeed_find;
    }

    public String getPressure_find() {
        return pressure_find;
    }

    public void setPressure_find(String pressure_find) {
        this.pressure_find = pressure_find;
    }

  /*  public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }*/

   /* public String getLatitude3() {
        return latitude3;
    }

    public void setLatitude3(String latitude3) {
        this.latitude3 = latitude3;
    }

    public String getLongitude3() {
        return longitude3;
    }

    public void setLongitude3(String longitude3) {
        this.longitude3 = longitude3;
    }*/

    public String getFindTimeZone() {
        return findTimeZone;
    }

    public void setFindTimeZone(String findTimeZone) {
        this.findTimeZone = findTimeZone;
    }

    public long getDate2() {
        return date2;
    }

    public void setDate2(long date2) {
        this.date2 = date2;
    }

    public double getLat_find() {
        return lat_find;
    }

    public void setLat_find(double lat_find) {
        this.lat_find = lat_find;
    }

    public double getLon_find() {
        return lon_find;
    }

    public void setLon_find(double lon_find) {
        this.lon_find = lon_find;
    }

   public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public int getHum_find() {
        return hum_find;
    }

    public void setHum_find(int hum_find) {
        this.hum_find = hum_find;
    }

    private double lat_find;
    private double lon_find;

    private int hum_find;

}