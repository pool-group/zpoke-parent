package com.zren.platform.zpoke.core.service.event;

import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.enums.EventClientCode;
import org.springframework.stereotype.Service;

/**
 * Join Event Service Impl
 *
 * @author k.y
 * @version Id: JoinEventServiceImpl.java, v 0.1 2019年09月30日 下午17:16 k.y Exp $
 */
@Service
public class JoinEventService extends BaseEventService {

    @Override
    public void invoke(Context context) {

    }

    @Override
    public int getCode() {
        return EventClientCode.JOIN.getCode();
    }
}