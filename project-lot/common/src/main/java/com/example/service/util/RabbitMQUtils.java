package com.example.service.util;


import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ 工具类：封装发送消息的方法
 */
@Component
public class RabbitMQUtils {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送纯字符串消息
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param message 消息内容（字符串）
     */
    public void send(String exchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    /**
     * 发送任意对象，会自动转为 JSON 字符串
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param object 任意对象
     */
    public void sendObject(String exchange, String routingKey, Object object) {
        String json = JSON.toJSONString(object);
        rabbitTemplate.convertAndSend(exchange, routingKey, json);
    }

    /**
     * 发送延迟消息（需要配置延迟插件或 TTL）
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param object 消息对象
     * @param delayMs 延迟时间（毫秒）
     */
    public void sendWithDelay(String exchange, String routingKey, Object object, int delayMs) {
        rabbitTemplate.convertAndSend(exchange, routingKey, object, message -> {
            message.getMessageProperties().setDelay(delayMs);
            return message;
        });
    }
}
