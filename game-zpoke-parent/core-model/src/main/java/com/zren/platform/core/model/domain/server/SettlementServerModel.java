package com.zren.platform.core.model.domain.server;

import com.zren.platform.core.model.domain.PlayerModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Settlement Server Model
 *
 * @author k.y
 * @version Id: SettlementServerModel.java, v 0.1 2019年10月26日 下午16:49 k.y Exp $
 */
@Data
public class SettlementServerModel extends PlayerModel {

    private int winSeat;
    private int[] winCards;
    private List<SettlementEntity> settlementInfo;
//    private BigDecimal poolAccount;

    @Data
    class SettlementEntity{
        private int seat;
        private int[] firstCard;
        private BigDecimal accountChange;

    }

}