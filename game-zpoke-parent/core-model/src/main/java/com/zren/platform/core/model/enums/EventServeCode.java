package com.zren.platform.core.model.enums;

/**
 * Event Serve Code
 *
 * @author k.y
 * @version Id: EventServeCode.java, v 0.1 2019年10月01日 下午09:44 k.y Exp $
 */
public enum EventServeCode {

    MATCH_FAIL(12201),
    JOIN(12202),
    READY(12203),
    AUTO_READY(12204),
    START(12205),
    CURRENT(12206),
    GIVE_UP(12207),
    FOLLOW(12208),
    ADD(12209),
    PASS(12210),
    FOLLOW_ANY(12211),
    PASS_GIVE_UP(12212),
    ALL_IN(12213),
    VS(12214),
    MASTER_SLAVE(12215),
    ONE_CARD(12216),
    TWO_CARD(12217),
    THR_CARD(12218),
    FOUR_CARD(12219),
    JOIN_BROAD(12220),
    QUIT(12221),
    KICK_OUT(12222),
    SETTLEMENT(12223),
    NEXT_ROUND(12224),
    PUSH_PLAYER(12225)
    ;

    final int code;

    EventServeCode(int code) {
        this.code=code;
    }

    public int getCode(){
        return this.code;
    }
}
