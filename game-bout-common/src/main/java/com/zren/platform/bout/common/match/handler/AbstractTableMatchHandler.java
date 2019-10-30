package com.zren.platform.bout.common.match.handler;

import com.zren.platform.bout.common.match.BasePlayer;
import com.zren.platform.bout.common.match.BaseTableService;
import com.zren.platform.bout.common.match.MatchConfig;
import com.zren.platform.bout.common.match.TableDTO;

public abstract class AbstractTableMatchHandler {

	
    private AbstractTableMatchHandler handler;
    
    protected BaseTableService baseTableService;
	
	public AbstractTableMatchHandler(BaseTableService baseTableService) {
		this.baseTableService=baseTableService;
	}
    
	
	protected AbstractTableMatchHandler getHandler() {
		return this.handler;
	}
    
    
    public void setNextHandler(AbstractTableMatchHandler handler) {
    	this.handler = handler;
    }
	
	public abstract TableDTO handle(String gameId,String roomId,MatchConfig config,BasePlayer player);
	
	
}
