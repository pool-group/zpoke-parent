package com.zren.platform.zpoke.core.service.event;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.domain.PlayerModel;
import com.zren.platform.core.model.domain.client.GiveUpClientModel;
import com.zren.platform.core.model.domain.client.OvertimeClientModel;
import com.zren.platform.core.model.domain.client.PlayerClientModel;
import com.zren.platform.core.model.domain.machine.PlayerMachineModel;
import com.zren.platform.core.model.domain.machine.TableMachineModel;
import com.zren.platform.core.model.domain.server.QuitServerModel;
import com.zren.platform.core.model.enums.EventClientCode;
import com.zren.platform.core.model.enums.EventServeCode;
import com.zren.platform.zpoke.common.util.enums.PlayActionEnum;
import com.zren.platform.zpoke.common.util.enums.TableStatsEnum;
import com.zren.platform.zpoke.common.util.tool.RedisCommon;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Over time Event Service
 *
 * @author k.y
 * @version Id: KickoutEventService.java, v 0.1 2019年10月24日 下午11:42 k.y Exp $
 */
@Service
public class OvertimeEventService extends BaseEventService {

    @Override
    public void invoke(Context context) {
        OvertimeClientModel model=new OvertimeClientModel();
        model.setOffset(context.getNativeJson().getInteger("offset"));
        this.initModel(model,context);

        Optional<PlayerMachineModel> optional=this.tableMachineModel.getPlayerList().stream().filter(s->s.getUserId().equals(model.getUserId())).findFirst();
        if(this.tableMachineModel.getTableStatsEnum().equals(TableStatsEnum.STARTING)
                &&optional.get().getPlayActionEnum().equals(PlayActionEnum.THINKING)
                &&optional.get().getEffective().contains(model.getOffset())){
            GiveUpClientModel giveUpClientModel=new GiveUpClientModel();
            giveUpClientModel.setCode(EventClientCode.GIVE_UP.getCode());
            giveUpClientModel.setTableNo(model.getTableNo());
            giveUpClientModel.setUserId(model.getUserId());
            giveUpClientModel.setRoomId(model.getRoomId());
            this.handleDelayQueue.addHandleEvent(JSON.toJSONString(giveUpClientModel),0L);
        }else if(this.tableMachineModel.getTableStatsEnum().equals(TableStatsEnum.WAIT_START)){
            PlayerModel kickOutServerModel=new PlayerModel();
            kickOutServerModel.setCode(EventServeCode.KICK_OUT.getCode());
            kickOutServerModel.setTableNo(model.getTableNo());
            kickOutServerModel.setOffset(this.tableMachineModel.getOffset().getAndIncrement());
            this.sendById(Lists.newArrayList(model.getUserId()),kickOutServerModel,0L);
            QuitServerModel quitServerModel=new QuitServerModel();
            quitServerModel.setCode(EventServeCode.QUIT.getCode());
            quitServerModel.setTableNo(model.getTableNo());
            quitServerModel.setOffset(this.tableMachineModel.getOffset().getAndIncrement());
            quitServerModel.setSeat(this.tableMachineModel.getCurrentSeat());
            this.send(this.findOtherPlayer(model.getUserId()),quitServerModel,0L);
        }
        this.hashPut(RedisCommon.getTablePlayerKey(model.getBrand(),model.getGameId(),model.getRoomId()),model.getTableNo(), JSON.toJSONString(tableMachineModel));
    }

    @Override
    public int getCode() {
        return EventClientCode.OVER_TIME.getCode();
    }
}