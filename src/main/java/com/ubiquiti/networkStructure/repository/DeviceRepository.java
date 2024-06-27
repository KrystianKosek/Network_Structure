package com.ubiquiti.networkStructure.repository;

import com.ubiquiti.networkStructure.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    Device findByMacAddress(String macAddress);
}
