package com.ubiquiti.networkStructure.model;

import com.ubiquiti.networkStructure.dto.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Entity
@Table(name = "Devices")
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private DeviceType deviceType;

    @Column(unique = true)
    private String macAddress;

    private String uplinkMacAddress;
}
