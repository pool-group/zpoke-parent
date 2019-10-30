package com.zren.platform.core.model.queue;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Message
 *
 * @author k.y
 * @version Id: Message.java, v 0.1 2019年10月03日 下午14:28 k.y Exp $
 */
@Data
@ToString
public class Message implements Delayed {

    private String topic;

    private List<Long> ids;

    private byte[] body;

    private long excuteTime;

    public Message(String topic, List<Long> userIds, byte[] body, long delayTime) {
        this.topic = topic;
        this.ids = userIds;
        this.body = body;
        this.excuteTime = TimeUnit.NANOSECONDS.convert(delayTime, TimeUnit.MILLISECONDS) + System.nanoTime();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.excuteTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}