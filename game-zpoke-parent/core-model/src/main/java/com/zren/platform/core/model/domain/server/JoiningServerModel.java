package com.zren.platform.core.model.domain.server;

import com.zren.platform.zpoke.common.util.enums.TableLimitEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Join Server Model
 *
 * @author k.y
 * @version Id: JoinServerModel.java, v 0.1 2019年10月03日 下午20:00 k.y Exp $
 */
@Data
public class JoiningServerModel extends JoinServerModel {

    private int leftTime;
    private int maxTime= TableLimitEnum.MAX_OPERATE_TIME.getNumber();
    private Integer leaderSeat;
    private Integer currentSeat;
    private int[] commonCard;
    private BigDecimal totalChip;


}