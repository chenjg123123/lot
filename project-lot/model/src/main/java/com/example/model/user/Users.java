package com.example.model.user;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Users {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String email;
    private String phone;
    private String role;
    private Integer status;
    private Timestamp lastLoginTime;
    private String lastLoginIp;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
