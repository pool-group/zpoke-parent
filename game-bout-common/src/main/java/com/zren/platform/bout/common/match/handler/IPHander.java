package com.zren.platform.bout.common.match.handler;

import com.zren.platform.bout.common.match.BasePlayer;
import com.zren.platform.bout.common.match.BaseTableService;
import com.zren.platform.bout.common.match.MatchConfig;
import com.zren.platform.bout.common.match.TableDTO;

public class IPHander extends AbstractTableMatchHandler{

	public IPHander(BaseTableService baseTableService) {
		super(baseTableService);
	}

	@Override
	public TableDTO handle(String gameId, String roomId, MatchConfig config, BasePlayer player) {

		    TableDTO dto = this.baseTableService.availableTable(gameId, roomId,player.getBrand());
			if(this.baseTableService.getIpTimes(gameId,roomId,dto.getTableId(),player.getIp())<config.getIps()) {
			}else {
				this.baseTableService.putAvailableTable(gameId, roomId, dto.getTableId(),player.getBrand());
				dto.setBaseTable(this.baseTableService.createTable(gameId, roomId,player.getBrand()));
				dto.setNew(true);
			}
			return dto;
	}

}
