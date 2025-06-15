package com.example.notificationservice.controller;

import com.example.model.common.R;
import com.example.model.notification.Notification;
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

    @PostMapping("/add")
    public R add(@RequestBody Notification notification) {
        return R.success(notificationService.save(notification));
    }

    @PutMapping("/update")
    public R update(@RequestBody Notification notification) {
        return R.success(notificationService.updateById(notification));
    }

    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Long id) {
        return R.success(notificationService.removeById(id));
    }
} 