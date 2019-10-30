package com.zren.platform.zpoke.core.service.event;

import com.alibaba.fastjson.JSON;
import com.assembly.common.util.DataUtil;
import com.assembly.common.util.LogUtil;
import com.google.common.collect.Lists;
import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.domain.client.PlayerClientModel;
import com.zren.platform.core.model.domain.machine.TableMachineModel;
import com.zren.platform.core.model.domain.server.NextRoundServerModel;
import com.zren.platform.core.model.enums.EventClientCode;
import com.zren.platform.core.model.factory.ZpokeCardFactory;
import com.zren.platform.zpoke.common.util.enums.UserTypeEnum;
import com.zren.platform.zpoke.common.util.tool.RedisCommon;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Next Round Event Service
 *
 * @author k.y
 * @version Id: NextRoundEventService.java, v 0.1 2019年10月25日 下午13:01 k.y Exp $
 */
@Service
public class NextRoundEventService extends BaseEventService {

    @Override
    public void invoke(Context context) {
        PlayerClientModel model=new PlayerClientModel();
        this.initModel(model,context);
        LogUtil.info(String.format("start invoke NextRoundEventService: [ %s ]",model));
        NextRoundServerModel nextRoundServerModel=new NextRoundServerModel();
        this.nextRoundProcess(model,nextRoundServerModel);

        this.tableMachineModel.getPlayerList().stream().filter(s->s.getUserTypeEnum().equals(UserTypeEnum.PLAYER)).forEach(v->{
            List<ZpokeCardFactory.CardBO> resultList= Lists.newArrayList();
            ZpokeCardFactory.findOneWeight(v.getCards(), this.tableMachineModel.getCommonCard(),resultList,null);
            nextRoundServerModel.setOptimalCardType(resultList.get(0).getOptimalCardType());
            nextRoundServerModel.setOptimalCards(resultList.get(0).findOptimalCards());
            v.setOptimalCards(nextRoundServerModel.getOptimalCards());
            v.setOptimalCardType(nextRoundServerModel.getOptimalCardType());
            v.setWeight(resultList.get(0).getWeight());
            //待确认
//            nextRoundServerModel.setMainPool();
            this.hashPut(RedisCommon.getTablePlayerKey(model.getBrand(),model.getGameId(),model.getRoomId()),model.getTableNo(), JSON.toJSONString(this.tableMachineModel));
            this.sendById(Lists.newArrayList(v.getUserId()),nextRoundServerModel,0L);
        });
        this.forward();
    }

    @Override
    public int getCode() {
        return EventClientCode.NEXT_ROUND.getCode();
    }
}