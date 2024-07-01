package com.ubiquiti.networkStructure.dto;

import java.util.List;

/**
 * Response object of network device topology.
 *
 * @param deviceType        type of the device
 * @param macAddress        mac address of the device
 * @param aggregatedDevices list of aggregated devices
 */
public record TopologyNetworkDeviceResponse(
        DeviceType deviceType,
        String macAddress,
        List<TopologyNetworkDeviceResponse> aggregatedDevices
) {
}
