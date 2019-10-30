package com.zren.platform.core.model.domain.client;

import com.zren.platform.core.model.domain.Model;
import com.zren.platform.zpoke.common.util.enums.UserTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author k.y
 * @version Id: PlayerClientModel.java, v 0.1 2019年10月21日 下午19:12 k.y Exp $
 */
@Data
public class PlayerClientModel extends Model {

    private String brand;
    private Integer gameId=12;
    private Integer roomId;
    private Long userId;
    private String userName;
    private BigDecimal callBet=BigDecimal.ZERO;
}