package com.mali.flight.opensky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpenSkyApiResponse {
    @JsonProperty("time")
    private int timeStamp;

    @JsonProperty("states")
    private List<List<Object>> sourceStates;
}
