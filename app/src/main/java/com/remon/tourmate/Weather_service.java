package com.remon.tourmate;

import com.remon.tourmate.CurrentWeather.WeatherResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Weather_service {

    @GET
    Call<WeatherResponse> getWeatherData(@Url String url);

}
