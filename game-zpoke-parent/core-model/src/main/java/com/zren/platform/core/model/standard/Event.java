package com.zren.platform.core.model.standard;

import com.zren.platform.core.model.context.Context;

/**
 * Event Service
 *
 * @author k.y
 * @version Id: EventService.java, v 0.1 2019年09月30日 下午17:05 k.y Exp $
 */
public interface Event {

    void invoke(Context context);

    int getCode();

}