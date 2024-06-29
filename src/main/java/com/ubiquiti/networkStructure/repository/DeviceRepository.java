package com.ubiquiti.networkStructure.repository;

import com.ubiquiti.networkStructure.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository of device.
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    /**
     * Gets device by mac address.
     *
     * @param macAddress mac address of the device
     * @return device or null if not found
     */
    Device findByMacAddress(String macAddress);
}
