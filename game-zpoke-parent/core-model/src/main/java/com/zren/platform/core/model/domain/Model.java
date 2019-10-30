package com.zren.platform.core.model.domain;

import com.zren.platform.core.model.context.BaseSerializable;
import lombok.Data;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Domain model abstraction
 *
 * @author k.y
 * @version Id: Model.java, v 0.1 2019年09月30日 下午18:19 k.y Exp $
 */
@Data
public abstract class Model extends BaseSerializable {

    private String sid;
    private int code;
    private String tableNo;
    private int offset=1;
}