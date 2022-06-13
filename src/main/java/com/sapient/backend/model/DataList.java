package com.sapient.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class DataList {
    private int dt;
    private Main main;
    private ArrayList<Weather> weather;
    private Wind wind;
    @JsonProperty("dt_txt")
    private String dateText;
}
