package com.example.model.devices;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Devices {
    private Long id;
    private String name;
    private String type;
    private String model;
    private String serialNumber;
    private String status;
    private Long companyId;
    private String location;
    private java.sql.Date installationDate;
    private Integer warrantyPeriod;
    private Timestamp lastHeartbeat;
    private String firmwareVersion;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
