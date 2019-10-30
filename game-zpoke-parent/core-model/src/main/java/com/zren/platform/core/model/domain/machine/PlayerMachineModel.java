package com.zren.platform.core.model.domain.machine;

import com.google.common.collect.Lists;
import com.zren.platform.core.model.enums.CardMouldEnum;
import com.zren.platform.zpoke.common.util.enums.PlayActionEnum;
import com.zren.platform.zpoke.common.util.enums.UserTypeEnum;
import com.zren.platform.core.model.domain.Machine;
import com.zren.platform.core.model.enums.CardFirstEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Player Model
 *
 * @author k.y
 * @version Id: PlayerModel.java, v 0.1 2019年10月02日 下午14:21 k.y Exp $
 */
@Data
public class PlayerMachineModel extends Machine {

    private Long userId;
    private String userName;
    private Byte features=1;
    private Long batchId;
    private Integer headUrl;
    private BigDecimal balance;
    private Integer seat;
    private PlayActionEnum playActionEnum=PlayActionEnum.JOIN;
    private BigDecimal roundBet=BigDecimal.ZERO;
    private BigDecimal totalBet=BigDecimal.ZERO;
    private UserTypeEnum userTypeEnum;
    private Integer robotry;
    private int[] cards;
    private CardFirstEnum cardFirstEnum;
    private CardMouldEnum optimalCardType;
    private int[] optimalCards;
    private int weight;
    private Long actionTime;
    private LinkedList<Integer> effective= Lists.newLinkedList();
}