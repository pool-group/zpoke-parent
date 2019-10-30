package com.zren.platform.zpoke.core.service.event;

import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.enums.EventClientCode;
import org.springframework.stereotype.Service;

/**
 * Current Event Service Impl
 *
 * @author k.y
 * @version Id: CurrentEventServiceImpl.java, v 0.1 2019年09月30日 下午17:04 k.y Exp $
 */
@Service
public class CurrentEventService extends BaseEventService {

    @Override
    public void invoke(Context context) {

    }

    @Override
    public int getCode() {
        return EventClientCode.CURRENT.getCode();
    }
}