package com.weatherupdate.glare.models;

import io.realm.RealmObject;

public class RealmData extends RealmObject {
    String searchedLatitude;



    public RealmData() {
    }

    public String getSearchedLatitude() {
        return searchedLatitude;
    }

    public void setSearchedLatitude(String searchedLatitude) {
        this.searchedLatitude = searchedLatitude;
    }

    public String getSearchedLongitude() {
        return searchedLongitude;
    }

    public void setSearchedLongitude(String searchedLongitude) {
        this.searchedLongitude = searchedLongitude;
    }

    String searchedLongitude;

}
