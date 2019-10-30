package com.zren.platform.core.model.domain.server;

import com.zren.platform.core.model.domain.Model;
import com.zren.platform.zpoke.common.util.enums.TableLimitEnum;
import com.zren.platform.core.model.enums.CardFirstEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Start Server Model
 *
 * @author k.y
 * @version Id: StartServerModel.java, v 0.1 2019年10月03日 下午16:39 k.y Exp $
 */
@Data
public class StartServerModel extends Model {

    private Integer leaderSeat;
    private Integer currentSeat;
    private Integer bigSeat;
    private Integer smallSeat;
    private BigDecimal bigBet;
    private BigDecimal smallBet;
    private CardFirstEnum firstCardType;
    private int[] firstCard;
    private int maxTime= TableLimitEnum.MAX_OPERATE_TIME.getNumber();
}