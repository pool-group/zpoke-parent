package com.zren.platform.zpoke.core.service.event;

import com.zren.platform.core.model.context.Context;
import org.springframework.stereotype.Service;

/**
 * Ready Event Service
 *
 * @author k.y
 * @version Id: ReadyEventService.java, v 0.1 2019年10月26日 下午17:53 k.y Exp $
 */
@Service
public class ReadyEventService extends BaseEventService {

    @Override
    public void invoke(Context context) {

    }

    @Override
    public int getCode() {
        return 0;
    }
}