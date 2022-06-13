package com.sapient.backend.controller;

import com.sapient.backend.exception.ApiException;
import com.sapient.backend.exception.ValidationException;
import com.sapient.backend.model.WeatherPrediction;
import com.sapient.backend.service.impl.WeatherServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(WeatherController.class)
@RunWith(MockitoJUnitRunner.class)
public class WeatherControllerTest {

    @Spy
    @InjectMocks
    private static WeatherController weatherController;
    @Mock
    WeatherServiceImpl weatherService;

    @BeforeClass
    public static void setUp() {
        weatherController = new WeatherController();
    }

    @Test
    public void testOkResponseWhenValidDataReceived() throws ApiException {
        String city = "Pune";
        List<WeatherPrediction> fakeResults = new ArrayList<>();
        WeatherPrediction wp = new WeatherPrediction();
        wp.setRain(true);
        fakeResults.add(wp);
        Assertions.assertEquals(HttpStatus.OK, weatherController.getWeatherData(city).getStatusCode());
    }

    @Test
    public void testBadRequestWhenCityNotProvided() throws ApiException {
        String city = "";
        Mockito.doThrow(new ValidationException("")).when(weatherService)
                .getWeatherData(Mockito.anyString());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            weatherController.getWeatherData(city);
        });
    }
}
