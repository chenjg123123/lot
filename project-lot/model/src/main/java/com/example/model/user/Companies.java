package com.example.model.user;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Companies {
    private Long id;
    private String name;
    private String address;
    private String contactPerson;
    private String contactPhone;
    private String businessLicense;
    private String industry;
    private String scale;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
