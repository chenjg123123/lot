package com.example.model.user;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserCompany {
    private Long id;
    private Long userId;
    private Long companyId;
    private Integer isPrimary;
    private Timestamp createdAt;
}
