package com.example.deviceservice.config;


import com.example.service.config.BaseRabbitMQConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceRabbitMQConfig extends BaseRabbitMQConfig {

    @Override
    protected String exchangeName() {
        return "hot.exchange";
    }

    @Override
    protected String queueName() {
        return "hot.device.queue";
    }

    @Override
    protected String routingKey() {
        return "hot.device.refresh";
    }

    public static final String EXCHANGE_NAME = "hot.exchange";
    public static final String QUEUE_NAME = "hot.device.queue";
    public static final String ROUTING_KEY = "hot.device.refresh";
    public static final String ROUTING_KEY_SECOND = "hot.device.select";
}

