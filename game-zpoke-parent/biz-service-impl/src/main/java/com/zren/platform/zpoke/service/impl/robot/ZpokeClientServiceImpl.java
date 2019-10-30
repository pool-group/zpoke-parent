package com.zren.platform.zpoke.service.impl.robot;

import com.alibaba.fastjson.JSON;
import com.assembly.template.engine.callback.AbstractOpCallback;
import com.assembly.template.engine.context.EngineContext;
import com.assembly.template.engine.result.IBaseResult;
import com.assembly.template.engine.template.impl.BizOpCenterServiceTemplate;
import com.zren.platform.zpoke.biz.shared.forward.DispatcherCenter;
import com.zren.platform.core.model.context.MessageContext;
import com.zren.platform.zpoke.common.service.facade.robot.ZpokeClientFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Zpoke Robot Client Service Impl
 *
 * @author k.y
 * @version Id: ZpokeRobotClientServiceImpl.java, v 0.1 2019年10月22日 下午11:26 k.y Exp $
 */
@RestController
public class ZpokeClientServiceImpl implements ZpokeClientFacade {

    @Autowired
    private BizOpCenterServiceTemplate bizOpCenterServiceTemplate;
    @Autowired
    private DispatcherCenter dispatcherCenter;

    @Override
    public IBaseResult invoke(String message) {
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<String,Void>(){

            @Override
            public void initContent(EngineContext<String, Void> context) {
                context.setInputModel(message);
            }

            @Override
            public void doProcess(EngineContext<String, Void> context) {
                dispatcherCenter.excute(new MessageContext(JSON.parseObject(message),""));
            }
        });
    }
}