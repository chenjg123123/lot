package com.example.model.ticket;
import lombok.Data;

import java.sql.Timestamp;
@Data
public class Tickets {
    private Long id;
    private String title;
    private String type;
    private String priority;
    private String status;
    private Long deviceId;
    private Long creatorId;
    private Long assigneeId;
    private String description;
    private String solution;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
