package com.ubiquiti.networkStructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubiquiti.networkStructure.dto.DeviceType;
import com.ubiquiti.networkStructure.dto.NetworkDeviceRequest;
import com.ubiquiti.networkStructure.dto.NetworkDeviceResponse;
import com.ubiquiti.networkStructure.dto.TopologyNetworkDeviceResponse;
import com.ubiquiti.networkStructure.service.DeviceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DeviceRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceService deviceService;

    @Test
    void shouldReturnAllRegisteredDevices() throws Exception {
        // given
        List<NetworkDeviceResponse> listOfDevices = List.of(new NetworkDeviceResponse(DeviceType.GATEWAY, "12:12:12:12:12:12"), new NetworkDeviceResponse(DeviceType.SWITCH, "21:21:21:21:21:21"));
        when(deviceService.getAllRegisteredDevices()).thenReturn(listOfDevices);

        // when & then
        this.mockMvc.perform(get("/api/devices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.statusCode").value("SUCCESS"))
                .andExpect(jsonPath("$.body", hasSize(2)))
                .andExpect(jsonPath("$.body[0].deviceType").value("Gateway"))
                .andExpect(jsonPath("$.body[0].macAddress").value("12:12:12:12:12:12"))
                .andExpect(jsonPath("$.body[1].deviceType").value("Switch"))
                .andExpect(jsonPath("$.body[1].macAddress").value("21:21:21:21:21:21"));

        verify(this.deviceService).getAllRegisteredDevices();
    }

    @Test
    void shouldReturnDeviceWithGivenMacAddress() throws Exception {
        // given
        final String macAddress = "4F:4F:4F:4F:4F:4F";
        NetworkDeviceResponse deviceResponse = new NetworkDeviceResponse(DeviceType.GATEWAY, macAddress);
        when(deviceService.getDeviceByMacAddress(any())).thenReturn(deviceResponse);

        // when & then
        this.mockMvc.perform(get("/api/devices/" + macAddress))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.statusCode").value("SUCCESS"))
                .andExpect(jsonPath("$.body.deviceType").value(deviceResponse.deviceType().getDeviceType()))
                .andExpect(jsonPath("$.body.macAddress").value(deviceResponse.macAddress()));

        verify(this.deviceService).getDeviceByMacAddress(macAddress);
    }

    @Test
    void shouldReturnProperStatusCodeAndDescriptionWhenInvalidStructureOfRequestBodyProvided() throws Exception {
        // given
        final NetworkDeviceRequest request = new NetworkDeviceRequest(DeviceType.GATEWAY, null, "abc");
        String reqBody = new ObjectMapper().writeValueAsString(request);

        // when & then
        this.mockMvc.perform(post("/api/devices")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(reqBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.statusCode").value("ERROR"))
                .andExpect(jsonPath("$.statusDescription", containsString("Mac address is required")))
                .andExpect(jsonPath("$.statusDescription", containsString("Invalid structure of uplink mac address")));

        verify(this.deviceService, never()).registerNewDevice(any());
    }

    @Test
    void shouldRegisterNewDevice() throws Exception {
        // given
        final NetworkDeviceRequest request = new NetworkDeviceRequest(DeviceType.GATEWAY, "1F:1F:1F:1F:1F:1F", null);
        String reqBody = new ObjectMapper().writeValueAsString(request);

        // when & then
        this.mockMvc.perform(post("/api/devices")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(reqBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.statusCode").value("SUCCESS"))
                .andExpect(jsonPath("$.statusDescription").value("Registered new device"));

        verify(this.deviceService).registerNewDevice(request);
    }

    @Test
    void shouldReturnNetworkTopology() throws Exception {
        // given
        List<TopologyNetworkDeviceResponse> listOfDevices = List.of(new TopologyNetworkDeviceResponse(
                DeviceType.GATEWAY,
                "12:12:12:12:12:12",
                List.of(new TopologyNetworkDeviceResponse(DeviceType.ACCESS_POINT, "A1:A1:A1:A1:A1:A1", Collections.emptyList()))));
        when(deviceService.getNetworkDeviceTopology()).thenReturn(listOfDevices);

        // when & then
        this.mockMvc.perform(get("/api/devices/topology"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.statusCode").value("SUCCESS"))
                .andExpect(jsonPath("$.body", hasSize(1)))
                .andExpect(jsonPath("$.body[0].deviceType").value(listOfDevices.getFirst().deviceType().getDeviceType()))
                .andExpect(jsonPath("$.body[0].macAddress").value(listOfDevices.getFirst().macAddress()))
                .andExpect(jsonPath("$.body[0].aggregatedDevices", hasSize(1)))
                .andExpect(jsonPath("$.body[0].aggregatedDevices[0].deviceType").value(listOfDevices.getFirst().aggregatedDevices().getFirst().deviceType().getDeviceType()))
                .andExpect(jsonPath("$.body[0].aggregatedDevices[0].macAddress").value(listOfDevices.getFirst().aggregatedDevices().getFirst().macAddress()))
                .andExpect(jsonPath("$.body[0].aggregatedDevices[0].aggregatedDevices", hasSize(0)));

        verify(this.deviceService).getNetworkDeviceTopology();
    }

    @Test
    void shouldReturnNetworkTopologyStartedFromDeviceWithGivenMacAddress() throws Exception {
        // given
        final String macAddress = "12:12:12:12:12:12";
        List<TopologyNetworkDeviceResponse> listOfDevices = List.of(new TopologyNetworkDeviceResponse(
                DeviceType.GATEWAY,
                "12:12:12:12:12:12",
                List.of(new TopologyNetworkDeviceResponse(DeviceType.ACCESS_POINT, "A1:A1:A1:A1:A1:A1", Collections.emptyList()))));
        when(deviceService.getNetworkDeviceTopologyFromGivenDevice(any())).thenReturn(listOfDevices);

        // when & then
        this.mockMvc.perform(get("/api/devices/topology/" + macAddress))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.statusCode").value("SUCCESS"))
                .andExpect(jsonPath("$.body", hasSize(1)))
                .andExpect(jsonPath("$.body[0].deviceType").value(listOfDevices.getFirst().deviceType().getDeviceType()))
                .andExpect(jsonPath("$.body[0].macAddress").value(listOfDevices.getFirst().macAddress()))
                .andExpect(jsonPath("$.body[0].aggregatedDevices", hasSize(1)))
                .andExpect(jsonPath("$.body[0].aggregatedDevices[0].deviceType").value(listOfDevices.getFirst().aggregatedDevices().getFirst().deviceType().getDeviceType()))
                .andExpect(jsonPath("$.body[0].aggregatedDevices[0].macAddress").value(listOfDevices.getFirst().aggregatedDevices().getFirst().macAddress()))
                .andExpect(jsonPath("$.body[0].aggregatedDevices[0].aggregatedDevices", hasSize(0)));

        verify(this.deviceService).getNetworkDeviceTopologyFromGivenDevice(macAddress);
    }
}
