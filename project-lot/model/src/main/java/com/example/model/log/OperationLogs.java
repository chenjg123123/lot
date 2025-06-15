package com.example.model.log;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OperationLogs {
    private Long id;
    private Long userId;
    private String action;
    private String resource;
    private String details; // JSON 字符串
    private String ip;
    private String userAgent;
    private Timestamp timestamp;
}
