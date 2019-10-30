package com.zren.platform.core.model.queue;

import lombok.Data;
import lombok.ToString;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author k.y
 * @version Id: OverTimeMessage.java, v 0.1 2019年10月24日 下午14:11 k.y Exp $
 */
@Data
@ToString
public class OverTimeMessage implements Delayed {

    private String json;

    private long excuteTime;

    public OverTimeMessage(String json, long delayTime) {
        this.json=json;
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