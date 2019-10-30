package com.zren.platform.zpoke.common.service.integration.api.robot;

import com.assembly.common.exception.BizRuntimeException;
import com.assembly.common.exception.SysRuntimeException;
import com.assembly.common.util.BeanUtil;
import com.assembly.common.util.LogUtil;
import com.zren.platform.common.service.facade.api.zpoke.ZpokeRobotClientFacade;
import com.zren.platform.common.service.facade.dto.in.zpoke.TableMachineDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.core.model.domain.Machine;
import com.zren.platform.core.model.domain.Model;
import com.zren.platform.core.model.domain.machine.TableMachineModel;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Zpoke Robot Client Integration
 *
 * @author k.y
 * @version Id: ZpokeRobotClientIntegration.java, v 0.1 2019年10月22日 下午13:49 k.y Exp $
 */
@Service
@RequiredArgsConstructor
public class ZpokeRobotClientIntegration {

    final private ZpokeRobotClientFacade zpokeRobotClientFacade;

    @Async
    public void invoke(TableMachineModel tableMachineModel){
        LogUtil.info(String.format(" zpokeRobotClientFacade.invoke(machine): machine=[ %s ]",tableMachineModel));
        try {

            TableMachineDTO tableMachineDTO=new TableMachineDTO();
            BeanUtil.copyPropertiesIgnoreNull(tableMachineModel,tableMachineDTO);
            tableMachineDTO.setSid(tableMachineModel.getTableId()+tableMachineModel.getOffset());
            tableMachineDTO.setRound(tableMachineModel.getRoundEnum());
            RobotBaseResult robotBaseResult=zpokeRobotClientFacade.invoke(tableMachineDTO);
            if(!robotBaseResult.isSuccess()){
                throw new BizRuntimeException(String.format(" zpokeRobotClientFacade.invoke() isSuccess=false, exception:[ %s ]",robotBaseResult.getErrorContext().fetchCurrentError().toString()));
            }
        } catch (Exception e) {
            throw new SysRuntimeException(" zpokeRobotClientFacade.invoke() is exception! ",e);
        }
    }
}