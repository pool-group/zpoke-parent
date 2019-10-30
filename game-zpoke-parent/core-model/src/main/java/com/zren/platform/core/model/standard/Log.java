package com.zren.platform.core.model.standard;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * Log
 *
 * @author k.y
 * @version Id: Log.java, v 0.1 2019年10月18日 下午15:36 k.y Exp $
 */
public abstract class Log {


    /**
     * System health monitoring log
     */
    @Slf4j(topic = "healthy_monitor")
    public static class HEALTHY { public final static Logger LOG      =     log;}
}