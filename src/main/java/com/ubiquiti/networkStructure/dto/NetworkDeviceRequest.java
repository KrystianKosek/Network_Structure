package com.ubiquiti.networkStructure.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO of new network device.
 *
 * @param deviceType       type of the device
 * @param macAddress       mac address of the device
 * @param uplinkMacAddress (optional) uplink mac address of the device
 */
public record NetworkDeviceRequest(
        @NotNull(message = "Type of the device is required!") DeviceType deviceType,
        @NotNull(message = "Mac address is required") @Pattern(regexp = MAC_ADDRESS_PATTERN, message = "Invalid structure of mac address") String macAddress,
        @Pattern(regexp = MAC_ADDRESS_PATTERN, message = "Invalid structure of uplink mac address") String uplinkMacAddress
) {
    private final static String MAC_ADDRESS_PATTERN = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
}