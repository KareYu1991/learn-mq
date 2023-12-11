package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DelayExchangeConfig {

    private static final String exchangeName = "delay.topic1";
    private static final String queueName = "delay.queue1";
    private static final String routingKey = "delay1";

    @Bean
    public TopicExchange delayExchange1() {
        return ExchangeBuilder.topicExchange(exchangeName).durable(true)
                .delayed() // 开启延迟
                .build();
    }

    @Bean
    public Queue delayQueue1() {
        return QueueBuilder.durable(queueName).lazy().build();
    }

    @Bean
    public Binding bindingDelay1(Queue delayQueue1, TopicExchange delayExchange1) {
        return BindingBuilder.bind(delayQueue1).to(delayExchange1).with(routingKey);
    }

}
