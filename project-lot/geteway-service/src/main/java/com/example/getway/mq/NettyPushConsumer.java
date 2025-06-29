package com.example.getway.mq;

import com.alibaba.fastjson2.JSON;
import com.example.model.common.AsyncMessage;
import com.example.getway.netty.WebSocketFrameHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class NettyPushConsumer {
    @RabbitListener(queues = "netty.push.queue")
    public void receive(AsyncMessage<Map<String,Object>>  message) {
        System.out.println("receive message: " + message);
        WebSocketFrameHandler.sendMessage(message.getRequestId(), JSON.toJSONString(message));
    }


}

