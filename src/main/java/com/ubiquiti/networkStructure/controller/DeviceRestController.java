package com.ubiquiti.networkStructure.controller;

import com.ubiquiti.networkStructure.dto.*;
import com.ubiquiti.networkStructure.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Device rest controller.
 */
@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
//SLF4J
public class DeviceRestController {

    private final DeviceService deviceService;

    /**
     * Returns all registered devices, in the following order "Gateway > Switch > Access Point"
     *
     * @return response object with list of devices
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response<List<NetworkDeviceResponse>> getAllRegisteredDevices() {
        return new Response<>(deviceService.getAllRegisteredDevices()).statusCode(StatusCode.SUCCESS);
    }

    /**
     * Returns device with given mac address.
     *
     * @param macAddress mac address of device
     * @return response object with device or null if not found
     */
    @GetMapping("{macAddress}")
    @ResponseStatus(HttpStatus.OK)
    public Response<NetworkDeviceResponse> getDeviceByMacAddress(@PathVariable("macAddress") String macAddress) {
        return new Response<>(deviceService.getDeviceByMacAddress(macAddress)).statusCode(StatusCode.SUCCESS);
    }

    /**
     * Registers new network device.
     *
     * @param newDevice new device
     * @return response object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Status registerNewDevice(@Valid @RequestBody NetworkDeviceRequest newDevice) {
        deviceService.registerNewDevice(newDevice);
        return new Status("Added new device", StatusCode.SUCCESS);
    }

    /**
     * Returns all registered network device topology.
     *
     * @return response object with tree structure of registered devices
     */
    @GetMapping("/topology")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<TopologyNetworkDeviceResponse>> getNetworkTopology() {
        return new Response<>(deviceService.getNetworkDeviceTopology()).statusCode(StatusCode.SUCCESS);
    }

    /**
     * Returns registered network device topology starting from a device with given mac address.
     *
     * @param macAddress mac address of starting device
     * @return response object with tree structure of registered devices
     */
    @GetMapping("/topology/{macAddress}")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<TopologyNetworkDeviceResponse>> getNetworkTopologyFromGivenMacAddress(@PathVariable("macAddress") String macAddress) {
        return new Response<>(deviceService.getNetworkDeviceTopologyFromGivenDevice(macAddress)).statusCode(StatusCode.SUCCESS);
    }
}
