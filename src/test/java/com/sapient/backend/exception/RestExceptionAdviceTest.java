package com.sapient.backend.exception;


import com.sapient.backend.controller.WeatherController;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(WeatherController.class)
@RunWith(MockitoJUnitRunner.class)
public class RestExceptionAdviceTest {

    @Spy
    @InjectMocks
    private static WeatherController weatherController;
    private static MockMvc mockMvc;
    @Mock
    WeatherServiceImpl weatherService;

    @BeforeClass
    public static void setup() {
        weatherController = new WeatherController();
    }

    @Test
    public void whenInvalidInputThenReturns400() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(weatherController)
                .setControllerAdvice(new RestExceptionAdvice())
                .build();
        MockHttpServletResponse response = mockMvc.perform(get("/weather/prediction")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void whenDataNotAvailableThenReturns() throws Exception {

        String city = "pune";
        Mockito.doThrow(new DataNotFoundException("Data not available for provided city"))
                .when(weatherService)
                .getWeatherData(city);

        mockMvc = MockMvcBuilders.standaloneSetup(weatherController)
                .setControllerAdvice(new RestExceptionAdvice())
                .build();

        MockHttpServletResponse response = mockMvc.perform(get("/weather/prediction")
                        .queryParam("city", city)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        Assertions.assertEquals("{\"message\":\"Data not available for provided city\"}", response.getContentAsString());
    }

    @Test
    public void whenDataNotAvailableWithParamsThenReturns() throws Exception {

        String city = "pune";
        Mockito.doThrow(new DataNotFoundException("Data not available for: {}", city))
                .when(weatherService)
                .getWeatherData(city);

        mockMvc = MockMvcBuilders.standaloneSetup(weatherController)
                .setControllerAdvice(new RestExceptionAdvice())
                .build();

        MockHttpServletResponse response = mockMvc.perform(get("/weather/prediction")
                        .queryParam("city", city)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        Assertions.assertEquals("{\"message\":\"Data not available for: pune\"}", response.getContentAsString());
    }

    @Test
    public void whenInternalServerErrorThenReturns() throws Exception {

        String city = "pune";
        Mockito.doThrow(new InternalServerException("Issue in the backed"))
                .when(weatherService)
                .getWeatherData(city);

        mockMvc = MockMvcBuilders.standaloneSetup(weatherController)
                .setControllerAdvice(new RestExceptionAdvice())
                .build();

        MockHttpServletResponse response = mockMvc.perform(get("/weather/prediction")
                        .queryParam("city", city)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        Assertions.assertEquals("{\"message\":\"Issue in the backed\"}", response.getContentAsString());
    }

    @Test
    public void whenInternalServerErrorWithParamsThenReturns() throws Exception {

        String city = "pune";
        Mockito.doThrow(new InternalServerException("Error occurred while calling open weather API status :{}", 401))
                .when(weatherService)
                .getWeatherData(city);

        mockMvc = MockMvcBuilders.standaloneSetup(weatherController)
                .setControllerAdvice(new RestExceptionAdvice())
                .build();

        MockHttpServletResponse response = mockMvc.perform(get("/weather/prediction")
                        .queryParam("city", city)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        Assertions.assertEquals("{\"message\":\"Error occurred while calling open weather API status :401\"}", response.getContentAsString());
    }

    @Test
    public void whenValidationExceptionReturns() throws Exception {

        String city = "invalid+City123Name";
        Mockito.doThrow(new ValidationException("Invalid city name :{}", city))
                .when(weatherService)
                .getWeatherData(city);

        mockMvc = MockMvcBuilders.standaloneSetup(weatherController)
                .setControllerAdvice(new RestExceptionAdvice())
                .build();

        MockHttpServletResponse response = mockMvc.perform(get("/weather/prediction")
                        .queryParam("city", city)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertEquals("{\"message\":\"Invalid city name :invalid+City123Name\"}", response.getContentAsString());
    }
}
