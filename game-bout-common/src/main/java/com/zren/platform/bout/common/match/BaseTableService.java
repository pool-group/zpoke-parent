package com.zren.platform.bout.common.match;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zren.platform.bout.common.error.BizErrors;
import com.zren.platform.bout.common.util.Convert;
import com.zren.platform.bout.common.util.ThreadUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BaseTableService {

	@Autowired
	private RedisTemplate<String, Object> redis;

	@Autowired
	private MatchConfig matchConfig;

	private final String TABLE_MAP_KEY_PATTEN = "match:%s:%s:%s:tablemap";
	private final String TABLE_AVAILABLE_KEY_PATTEN = "match:%s:%s:%s:available";
	private final String ROOM_PERSON_NUM_KEY_PATTEN = "match:%s:%s:%s:person";
	private final String TABLE_PERSON_ONLINE_MAP = "match:%s:%s:%s:person-table";
	private final String TABLE_SEAT_PATTEN = "match:%s:%s:%s:table-seats";
	private final String TABLE_IP_LIST = "match:%s:%s:%s:ips";

	public BaseTable createTable(String gameId, String roomId, String brand) {
		String key = String.format(TABLE_MAP_KEY_PATTEN, gameId, roomId, brand);
		// 如果桌台最大数量超出限制，不再创建桌台
		if (redis.opsForHash().size(key) >= matchConfig.getTableCapcity()) {
			log.error("创建桌台失败：game:{},room:{},已经超出最大限制", gameId, roomId);
			return null;
		}
		String tableId = UUID.randomUUID().toString();
		BaseTable table = BaseTable.wrap(gameId,roomId,tableId,brand);

		redis.opsForHash().put(key, table.getTableId(), JSON.toJSONString(table));
		return table;
	}

	public long getTableNum(String gameId, String roomId, String brand) {
		String key = String.format(TABLE_MAP_KEY_PATTEN, gameId, roomId, brand);
		return redis.opsForHash().size(key);
	}

	public long getPersonNum(String gameId, String roomId, String brand) {
		String key = String.format(ROOM_PERSON_NUM_KEY_PATTEN, gameId, roomId, brand);
		return redis.opsForHash().size(key);
	}

	public void delTable(BaseTable table) {
		String key = String.format(TABLE_MAP_KEY_PATTEN, table.getGameId(), table.getRoomId(), table.getBrand());
		redis.opsForHash().delete(key, table.getTableId());
		redis.delete(String.format(TABLE_SEAT_PATTEN, table.getGameId(),table.getRoomId(),table.getTableId()));
		key = String.format(TABLE_AVAILABLE_KEY_PATTEN, table.getGameId(),table.getRoomId(),table.getBrand());
		redis.opsForList().remove(key, -1, table.getTableId());
	}

	public TableDTO availableTable(String gameId, String roomId, String brand) {
		TableDTO dto = new TableDTO();
		String key = String.format(TABLE_AVAILABLE_KEY_PATTEN, gameId, roomId, brand);
		String tableId = Convert.parseString(redis.opsForList().rightPop(key));
		if (tableId != null) {
			Object obj = redis.opsForHash().get(String.format(TABLE_MAP_KEY_PATTEN, gameId, roomId, brand), tableId);
			if (obj != null) {
				dto.setBaseTable(JSON.parseObject(obj.toString(), BaseTable.class));
				return dto;
			}
		}
		dto.setBaseTable(createTable(gameId, roomId, brand));
		dto.setNew(true);
		return dto;
	}

	public void putAvailableTable(String gameId, String roomId, String tableId, String brand) {
		String key = String.format(TABLE_AVAILABLE_KEY_PATTEN, gameId, roomId, brand);
		redis.opsForList().leftPush(key, tableId);
	}

	/*
	 * 从匹配列表中移除数据，默认重试3次
	 */
	public void fetchTableById(String gameId, String roomId, String tableId, String brand) {
		String key = String.format(TABLE_AVAILABLE_KEY_PATTEN, gameId, roomId, brand);
		int i=0;
		while(redis.opsForList().remove(key, 0, tableId)==0&&i<3) {
			ThreadUtil.sleep(50);
			i++;
		}
        if(i>=3) {
        	throw BizErrors.E4002;
        }
	}

	public BaseTable getTable(String gameId, String roomId, String tableId, String brand) {
		String key = String.format(TABLE_MAP_KEY_PATTEN, gameId, roomId, brand);
		Object obj = redis.opsForHash().get(key, tableId);
		if (obj == null) {
			return null;
		}
		return JSON.parseObject(obj.toString(), BaseTable.class);
	}

	public void saveTablePlayer(BaseTable table,BasePlayer player) {
		String key = String.format(TABLE_PERSON_ONLINE_MAP, table.getGameId(), table.getRoomId(), table.getBrand());
		redis.opsForHash().put(key, player.getPlayerId(), table.getTableId());
		saveIP(table.getGameId(), table.getRoomId(), table.getTableId(), player.getIp());
		setTableSeats(table.getGameId(), table.getRoomId(), table.getTableId(), 1);
	}

	public String getPlayerOnlineTableId(String gameId, String roomId, String playerId, String brand) {
		String key = String.format(TABLE_PERSON_ONLINE_MAP, gameId, roomId, brand);
		Object obj = redis.opsForHash().get(key, playerId);
		if (obj == null) {
			return null;
		}
		return obj.toString();
	}

	public void delPlayerOnlineTableId(String gameId, String roomId, String playerId, String brand) {
		String key = String.format(TABLE_PERSON_ONLINE_MAP, gameId, roomId, brand);
		redis.opsForHash().delete(key, playerId);
	}

	public Long setTableSeats(String gameId, String roomId, String tableId, int i) {
		String key = String.format(TABLE_SEAT_PATTEN, gameId, roomId, tableId);
		long v = redis.opsForValue().increment(key, i);
		return v;
	}
	
	public Long getTableSeats(String gameId, String roomId, String tableId) {
		String key = String.format(TABLE_SEAT_PATTEN, gameId, roomId, tableId);
		Object o = redis.opsForValue().get(key);
		if(o==null) {
			return 0l;
		}
		return Convert.parseLong(o);
	}
	
	public void saveIP(String gameId,String roomId,String tableId,String ip) {
		String key = String.format(TABLE_IP_LIST, gameId,roomId,tableId);
		redis.opsForList().rightPush(key, ip);
	}
	
	public long getIpTimes(String gameId,String roomId,String tableId,String ip) {
		String key = String.format(TABLE_IP_LIST, gameId,roomId,tableId);
		List<Object> ips = redis.opsForList().range(key, 0, -1);
		if(ips!=null&&ips.size()>0) {
			return ips.stream().filter(p->p.toString().equals(ip)).count();
		}
		return 0;
	}
	
	public void removeIP(String gameId,String roomId,String tableId,String ip) {
		String key = String.format(TABLE_IP_LIST, gameId,roomId,tableId);
		redis.opsForList().remove(key, -1, ip);
	}
}
