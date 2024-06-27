package com.ubiquiti.networkStructure.exception;

public class NetworkDeviceNotFoundException extends RuntimeException {
    public NetworkDeviceNotFoundException(String message) {
        super(message);
    }
}
