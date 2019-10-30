package com.zren.platform.bout.common.match.handler;

import com.zren.platform.bout.common.match.BasePlayer;
import com.zren.platform.bout.common.match.BaseTable;
import com.zren.platform.bout.common.match.BaseTableService;
import com.zren.platform.bout.common.match.MatchConfig;
import com.zren.platform.bout.common.match.TableDTO;

/**
 * 桌台最大数量和最大人数匹配规则处理
 * @author gavin.lyu
 *
 */
public class TableAndPersonHandler extends AbstractTableMatchHandler{

	public TableAndPersonHandler(BaseTableService tableService) {
		super(tableService);
	}

	@Override
	public TableDTO handle(String gameId, String roomId, MatchConfig config, BasePlayer player) {
		TableDTO dto = new TableDTO();
		if((config.getMaxTable()>0&&this.baseTableService.getTableNum(gameId, roomId,player.getBrand())<config.getMaxTable())||(config.getMaxPerson()>0&&this.baseTableService.getPersonNum(gameId, roomId,player.getBrand())<config.getMaxPerson())) {
			BaseTable table = this.baseTableService.createTable(gameId, roomId,player.getBrand());
			dto.setNew(true);
			dto.setBaseTable(table);
			return dto;
		}
		
		if(this.getHandler()!=null) {
			return this.getHandler().handle(gameId, roomId, config, player);
		}
		
		
	    dto = this.baseTableService.availableTable(gameId, roomId,player.getBrand());
		return dto;
	}

}
