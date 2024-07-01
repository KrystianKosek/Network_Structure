package com.ubiquiti.networkStructure.dto;


/**
 * Response object of network device.
 *
 * @param deviceType type of the device
 * @param macAddress mac address of the device
 */
public record NetworkDeviceResponse(
        DeviceType deviceType,
        String macAddress) {
}
