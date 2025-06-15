package com.example.notificationservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.notification.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
} 