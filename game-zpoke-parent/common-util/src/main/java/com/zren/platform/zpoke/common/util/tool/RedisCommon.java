package com.zren.platform.zpoke.common.util.tool;

import java.text.MessageFormat;

/**
 * Redis Common
 *
 * @author k.y
 * @version Id: RedisCommon.java, v 0.1 2019年10月01日 下午15:27 k.y Exp $
 */
public class RedisCommon {

    public static String getPlayerInfoKey(){
        return "connect:playerInfoKey:info";
    }

    public static String getTablePlayerCountKey(String brand,Integer gameId,Integer roomId){
        return MessageFormat.format("{0}:{1}:{2}:tablePlayerCountKey",brand,gameId,roomId);
    }

    public static String getTablePlayerKey(String brand,Integer gameId,Integer roomId){
        return MessageFormat.format("{0}:{1}:{2}:tablePlayerKey",brand,gameId,roomId);
    }

    public static String getTablePlayerLockKey(String brand,Integer gameId,Integer roomId,String tableId){
        return MessageFormat.format("{0}:{1}:{2}:tablePlayerLockKey:{3}",brand,gameId,roomId,tableId);
    }
}