package com.zren.platform.zpoke.core.service.event;

import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.domain.PlayerModel;
import com.zren.platform.core.model.domain.client.PlayerClientModel;
import com.zren.platform.core.model.enums.EventClientCode;
import com.zren.platform.core.model.enums.EventServeCode;
import com.zren.platform.zpoke.common.util.enums.PlayActionEnum;
import org.springframework.stereotype.Service;

/**
 * Pass Event Service
 *
 * @author k.y
 * @version Id: PassEventService.java, v 0.1 2019年10月25日 下午16:24 k.y Exp $
 */
@Service
public class PassEventService extends BaseEventService {

    @Override
    public void invoke(Context context) {
        PlayerClientModel model=new PlayerClientModel();
        this.initModel(model,context);
        PlayerModel playerModel=new PlayerModel();
        playerModel.setCode(EventServeCode.PASS.getCode());
        this.analysisProcess(model,playerModel, PlayActionEnum.PASS);
    }

    @Override
    public int getCode() {
        return EventClientCode.PASS.getCode();
    }
}