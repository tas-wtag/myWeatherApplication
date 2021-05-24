package com.weatherupdate.glare.models;

public class Weatherdata {

    private String temp2;
    private String rain2;
    private String img3;
    private String vv;

    public Weatherdata(String temp2, String rain2, String img3, String vv) {
        this.temp2 = temp2;
        this.rain2 = rain2;
        this.img3 = img3;
        this.vv = vv;
    }

    public String getVv() {
        return vv;
    }

    public String getTemp2() {
        return temp2;
    }

    public String getRain2() {
        return rain2;
    }

    public String getImg3() {
        return img3;
    }
}