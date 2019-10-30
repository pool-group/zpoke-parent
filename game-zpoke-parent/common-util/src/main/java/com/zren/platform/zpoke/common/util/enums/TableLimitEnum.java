package com.zren.platform.zpoke.common.util.enums;

/**
 * Table Limit Enum
 *
 * @author k.y
 * @version Id: TableLimitEnum.java, v 0.1 2019年10月02日 下午09:49 k.y Exp $
 */
public enum TableLimitEnum {

    ALLOW_NUMBER(0),
    MIN_NUMBER(4),
    MAX_NUMBER(9),
    MAX_OPERATE_TIME(20000);

    private int number;

    TableLimitEnum(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
