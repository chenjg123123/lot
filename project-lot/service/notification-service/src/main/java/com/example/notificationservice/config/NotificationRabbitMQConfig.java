package com.example.notificationservice.config;

import com.example.service.config.BaseRabbitMQConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationRabbitMQConfig extends BaseRabbitMQConfig {

    @Override
    protected String exchangeName() {
        return "hot.exchange";
    }

    @Override
    protected String queueName() {
        return "hot.notification.queue";
    }

    @Override
    protected String routingKey() {
        return "hot.notification.refresh";
    }

    public static final String EXCHANGE_NAME = "hot.exchange";
    public static final String QUEUE_NAME = "hot.notification.queue";
    public static final String ROUTING_KEY = "hot.notification.refresh";
}
