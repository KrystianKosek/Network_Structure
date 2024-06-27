package com.ubiquiti.networkStructure.dto;

public record NetworkDeviceRequest(
        DeviceType deviceType,
        String macAddress,
        String uplinkMacAddress
) {
}
