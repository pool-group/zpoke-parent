package com.zren.platform.core.model.enums;

import java.io.Serializable;
/**
 * Round Enum
 *
 * @author k.y
 * @version Id: RoundEnum.java, v 0.1 2019年10月21日 下午16:20 k.y Exp $
 */
public enum RoundEnum implements Serializable {

    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR;

    String round;

    public String getRound() {
        return round;
    }

    public RoundEnum next(){
        switch (this){
            case ONE:
                return RoundEnum.TWO;
            case TWO:
                return RoundEnum.THREE;
            case THREE:
                return RoundEnum.FOUR;
            case FOUR:
                return RoundEnum.ZERO;
        }
        return null;
    }

    public static final long serialVersionUID = 8835869623271753897L;
}
