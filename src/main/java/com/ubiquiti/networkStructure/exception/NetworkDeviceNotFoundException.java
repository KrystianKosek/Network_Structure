package com.ubiquiti.networkStructure.exception;

/**
 * Exception thrown when searched network device not found.
 */
public class NetworkDeviceNotFoundException extends RuntimeException {
    
    /**
     * Default constructor with exception message.
     *
     * @param message message
     */
    public NetworkDeviceNotFoundException(String message) {
        super(message);
    }
}
