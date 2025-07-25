package com.mali.flight.opensky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AirplaneState {
    @JsonProperty("icao24")
    private String icao24;

    @JsonProperty("callsign")
    private String callSign;

    @JsonProperty("origin_country")
    private String originCountry;

    @JsonProperty("time_position")
    private Integer timePosition;

    @JsonProperty("last_contact")
    private Integer lastContact;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("baro_altitude")
    private Double barometricAltitude;

    @JsonProperty("on_ground")
    private Boolean onGround;

    @JsonProperty("velocity")
    private Double velocity;

    @JsonProperty("true_track")
    private Double trueTrack;

    @JsonProperty("vertical_rate")
    private Float verticalRate;

    @JsonProperty("sensors")
    private List<Integer> sensors;

    @JsonProperty("geo_altitude")
    private Double geoAltitude;

    @JsonProperty("squawk")
    private String squawk;

    @JsonProperty("spi")
    private Boolean specialPurpose;

    @JsonProperty("position_source")
    private Integer positionSource;

    @JsonProperty("category")
    private AircraftCategory category;
}
