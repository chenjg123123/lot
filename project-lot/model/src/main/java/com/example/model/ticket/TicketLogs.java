package com.example.model.ticket;
import lombok.Data;

import java.sql.Timestamp;
@Data
public class TicketLogs {
    private Long id;
    private Long ticketId;
    private Long operatorId;
    private String action;
    private String comment;
    private Timestamp createdAt;
}
