package com.zren.platform.bout.common.util;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.zren.platform.bout.common.error.Errors;
import com.zren.platform.bout.common.error.LogicError;


/**
 * 通过redis实现分布式锁
 * @author gavin.lyu
 *
 */
public final class TxLock {
	private static final Logger logger = LoggerFactory.getLogger(TxLock.class);

	private static String TX_LOCK_PREFIX = "txl";

	private String lockType;

	private RedisTemplate<String,Object> redis;
	
	private int timeout;

	private LogicError le;

	private Long val;

	private boolean isLock = false;

	public static TxLock wrap(RedisTemplate<String,Object> redis, String lockType) {
		return new TxLock(redis, lockType, 5000, Errors.E404);
	}

	public static TxLock wrap(RedisTemplate<String,Object> redis, String lockType, LogicError errorType) {
		return new TxLock(redis, lockType, 5000, errorType);
	}

	public static TxLock wrap(RedisTemplate<String,Object> redis, String lockType, int timeout, LogicError e) {
		return new TxLock(redis, lockType, timeout, e);
	}

	private TxLock(RedisTemplate<String,Object> rs, String lt, int timeout, LogicError e) {
		if ((null == rs) || (null == lt)) {
			throw Errors.clone(Errors.E700, "distributed lock init fail. redis or lockType is null");
		}

		this.redis = rs;
		this.lockType = (TX_LOCK_PREFIX + lt);
		this.timeout = timeout;
		if (null != e) {
			this.le = e;
		} else {
			this.le = Errors.E404;
		}
	}

	public boolean tryLock(String lockId) {
		try {
			return lock(lockId);
		} catch (Throwable localThrowable) {
		}
		return false;
	}

	public boolean lock(String lockId) {
		return lock(lockId, 0, 100L);
	}

	public boolean lock(String lockId, int retryTimes, long delayMills) {
		try {
			String k = String.format("%s:%s", this.lockType , lockId);
			long current = System.currentTimeMillis();
			this.val = Long.valueOf(current + this.timeout + 1L);
			String value = Long.toString(this.val.longValue());
			if (redis.opsForValue().setIfAbsent(k, value)) {
				redis.expire(k, this.timeout / 1000,TimeUnit.SECONDS);
				this.isLock = true;
				return this.isLock;
			}

			Long l = getLong(k);
			if (null == l) {
				this.isLock = lock(lockId, retryTimes - 1, delayMills);
				return this.isLock;
			}
			if (l.longValue() < current) {
				Long nl = getsetLong(k, value);
				if (null == nl) {
					redis.expire(k, this.timeout / 1000,TimeUnit.SECONDS);
					this.isLock = true;
					return this.isLock;
				}
				if (l.longValue() == nl.longValue()) {
					redis.expire(k, this.timeout / 1000,TimeUnit.SECONDS);
					this.isLock = true;
					return this.isLock;
				}
			}
		} catch (Exception ex) {
			if (retryTimes > 0) {
				ThreadUtil.sleep(delayMills);
				return lock(lockId, retryTimes - 1, delayMills);
			}

			String reason = "distributed lock[type:%s] fail with lockId:%s";
			reason = String.format(reason, new Object[] { this.lockType, lockId });
			logger.error(reason, ex);
			throw Errors.clone(this.le, reason);
		}

		if (retryTimes > 0) {
			ThreadUtil.sleep(delayMills);
			return lock(lockId, retryTimes - 1, delayMills);
		}

		String reason = "distributed lock[type:%s] fail with lockId:%s";
		reason = String.format(reason, new Object[] { this.lockType, lockId });
		throw Errors.clone(this.le, reason);
	}

	public boolean unlock(String lockId) {
		if (!this.isLock) {
			return false;
		}
		try {
			String k = String.format("%s:%s", this.lockType , lockId);
			Long v = getLong(k);
			if (null == v) {
				return true;
			}
			if (v.longValue() == this.val.longValue()) {
				redis.delete(k);
			} else {
				logger.warn("distributed unlock val[{}] != v[{}]", this.val, v);
			}
			return true;
		} catch (Exception ex) {
			String reason = "distributed unlock[type:%s] fail with lockId:%s";
			reason = String.format(reason, new Object[] { this.lockType, lockId });
			logger.error(reason, ex);
		}
		return false;
	}

	private void del(String k) {
		try {
			redis.delete(k);
		} catch (Exception ex) {
			String reason = "distributed data empty error. with lock cache key:%s";
			reason = String.format(reason, new Object[] { k });
			logger.error(reason, ex);
		}
	}

	private Long getLong(String k) {
		Object v = redis.opsForValue().get(k);
		try {
			if (null != v) {
				return Long.valueOf(v.toString());
			}
		} catch (Exception ex) {
			String cause = String.format("lock data format, k:%s, v:%s", new Object[] { k, v });
			logger.error(cause, ex);
			del(k);
		}
		return null;
	}

	private Long getsetLong(String k, String v) {

		Object old = redis.opsForValue().getAndSet(k, v);
		try {
			if (null != old) {
				return Long.valueOf(old.toString());
			}
		} catch (Exception ex) {
			String cause = String.format("lock data format, k:%s, v:%s, old:%s", new Object[] { k, v, old });
			logger.error(cause, ex);
			del(k);
		}
		return null;
	}

	private TxLock() {
	}
}
