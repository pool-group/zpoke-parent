package com.zren.platform.bout.common.util;

import java.math.BigDecimal;

public class Convert {
	public static Boolean parseBoolean(Object value) {
		if (value instanceof Boolean) {
			return (Boolean)value;
		}
		if (value instanceof Integer) {
			return ((Integer)value) == 0 ? false : true;
		}
		if (value instanceof Long) {
			return ((Long)value) == 0 ? false : true;
		}
		if (value instanceof Short) {
			return ((Short)value) == 0 ? false : true;
		}
		if (value instanceof Byte) {
			return ((Byte)value) == 0 ? false : true;
		}
		if (value instanceof String) {
			return Boolean.parseBoolean((String)value);
		}
		return false;
	}
	
	public static Byte parseByte(Object value) {
		if (value instanceof Byte) {
			return (Byte)value;
		}
		if (value instanceof String) {
			return Byte.parseByte((String)value);
		}
		if (value instanceof Short) {
			return ((Short)value).byteValue();
		}
		if (value instanceof Integer) {
			return ((Integer)value).byteValue();
		}
		if (value instanceof Long) {
			return ((Long)value).byteValue();
		}
		if (value instanceof Float) {
			return ((Float)value).byteValue();
		}
		if (value instanceof Double) {
			return ((Double)value).byteValue();
		}
		if (value instanceof BigDecimal) {
			return ((BigDecimal)value).byteValue();
		}
		return 0;
	}
	 
	public static Short parseShort(Object value) {
		if (value instanceof Short) {
			return (Short)value;
		}
		if (value instanceof String) {
			return Short.parseShort((String)value);
		}
		if (value instanceof Integer) {
			return ((Integer)value).shortValue();
		}
		if (value instanceof Long) {
			return ((Long)value).shortValue();
		}
		if (value instanceof Float) {
			return ((Float)value).shortValue();
		}
		if (value instanceof Double) {
			return ((Double)value).shortValue();
		}
		if (value instanceof BigDecimal) {
			return ((BigDecimal)value).shortValue();
		}
		return 0;
	}
	
	public static Integer parseInt(Object value) {
		if (value instanceof Integer) {
			return (Integer)value;
		}
		if (value instanceof String) {
			return Integer.parseInt((String)value);
		}
		if (value instanceof Long) {
			return ((Long)value).intValue();
		}
		if (value instanceof Float) {
			return ((Float)value).intValue();
		}
		if (value instanceof Double) {
			return ((Double)value).intValue();
		}
		if (value instanceof BigDecimal) {
			return ((BigDecimal)value).intValue();
		}
		return 0;
	}
	
	public static Long parseLong(Object value) {
		if (value instanceof Long) {
			return (Long)value;
		}
		if (value instanceof String) {
			return Long.parseLong((String)value);
		}
		if (value instanceof Integer) {
			return ((Integer)value).longValue();
		}
		if (value instanceof Short) {
			return ((Short)value).longValue();
		}
		if (value instanceof Float) {
			return ((Float)value).longValue();
		}
		if (value instanceof Double) {
			return ((Double)value).longValue();
		}
		if (value instanceof BigDecimal) {
			return ((BigDecimal)value).longValue();
		}
		return 0L;
	}
	
	public static Float parseFloat(Object value) {
		if (value instanceof String) {
			return Float.parseFloat((String)value);
		}
		if(value instanceof Integer){
			return ((Integer) value).floatValue();
		}
		if(value instanceof Long){
			return ((Long) value).floatValue();
		}
		return (Float)value;
	}
	
	public static Double parseDouble(Object value) {
		if (value instanceof String) {
			return Double.parseDouble((String)value);
		}
		return (Double)value;
	}
	
	public static String parseString(Object value) {
		if(value==null) {
			return "";
		}
		return String.valueOf(value);
	}
	
	public static Character parseChar(Object value) {
		return (Character)value;
	}
	
	public static Object parseTo(Object value, Class<?> toClass) {
		if (Objects.isString(toClass)) {
			return Convert.parseString(value);
		}
		if (Objects.isInt(toClass)) {
			return Convert.parseInt(value);
		}
		if (Objects.isBoolean(toClass)) {
			return Convert.parseBoolean(value);
		}
		if (Objects.isLong(toClass)) {
			return Convert.parseLong(value);
		}
		if (Objects.isFloat(toClass)) {
			return Convert.parseFloat(value);
		}
		if (Objects.isDouble(toClass)) {
			return Convert.parseDouble(value);
		}
		if (Objects.isByte(toClass)) {
			return Convert.parseByte(value);
		}
		if (Objects.isShort(toClass)) {
			return Convert.parseShort(value);
		}
		if (Objects.isChar(toClass)) {
			return Convert.parseChar(value);
		}
		return value;
	}

}
