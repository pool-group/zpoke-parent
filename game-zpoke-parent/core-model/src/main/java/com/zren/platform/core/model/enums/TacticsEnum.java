package com.zren.platform.core.model.enums;

/**
 * Tactics Enum
 *
 * @author k.y
 * @version Id: TacticsEnum.java, v 0.1 2019年10月18日 下午18:32 k.y Exp $
 */
public enum TacticsEnum {

    MIN(-1),
    LOSS(0),
    GAIN(1),
    RANDOM(2),
    SEC_MAX(3);

    private int code;

    public int getCode() {
        return code;
    }

    TacticsEnum(Integer code) {
        this.code=code;
    }
}
