package com.example.model.devices;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DeviceData {
    private Long id;
    private Long deviceId;
    private String dataType;
    private java.math.BigDecimal value;
    private String unit;
    private Timestamp timestamp;
}
