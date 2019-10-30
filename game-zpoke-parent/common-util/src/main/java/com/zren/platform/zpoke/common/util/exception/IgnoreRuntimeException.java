package com.zren.platform.zpoke.common.util.exception;

/**
 * Ignore Runtime Exception
 *
 * @author k.y
 * @version Id: IgnoreRuntimeException.java, v 0.1 2019年10月25日 下午11:55 k.y Exp $
 */
public class IgnoreRuntimeException extends RuntimeException {

    public IgnoreRuntimeException() {
    }

    public IgnoreRuntimeException(String message) {
        super(message);
    }
}