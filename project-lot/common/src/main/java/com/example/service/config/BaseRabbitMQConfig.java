package com.example.service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

/**
 * RabbitMQ 公共基础配置类，供各微服务继承
 */
public abstract class BaseRabbitMQConfig {

    /**
     * 子类实现：指定交换机名称
     */
    protected abstract String exchangeName();

    /**
     * 子类实现：指定队列名称
     */
    protected abstract String queueName();

    /**
     * 子类实现：指定路由键
     */
    protected abstract String routingKey();

    @Bean
    public Queue queue() {
        return new Queue(queueName(), true); // 持久化
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName(), true, false); // 持久化
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(routingKey());
    }


}
