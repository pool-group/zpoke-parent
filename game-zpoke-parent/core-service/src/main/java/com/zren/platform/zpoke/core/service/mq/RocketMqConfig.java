package com.zren.platform.zpoke.core.service.mq;

/**
 * @author k.y
 * @version Id: RocketMqConfig.java, v 0.1 2018年11月22日 下午15:41 k.y Exp $
 */

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@ConfigurationProperties(prefix = "com.zren.rocket")
@Component
public class RocketMqConfig {

    private Map<String, RocketMqProp> consumer = new HashMap<>();

    private Map<String, RocketMqProp> producer = new HashMap<>();

}
