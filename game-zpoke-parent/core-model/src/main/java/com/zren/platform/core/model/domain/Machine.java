package com.zren.platform.core.model.domain;

import com.zren.platform.core.model.context.BaseSerializable;
import lombok.Data;

/**
 * Machine
 *
 * @author k.y
 * @version Id: Machine.java, v 0.1 2019年10月02日 下午18:49 k.y Exp $
 */
@Data
public class Machine extends BaseSerializable {

    private String brand;
    private Integer gameId=12;
    private Integer roomId;
}