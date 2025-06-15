package com.example.model.devices;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DeviceAlarmRules {
    private Long id;
    private Long deviceId;
    private String dataType;
    private String ruleCondition;
    private java.math.BigDecimal threshold;
    private String severity;
    private String notificationType;
    private Integer status;
    private Timestamp createdAt;
}
