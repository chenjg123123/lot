package com.example.notificationservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.Notification.Notifications;
import com.example.notificationservice.mapper.NotificationMapper;
import com.example.notificationservice.service.NotificationService;
import com.example.service.util.DateTransUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notifications> implements NotificationService {
    @Autowired
    private DateTransUtil dateTransUtil;
    @Override
    /**
     * 获取最近两周的设备数
     */
    public Integer[] twoweek() {
        return new Integer[]{this.baseMapper.countWeekBeforeLastDevices(), this.baseMapper.countThisDevicesWeek()};
    }

    /**
     * 获取本周警告次数
     * @return
     */
    @Override
    public List<Map<String, Object>> weekuse() {
        return dateTransUtil.convertDateToWeek(this.baseMapper.countOrdersByDayThisWeek());
    }
}