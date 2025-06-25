package com.example.deviceservice.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.example.model.devices.Devices;

import java.util.List;
import java.util.Map;

public interface DeviceService extends IService<Devices> {
    Integer[] twoweek();
    Integer countOnlineDevices();
    List<Map<String, Object>> countStatusDevices();
    List<Map<String, Object>> weekOnline();
    List<Devices> listByKey(String name, String status);
}