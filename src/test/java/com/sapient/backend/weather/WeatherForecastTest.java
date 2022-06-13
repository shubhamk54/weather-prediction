package com.sapient.backend.weather;

import com.sapient.backend.exception.ApiException;
import com.sapient.backend.exception.ValidationException;
import com.sapient.backend.fixures.WeatherPredictionDataFixture;
import com.sapient.backend.model.WeatherApiResponse;
import com.sapient.backend.model.WeatherPrediction;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class WeatherForecastTest {

    static WeatherForecast weatherForecast = new WeatherForecast();
    static WeatherForecast weatherForecastSpy = Mockito.spy(weatherForecast);
    static WeatherForecast weatherForecastFor2DaysSpy = Mockito.spy(new WeatherForecast(2));


    @BeforeClass
    public static void setUp() throws ApiException {

        Optional<WeatherApiResponse> mockedWeatherApiResponse = WeatherPredictionDataFixture.getWeatherApiResponseStub();

        Mockito.doReturn(mockedWeatherApiResponse).when(weatherForecastFor2DaysSpy)
                .getDataFromApi(Mockito.anyString(), Mockito.anyInt());

        Mockito.doReturn(mockedWeatherApiResponse).when(weatherForecastSpy)
                .getDataFromApi(Mockito.anyString(), Mockito.anyInt());
    }

    @Test
    public void testCityIsProvided() throws ApiException {
        String city = "pune";
        List<WeatherPrediction> actualPredictionList = weatherForecastSpy.getPrediction(city);
        Assertions.assertEquals(3, actualPredictionList.size());
    }

    @Test
    public void testFor2Days() throws ApiException {
        String city = "pune";
        weatherForecastFor2DaysSpy.setUnits("imperial");
        List<WeatherPrediction> actualPredictionList = weatherForecastFor2DaysSpy.getPrediction(city);
        Assertions.assertEquals(2, actualPredictionList.size());
    }

    @Test
    public void testCityIsNotProvided() {
        String city = "";
        ValidationException exception = assertThrows(ValidationException.class,
                () -> weatherForecast.getPrediction(city));

        String expectedMessage = "Value of city should not be blank";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testEmptyDataSuccess() throws ApiException {
        String city = "Pune";

        Mockito.doReturn(Optional.empty()).when(weatherForecastSpy)
                .getDataFromApi(Mockito.anyString(), Mockito.anyInt());

        List<WeatherPrediction> data = weatherForecastSpy.getPrediction(city);
        Assertions.assertEquals(0, data.size());
    }
}
