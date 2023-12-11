package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfigure {

    /**
     * 声明交换机
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
//        ExchangeBuilder.fanoutExchange("yu.fanout1").durable(true);
        return new FanoutExchange("yu.fanout1",true,true);
    }


    /**
     * 声明队列
     * @return
     */
    @Bean
    public Queue fanQueue1(){
        return new Queue("fanout1.queue1",true);
    }

    /**
     * 将队列绑定到交换机
     * @param fanoutExchange
     * @param fanQueue1
     * @return
     */
    @Bean
    public Binding fanoutBind1(FanoutExchange fanoutExchange,Queue fanQueue1){
        return BindingBuilder.bind(fanQueue1).to(fanoutExchange);
    }

}
