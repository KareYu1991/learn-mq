package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectConfigure {

    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("yu.direct1",true,true);
    }


    /**
     * 声明队列
     */
    @Bean
    public Queue directQueue1(){
        return new Queue("direct1.queue1",true);
    }

    /**
     * 将队列绑定到交换机,需要指定key
     */
    @Bean
    public Binding directBind1(DirectExchange directExchange,Queue directQueue1){
        return BindingBuilder.bind(directQueue1).to(directExchange).with("blue");
    }



    /**
     * 多个队列和key需要多次声明
     */
    @Bean
    public Queue directQueue2(){
        Queue queue = new Queue("direct1.queue2", true);
        return queue;
    }
    @Bean
    public Binding directBind2(){
        return BindingBuilder.bind(directQueue2()).to(directExchange()).with("red");
    }


    /**
     * 惰性队列
     */
    @Bean
    public Queue directQueue3(){
        // 使用QueueBuilder直接设置惰性队列
        return QueueBuilder
                .durable("direct1.queue3")
                .lazy()
                .build();
    }

}
