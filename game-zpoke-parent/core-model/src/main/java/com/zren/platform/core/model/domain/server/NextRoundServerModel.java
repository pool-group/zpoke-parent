package com.zren.platform.core.model.domain.server;

import com.zren.platform.core.model.domain.PlayerModel;
import com.zren.platform.core.model.enums.CardMouldEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Next Round Server Model
 *
 * @author k.y
 * @version Id: NextRoundServerModel.java, v 0.1 2019年10月25日 下午13:06 k.y Exp $
 */
@Data
public class NextRoundServerModel extends PlayerModel {

    private CardMouldEnum optimalCardType;
    private int[] optimalCards;
    private BigDecimal mainPool=BigDecimal.ZERO;
}