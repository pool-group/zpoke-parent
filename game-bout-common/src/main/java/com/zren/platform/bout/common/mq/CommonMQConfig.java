package com.zren.platform.bout.common.mq;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 *@author gavin
 *@since 2019年1月4日 下午3:39:43
*/
@ConfigurationProperties(prefix="commonmq")
@Data
public class CommonMQConfig {

	private Map<String,Map<String,String>> items;
	
	
}
