package com.zren.platform.zpoke.core.service.queue;

import com.assembly.common.exception.SysRuntimeException;
import com.assembly.common.util.LogUtil;
import com.zren.platform.core.model.queue.Message;
import com.zren.platform.core.model.queue.OverTimeMessage;
import com.zren.platform.zpoke.core.service.factory.BusinessThreadsFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handle Message Delay Queue
 *
 * @author k.y
 * @version Id: HandleMessageDelayQueue.java, v 0.1 2019年10月03日 下午14:33 k.y Exp $
 */
@Component
public class HandleDelayQueue extends BusinessThreadsFactory {


    public void addHandleMessage(List<Long> userIds, String json, long delayTime){
        LogUtil.info(String.format(" Add HandleMessage Message: message=[ %s ]", json));
        String topic = this.rocketMqConfig.getProducer().get("zpoke").getTopic();
        if(null==topic){
            throw new SysRuntimeException("topic is null !");
        }
        Message message = new Message(topic, userIds, json.getBytes(), delayTime);
        this.handleMessageQueue.offer(message);
    }

    public void addHandleEvent(String json,long delayTime){
        LogUtil.info(String.format(" Add HandleEvent Message: message=[ %s ]", json));
        OverTimeMessage overTimeMessage=new OverTimeMessage(json,delayTime);
        this.handleEventQueue.offer(overTimeMessage);
    }
}