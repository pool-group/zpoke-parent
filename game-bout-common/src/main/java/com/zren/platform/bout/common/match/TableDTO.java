package com.zren.platform.bout.common.match;


import lombok.Data;

@Data
public class TableDTO {

	private boolean isNew = false;
	
	private BaseTable baseTable;
	
	public String getTableId() {
		return baseTable.getTableId();
	}
	
	public String getGameId() {
		return baseTable.getGameId();
	}
	
	public String getRoomId() {
		return baseTable.getRoomId();
	}
	
	public String getBrand() {
		return baseTable.getBrand();
	}
}
