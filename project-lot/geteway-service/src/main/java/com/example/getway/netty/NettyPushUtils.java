package com.example.getway.netty;

import com.alibaba.fastjson2.JSON;
import com.example.model.common.AsyncMessage;
import org.springframework.stereotype.Component;

@Component
public class NettyPushUtils {
    public static <T> void push(String requestId, String type, T payload) {
        AsyncMessage<T> msg = new AsyncMessage<>(requestId, type, payload);
        WebSocketFrameHandler.sendMessage(requestId, JSON.toJSONString(msg));
    }
}

