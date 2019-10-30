package com.zren.platform.core.model.enums;

/**
 * Event Client Code
 *
 * @author k.y
 * @version Id: EventClientCode.java, v 0.1 2019年10月01日 下午09:49 k.y Exp $
 */
public enum EventClientCode {

    CONNECT(1000),
    MATCH(12101),
    JOIN(12102),
    READY(12103),
    AUTO_READY(12104),
    START(12105),
    CURRENT(12106),
    GIVE_UP(12107),
    FOLLOW(12108),
    ADD(12109),
    PASS(12110),
    FOLLOW_ANY(12111),
    PASS_GIVE_UP(12112),
    ALL_IN(12113),
    VS(12114),
    MASTER_SLAVE(12115),
    ONE_CARD(12116),
    TWO_CARD(12117),
    THR_CARD(12118),
    FOUR_CARD(12119),
    DEPOSIT_CARD(12120),
    QUIT(12121),
    KICK_OUT(12122),
    OVER_TIME(12123),
    NEXT_ROUND(12124),
    SETTLEMENT(12125)
    ;

    final int code;

    EventClientCode(int code) {
        this.code=code;
    }

    public int getCode() {
        return code;
    }
}
