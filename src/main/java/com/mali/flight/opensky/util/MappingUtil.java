package com.mali.flight.opensky.util;

import com.mali.flight.opensky.dto.AircraftCategory;
import com.mali.flight.opensky.dto.AirplaneState;

import java.util.List;

public class MappingUtil {

    public static AirplaneState getAirplaneStates(List<Object> array) {
        AirplaneState state = new AirplaneState();
        state.setIcao24((String) array.get(0));
        state.setCallSign((String) array.get(1));
        state.setOriginCountry((String) array.get(2));
        state.setTimePosition(asInteger(array.get(3)));
        state.setLastContact(asInteger(array.get(4)));
        state.setLongitude(asDouble(array.get(5)));
        state.setLatitude(asDouble(array.get(6)));
        state.setBarometricAltitude(asDouble(array.get(7)));
        state.setOnGround(asBoolean(array.get(8)));
        state.setVelocity(asDouble(array.get(9)));
        state.setTrueTrack(asDouble(array.get(10)));
        state.setVerticalRate(asFloat(array.get(11)));
        state.setGeoAltitude(asDouble(array.get(13)));
        state.setSquawk((String) array.get(14));
        state.setSpecialPurpose(asBoolean(array.get(15)));
        state.setPositionSource(asInteger(array.get(16)));
        // aircraft category is not always present
        if (array.size() > 17 && array.get(17) != null) {
            state.setCategory(AircraftCategory.fromValue(asInteger(array.get(17))));
        }
        return state;
    }


    public static Integer asInteger(Object value) {
        switch (value) {
            case Integer i -> {
                return i;
            }
            case Number number -> {
                return number.intValue();
            }
            case String s -> {
                try {
                    return Integer.parseInt(s);
                } catch (NumberFormatException ignored) {
                }
            }
            case null, default -> {
                return null;
            }
        }
        return null;
    }

    public static Double asDouble(Object value) {
        switch (value) {
            case Double v -> {
                return v;
            }
            case Number number -> {
                return number.doubleValue();
            }
            case String s -> {
                try {
                    return Double.parseDouble(s);
                } catch (NumberFormatException ignored) {
                }
            }
            case null, default -> {
                return null;
            }
        }
        return null;
    }

    public static Float asFloat(Object value) {
        switch (value) {
            case Float v -> {
                return v;
            }
            case Number number -> {
                return number.floatValue();
            }
            case String s -> {
                try {
                    return Float.parseFloat(s);
                } catch (NumberFormatException ignored) {
                }
            }
            case null, default -> {
                return null;
            }
        }
        return null;
    }

    public static Boolean asBoolean(Object value) {
        return switch (value) {
            case Boolean b -> b;
            case Number number -> number.intValue() != 0;
            case String s -> Boolean.parseBoolean(s);
            case null, default -> null;
        };
    }

}
