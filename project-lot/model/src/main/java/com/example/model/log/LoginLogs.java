package com.example.model.log;
import lombok.Data;
import java.sql.Timestamp;
@Data
public class LoginLogs {
    private Long id;
    private Long userId;
    private String status;
    private String ip;
    private String userAgent;
    private String failReason;
    private Timestamp timestamp;
}
