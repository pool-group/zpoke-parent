package com.zren.platform.bout.common.match.handler;

import com.zren.platform.bout.common.match.BasePlayer;
import com.zren.platform.bout.common.match.BaseTable;
import com.zren.platform.bout.common.match.BaseTableService;
import com.zren.platform.bout.common.match.MatchConfig;
import com.zren.platform.bout.common.match.TableDTO;

public class PlayerOnlineHandler extends AbstractTableMatchHandler{

	public PlayerOnlineHandler(BaseTableService baseTableService) {
		super(baseTableService);
	}

	@Override
	public TableDTO handle(String gameId, String roomId, MatchConfig config, BasePlayer player) {
		TableDTO dto = new TableDTO();
		String tableId = this.baseTableService.getPlayerOnlineTableId(gameId, roomId, player.getPlayerId(),player.getBrand());
		if(tableId!=null) {
			//this.baseTableService.fetchTableById(gameId, roomId, tableId);
			BaseTable table = this.baseTableService.getTable(gameId, roomId, tableId,player.getBrand());
			if(table==null) {
				dto.setNew(true);
				table = this.baseTableService.createTable(gameId, roomId,player.getBrand());
			}
			dto.setBaseTable(table);
			return dto;
		}else {
			if(this.getHandler()!=null) {
				return this.getHandler().handle(gameId, roomId, config, player);
			}
		}
		return null;
	}

}
