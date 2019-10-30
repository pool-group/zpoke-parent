package com.zren.platform.zpoke.common.service.integration.entity;

import io.swagger.models.auth.In;
import lombok.Data;

import java.math.BigDecimal;

/**
 * RobotInfo Entity
 *
 * @author k.y
 * @version Id: RobotInfoEntity.java, v 0.1 2019年10月02日 下午11:05 k.y Exp $
 */
@Data
public class RobotInfoEntity {

    private Integer id;
    private Long userId;
    private Byte features=1;
    private Long batchId;
    private String userName;
    private String brand;
    private Integer headUrl;
    private String status;
    private BigDecimal balance;
}