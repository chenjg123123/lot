package com.example.getway.config;

import com.example.service.config.BaseRabbitMQConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetewayRabbitConfig extends BaseRabbitMQConfig {

    // 配置常量：推送专用队列与路由键
    public static final String EXCHANGE_NAME = "netty.exchange";
    public static final String QUEUE_NAME = "netty.push.queue";
    public static final String ROUTING_KEY = "netty.push";

    @Override
    protected String exchangeName() {
        return EXCHANGE_NAME;
    }

    @Override
    protected String queueName() {
        return QUEUE_NAME;
    }

    @Override
    protected String routingKey() {
        return ROUTING_KEY;
    }
}
