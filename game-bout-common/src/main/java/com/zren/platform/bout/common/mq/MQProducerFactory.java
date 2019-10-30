package com.zren.platform.bout.common.mq;

/**
 *@author gavin
 *@since 2019年1月4日 上午11:39:39
*/

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MQProducerFactory {

	@Autowired
	private final CommonMQConfig commonMQConfig;

	private Map<String,DefaultMQProducer> pool = new HashMap<>();
	
	@PostConstruct
	public void wrap() {
		Iterator<Entry<String, Map<String,String>>> iterator = commonMQConfig.getItems().entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String,Map<String,String>> entry = iterator.next();
			pool.put(entry.getKey(), initProducer(entry.getValue().get("producerGroup"),entry.getValue().get("namesrvAddr")));
		}
	}
	
	private DefaultMQProducer initProducer(String groupName,String namesrvAddr) {
		
		 DefaultMQProducer producer;
	        producer = new DefaultMQProducer(groupName);
	        producer.setNamesrvAddr(namesrvAddr);
	        try {
	            producer.start();
	          
	        } catch (MQClientException e) {
	            e.printStackTrace();
	        }
            return producer;		
		
	}
	
	public DefaultMQProducer getProducer(String key) {
		
		return pool.get(key);
	}
}
