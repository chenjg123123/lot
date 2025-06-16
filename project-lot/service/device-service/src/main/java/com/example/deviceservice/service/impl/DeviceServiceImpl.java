package com.example.deviceservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.deviceservice.mapper.DeviceMapper;
import com.example.deviceservice.service.DeviceService;
import com.example.model.devices.Devices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Devices> implements DeviceService {
} 