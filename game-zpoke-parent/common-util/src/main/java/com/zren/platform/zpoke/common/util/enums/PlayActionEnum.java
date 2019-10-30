package com.zren.platform.zpoke.common.util.enums;

/**
 * Play Action Enum
 *
 * @author k.y
 * @version Id: PlayActionEnum.java, v 0.1 2019年10月02日 下午15:17 k.y Exp $
 */
public enum PlayActionEnum {

    JOIN(1200,"加入桌台"),
    LOOK_ON(1201,"旁观"),
    READY(1202,"准备"),
    FOLLOW(1203,"跟注"),
    ADD(1204,"加注"),
    PASS(1205,"让牌"),
    GIVE_UP(1206,"弃牌"),
    FOLLOW_ANY(1207,"跟任何注"),
    WAITING(1208,"等待中"),
    PASS_GIVE_UP(1209,"让或弃牌"),
    ALL_IN(1210,"ALL IN"),
    VS(1211,"比牌"),
    THINKING(1212,"思考中"),
    DEPOSIT_CARD(1213,"牌局托管");

    private int code;
    private String desc;

    PlayActionEnum(int code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
}
