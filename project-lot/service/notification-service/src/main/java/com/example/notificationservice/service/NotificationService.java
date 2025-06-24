package com.example.notificationservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.Notification.Notifications;

import java.util.List;
import java.util.Map;

public interface NotificationService extends IService<Notifications> {
    Integer[] twoweek();
    List<Map<String, Object>> weekuse();
} 