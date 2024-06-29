package com.ubiquiti.networkStructure.comparator;

import com.ubiquiti.networkStructure.dto.DeviceType;
import com.ubiquiti.networkStructure.dto.NetworkDeviceResponse;

import java.util.Comparator;
import java.util.List;

/**
 * Device type comparator.
 */
public class DeviceTypeComparator implements Comparator<NetworkDeviceResponse> {

    private final List<DeviceType> definedOrder = List.of(DeviceType.GATEWAY, DeviceType.SWITCH, DeviceType.ACCESS_POINT);

    @Override
    public int compare(NetworkDeviceResponse o1, NetworkDeviceResponse o2) {
        return Integer.compare(definedOrder.indexOf(o1.deviceType()), definedOrder.indexOf(o2.deviceType()));
    }
}
