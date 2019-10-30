package com.zren.platform.zpoke.core.service.factory;

import com.alibaba.fastjson.JSON;
import com.assembly.common.exception.SysRuntimeException;
import com.assembly.common.util.LogUtil;
import com.assembly.template.engine.callback.AbstractOpCallback;
import com.assembly.template.engine.context.EngineContext;
import com.assembly.template.engine.template.impl.BizOpCenterServiceTemplate;
import com.zren.platform.core.model.context.MessageContext;
import com.zren.platform.core.model.queue.Message;
import com.zren.platform.core.model.queue.OverTimeMessage;
import com.zren.platform.core.model.standard.Dispatcher;
import com.zren.platform.zpoke.core.service.mq.RocketMqConfig;
import com.zren.platform.zpoke.core.service.mq.RocketProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Business Threads Factory
 *
 * @author k.y
 * @version Id: BusinessThreadsFactory.java, v 0.1 2019年10月24日 下午11:03 k.y Exp $
 */
@Component
public class BusinessThreadsFactory {

    @Autowired
    protected RocketProducer rocketProducer;
    @Autowired
    protected RocketMqConfig rocketMqConfig;
    @Autowired
    private Dispatcher dispatcher;
    @Autowired
    private BizOpCenterServiceTemplate bizOpCenterServiceTemplate;

    protected final DelayQueue<Message> handleMessageQueue=new DelayQueue();

    protected final DelayQueue<OverTimeMessage> handleEventQueue=new DelayQueue();

    private final ExecutorService businessExecutorService = Executors.newFixedThreadPool(2,new ThreadFactory() {

        private AtomicInteger threadIndex = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "BusinessThreadsFactory_"+this.threadIndex.getAndIncrement());
        }
    });

    @PostConstruct
    private void initialize(){

        this.businessExecutorService.execute(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        LogUtil.info("handleMessageQueue readying..");
                        Message take = handleMessageQueue.take();
                        List<Long> userIds=take.getIds();
                        for (Long id:userIds){
                            rocketProducer.sendMessage(take.getTopic(), String.valueOf(id),take.getBody());
                        }
                        LogUtil.info("handleMessageQueue ending..");
                    } catch (Exception e) {
                        LogUtil.error(e,"handleMessageQueue: send MQ message is error!");
                    }
                }
            }
        });

        this.businessExecutorService.execute(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    OverTimeMessage overTimeMessage=null;
                    try {
                        LogUtil.info("handleEventQueue readying..");
                        overTimeMessage=handleEventQueue.take();
                        LogUtil.info(String.format("handleEventQueue handleEventQueue.take() result=[ %s ]",overTimeMessage.getJson()));
                        dispatcher.excute(new MessageContext(JSON.parseObject(overTimeMessage.getJson()),""));
                        LogUtil.info("handleEventQueue ending..");
                    } catch (Exception e) {
                        LogUtil.error(e,String.format("handleEventQueue process message is error.. Request=[ %s ]",overTimeMessage.getJson()));
                    }
                }
            }
        });
    }
}