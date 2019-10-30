package com.zren.platform.zpoke.common.service.integration.cache;

import com.assembly.common.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Base Cache Service
 *
 * @author k.y
 * @version Id: BaseCacheService.java, v 0.1 2019年10月01日 下午15:12 k.y Exp $
 */
public abstract class BaseCacheService {

    @Autowired
    protected RedisTemplate redisTemplate;

    public boolean hashPut(String var1,String var2,String var3){
        try {
            redisTemplate.opsForHash().put(var1,var2,var3);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("hashPut is fail ! var1={0}, var2={1}, var3={2}",var1,var2,var3));
            throw e;
        }
        return true;
    }

    public Object hashFind(String var1,String var2){
        try {
            return redisTemplate.opsForHash().get(var1,var2);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("hashFind is fail ! var1={0}, var2={1}",var1,var2));
            throw e;
        }
    }

    public Long hashDelete(String var1,String var2){
        try {
            if(redisTemplate.opsForHash().hasKey(var1,var2)){
                return redisTemplate.opsForHash().delete(var1,var2);
            }
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("hashDelete is fail ! var1={0}, var2={1}",var1,var2));
            throw e;
        }
        return null;
    }

    public Long zSetDelete(String var1,String var2){
        try {
            if(null!=redisTemplate.opsForZSet().rank(var1,var2)){
                return redisTemplate.opsForZSet().remove(var1,var2);
            }
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("zSetDelete is fail ! var1={0}, var2={1}",var1,var2));
            throw e;
        }
        return null;
    }

    public boolean zSetPut(String var1,String var2,double var3){
        try {
            redisTemplate.opsForZSet().add(var1,var2,var3);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("zSetPut is fail ! var1={0}, var2={1}, var3={2}",var1,var2,var3));
            throw e;
        }
        return true;
    }

    public Set<String> zSetQuery(String var1, double var2, double var3){
        try {
            return redisTemplate.opsForZSet().rangeByScore(var1,var2,var3);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("zSetQuery is fail ! var1={0}, var2={1}, var3={2}",var1,var2,var3));
            throw e;
        }
    }

    public double zSetIncrementScore(String var1,String var2,double var3){
        try {
            return redisTemplate.opsForZSet().incrementScore(var1,var2,var3);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("zSetIncrementScore is fail ! var1={0}, var2={1}, var3={2}",var1,var2,var3));
            throw e;
        }
    }

}