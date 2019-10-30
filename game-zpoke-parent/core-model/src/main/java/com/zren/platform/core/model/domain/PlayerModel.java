package com.zren.platform.core.model.domain;

import lombok.Data;

import java.math.BigDecimal;


/**
 * Player Model
 *
 * @author k.y
 * @version Id: PlayerModel.java, v 0.1 2019年10月01日 下午17:04 k.y Exp $
 */
@Data
public class PlayerModel extends Model {

    private int[] cards;
    private BigDecimal callBet;
    private Integer actionSeat;
    private Integer currentSeat;
    private BigDecimal maxRoundBet;
}