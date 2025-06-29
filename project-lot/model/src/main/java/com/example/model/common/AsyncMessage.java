package com.example.model.common;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用 WebSocket 消息封装类，用于服务端发送消息到客户端
 * @param
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class  AsyncMessage<T> {
    private String requestId; // 用于标识前端连接/用户
    private String type;      // 消息类型，如 device:getlist、ticket:update
    private T payload;        // 实际数据内容，如分页数据、状态统计等
}

