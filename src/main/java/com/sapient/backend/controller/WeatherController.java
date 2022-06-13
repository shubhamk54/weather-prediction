package com.sapient.backend.controller;

import com.sapient.backend.exception.ApiException;
import com.sapient.backend.model.CommonApiResponse;
import com.sapient.backend.model.WeatherPrediction;
import com.sapient.backend.service.WeatherService;
import com.sapient.backend.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/weather")
@Api(value = Constants.SWAGGER_WEATHER_FORECAST_CONTROLLER_DESC)
@ApiResponses(value = {
        @ApiResponse(code = 200, message = Constants.MSG_DATA_FETCHED_SUCCESSFULLY, response = CommonApiResponse.class),
        @ApiResponse(code = 400, message = Constants.MSG_INVALID_INPUT, response = CommonApiResponse.class),
        @ApiResponse(code = 404, message = Constants.MSG_CITY_NOT_FOUND, response = CommonApiResponse.class),
        @ApiResponse(code = 500, message = Constants.MSG_INTERNAL_SERVER_ERROR, response = CommonApiResponse.class),
})
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @GetMapping("/prediction")
    public ResponseEntity<List<WeatherPrediction>> getWeatherData(
            @RequestParam @NotBlank(message = "Required String parameter city is not present")
            String city) throws ApiException {
        return new ResponseEntity<>(weatherService.getWeatherData(city), HttpStatus.OK);
    }
}
