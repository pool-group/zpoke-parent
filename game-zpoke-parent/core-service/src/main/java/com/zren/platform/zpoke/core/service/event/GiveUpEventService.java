package com.zren.platform.zpoke.core.service.event;

import com.zren.platform.zpoke.common.util.enums.PlayActionEnum;
import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.domain.client.GiveUpClientModel;
import com.zren.platform.core.model.domain.machine.TableMachineModel;
import com.zren.platform.core.model.domain.server.GiveUpServerModel;
import com.zren.platform.core.model.enums.EventClientCode;
import com.zren.platform.core.model.enums.EventServeCode;
import org.springframework.stereotype.Service;

/**
 * Give Up Event Service Impl
 *
 * @author k.y
 * @version Id: GiveUpEventServiceImpl.java, v 0.1 2019年09月30日 下午17:20 k.y Exp $
 */
@Service
public class GiveUpEventService extends BaseEventService {

    @Override
    public void invoke(Context context) {
        GiveUpClientModel model=new GiveUpClientModel();
        this.initModel(model,context);
        GiveUpServerModel giveUpServerModel=new GiveUpServerModel();
        giveUpServerModel.setCode(EventServeCode.GIVE_UP.getCode());
        this.analysisProcess(model,giveUpServerModel,PlayActionEnum.GIVE_UP);
    }

    @Override
    public int getCode() {
        return EventClientCode.GIVE_UP.getCode();
    }
}