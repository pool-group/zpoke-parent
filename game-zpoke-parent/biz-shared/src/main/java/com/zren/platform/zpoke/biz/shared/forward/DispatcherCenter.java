package com.zren.platform.zpoke.biz.shared.forward;

import com.assembly.common.util.ApplicationContextUtil;
import com.assembly.common.util.LogUtil;
import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.standard.Dispatcher;
import com.zren.platform.core.model.standard.Event;
import com.zren.platform.zpoke.common.util.exception.IgnoreRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * Message Dispatching Center
 *
 * @author k.y
 * @version Id: EventDispatcher.java, v 0.1 2019年09月30日 下午17:37 k.y Exp $
 */
@RequiredArgsConstructor
@Component
public class DispatcherCenter implements Dispatcher {

    private final ApplicationContextUtil applicationContextUtil;

    @Override
    public void excute(Context context) {
        Map<String, Event> map = applicationContextUtil.getInstance().getBeansOfType(Event.class);
        try {

            map.forEach((key, event) -> {
                if(event.getCode()==context.getCode()){

                    synchronized (this) {
                        event.invoke(context);
                    }
                }
            });
        } catch (IgnoreRuntimeException e) {}
    }

}