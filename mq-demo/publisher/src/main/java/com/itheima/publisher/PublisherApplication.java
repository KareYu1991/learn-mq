package com.itheima.publisher;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PublisherApplication {
    public static void main(String[] args) {
        SpringApplication.run(PublisherApplication.class);
    }

    /**
     * 不使用jdk默认的序列化，改为使用jackson序列化
     */
    @Bean
    public MessageConverter messageConverter(){
        Jackson2JsonMessageConverter jmc = new Jackson2JsonMessageConverter();
        // 增加uuid作为消息id
        jmc.setCreateMessageIds(true);
        return jmc;
    }

}
