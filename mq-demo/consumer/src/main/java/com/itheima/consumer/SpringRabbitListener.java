package com.itheima.consumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpringRabbitListener {


    /**
     * 注解RabbitListener声明监听消息
     *
     * @param msg
     */
    @RabbitListener(queues = "simple.queue")
    public void listenerMessage(String msg) {
        System.out.println("获取到消息：" + msg);
    }


    /**
     * workqueue模型
     *
     * @param msg
     */
    @RabbitListener(queues = "work.queue")
    public void worklistenerMessage1(String msg) throws InterruptedException {
        System.out.println("work1获取到消息：" + msg);
        Thread.sleep(20);
    }
    @RabbitListener(queues = "work.queue")
    public void worklistenerMessage2(String msg) throws InterruptedException {
        System.out.println("work2获取到消息：" + msg);
        Thread.sleep(200);
    }

    /**
     * 广播模式
     * @param msg
     * @throws InterruptedException
     */
    @RabbitListener(queues = "fanout.queue1")
    public void fanout1ListenerMessage(String msg) throws InterruptedException {
        System.out.println("fanout1获取到消息：" + msg);
    }
    @RabbitListener(queues = "fanout.queue2")
    public void fanout2ListenerMessage(String msg) throws InterruptedException {
        System.out.println("fanout2获取到消息：" + msg);
    }

    /**
     * 直连模式
     * 通过队列的绑定的key接收不同的消息
     * @param msg
     * @throws InterruptedException
     */
    @RabbitListener(queues = "direct.queue1")
    public void direct1ListenerMessage(String msg) throws InterruptedException {
        System.out.println("direct1获取到消息：" + msg);
    }
    @RabbitListener(queues = "direct.queue2")
    public void direct2ListenerMessage(String msg) throws InterruptedException {
        System.out.println("direct2获取到消息：" + msg);
    }

    /**
     * 主题模式
     * 通过队列的绑定的通配符key接收不同的消息
     * @param msg
     * @throws InterruptedException
     */
    @RabbitListener(queues = "topic.queue1")
    public void topic1ListenerMessage(String msg) throws InterruptedException {
        System.out.println("topic1的【#.china】获取到消息：" + msg);
    }
    @RabbitListener(queues = "topic.queue2")
    public void topic2ListenerMessage(String msg) throws InterruptedException {
        System.out.println("topic2的【news.#】获取到消息：" + msg);
    }

    /**
     * 通过在listener注解中嵌套注解声明绑定关系
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "topic1.queue2", durable = "true"),
                    exchange = @Exchange(name = "yu.topic1", type = ExchangeTypes.TOPIC),
                    key = {"#.china"}
            )
    )
    public void topic12ListenerMessage(String msg) throws InterruptedException {
        System.out.println("topic1-2的【#.china】获取到消息：" + msg);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "object1",durable = "true"),
                    exchange = @Exchange(name = "yu.direct2",type = ExchangeTypes.DIRECT),
                    key = {"o"}
            ))
    public void objectListenerMessage(Map<String,Object> map) throws InterruptedException {
        System.out.println("object的获取到消息：" + map);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "direct1.queue4", durable = "true",
                            arguments = @Argument(name = "x-queue-mode", value = "lazy")),
                    exchange = @Exchange(name = "yu.direct2", type = ExchangeTypes.DIRECT),
                    key = {"obj"}
            ))
    public void objectLazyListenerMessage(Map<String, Object> map) throws InterruptedException {
        System.out.println("object的获取到消息：" + map);
    }
}
