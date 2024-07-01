package com.ubiquiti.networkStructure.service;

import com.ubiquiti.networkStructure.dto.DeviceType;
import com.ubiquiti.networkStructure.dto.NetworkDeviceRequest;
import com.ubiquiti.networkStructure.dto.NetworkDeviceResponse;
import com.ubiquiti.networkStructure.dto.TopologyNetworkDeviceResponse;
import com.ubiquiti.networkStructure.exception.NetworkDeviceNotFoundException;
import com.ubiquiti.networkStructure.model.Device;
import com.ubiquiti.networkStructure.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {
    @Mock
    DeviceRepository deviceRepository;
    @InjectMocks
    DeviceService deviceService;

    @Test
    void testGetAllRegisteredDevices() {
        // given
        Device device1 = new Device().withId(1L).withDeviceType(DeviceType.SWITCH).withMacAddress("1D:1D:1D:1D:1D:1D");
        Device device2 = new Device().withId(2L).withDeviceType(DeviceType.ACCESS_POINT).withMacAddress("2A:2A:2A:2A:2A:2A").withUplinkDevice(device1);
        Device device3 = new Device().withId(3L).withDeviceType(DeviceType.GATEWAY).withMacAddress("3A:2A:3D:2A:4Z:2A").withUplinkDevice(device1);
        List<Device> devices = List.of(device1, device2, device3);
        when(deviceRepository.findAll()).thenReturn(devices);

        // when
        List<NetworkDeviceResponse> response = deviceService.getAllRegisteredDevices();

        // then
        verify(deviceRepository).findAll();
        assertEquals(3, response.size());
        assertEquals(new NetworkDeviceResponse(DeviceType.GATEWAY, device3.getMacAddress()), response.get(0));
        assertEquals(new NetworkDeviceResponse(DeviceType.SWITCH, device1.getMacAddress()), response.get(1));
        assertEquals(new NetworkDeviceResponse(DeviceType.ACCESS_POINT, device2.getMacAddress()), response.get(2));
    }

    @Test
    void testGetDeviceByNotRegisteredMacAddress() {
        // given
        when(deviceRepository.findByMacAddress(any())).thenReturn(null);
        final String macAddress = "12:12:12:12:12:12";

        // when
        NetworkDeviceNotFoundException exception = assertThrows(NetworkDeviceNotFoundException.class, () -> deviceService.getDeviceByMacAddress(macAddress));

        // then
        verify(deviceRepository).findByMacAddress(macAddress);
        assertEquals("Missing device with mac address: " + macAddress, exception.getMessage());
    }

    @Test
    void testGetDeviceByMacAddress() {
        // given
        Device device = new Device().withId(1L).withDeviceType(DeviceType.SWITCH).withMacAddress("1D:1D:1D:1D:1D:1D");
        when(deviceRepository.findByMacAddress(any())).thenReturn(device);

        // when
        NetworkDeviceResponse response = deviceService.getDeviceByMacAddress(device.getMacAddress());

        // then
        verify(deviceRepository).findByMacAddress(device.getMacAddress());
        assertEquals(device.getDeviceType(), response.deviceType());
        assertEquals(device.getMacAddress(), response.macAddress());
    }

    @Test
    void testRegisterNewDevice() {
        // given
        final String macAddress = "12:12:12:12:12:12";
        final String uplinkMacAddress = "1D:1D:1D:1D:1D:1D";
        final Device parentDevice = new Device().withId(1L).withMacAddress(uplinkMacAddress).withDeviceType(DeviceType.GATEWAY);
        NetworkDeviceRequest request = new NetworkDeviceRequest(DeviceType.ACCESS_POINT, macAddress, uplinkMacAddress);
        when(deviceRepository.findByMacAddress(uplinkMacAddress)).thenReturn(parentDevice);

        // when
        deviceService.registerNewDevice(request);

        // then
        verify(deviceRepository).findByMacAddress(uplinkMacAddress);
        verify(deviceRepository).save(new Device().withDeviceType(request.deviceType()).withMacAddress(request.macAddress()).withUplinkDevice(parentDevice));
    }

    @Test
    void testGetNetworkDeviceTopology() {
        // given
        Device device1 = new Device().withId(1L).withDeviceType(DeviceType.SWITCH).withMacAddress("1D:1D:1D:1D:1D:1D");
        Device device2 = new Device().withId(2L).withDeviceType(DeviceType.ACCESS_POINT).withMacAddress("2A:2A:2A:2A:2A:2A").withUplinkDevice(device1);
        Device device3 = new Device().withId(3L).withDeviceType(DeviceType.GATEWAY).withMacAddress("3A:2A:3D:2A:4Z:2A");
        List<Device> devices = List.of(device1, device2, device3);
        when(deviceRepository.findAll()).thenReturn(devices);

        // when
        List<TopologyNetworkDeviceResponse> response = deviceService.getNetworkDeviceTopology();

        // then
        verify(deviceRepository).findAll();
        assertEquals(2, response.size());
        assertEquals(new TopologyNetworkDeviceResponse(device1.getDeviceType(), device1.getMacAddress(),
                List.of(new TopologyNetworkDeviceResponse(device2.getDeviceType(), device2.getMacAddress(), Collections.emptyList()))), response.get(0));
        assertEquals(new TopologyNetworkDeviceResponse(device3.getDeviceType(), device3.getMacAddress(), Collections.emptyList()), response.get(1));
    }

    @Test
    void testGetNetworkDeviceTopologyStartedFromDeviceWithGivenMacAddress() {
        // given
        final String macAddress = "1D:1D:1D:1D:1D:1D";
        Device device1 = new Device().withId(1L).withDeviceType(DeviceType.SWITCH).withMacAddress(macAddress);
        Device device2 = new Device().withId(2L).withDeviceType(DeviceType.ACCESS_POINT).withMacAddress("2A:2A:2A:2A:2A:2A").withUplinkDevice(device1);
        Device device3 = new Device().withId(3L).withDeviceType(DeviceType.GATEWAY).withMacAddress("3A:2A:3D:2A:4Z:2A");
        List<Device> devices = List.of(device1, device2, device3);
        when(deviceRepository.findAll()).thenReturn(devices);
        when(deviceRepository.findByMacAddress(macAddress)).thenReturn(device1);

        // when
        List<TopologyNetworkDeviceResponse> response = deviceService.getNetworkDeviceTopologyFromGivenDevice(macAddress);

        // then
        verify(deviceRepository).findAll();
        verify(deviceRepository).findByMacAddress(macAddress);
        assertEquals(1, response.size());
        assertEquals(new TopologyNetworkDeviceResponse(device1.getDeviceType(), device1.getMacAddress(),
                List.of(new TopologyNetworkDeviceResponse(device2.getDeviceType(), device2.getMacAddress(), Collections.emptyList()))), response.getFirst());
    }
}
