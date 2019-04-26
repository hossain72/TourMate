package com.remon.tourmate;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.remon.tourmate.CurrentWeather.WeatherResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentWeatherFragment extends Fragment {

    private TextView currentTempTV, forecastTempTv;
    private RecyclerView recyclerView;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentTempTV = view.findViewById(R.id.currentTempTV);
        recyclerView = view.findViewById(R.id.forecastList);

        getCurrentWeather();
        getForecastWeather();

    }

    private void getCurrentWeather() {
        Weather_service weather_service = RetrofitClass.getRetrofitInstance().create(Weather_service.class);
        String url = String.format("weather?lat=23.9923078&lon=90.4226014&units=metric&appid=7882ca54b09fd1eb524ddc13392b0465");
        Call<WeatherResponse> weatherResponseCall = weather_service.getWeatherData(url);

        weatherResponseCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                if (response.code() == 200){
                    WeatherResponse weatherResponse = response.body();
                    currentTempTV.setText(String.valueOf(weatherResponse.getMain().getTempMin()));
                }

            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

            }
        });
    }

    private void getForecastWeather() {

        /*Weather_service weather_service = RetrofitClass.getRetrofitInstance().create(Weather_service.class);
        String url = String.format("forecast?lat=23.9923078&lon=90.4226014&units=metric&appid=7882ca54b09fd1eb524ddc13392b0465");
        Call<WeatherForcast> weatherForcastCall = weather_service.getForecastWeatherData(url);

        weatherForcastCall.enqueue(new Callback<WeatherForcast>() {
            @Override
            public void onResponse(Call<WeatherForcast> call, Response<WeatherForcast> response) {
                WeatherForcast weatherForcast = response.body();
                forecastTempTv.setText(String.valueOf(weatherForcast.getList().get(0).getMain().getTempMin()));
            }

            @Override
            public void onFailure(Call<WeatherForcast> call, Throwable t) {
                forecastTempTv.setText(t.getMessage());
            }
        });*/
    }



}
