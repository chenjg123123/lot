package com.example.logservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.log.Log;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper extends BaseMapper<Log> {
} 