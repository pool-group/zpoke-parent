package com.zren.platform.bout.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Objects {
	public static <T> boolean equals(T a, T b) {
		return a== null ? b == null : (a == b || a.equals(b));
	}
	
	public static boolean isNull(Object value) {
		return value == null;
	}
	
	public static boolean isPrimitive(Object value) {
		return value != null && (value.getClass().isPrimitive() || isInt(value)
			|| isBoolean(value) || isLong(value) || isFloat(value) || isDouble(value) 
			|| isByte(value) || isShort(value) || value.getClass().isEnum() || isChar(value));
	}
	
	public static boolean isPrimitive(Class<?> cls) {
		return cls.isPrimitive() || isInt(cls) || isBoolean(cls) || isLong(cls)
			|| isFloat(cls) || isDouble(cls) || isByte(cls) || isShort(cls)
			|| cls.isEnum() || isChar(cls);
	}
	
	public static Object defaultValue(Class<?> cls) {
		if (isString(cls)) {
			return Strings.Empty;
		}
		if (isInt(cls) || isLong(cls) || isByte(cls) || isShort(cls)) {
			return 0;
		}
		if (isBoolean(cls)) {
			return false;
		}
		if (isFloat(cls)) {
			return 0.0F;
		}
		if (isDouble(cls)) {
			return 0.0D;
		}
		if (isChar(cls)) {
			return '0';
		}
		return BeanUtils.newInstance(cls);
	}
	
	public static boolean isBoolean(Object value) {
		return value instanceof Boolean;
	}
	
	public static boolean isBoolean(Class<?> cls) {
		return cls == boolean.class || cls == Boolean.class;
	}
	
	public static boolean isByte(Object value) {
		return value instanceof Byte;
	}
	
	public static boolean isByte(Class<?> cls) {
		return cls == byte.class || cls == Byte.class;
	}
	
	public static boolean isShort(Object value) {
		return value instanceof Short;
	}
	
	public static boolean isShort(Class<?> cls) {
		return cls == short.class || cls == Short.class;
	}
	
	public static boolean isInt(Object value) {
		return value instanceof Integer;
	}
	
	public static boolean isInt(Class<?> cls) {
		return cls == int.class || cls == Integer.class;
	}
	
	public static boolean isLong(Object value) {
		return value instanceof Long;
	}
	
	public static boolean isLong(Class<?> cls) {
		return cls == long.class || cls == Long.class;
	}
	
	public static boolean isFloat(Object value) {
		return value instanceof Float;
	}
	
	public static boolean isFloat(Class<?> cls) {
		return cls == float.class || cls == Float.class;
	}
	
	public static boolean isDouble(Object value) {
		return value instanceof Double;
	}
	
	public static boolean isDouble(Class<?> cls) {
		return cls == double.class || cls == Double.class;
	}
	
	public static boolean isString(Object value) {
		return value instanceof String;
	}
	
	public static boolean isString(Class<?> cls) {
		return cls == String.class;
	}
	
	public static boolean isChar(Object value) {
		return value instanceof Character;
	}
	
	public static boolean isChar(Class<?> cls) {
		return cls == char.class || cls == Character.class;
	}
	
	public static boolean isArray(Object value) {
		return value != null && isArray(value.getClass());
	}
	
	public static boolean isArray(Class<?> cls) {
		return cls.isArray();
	}
	
	public static boolean isEnum(Object value) {
		return value != null && isEnum(value.getClass());
	}
	
	public static boolean isEnum(Class<?> cls) {
		return cls.isEnum();
	}
	
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
            e.printStackTrace();
		}
		return null;
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
            e.printStackTrace();
		}
		return null;
	}
}
