package com.example.notificationservice.controller;

import com.example.model.Notification.Notifications;
import com.example.model.common.R;
import com.example.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/notification")
@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/list")
    public R list() {
        return R.success(notificationService.list());
    }

    @GetMapping("/twoweek")
    public R twoweek() {
        return R.success(notificationService.twoweek());
    }

    @GetMapping("/weekuse")
    public R weekUse() {
        return R.success(notificationService.weekuse());
    }

} 