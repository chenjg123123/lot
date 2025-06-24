package com.example.deviceservice.controller;

import com.example.model.common.R;
import com.example.model.devices.Devices;
import com.example.deviceservice.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/device")
@RestController
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/list")
    public R list() {
        return R.success("获取成功" ,deviceService.list());
    }

    @GetMapping("/twoweek")
    public R twoweek() {
        return R.success(deviceService.twoweek());
    }

    @GetMapping("/getonline")
    public R getonline() {return R.success(deviceService.countOnlineDevices());}

    @GetMapping("/getstatus")
    public R getstatus() {
        return R.success(deviceService.countStatusDevices());
    }

    @GetMapping("/weekuserate")
    public R weekuserate() {
        return R.success(deviceService.weekOnline());
    }
} 