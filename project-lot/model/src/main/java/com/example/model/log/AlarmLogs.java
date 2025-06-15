package com.example.model.log;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AlarmLogs {
    private Long id;
    private Long deviceId;
    private Long ruleId;
    private java.math.BigDecimal value;
    private java.math.BigDecimal threshold;
    private String severity;
    private String status;
    private Timestamp resolvedTime;
    private Timestamp timestamp;
}
