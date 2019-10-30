package com.zren.platform.zpoke.core.service.mq;

import com.assembly.common.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class RocketProducer extends Producer<DefaultMQProducer> {

    @Override
    public void initProducer(String addr, String group, String topic) throws Exception {
        if (!PRODUCER_CONCURRENT_HASH_MAP.containsKey(topic)) {
            DefaultMQProducer defaultMQProducer = new DefaultMQProducer(group);
            defaultMQProducer.setNamesrvAddr(addr);
            defaultMQProducer.start();
            PRODUCER_CONCURRENT_HASH_MAP.put(topic, defaultMQProducer);
            log.info("new Producer success addr={} , group={} , topic={} ", addr, group, topic);
        }
    }

    @Override
    public void sendMessage(String topic, String tag, byte[] bytes) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message message = new Message(topic, tag, bytes);
        message.setFlag(0);
        if (!PRODUCER_CONCURRENT_HASH_MAP.containsKey(topic)) {
            log.error("produce is not exist topic={}", topic);
            return;
        }
        DefaultMQProducer defaultMQProducer = PRODUCER_CONCURRENT_HASH_MAP.get(topic);
        SendResult sendResult = defaultMQProducer.send(message);
        LogUtil.info(String.format(" rocketProducer.sendMessage: topic=[ %s ], tags=[ %s ], body=[ %s ], sendResult=[ %s ] ", topic, tag, new String(bytes, StandardCharsets.UTF_8),sendResult));
    }

    @Override
    public void shutDown(DefaultMQProducer defaultMQProducer) {
        defaultMQProducer.shutdown();
    }


    @PreDestroy
    @Override
    public void destroy() {
        PRODUCER_CONCURRENT_HASH_MAP.forEach((key, producer) -> {
            producer.shutdown();
        });
    }
}