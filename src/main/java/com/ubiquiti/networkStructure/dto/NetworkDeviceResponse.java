package com.ubiquiti.networkStructure.dto;


public record NetworkDeviceResponse(
        DeviceType deviceType,
        String macAddress) {
}
