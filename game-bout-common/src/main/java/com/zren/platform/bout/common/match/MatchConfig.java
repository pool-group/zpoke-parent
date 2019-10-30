package com.zren.platform.bout.common.match;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
@Component
@ConfigurationProperties(prefix = "match")
@Data
public class MatchConfig {

	
	private int maxPerson;//最大人数限制
	
	private int maxTable;//最大桌台数限制
	
	private int ips;//最大IP数量限制
	
	private int seatNum;//桌台座位数量
	
	private int tableCapcity;//最大桌台容量
	
}
