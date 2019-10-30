package com.zren.platform.bout.common.util;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @program: yz-game-parent
 * @description:
 * @author: Mr.Mao
 * @create: 2019-09-17 11:26
 */
public class BusinessUtil {

    /**
     * @param tableId 房间id
     * @param gameId 游戏id
     * @param broundId
     * @param roundId
     * @param roomId
     * @return
     */
    public static String buildCrossReferenceId(String tableId, String gameId, String broundId, String roundId, String roomId, RedisTemplate<String, Object> redis){
        StringBuilder sb = new StringBuilder().append(gameId).append("@")
                .append(roomId).append("@")
                .append(tableId).append("@")
                .append(broundId).append("@")
                .append(roundId).append("@");

        final String key = "gamelogic:"+sb;

        long count = 0;
        final Object obj = redis.opsForValue().get(key);
        if(obj == null){
            redis.opsForValue().set(key, 0);
            redis.expire(key, 600,TimeUnit.SECONDS);
        }else{
            count = redis.opsForValue().increment(key, 1);
        }
        return sb.append(count).toString();
    }


}
