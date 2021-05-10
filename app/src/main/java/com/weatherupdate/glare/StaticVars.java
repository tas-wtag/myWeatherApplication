package com.weatherupdate.glare;

import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

public class StaticVars {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    public static String city_find;
    public static final String IMG_URL2 = "https://openweathermap.org/img/w/";
    public static PlaceOptions placeOptions;
    public static final String IMG_URL = "https://openweathermap.org/img/w/";
    public static String getMapboxAccessToken() {
        return MAPBOX_ACCESS_TOKEN;
    }

    private static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoidGFzZmlhc2V1dGkiLCJhIjoiY2tubzd1b3U5MTVjMzJvbW9ybm5laGU4bSJ9.3XIBkPnAK9juMzlx-Rar9A";

}
