package com.ubiquiti.networkStructure.service;

import com.ubiquiti.networkStructure.comparator.DeviceTypeComparator;
import com.ubiquiti.networkStructure.dto.NetworkDeviceRequest;
import com.ubiquiti.networkStructure.dto.NetworkDeviceResponse;
import com.ubiquiti.networkStructure.dto.TopologyNetworkDeviceResponse;
import com.ubiquiti.networkStructure.exception.NetworkDeviceNotFoundException;
import com.ubiquiti.networkStructure.model.Device;
import com.ubiquiti.networkStructure.repository.DeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Device service.
 */
@AllArgsConstructor
@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final DeviceTypeComparator deviceTypeComparator = new DeviceTypeComparator();

    /**
     * Returns all registered devices, in the following order "Gateway > Switch > Access Point"
     *
     * @return list of devices
     */
    public List<NetworkDeviceResponse> getAllRegisteredDevices() {
        return deviceRepository.findAll().stream().map(this::mapToDto).sorted(deviceTypeComparator).toList();
    }

    /**
     * Returns device with given mac address.
     *
     * @param macAddress mac address of device
     * @return device
     */
    public NetworkDeviceResponse getDeviceByMacAddress(String macAddress) {
        Device device = deviceRepository.findByMacAddress(macAddress);
        if (device == null) {
            throw new NetworkDeviceNotFoundException(STR."Missing device with mac address: \{macAddress}");
        }
        return mapToDto(device);
    }

    /**
     * Registers new network device.
     *
     * @param newDevice new device
     */
    public void registerNewDevice(NetworkDeviceRequest newDevice) {
        Device parentDevice = deviceRepository.findByMacAddress(newDevice.uplinkMacAddress());
        deviceRepository.save(new Device().withDeviceType(newDevice.deviceType()).withMacAddress(newDevice.macAddress()).withUplinkDevice(parentDevice));
    }

    /**
     * Returns all registered network device topology.
     *
     * @return tree structure of registered devices
     */
    public List<TopologyNetworkDeviceResponse> getNetworkDeviceTopology() {
        List<Device> devices = deviceRepository.findAll();
        return createTreeByParent(devices, null);
    }

    /**
     * Returns registered network device topology starting from a device with given mac address.
     *
     * @param macAddress mac address of starting device
     * @return tree structure of registered devices
     */
    public List<TopologyNetworkDeviceResponse> getNetworkDeviceTopologyFromGivenDevice(String macAddress) {
        List<Device> devices = deviceRepository.findAll();
        Device device = deviceRepository.findByMacAddress(macAddress);
        if (device == null) {
            throw new NetworkDeviceNotFoundException(STR."Missing device with mac address: \{macAddress}");
        }
        List<TopologyNetworkDeviceResponse> aggregatedDevices = createTreeByParent(devices, macAddress);
        return List.of(new TopologyNetworkDeviceResponse(device.getDeviceType(), device.getMacAddress(), aggregatedDevices));
    }

    private NetworkDeviceResponse mapToDto(Device device) {
        return new NetworkDeviceResponse(device.getDeviceType(), device.getMacAddress());
    }

    private List<TopologyNetworkDeviceResponse> createTreeByParent(final List<Device> devices, String parentMacAddress) {
        List<TopologyNetworkDeviceResponse> result = new ArrayList<>();
        devices.forEach(device -> {
            if (Objects.equals(device.getUplinkDevice() == null ? null : device.getUplinkDevice().getMacAddress(), parentMacAddress)) {
                List<TopologyNetworkDeviceResponse> aggregatedDeviceResult = createTreeByParent(devices, device.getMacAddress());
                result.add(new TopologyNetworkDeviceResponse(device.getDeviceType(), device.getMacAddress(), aggregatedDeviceResult));
            }
        });
        return result;
    }
}
