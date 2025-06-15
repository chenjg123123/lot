package com.example.model.Notification;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class Notifications {
    private Long id;
    private Long userId;
    private String type;
    private String title;
    private String content;
    private Integer status;
    private Timestamp readTime;
    private Timestamp createdAt;
}
