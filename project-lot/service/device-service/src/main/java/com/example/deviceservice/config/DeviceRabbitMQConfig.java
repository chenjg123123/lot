package com.example.deviceservice.config;


import com.example.service.config.BaseRabbitMQConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceRabbitMQConfig extends BaseRabbitMQConfig {
    public static final String EXCHANGE_NAME = "hot.exchange";
    public static final String QUEUE_NAME = "hot.device.queue";
    public static final String SELECT_QUEUE = "sql.device.queue";
    public static final String ROUTING_KEY = "hot.device.refresh";
    public static final String ROUTING_KEY_SELECT = "sql.device.select";

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
    @Bean
    public Queue selectDeviceQueue() {
        return new Queue(SELECT_QUEUE, true);
    }

    @Bean
    public Binding selectBinding() {
        return BindingBuilder
                .bind(selectDeviceQueue())
                .to(exchange()) // 复用 exchange() 方法
                .with(ROUTING_KEY_SELECT);
    }

}

