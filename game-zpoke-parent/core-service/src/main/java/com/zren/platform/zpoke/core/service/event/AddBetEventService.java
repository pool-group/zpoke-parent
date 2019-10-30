package com.zren.platform.zpoke.core.service.event;

import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.domain.PlayerModel;
import com.zren.platform.core.model.domain.client.PlayerClientModel;
import com.zren.platform.core.model.domain.machine.TableMachineModel;
import com.zren.platform.core.model.enums.EventClientCode;
import com.zren.platform.core.model.enums.EventServeCode;
import com.zren.platform.zpoke.common.util.enums.PlayActionEnum;
import org.springframework.stereotype.Service;

/**
 * Add Bet Event Service Impl
 *
 * @author k.y
 * @version Id: AddBetEventServiceImpl.java, v 0.1 2019年09月30日 下午17:19 k.y Exp $
 */
@Service
public class AddBetEventService extends BaseEventService {

    @Override
    public void invoke(Context context) {

        PlayerClientModel model=new PlayerClientModel();
        model.setCallBet(context.getNativeJson().getBigDecimal("callBet"));
        this.initModel(model,context);
        PlayerModel playerModel=new PlayerModel();
        playerModel.setCode(EventServeCode.ADD.getCode());
        this.analysisProcess(model,playerModel, PlayActionEnum.ADD);
    }

    @Override
    public int getCode() {
        return EventClientCode.ADD.getCode();
    }
}