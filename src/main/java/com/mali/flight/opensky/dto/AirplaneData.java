package com.mali.flight.opensky.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class AirplaneData {
    private Instant timeStamp;

    private List<AirplaneState> airplaneStates;
}
