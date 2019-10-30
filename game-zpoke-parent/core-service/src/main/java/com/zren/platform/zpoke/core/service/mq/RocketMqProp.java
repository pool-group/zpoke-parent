package com.zren.platform.zpoke.core.service.mq;

import lombok.Data;
import lombok.ToString;

/**
 * @author k.y
 * @version Id: RocketMqProp.java, v 0.1 2018年11月22日 下午15:42 k.y Exp $
 */
@Data
@ToString(callSuper = true)
public class RocketMqProp {

    private String addr;

    private String group;

    private String topic;

    /**
     * 消息监听器
     */
    private String listener;

}
