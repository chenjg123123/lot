package com.example.deviceservice.controller;

import com.example.model.common.R;
import com.example.model.device.Device;
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
        return R.success(deviceService.list());
    }

    @PostMapping("/add")
    public R add(@RequestBody Device device) {
        return R.success(deviceService.save(device));
    }

    @PutMapping("/update")
    public R update(@RequestBody Device device) {
        return R.success(deviceService.updateById(device));
    }

    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Long id) {
        return R.success(deviceService.removeById(id));
    }
} 