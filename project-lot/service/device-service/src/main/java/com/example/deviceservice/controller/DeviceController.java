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

    @PostMapping("/add")
    public R add(@RequestBody Devices device) {
        return R.success("添加成功",deviceService.save(device));
    }

    @PutMapping("/update")
    public R update(@RequestBody Devices device) {
        return R.success("更新成功",deviceService.updateById(device));
    }

    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Long id) {
        return R.success("删除成功",deviceService.removeById(id));
    }
} 