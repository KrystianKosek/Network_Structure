package com.ubiquiti.networkStructure.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * Device type enum.
 */
@AllArgsConstructor
public enum DeviceType {
    GATEWAY("Gateway"),
    SWITCH("Switch"),
    ACCESS_POINT("Access Point");

    private final String name;

    @JsonValue
    public String getDeviceType() {
        return name;
    }

    @JsonCreator
    public static DeviceType fromName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Device type cannot be null.");
        }
        for (DeviceType deviceType : values()) {
            if (deviceType.getDeviceType().equals(name)) {
                return deviceType;
            }
        }

        throw new IllegalArgumentException("Invalid value for Device type Enum: " + name);
    }
}
