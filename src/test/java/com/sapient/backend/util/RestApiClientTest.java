package com.sapient.backend.util;


import com.sapient.backend.exception.ApiException;
import com.sapient.backend.fixures.WeatherPredictionDataFixture;
import com.sapient.backend.model.RestErrorResponse;
import com.sapient.backend.model.WeatherApiResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class RestApiClientTest {

    private static String urlGet = "https://api.openweathermap.org/data/2.5/forecast?q=Pune&appid=test_id&cnt=28&units=metric";
    private static String urlPost = "https://api.test.com/data/2.5/forecast";
    private static RestApiClient restApiClientGet = new RestApiClient(urlGet);
    private static RestApiClient restApiClientGetSpy = Mockito.spy(restApiClientGet);
    private static RestApiClient restApiClientPost = new RestApiClient(HttpMethod.POST, urlPost);
    private static RestApiClient restApiClientPostSpy = Mockito.spy(restApiClientPost);

    @BeforeClass
    public static void setUp() throws ApiException {

        Optional<WeatherApiResponse> mockedWeatherApiResponse = WeatherPredictionDataFixture.getWeatherApiResponseStub();

        Mockito.doReturn(mockedWeatherApiResponse.get())
                .when(restApiClientGetSpy)
                .build(WeatherApiResponse.class);


    }


    @Test
    public void testRestClientUrlAndResponse() throws ApiException {

        String expectedUrl = "https://api.openweathermap.org/data/2.5/forecast?q=Pune&q=london&appid=test_id&appid=test_id&cnt=28&cnt=28&units=metric&units=metric";

        WeatherApiResponse apiResponse = restApiClientGetSpy
                .queryParam("appid", "test_id")
                .queryParam("q", "london")
                .queryParam("units", "metric")
                .queryParam("cnt", String.valueOf(28))
                .build(WeatherApiResponse.class);

        Optional<WeatherApiResponse> weatherApiResponse = Optional.of(apiResponse);
        Assertions.assertTrue(weatherApiResponse.isPresent());
        Assertions.assertEquals("200", weatherApiResponse.get().getCod());
        Assertions.assertEquals(expectedUrl, restApiClientGetSpy.getUrlTemplate());
    }

    @Test
    public void testRestClientPostAndResponse() throws ApiException {

        Mockito.doReturn(new RestErrorResponse("Unable to send data"))
                .when(restApiClientPostSpy)
                .build(RestErrorResponse.class);

        RestErrorResponse response = restApiClientPostSpy
                .addHeader("appid", "test_id")
                .body("field", "test-values")
                .build(RestErrorResponse.class);

        Assertions.assertEquals("Unable to send data", response.getMessage());
    }
}
