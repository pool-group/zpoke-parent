package com.zren.platform.core.model.domain.server;

import com.zren.platform.zpoke.common.util.enums.PlayActionEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Player Server Model
 *
 * @author k.y
 * @version Id: PlayerServerModel.java, v 0.1 2019年10月03日 下午16:38 k.y Exp $
 */
@Data
public class PlayerServerModel {

    private Long userId;
    private String userName;
    private String brand;
    private Integer gameId=12;
    private Integer roomId;
    private Integer headUrl;
    private BigDecimal balance;
    private Integer seat;
    private PlayActionEnum playAction=PlayActionEnum.JOIN;
    private int roundBet;
    private int totalBet;
}