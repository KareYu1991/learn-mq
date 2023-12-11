package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 限定只有开启重试机制时，才使用错误队列
 */
@Configuration
@ConditionalOnProperty(name = "spring.rabbitmq.listener.simple.retry.enabled", havingValue = "true")
public class ErrorMessageConfig {

    private static final String exchangeName = "error.exchange1";
    private static final String queueName = "error.queue1";
    private static final String routingKey = "error1";

    @Bean
    public TopicExchange errorExchange1() {
        return new TopicExchange(exchangeName, true, true);
    }

    @Bean
    public Queue errorQueue1() {
        return QueueBuilder.durable(queueName).lazy().build();
    }

    @Bean
    public Binding bindingError1(Queue errorQueue1, TopicExchange errorExchange1) {
        return BindingBuilder.bind(errorQueue1).to(errorExchange1).with(routingKey);
    }


    /**
     * 替换自定义的错误队列
     */
    @Bean
    public MessageRecoverer errorMessageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, exchangeName, routingKey);
    }
}
