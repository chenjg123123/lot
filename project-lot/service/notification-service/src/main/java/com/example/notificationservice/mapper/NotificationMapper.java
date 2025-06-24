package com.example.notificationservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.Notification.Notifications;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface NotificationMapper extends BaseMapper<Notifications> {
    @Select("SELECT COUNT(*) FROM notifications " +
            "WHERE created_at >= DATE_SUB(STR_TO_DATE(CONCAT(YEARWEEK(CURDATE(), 1), ' Monday'), '%X%V %W'), INTERVAL 14 DAY) " +
            "AND created_at < DATE_SUB(STR_TO_DATE(CONCAT(YEARWEEK(CURDATE(), 1), ' Monday'), '%X%V %W'), INTERVAL 7 DAY)")
    int countWeekBeforeLastDevices();
    @Select("SELECT COUNT(*) FROM notifications " +
            "WHERE created_at >= DATE_SUB(STR_TO_DATE(CONCAT(YEARWEEK(CURDATE(), 1), ' Monday'), '%X%V %W'), INTERVAL 7 DAY) " +
            "AND created_at < STR_TO_DATE(CONCAT(YEARWEEK(CURDATE(), 1), ' Monday'), '%X%V %W')")
    int countThisDevicesWeek();

    @Select("SELECT COUNT(*) FROM notifications")
    int countTotalDevices();

    @Select("""
            SELECT 
              days.date AS day,
              IFNULL(COUNT(t.id), 0) AS order_count
            FROM (
              SELECT 
                DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL seq DAY) AS date
              FROM (
                SELECT 0 AS seq UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL 
                SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6
              ) AS seqs
            ) AS days
            LEFT JOIN notifications t ON DATE(t.created_at) = days.date
            GROUP BY days.date
            ORDER BY days.date
            """)
    List<Map<String, Object>> countOrdersByDayThisWeek();
} 