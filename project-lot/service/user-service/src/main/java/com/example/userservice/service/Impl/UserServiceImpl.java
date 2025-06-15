package com.example.userservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.user.Users;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, Users> implements UserService {
    @Override
    public Users Login(String username, String password) {
        Users user = getOne(new QueryWrapper<Users>().eq("username", username));
        if(user != null && user.getPassword().equals(password)) return user;
        return null;
    }

}
