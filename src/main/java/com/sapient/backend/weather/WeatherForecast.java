package com.sapient.backend.weather;

import com.sapient.backend.exception.ApiException;
import com.sapient.backend.exception.DataNotFoundException;
import com.sapient.backend.exception.ValidationException;
import com.sapient.backend.model.DataList;
import com.sapient.backend.model.Weather;
import com.sapient.backend.model.WeatherApiResponse;
import com.sapient.backend.model.WeatherPrediction;
import com.sapient.backend.util.RestApiClient;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class WeatherForecast implements IForecast {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherForecast.class);
    private static final Integer[] SLOTS = new Integer[]{0, 3, 6, 9, 12, 15, 18, 21};
    @Value("${weather-app-api-url}")
    private String weatherAppUrl;
    @Value("${weather-app-api-key}")
    private String apiKey;
    @Getter
    @Setter
    private String units = "metric";
    private int numberOfDays = 3;
    private Integer currentDaySlots;

    public WeatherForecast() {
    }

    public WeatherForecast(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    @Override
    public List<WeatherPrediction> getPrediction(String city) throws ApiException {

        if (StringUtils.isEmpty(city)) {
            throw new ValidationException("Value of city should not be blank");
        }
        int recordCount = getRecordCount();
        LOG.debug("Record count of {} up to {} days", recordCount, numberOfDays);

        List<WeatherPrediction> predictions = new ArrayList<>();

        Optional<WeatherApiResponse> weatherApiResponse = getDataFromApi(city, recordCount);
        if (weatherApiResponse.isPresent()) {


            List<DataList> upcomingDaysForecasts = weatherApiResponse.get().getList()
                    .subList(currentDaySlots, recordCount);

            for (int index = 0; index < upcomingDaysForecasts.size() - 1; index = index + SLOTS.length) {
                WeatherPrediction prediction = getPredictionData(upcomingDaysForecasts.subList(index, index + SLOTS.length));
                String date = upcomingDaysForecasts.get(index).getDateText().substring(0, 10);
                prediction.setDate(date);
                predictions.add(prediction);
            }
        }
        return predictions;
    }

    private WeatherPrediction getPredictionData(List<DataList> subList) {
        double lowTemperature = Integer.MAX_VALUE;
        double highTemperature = Integer.MIN_VALUE;
        double maxWindSpeed = Integer.MIN_VALUE;
        boolean isRain = false;
        boolean isThunderstorm = false;
        for (DataList data : subList) {

            if (lowTemperature > data.getMain().getTempMin()) {
                lowTemperature = data.getMain().getTempMin();
            }
            if (highTemperature < data.getMain().getTempMax()) {
                highTemperature = data.getMain().getTempMax();
            }
            if (maxWindSpeed < data.getWind().getSpeed()) {
                maxWindSpeed = data.getWind().getSpeed();
            }
            if (!isRain) {
                isRain = weatherAppearanceById(data.getWeather(), RAIN_START_ID);
            }

            if (!isThunderstorm) {
                isThunderstorm = weatherAppearanceById(data.getWeather(), THUNDERSTORM_START_ID);
            }
        }

        WeatherPrediction weatherPrediction = new WeatherPrediction(highTemperature, lowTemperature, maxWindSpeed);
        weatherPrediction.setRain(isRain);
        weatherPrediction.setThunderstorm(isThunderstorm);
        return weatherPrediction;
    }

    private boolean weatherAppearanceById(List<Weather> weatherData, int startId) {
        return weatherData.stream().anyMatch(weather -> weather.getId() >= startId && weather.getId() < startId + 100);
    }

    @Override
    public int getRecordCount() {

        DateFormat outputFormat = new SimpleDateFormat("HH");
        outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String output = outputFormat.format(new Date());
        int currentHour = Integer.parseInt(output);
        currentDaySlots = (int) Arrays.stream(SLOTS)
                .filter(slot -> slot > currentHour).count();
        LOG.debug("Total slots for current day: {}", currentDaySlots);
        return currentDaySlots + (SLOTS.length * numberOfDays);
    }

    @Override
    public Optional<WeatherApiResponse> getDataFromApi(String city, int recordCount) throws ApiException {
        LOG.debug("Fetch forecast for: {}", city);
        try {
            WeatherApiResponse weatherApiResponse = new RestApiClient(weatherAppUrl)
                    .queryParam("appid", apiKey)
                    .queryParam("q", city)
                    .queryParam("units", units)
                    .queryParam("cnt", String.valueOf(recordCount))
                    .build(WeatherApiResponse.class);
            LOG.debug("Response from weather prediction API:{}", weatherApiResponse);

            return Optional.of(weatherApiResponse);
        } catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data Not available for city: {}", city);
        }
    }
}
