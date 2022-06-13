package com.sapient.backend.service;

import com.sapient.backend.exception.ApiException;
import com.sapient.backend.model.WeatherPrediction;

import java.util.List;

public interface WeatherService {

    List<WeatherPrediction> getWeatherData(String city) throws ApiException;

}
