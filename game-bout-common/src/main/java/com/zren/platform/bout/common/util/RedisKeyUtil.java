package com.zren.platform.bout.common.util;

import java.text.MessageFormat;

import org.springframework.util.Assert;

public abstract class RedisKeyUtil {

  private static final String ROOM_LOCK = "zjh:{0}:{1}";
  private static final String TABLE_CREATE_LOCK = ROOM_LOCK +":create";
  private static final String TABLE_DELETE_LOCK = ROOM_LOCK +":delete";
  private static final String TABLE_LOCK = ROOM_LOCK + ":{2}";
  private static final String TABLE_ACTION_LOCK = ROOM_LOCK +":{2}:action";

  private static final String DELAYED_QUEUE="queue:%s";

  /**
   * 获取延时队列key
   * @param gameId
   * @return
   */
  public static String getDelayedQueueKey(String gameId) {
	  return String.format(DELAYED_QUEUE, gameId);
  }
  


  public static String getTableActionLock(String gameId,String roomId,String tableId){
    Assert.isTrue(Strings.isNotBlank(gameId),"gameId 不能为空");
    Assert.isTrue(Strings.isNotBlank(roomId),"roomId 不能为空");
    Assert.isTrue(Strings.isNotBlank(tableId),"tableId 不能为空");
    return  MessageFormat.format(TABLE_ACTION_LOCK,gameId,roomId,tableId);
  }

  public  static String getKeyForTableCreate(String gameId, String roomId){
    Assert.isTrue(Strings.isNotBlank(gameId),"gameId 不能为空");
    Assert.isTrue(Strings.isNotBlank(roomId),"roomId 不能为空");
    return  MessageFormat.format(TABLE_CREATE_LOCK,gameId,roomId);
  }
  public  static String getKeyForTableDelete(String gameId, String roomId){
    Assert.isTrue(Strings.isNotBlank(gameId),"gameId 不能为空");
    Assert.isTrue(Strings.isNotBlank(roomId),"roomId 不能为空");
    return  MessageFormat.format(TABLE_DELETE_LOCK,gameId,roomId);
  }

  public  static String getBaseKeyForTable(String gameId,String roomId,String tableId){
    Assert.isTrue(Strings.isNotBlank(gameId),"gameId 不能为空");
    Assert.isTrue(Strings.isNotBlank(roomId),"roomId 不能为空");
    Assert.isTrue(Strings.isNotBlank(tableId),"tableId 不能为空");
    return  MessageFormat.format(TABLE_LOCK,gameId,roomId,tableId);
  }

  public static String getBaseKeyForRoom(String gameId,String roomId){
    Assert.isTrue(Strings.isNotBlank(gameId),"gameId 不能为空");
    Assert.isTrue(Strings.isNotBlank(roomId),"roomId 不能为空");
    return  MessageFormat.format(ROOM_LOCK,gameId,roomId);
  }
}
