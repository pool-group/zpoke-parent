package com.zren.platform.common.service.integration.entity;

import com.jxinternet.platform.service.balance.game.entity.UserBalance;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * User Balance Entity
 *
 * @author k.y
 * @version Id: UserBalanceEntity.java, v 0.1 2019年10月01日 下午16:22 k.y Exp $
 */
@Data
public class UserBalanceEntity {
    private Long accountId;
    private Long clientId;
    private Long agentId;
    private Long userId;
    private String username;
    private BigDecimal balance;
    private BigDecimal aggregateBonus;
    private BigDecimal aggregateBetRequirements;
    private UserBalance.AccountStateEnum state;
    private int version;
    private int topupCount;
    private BigDecimal topupAggregateAmount;
    private BigDecimal topupMaxOnetimeAmount;
    private int withdrawCount;
    private BigDecimal withdrawAggregateAmount;
    private BigDecimal aggregateTransferIn;
    private BigDecimal aggregateTransferOut;
    private BigDecimal aggregateBets;
    private BigDecimal aggregateWins;
    private BigDecimal aggregateRebates;
    private BigDecimal aggregateFees;
    private BigDecimal aggregateCommissions;
    private BigDecimal teamBalance;
    private Instant createdTime;
    private Instant updatedTime;
}