package com.itheima.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SpringRabbitDelayListener {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "delay.queue2", durable = "true",
                            arguments = @Argument(name = "x-queue-mode", value = "lazy")),
                    exchange = @Exchange(name = "delay.topic1", type = ExchangeTypes.TOPIC, delayed = "true"),
                    key = {"delay1"}
            ))
    public void objectDelayListenerMessage(Map<String, Object> map) throws InterruptedException {
        System.out.println("object的获取到消息：" + map);
        log.info("消息接收完成");
    }
}
