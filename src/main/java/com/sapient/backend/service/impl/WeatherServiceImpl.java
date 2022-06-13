package com.sapient.backend.service.impl;

import com.sapient.backend.exception.ApiException;
import com.sapient.backend.model.WeatherPrediction;
import com.sapient.backend.service.WeatherService;
import com.sapient.backend.weather.WeatherForecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    WeatherForecast weatherForecast;

    @Cacheable(value = "weatherData", key = "#city")
    @Override
    public List<WeatherPrediction> getWeatherData(String city) throws ApiException {
        return weatherForecast.getPrediction(city);
    }
}
