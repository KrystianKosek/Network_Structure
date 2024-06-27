package com.ubiquiti.networkStructure.controller;

import com.ubiquiti.networkStructure.dto.*;
import com.ubiquiti.networkStructure.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
//slf4j
public class DeviceRestController {

    private final DeviceService deviceService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response<List<NetworkDeviceResponse>> getAllRegisteredDevices() {
        // add sorting
        // add validation of mac address (regex)
        return new Response<>(deviceService.getAllRegisteredDevices()).statusCode(StatusCode.SUCCESS);
    }

    @GetMapping("{macAddress}")
    @ResponseStatus(HttpStatus.OK)
    public Response<NetworkDeviceResponse> getDeviceByMacAddress(@PathVariable("macAddress") String macAddress) {
        return new Response<>(deviceService.getDeviceByMacAddress(macAddress)).statusCode(StatusCode.SUCCESS);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Status registerNewDevice(@RequestBody NetworkDeviceRequest newDevice) {
        // validation,        uplinkMacAddress may be null, other can not be null
        deviceService.registerNewDevice(newDevice);
        return new Status("Added new device", StatusCode.SUCCESS);
    }
}
