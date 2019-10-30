package com.zren.platform.core.model.enums;

/**
 * Card Mould Enum
 *
 * @author k.y
 * @version Id: CardMouldEnum.java, v 0.1 2019年10月16日 下午16:06 k.y Exp $
 */
public enum CardMouldEnum {

    MAX_SAME_COLOR_LINK(100,"皇家同花顺"),
    SAME_COLOR_LINK(90,"同花顺"),
    FOUR_SAME_VALUE(80,"四条"),
    THREE_TWO_SAME_VALUE(70,"葫芦"),
    SAME_COLOR_UNLINK(60,"同花"),
    DIFFERENT_COLOR_LINK(50,"顺子"),
    THREE_SAME_VALUE(40,"三条"),
    TWO_SAME_VALUE(30,"两对"),
    ONE_SAME_VALUE(20,"一对"),
    SINGLE_VALUE(10,"高牌");

    private int weight;
    private String desc;

    CardMouldEnum(int weight,String desc) {
        this.weight = weight;
        this.desc = desc;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
