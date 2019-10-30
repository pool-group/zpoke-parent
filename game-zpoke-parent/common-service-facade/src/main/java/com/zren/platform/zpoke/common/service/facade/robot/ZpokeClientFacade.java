package com.zren.platform.zpoke.common.service.facade.robot;

import com.assembly.template.engine.result.IBaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Zpoke Robot Client Facade
 *
 * @author k.y
 * @version Id: ZpokeRobotClientFacade.java, v 0.1 2019年10月22日 下午11:08 k.y Exp $
 */
@FeignClient(value = "game-zpoke-app")
public interface ZpokeClientFacade {

    @PostMapping("/zpokeClientFacade/invoke")
    IBaseResult invoke(@RequestParam(value="message",required=false) String message);
}
