package com.zren.platform.bout.common.match;

public class BaseTable {

	private String gameId;
	
	private String roomId;
	
	private String tableId;
	
	private String brand;
	
	/**
	 * @return the gameId
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * @param gameId the gameId to set
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	/**
	 * @return the roomId
	 */
	public String getRoomId() {
		return roomId;
	}

	/**
	 * @param roomId the roomId to set
	 */
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	/**
	 * @return the tableId
	 */
	public String getTableId() {
		return tableId;
	}

	/**
	 * @param tableId the tableId to set
	 */
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	public BaseTable(String gameId,String roomId,String tableId,String brand) {
		this.gameId=gameId;
		this.roomId=roomId;
		this.tableId=tableId;
		this.brand=brand;
	}
	
	public static BaseTable wrap(String gameId,String roomId,String tableId,String brand) {
		return new BaseTable(gameId,roomId,tableId,brand);
	}
	
	
}
