package com.zren.platform.zpoke.core.service.event;

import com.google.common.collect.Lists;
import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.domain.client.PlayerClientModel;
import com.zren.platform.core.model.domain.machine.PlayerMachineModel;
import com.zren.platform.core.model.domain.server.SettlementServerModel;
import com.zren.platform.core.model.enums.EventClientCode;
import com.zren.platform.core.model.enums.EventServeCode;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Settlement Event Service
 *
 * @author k.y
 * @version Id: SettlementEventService.java, v 0.1 2019年10月26日 下午16:27 k.y Exp $
 */
@Service
public class SettlementEventService extends BaseEventService {

    @Override
    public void invoke(Context context) {
        PlayerClientModel model=new PlayerClientModel();
        this.initModel(model,context);

        SettlementServerModel serverModel=new SettlementServerModel();
        serverModel.setCode(EventServeCode.SETTLEMENT.getCode());
        serverModel.setTableNo(model.getTableNo());
        serverModel.setOffset(this.tableMachineModel.getOffset().getAndIncrement());

        int max=this.tableMachineModel.getPlayerList().stream().mapToInt(PlayerMachineModel::getWeight).max().getAsInt();
        List<PlayerMachineModel> result=this.tableMachineModel.getPlayerList().stream().filter(s->s.getWeight()==max).collect(Collectors.toList());



        //牌一样大，怎么处理



//        serverModel.setWinSeat();
//        serverModel.setWinCards();
//        serverModel.setSettlementInfo();
    }

    @Override
    public int getCode() {
        return EventClientCode.SETTLEMENT.getCode();
    }
}