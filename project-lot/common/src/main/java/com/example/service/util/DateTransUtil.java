package com.example.service.util;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class DateTransUtil {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    //  将日期转化成星期
    private static String[] numToWeek = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六","星期日"};
    public static List<Map<String, Object>> convertDateToWeek(List<Map<String, Object>> originalList) {

        for (Map<String, Object> map : originalList) {
            Object dateObj = map.get("day");
            if (dateObj != null) {
                try {
                    LocalDate date = LocalDate.parse(dateObj.toString(), formatter);
                    DayOfWeek dayOfWeek = date.getDayOfWeek(); // MONDAY = 1
                    int index = dayOfWeek.getValue() - 1;       // 0-based index
                    String weekDay = numToWeek[index];
                    map.put("week", weekDay);                  // 新增字段
                    map.remove("date");                         // 可选：移除原来的日期字段
                } catch (Exception e) {
                    map.put("week", "日期错误");
                }
            } else {
                map.put("week", "无日期");
            }
        }

        return originalList;
    }
}
