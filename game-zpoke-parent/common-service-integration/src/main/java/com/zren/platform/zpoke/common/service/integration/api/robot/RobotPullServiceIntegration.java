package com.zren.platform.zpoke.common.service.integration.api.robot;

import com.alibaba.fastjson.JSON;
import com.zren.platform.common.service.facade.api.robot.RobotPullManage;
import com.zren.platform.common.service.facade.dto.in.robotInfo.RobotInitInputModelDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.core.model.domain.machine.PlayerInfoMachineModel;
import com.zren.platform.zpoke.common.service.integration.cache.BaseCacheService;
import com.zren.platform.zpoke.common.service.integration.entity.RobotInfoEntity;
import com.zren.platform.zpoke.common.util.tool.RedisCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Robot Pull Service Integration
 *
 * @author k.y
 * @version Id: RobotPullServiceIntegration.java, v 0.1 2019年10月02日 下午10:41 k.y Exp $
 */
@Service
public class RobotPullServiceIntegration extends BaseCacheService {

    @Autowired
    RobotPullManage robotPullManage;

    public List<RobotInfoEntity> createRobot(RobotInitInputModelDTO dto){
        RobotBaseResult result=robotPullManage.createRobot(dto);
        List<RobotInfoEntity> resultList=null;
        if(result.isSuccess()){
            LinkedHashMap map= (LinkedHashMap)result.getResultObj();
            List<LinkedHashMap> roboList=(List<LinkedHashMap>)map.get("robotInfolst");
            resultList=JSON.parseArray(JSON.toJSONString(roboList),RobotInfoEntity.class);
        }
        resultList.stream().forEach(v->{
            PlayerInfoMachineModel playerInfoMachineModel=new PlayerInfoMachineModel();
            playerInfoMachineModel.setBrand(v.getBrand());
            playerInfoMachineModel.setRoomId(dto.getRoomId());
            playerInfoMachineModel.setUserId(v.getUserId());
            playerInfoMachineModel.setUsername(v.getUserName());
            playerInfoMachineModel.setTimestamp(System.currentTimeMillis());
            this.hashPut(RedisCommon.getPlayerInfoKey(), v.getUserId().toString(),JSON.toJSONString(playerInfoMachineModel));
        });
        return resultList;
    }
}