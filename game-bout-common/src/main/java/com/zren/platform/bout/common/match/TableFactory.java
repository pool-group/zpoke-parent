package com.zren.platform.bout.common.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.zren.platform.bout.common.error.BizErrors;
import com.zren.platform.bout.common.match.handler.AbstractTableMatchHandler;
import com.zren.platform.bout.common.match.handler.IPHander;
import com.zren.platform.bout.common.match.handler.PlayerOnlineHandler;
import com.zren.platform.bout.common.match.handler.TableAndPersonHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TableFactory {
	
	@Value("${game.id}")
	private String gameId;

	@Autowired
	private MatchConfig matchConfig;

	@Autowired
	private BaseTableService tableService;

	@Autowired
	private GameTableHook gameTableHook;

	public TableDTO matchTable(String gameId, String roomId, BasePlayer player) {

		AbstractTableMatchHandler playerOnline = new PlayerOnlineHandler(tableService);
		AbstractTableMatchHandler tpHandler = new TableAndPersonHandler(tableService);
		AbstractTableMatchHandler ipHandler = new IPHander(tableService);

		playerOnline.setNextHandler(tpHandler);
		tpHandler.setNextHandler(ipHandler);
		TableDTO table = playerOnline.handle(gameId, roomId, matchConfig, player);
		if (table == null) {
			throw BizErrors.E4000;
		}
		return table;

	}
	
	/**
	 * 
	
	 * @Title: matchTable
	
	 * @Description: 换桌匹配
	
	 * @param gameId
	 * @param roomId
	 * @param player
	 * @param orignalTableId
	 * @return
	
	 * @return: TableDTO
	 */
	public TableDTO matchTable(String gameId,String roomId,BasePlayer player,String orignalTableId) {
		AbstractTableMatchHandler playerOnline = new PlayerOnlineHandler(tableService);
		AbstractTableMatchHandler tpHandler = new TableAndPersonHandler(tableService);
		AbstractTableMatchHandler ipHandler = new IPHander(tableService);

		playerOnline.setNextHandler(tpHandler);
		tpHandler.setNextHandler(ipHandler);
		TableDTO table = playerOnline.handle(gameId, roomId, matchConfig, player);
		if (table == null) {
			throw BizErrors.E4000;
		}
		if(!table.isNew()) {
			if(table.getTableId().equals(orignalTableId)) {
				tableService.putAvailableTable(table.getGameId(), table.getRoomId(), table.getTableId(), table.getBrand());
				table.setBaseTable(tableService.createTable(gameId, roomId,player.getBrand()));
				table.setNew(true);
			}
		}
		return table;
		
	}

	/**
	 * 机器人加入桌台一定要调用此方法
	 * 
	 * @param table
	 * @param personNum
	 */
	public void joinTable(BaseTable table,BasePlayer player) {
		long seats = tableService.getTableSeats(gameId, table.getRoomId(), table.getTableId());
		if (matchConfig.getSeatNum()-seats < 1) {
			throw BizErrors.E4001;
		}
        tableService.setTableSeats(gameId, table.getRoomId(), table.getTableId(), 1);
        if(player.getIp()!=null&&!"".equals(player.getIp())) {
        	tableService.saveIP(gameId, table.getRoomId(), table.getTableId(),player.getIp());
        }
	}
	
	
	public void releaseTable(BaseTable table) {
		long seats = tableService.getTableSeats(table.getGameId(), table.getRoomId(), table.getTableId());
		if(seats<1) {
			tableService.delTable(table);
		}else if(seats < matchConfig.getSeatNum()) {
	        tableService.putAvailableTable(gameId, table.getRoomId(), table.getTableId(), table.getBrand());
		}
	}
	

	/**
	 * 玩家离开房间
	 * 
	 * @param gameId
	 * @param roomId
	 * @param tableId
	 * @param ip      如果是机器人，ip可以为空
	 */
	public void leaveTable(BaseTable table, BasePlayer player) {
		try {
			tableService.delPlayerOnlineTableId(gameId, table.getRoomId(), player.getPlayerId(), table.getBrand());
                tableService.fetchTableById(gameId, table.getRoomId(), table.getTableId(), table.getBrand());
				long seats = tableService.setTableSeats(gameId, table.getRoomId(), table.getTableId(), -1);
				if(player.getIp()!=null) {
					tableService.removeIP(gameId, table.getRoomId(), table.getTableId(), player.getIp());
				}
				// 桌台没有玩家的情况下，销毁
				if (seats == 0) {
					tableService.delTable(table);
					gameTableHook.destroyTable(gameId, table.getRoomId(),table.getTableId(),table.getBrand());
				}else if(seats < matchConfig.getSeatNum()) {
			        tableService.putAvailableTable(gameId, table.getRoomId(), table.getTableId(), table.getBrand());
				}
		} catch (Throwable e) {
			throw e;
		}
	}

}
