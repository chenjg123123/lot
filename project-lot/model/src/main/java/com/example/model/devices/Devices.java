package com.example.model.devices;

import lombok.Data;
import java.sql.Timestamp;
import java.util.Map;

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

    public static Devices fromMap(Map<String, Object> payload) {
        Devices devices = new Devices();
        devices.name = (String) payload.get("name");
        devices.type = (String) payload.get("type");
        devices.model = (String) payload.get("model");
        devices.companyId = ((Number) payload.get("companyId")).longValue();
        devices.location = (String) payload.get("location");
        devices.warrantyPeriod = (Integer) payload.get("warrantyPeriod");
        devices.firmwareVersion = (String) payload.get("firmwareVersion");
        return  devices;
    }
}
