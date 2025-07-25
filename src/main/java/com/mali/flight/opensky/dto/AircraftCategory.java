package com.mali.flight.opensky.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AircraftCategory {
    NO_INFORMATION(0),
    NO_ADSB_EMITTER_CATEGORY_INFORMATION(1),
    LIGHT(2),
    SMALL(3),
    LARGE(4),
    HIGH_VORTEX_LARGE(5),
    HEAVY(6),
    HIGH_PERFORMANCE(7),
    ROTORCRAFT(8),
    GLIDER_SAILPLANE(9),
    LIGHTER_THAN_AIR(10),
    PARACHUTIST_SKYDIVER(11),
    ULTRALIGHT_HANGGLIDER_PARAGLIDER(12),
    RESERVED(13),
    UNMANNED_AERIAL_VEHICLE(14),
    SPACE_TRANS_ATMOSPHERIC_VEHICLE(15),
    SURFACE_VEHICLE_EMERGENCY(16),
    SURFACE_VEHICLE_SERVICE(17),
    POINT_OBSTACLE(18),
    CLUSTER_OBSTACLE(19),
    LINE_OBSTACLE(20);

    private final int value;

    AircraftCategory(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static AircraftCategory fromValue(Integer value) {
        for (AircraftCategory category : values()) {
            if (category.value == value) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown AircraftCategory value: " + value);
    }
}
