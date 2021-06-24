package com.weatherupdate.glare.utilities;

import com.weatherupdate.glare.models.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface weatherapi {
    @GET("weather")
    Call <WeatherData> getweather(@Query("lat") String lat,
                                  @Query("lon") String lon,
                                  @Query("appid") String app_id);
}
