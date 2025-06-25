package com.example.deviceservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.deviceservice.mapper.DeviceMapper;
import com.example.deviceservice.service.DeviceService;
import com.example.model.devices.Devices;
import com.example.service.util.DateTransUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Devices> implements DeviceService{

    @Autowired
    private DateTransUtil dateTransUtil;

    /**
     * 获取在线设备数
     * @return
     */
    @Override
    public Integer countOnlineDevices() {
       return this.baseMapper.countOnilne();
    }

    /**
     * 获取各个状态设备
     * @return
     */
    @Override
    public List<Map<String, Object>> countStatusDevices() {
        return this.baseMapper.countStatusDevices();
    }

    /**
     * 获取本周在线设备率
     * @return
     */
    @Override
    public List<Map<String, Object>> weekOnline() {
       return dateTransUtil.convertDateToWeek(this.baseMapper.countOnlineByDayThisWeek());
    }

    @Override
    public List<Devices> listByKey(String name, String status) {
        QueryWrapper<Devices> devicesQueryWrapper = new QueryWrapper<Devices>();
        if(name != null) devicesQueryWrapper.like("name", name);
        if(status != null) devicesQueryWrapper.like("status", status);
        return this.baseMapper.selectList(devicesQueryWrapper);
    }

    /**
     * 获取最近两周设备数
     * @return
     */
    @Override
    public Integer[] twoweek() {
        return new Integer[]{this.baseMapper.countLastDevicesThisWeek(), this.baseMapper.countDevicesExceptLastWeek()};
    }
}