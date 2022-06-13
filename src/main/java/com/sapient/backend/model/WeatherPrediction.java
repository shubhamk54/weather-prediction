package com.sapient.backend.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class WeatherPrediction {

    private static final int HIGH_TEMP_THRESHOLD = 40;
    private static final int MAX_THRESHOLD_WIND_SPEED = 10;
    private static final double MPS_TO_MPH_CONVERSION_RATE = 2.237;
    private String date;
    private double highTemperature;
    private double lowTemperature;
    private double windSpeed;
    private Suggestion suggestion;
    private boolean isThunderstorm;
    private boolean isRain;

    public WeatherPrediction(double highTemperature, double lowTemperature, double windSpeed) {
        this.highTemperature = highTemperature;
        this.lowTemperature = lowTemperature;
        this.windSpeed = windSpeed;
    }

    public double getWindSpeed() {
        return (double) Math.round(windSpeed * MPS_TO_MPH_CONVERSION_RATE * 100) / 100;
    }

    public List<String> getSuggestion() {

        List<String> suggestions = new ArrayList<>();
        if (highTemperature > HIGH_TEMP_THRESHOLD) {
            suggestions.add(Suggestion.USE_SUN_SCREEN.getSuggestionText());
        }
        if (getWindSpeed() > MAX_THRESHOLD_WIND_SPEED) {
            suggestions.add(Suggestion.WINDY.getSuggestionText());
        }

        if (isRain) {
            suggestions.add(Suggestion.CARRY_UMBRELLA.getSuggestionText());
        }
        if (isThunderstorm) {
            suggestions.add(Suggestion.THUNDERSTORM.getSuggestionText());
        }
        return suggestions;
    }
}
