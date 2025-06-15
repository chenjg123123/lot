package com.example.model.Notification;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class NotificationConfigs {
    private Long id;
    private Long userId;
    private String type;
    private String config; // JSON 字符串
    private Integer isDefault;
    private Integer status;
    private Timestamp createdAt;
}
