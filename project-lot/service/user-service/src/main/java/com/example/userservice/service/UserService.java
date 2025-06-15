package com.example.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.user.Users;

public interface UserService extends IService<Users>{

    Users Login(String username, String password);

}
