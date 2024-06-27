package com.ubiquiti.networkStructure.service;

import com.ubiquiti.networkStructure.dto.NetworkDeviceRequest;
import com.ubiquiti.networkStructure.dto.NetworkDeviceResponse;
import com.ubiquiti.networkStructure.exception.NetworkDeviceNotFoundException;
import com.ubiquiti.networkStructure.model.Device;
import com.ubiquiti.networkStructure.repository.DeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public List<NetworkDeviceResponse> getAllRegisteredDevices() {
        return deviceRepository.findAll().stream().map(device -> new NetworkDeviceResponse(device.getDeviceType(), device.getMacAddress())).toList();
    }

    public NetworkDeviceResponse getDeviceByMacAddress(String macAddress) {
        Device device = deviceRepository.findByMacAddress(macAddress);
        if (device == null) {
            throw new NetworkDeviceNotFoundException("Missing device with mac address: " + macAddress);
        }
        return new NetworkDeviceResponse(device.getDeviceType(), device.getMacAddress());
    }

    public void registerNewDevice(NetworkDeviceRequest newDevice) {
        deviceRepository.save(new Device().withDeviceType(newDevice.deviceType()).withMacAddress(newDevice.macAddress()).withUplinkMacAddress(newDevice.uplinkMacAddress()));
    }
}
