package com.sapient.backend.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Main {
    private double temp;
    @JsonProperty("temp_min")
    private double tempMin;
    @JsonProperty("temp_max")
    private double tempMax;
}
