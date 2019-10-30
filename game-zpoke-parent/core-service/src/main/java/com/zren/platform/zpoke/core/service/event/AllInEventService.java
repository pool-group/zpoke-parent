package com.zren.platform.zpoke.core.service.event;

import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.domain.PlayerModel;
import com.zren.platform.core.model.domain.client.PlayerClientModel;
import com.zren.platform.core.model.domain.machine.PlayerMachineModel;
import com.zren.platform.core.model.enums.EventClientCode;
import com.zren.platform.core.model.enums.EventServeCode;
import com.zren.platform.zpoke.common.util.enums.PlayActionEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * AllIn Event Service
 *
 * @author k.y
 * @version Id: AllInEventService.java, v 0.1 2019年10月26日 下午20:27 k.y Exp $
 */
@Service
public class AllInEventService extends BaseEventService {

    @Override
    public void invoke(Context context) {
        PlayerClientModel model=new PlayerClientModel();
        model.setCallBet(context.getNativeJson().getBigDecimal("callBet"));
        this.initModel(model,context);
        PlayerModel playerModel=new PlayerModel();
        playerModel.setCode(EventServeCode.ALL_IN.getCode());
        PlayerMachineModel play=tableMachineModel.getPlayerList().stream().filter(s->s.getUserId().equals(model.getUserId())).findFirst().get();
        BigDecimal maxRoundBet=this.findMaxRoundBet();
        BigDecimal shouldBet=maxRoundBet.subtract(play.getRoundBet());
        this.analysisProcess(model,playerModel, this.findAvailableBalance(play).compareTo(shouldBet)==-1?PlayActionEnum.FOLLOW:PlayActionEnum.ADD);
    }

    @Override
    public int getCode() {
        return EventClientCode.ALL_IN.getCode();
    }
}