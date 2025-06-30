package com.example.deviceservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.deviceservice.DTO.delectDeviceDTO;
import com.example.model.devices.Devices;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeviceMapper extends BaseMapper<Devices> {


    @Select("SELECT COUNT(*) FROM devices " +
            "WHERE created_at >= DATE_SUB(STR_TO_DATE(CONCAT(YEARWEEK(CURDATE(), 1), ' Monday'), '%X%V %W'), INTERVAL 7 DAY) " +
            "AND created_at < STR_TO_DATE(CONCAT(YEARWEEK(CURDATE(), 1), ' Monday'), '%X%V %W') ")
    int countLastDevicesThisWeek();

    @Select("SELECT COUNT(*) FROM devices " +
            "WHERE created_at < STR_TO_DATE(CONCAT(YEARWEEK(CURDATE(), 1), ' Monday'), '%X%V %W') " +
            "OR created_at >= DATE_ADD(STR_TO_DATE(CONCAT(YEARWEEK(CURDATE(), 1), ' Monday'), '%X%V %W'), INTERVAL 7 DAY)")
    int countDevicesExceptLastWeek();

    @Select("SELECT COUNT(*) FROM devices")
    int countTotalDevices();

    @Select("SELECT COUNT(*) FROM devices WHERE status = 'online'")
    int countOnilne();

    @Select("SELECT status, COUNT(*) as num FROM devices GROUP BY status")
    List<Map<String, Object>> countStatusDevices();


    @Select({
            "SELECT ",
            "  d.date AS day,",
            "  IFNULL(t.online_rate, 0) AS online_rate ",
            "FROM (",
            "  SELECT DATE_ADD(CURDATE(), INTERVAL (seq - WEEKDAY(CURDATE())) DAY) AS date",
            "  FROM (",
            "    SELECT 0 seq UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL ",
            "    SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6",
            "  ) AS seqs",
            ") d ",
            "LEFT JOIN device_online_stats t ON DATE(t.created_at) = d.date ",
            "ORDER BY d.date"
    })
    List<Map<String, Object>> countOnlineByDayThisWeek();

    int deleteByIdAndNameList(@Param("list") List<delectDeviceDTO> list);

} 