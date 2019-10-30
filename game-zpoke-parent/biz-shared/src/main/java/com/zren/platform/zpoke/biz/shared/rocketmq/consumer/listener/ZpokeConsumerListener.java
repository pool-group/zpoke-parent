package com.zren.platform.zpoke.biz.shared.rocketmq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.assembly.common.redis.redisson.DistributedLock;
import com.assembly.common.redis.redisson.HLock;
import com.assembly.common.util.LogUtil;
import com.assembly.template.engine.callback.AbstractOpCallback;
import com.assembly.template.engine.context.EngineContext;
import com.assembly.template.engine.result.IBaseResult;
import com.assembly.template.engine.template.impl.BizOpCenterServiceTemplate;
import com.zren.platform.zpoke.biz.shared.forward.DispatcherCenter;
import com.zren.platform.core.model.context.MessageContext;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Zpoke Consumer Listener
 *
 * @author k.y
 * @version Id: ZpokeConsumerListener.java, v 0.1 2019年09月28日 下午17:44 k.y Exp $
 */
@RequiredArgsConstructor
@Component(value = "zpokeListener")
public class ZpokeConsumerListener implements MessageListenerConcurrently {

    private final BizOpCenterServiceTemplate bizOpCenterServiceTemplate;
    private final DistributedLock distributedLock;
    private final DispatcherCenter dispatcherCenter;

    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            onMessage(msgs, consumeConcurrentlyContext);
        }finally {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }

    private IBaseResult onMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext){
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<String,Void>(){

            @Override
            public void doProcess(EngineContext<String, Void> context) {
                msgs.forEach(message -> {
                    LogUtil.info(String.format(" zpokeListener consumeMessage: topic=[ %s ], tags=[ %s ], body=[ %s ],msgId=[ %s ] ", message.getTopic(), message.getTags(), new String(message.getBody(), StandardCharsets.UTF_8),message.getMsgId()));
                    Long startTime=System.currentTimeMillis();
                    String msg = new String(message.getBody(), StandardCharsets.UTF_8);
                    try(HLock lock=distributedLock.getLock("msgLock:"+message.getMsgId())) {
                        Boolean isLocked=lock.tryLock(0, 30, TimeUnit.SECONDS);
                        if(isLocked){
                            LogUtil.info(String.format(" [ %s ]Message Processing tryLock success：isTrue=[ %s ], 耗时：time= [ %s ]ms",message.getMsgId(),isLocked,System.currentTimeMillis()-startTime));
                            dispatcherCenter.excute(new MessageContext(JSONObject.parseObject(msg),message.getTags()));
                        }else {
                            LogUtil.info(String.format(" [ %s ]Repeated Message Processing tryLock fail：isTrue=[ %s ], 耗时：time= [ %s ]ms",message.getMsgId(),isLocked,System.currentTimeMillis()-startTime));
                        }
                    } catch (Exception e) {
                        LogUtil.error(e,String.format(" Ignored exception message [ %s ]",msg));
                    }
                });
            }
        });
    }
}