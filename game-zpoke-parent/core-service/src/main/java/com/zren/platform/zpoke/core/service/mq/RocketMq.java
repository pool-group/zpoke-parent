package com.zren.platform.zpoke.core.service.mq;

import com.assembly.common.util.ApplicationContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@Component
public class RocketMq {

    private final RocketMqConfig rocketMqConfig;

    private final RocketConsumer rocketConsumer;

    private final RocketProducer rocketProducer;

    private final ApplicationContextUtil applicationContextUtil;

    @PostConstruct
    public void initRocket() {

        rocketMqConfig.getConsumer().forEach((key, rocketMqProp) -> {
            try {
                MessageListener consumerListener = applicationContextUtil.getInstance().getBean(rocketMqProp.getListener(), MessageListener.class);
                rocketConsumer.initConsumer(rocketMqProp.getAddr(), rocketMqProp.getGroup(), rocketMqProp.getTopic(), consumerListener);
            } catch (Exception e) {
                log.error("consumer start error key={} prop={}", key, rocketMqProp.toString(), e);
                System.exit(0);
            }
        });

        rocketMqConfig.getProducer().forEach((key, rocketMqProp) -> {
            try {
                rocketProducer.initProducer(rocketMqProp.getAddr(), rocketMqProp.getGroup(), rocketMqProp.getTopic());
            } catch (Exception e) {
                log.error("producer start error key:{} prop={}", key, rocketMqProp.toString(), e);
                System.exit(0);
            }
        });
    }
}

