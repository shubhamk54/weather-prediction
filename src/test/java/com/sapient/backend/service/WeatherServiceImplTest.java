package com.sapient.backend.service;


import com.sapient.backend.exception.ApiException;
import com.sapient.backend.exception.ValidationException;
import com.sapient.backend.fixures.WeatherPredictionDataFixture;
import com.sapient.backend.model.WeatherApiResponse;
import com.sapient.backend.model.WeatherPrediction;
import com.sapient.backend.service.impl.WeatherServiceImpl;
import com.sapient.backend.weather.WeatherForecast;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceImplTest {

    @Spy
    @InjectMocks
    private static WeatherServiceImpl weatherServiceImpl;

    @Spy
    WeatherForecast weatherForecast = new WeatherForecast();

    @BeforeClass
    public static void setUp() {
        weatherServiceImpl = new WeatherServiceImpl();
    }

    @Test
    public void testCityIsProvided() throws ApiException {
        String city = "Pune";

        Optional<WeatherApiResponse> mockedWeatherApiResponse = WeatherPredictionDataFixture.getWeatherApiResponseStub();

        Mockito.doReturn(mockedWeatherApiResponse).when(weatherForecast)
                .getDataFromApi(Mockito.anyString(), Mockito.anyInt());

        List<WeatherPrediction> actualPredictionList = weatherServiceImpl.getWeatherData(city);
        Assertions.assertEquals(3, actualPredictionList.size());
    }

    @Test
    public void testCityIsNotProvided() {
        String city = "";
        ValidationException exception = assertThrows(ValidationException.class,
                () -> weatherServiceImpl.getWeatherData(city));

        String expectedMessage = "Value of city should not be blank";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }
}
