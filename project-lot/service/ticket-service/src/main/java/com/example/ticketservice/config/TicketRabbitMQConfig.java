package com.example.ticketservice.config;

import com.example.service.config.BaseRabbitMQConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketRabbitMQConfig extends BaseRabbitMQConfig {

    @Override
    protected String exchangeName() {
        return "hot.exchange";
    }

    @Override
    protected String queueName() {
        return "hot.ticket.queue";
    }

    @Override
    protected String routingKey() {
        return "hot.ticket.refresh";
    }

    public static final String EXCHANGE_NAME = "hot.exchange";
    public static final String QUEUE_NAME = "hot.ticket.queue";
    public static final String ROUTING_KEY = "hot.ticket.refresh";
}
