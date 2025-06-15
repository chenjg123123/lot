package com.example.deviceservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.device.Device;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
} 