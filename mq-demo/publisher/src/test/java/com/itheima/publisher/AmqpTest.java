package com.itheima.publisher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
public class AmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到交换机
     */
    @Test
    public void sendTest() {
        //key队列名称,mgs是消息
        String exchange = "/";
        String routing = "simple.queue";
        String message = "999999";
        rabbitTemplate.convertAndSend(routing, message);
    }

    /**
     * workqueue模型
     */
    @Test
    public void sendWorkTest() {
        String routing = "work.queue";
        String message = "第";
        for (int i = 1; i <= 50; i++) {
            rabbitTemplate.convertAndSend(routing, message + i + "条消息");
        }
    }

    /**
     * 广播模式
     */
    @Test
    public void sendFanOutTest() {
        // 交换机
        String exchange = "admintest.fanout";
        // 广播 不需要指定队列
        String routing = "";
        String message = "hello";
        rabbitTemplate.convertAndSend(exchange, routing, message);
    }

    /**
     * 直连模式
     */
    @Test
    public void sendDirectBlueTest() {
        // 交换机
        String exchange = "admintest.direct";
        // key
        String routing = "blue";
        String message = "hello-blue";
        rabbitTemplate.convertAndSend(exchange, routing, message);
    }

    @Test
    public void sendDirectRedTest() {
        // 交换机
        String exchange = "admintest.direct";
        // key
        String routing = "red";
        String message = "hello-red";
        rabbitTemplate.convertAndSend(exchange, routing, message);
    }

    @Test
    public void sendDirectYellowTest() {
        // 交换机
        String exchange = "admintest.direct";
        // key
        String routing = "yellow";
        String message = "hello-yellow";
        rabbitTemplate.convertAndSend(exchange, routing, message);
    }


    /**
     * topic模式
     * 通配符匹配key
     */
    @Test
    public void sendTopicChinaTest() {
        // 交换机
        String exchange = "admintest.topic";
        // 通配符匹配的key
        String routing = "this.china";
        String message = "hello-china";
        rabbitTemplate.convertAndSend(exchange, routing, message);
    }

    @Test
    public void sendTopicNewsTest() {
        // 交换机
        String exchange = "admintest.topic";
        // 通配符匹配的key
        String routing = "news.yes";
        String message = "hello-yes";
        rabbitTemplate.convertAndSend(exchange, routing, message);
    }

    @Test
    public void sendTopicChinaTest1() {
        // 交换机
        String exchange = "yu.topic1";
        // 通配符匹配的key
        String routing = "this.china";
        String message = "hello-china";
        rabbitTemplate.convertAndSend(exchange, routing, message);
    }

    @Test
    public void sendObjectTest1() {
        // 交换机
        String exchange = "yu.direct2";
        String routing = "o";
        // 传递对象
        Map<String, Object> map = new HashMap<>();
        map.put("name", "lisi");
        map.put("age", 20);
        rabbitTemplate.convertAndSend(exchange, routing, map);
    }

    /**
     * 在发消息时进行确认处理
     * 创建CorrelationData对象，向里面的future添加回调处理
     * 然后将CorrelationData对象作为参数，在发送消息
     */
    @Test
    public void sendConfirmTest1() {
        CorrelationData cd = new CorrelationData();
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                // 代表程序处理失败，基本不会触发
                log.error("发送消息异常", ex);
            }
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                // result 是回调内容  true是ack false是nack
                log.debug("收到confirm callback回执");
                if (result.isAck()) {
                    log.debug("发送成功，应答ack");
                } else {
                    // 交换机错误时会有nack，如果只是路由错误不会产生nack
                    log.error("发送失败，应答nack result:{}", result.getReason());
                }
            }
        });
        // 发送消息
        String exchange = "yu.direct2";
        String routing = "o";
        Map<String, Object> map = new HashMap<>();
        map.put("name", "lisi");
        rabbitTemplate.convertAndSend(exchange, routing, map, cd);

        // 保证能打印日志
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 消息持久化
     */
    @Test
    public void sendDurableObjectTest() {
        String exchange = "yu.direct2";
        String routing = "o";
        Map<String, Object> map = new HashMap<>();
        map.put("name", "lisi");
        map.put("age", 20);
//        MessagePostProcessor message = new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
//                return message;
//            }
//        };
        rabbitTemplate.convertAndSend(exchange, routing, map, m -> {
            m.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return m;
        });
    }

    @Test
    public void sendDelayObjectTest() {
        String exchange = "delay.topic1";
        String routing = "delay1";
        Map<String, Object> map = new HashMap<>();
        map.put("name", "lisi");
        map.put("age", 20);
        rabbitTemplate.convertAndSend(exchange, routing, map, m -> {
            MessageProperties mp = m.getMessageProperties();
            mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            mp.setDelay(10000);
            return m;
        });
        log.info("消息发送完成");
    }
}
