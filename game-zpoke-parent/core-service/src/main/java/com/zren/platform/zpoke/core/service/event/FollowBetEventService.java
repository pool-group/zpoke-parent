package com.zren.platform.zpoke.core.service.event;

import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.domain.client.FollowBetClientModel;
import com.zren.platform.core.model.domain.machine.TableMachineModel;
import com.zren.platform.core.model.domain.server.FollowBetServerModel;
import com.zren.platform.core.model.enums.EventClientCode;
import com.zren.platform.core.model.enums.EventServeCode;
import com.zren.platform.zpoke.common.util.enums.PlayActionEnum;
import org.springframework.stereotype.Service;

/**
 * Follow Bet Event Service
 *
 * @author k.y
 * @version Id: FollowBetEventService.java, v 0.1 2019年10月21日 下午15:06 k.y Exp $
 */
@Service
public class FollowBetEventService extends BaseEventService {

    @Override
    public void invoke(Context context) {
        FollowBetClientModel model=new FollowBetClientModel();
        model.setCallBet(context.getNativeJson().getBigDecimal("callBet"));
        this.initModel(model,context);
        FollowBetServerModel followBetServerModel=new FollowBetServerModel();
        followBetServerModel.setCode(EventServeCode.FOLLOW.getCode());
        this.analysisProcess(model,followBetServerModel, PlayActionEnum.FOLLOW);
    }

    @Override
    public int getCode() {
        return EventClientCode.FOLLOW.getCode();
    }
}