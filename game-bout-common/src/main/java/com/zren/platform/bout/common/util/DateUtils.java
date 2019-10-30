package com.zren.platform.bout.common.util;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
	public static final String datePattern = "yyyy-MM-dd";
	public static final String timePattern = "HH:mm:ss";
	public static final String fullDatePattern = "yyyy-MM-dd HH:mm:ss";
	public static final DateTimeFormatter SIMPLE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

	public static final DateTimeFormatter Y_M_H_M =  DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	/**
	 * 格式化时间
	 * 
	 * @param date
	 *            时间
	 * @param pattern
	 *            格式化字符串
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		}
		return "";
	}

	/**
	 * 将时间对象的日期部分格化化
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateString(Date date) {
		return formatDate(date, datePattern);
	}

	/**
	 * 将时间对象的日期、时间部分格化化
	 * 
	 * @param date
	 * @return
	 */
	public static String getFullString(Date date) {
		return formatDate(date, fullDatePattern);
	}

	/**
	 * 将时间对象的时间部分格化化
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeString(Date date) {
		return formatDate(date, timePattern);
	}

	/**
	 * 按照日期格式，将字符串解析为日期对象
	 * 
	 * @param aMask
	 *            输入字符串的格式
	 * @param str
	 *            一个按aMask格式排列的日期的字符串描述
	 * @return Date 对象
	 */
	public static Date parseDate(String str, String[] patterns) {
		for (String pattern : patterns) {
			try {
				SimpleDateFormat df = new SimpleDateFormat(pattern);
				return df.parse(str);
			} catch (Exception pe) {
			}
		}
		return null;
	}

	/**
	 * 按照日期格式，将字符串解析为日期对象
	 * 
	 * @param aMask
	 *            输入字符串的格式
	 * @param str
	 *            一个按aMask格式排列的日期的字符串描述
	 * @return Date 对象
	 */
	public static Date parseDate(String str, String pattern) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.parse(str);
		} catch (Exception pe) {
			return null;
		}
	}

	/**
	 * 将<code>datePattern<code>为格式的字符串解析为日期对象
	 * 
	 * @param str
	 * @return
	 */
	public static Date dateStringToDate(String str) {
		return parseDate(str, datePattern);
	}

	/**
	 * add by fa ,2008.12.11 将日期对象转换为“yyyyMMdd”String
	 * 
	 * @param date
	 * @return String
	 */
	public static String dateToShortString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(date);
	}

	public static final String TIME_FORMAT = "HH:mm:ss:SS";
	public static final String DEFAULT_SHORT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_SHORT_DATE_FORMAT_ZH = "yyyy年M月d日";
	public static final String DEFAULT_LONG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SS";
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_TIMESTAMP = "yyyyMMddHHmmss";
	public static final String JAVA_MIN_SHORT_DATE_STR = "1970-01-01";
	public static final String JAVA_MIN_LONG_DATE_STR = "1970-01-01 00:00:00:00";
	public static final String DEFAULT_PERIOD_FORMAT = "{0}天{1}小时{2}分钟";
	public static final String JAVA_MAX_SHORT_DATE_STR = "9999-12-31";

	public static Date addDate(String datepart, int number, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (datepart.equals("yy"))
			cal.add(1, number);
		else if (datepart.equals("MM"))
			cal.add(2, number);
		else if (datepart.equals("dd"))
			cal.add(5, number);
		else if (datepart.equals("HH"))
			cal.add(11, number);
		else if (datepart.equals("mm"))
			cal.add(12, number);
		else if (datepart.equals("ss"))
			cal.add(13, number);
		else {
			throw new IllegalArgumentException("DateUtil.addDate()方法非法参数值：" + datepart);
		}

		return cal.getTime();
	}

	public static boolean compareNow(String time1) {
		return compareTime(time1, currentStr(), "yyyy-MM-dd HH:mm:ss");
	}

	public static boolean compareTime(String time1, String time2) {
		return compareTime(time1, time2, "yyyy-MM-dd HH:mm:ss");
	}

	public static boolean compareTime(String time1, String time2, String dateFormat) {
		SimpleDateFormat t1 = new SimpleDateFormat(dateFormat);
		SimpleDateFormat t2 = new SimpleDateFormat(dateFormat);
		try {
			Date d1 = t1.parse(time1);
			Date d2 = t2.parse(time2);
			return d1.before(d2);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Date convert(String date, String format) {
		if ((date == null) || (date.equals("")))
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (Exception e) {
			throw new RuntimeException("DateUtil.convert():" + e.getMessage());
		}
	}

	public static String convert(Date date, String format) {
		if (date == null)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static Date convert(String date) {
		int len = date.length();
		return convert(date, (len < 11) ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm:ss");
	}

	public static Date convert(long time) {
		return new Date(time);
	}

	public static Date convert(Long time) {
		return new Date(time.longValue());
	}

	public static String convert(Date date) {
		return convert(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date toDate(Object o) {
		if (null == o)
			return null;

		if (o instanceof Date)
			return ((Date) o);

		if (o instanceof String)
			return convert((String) o);

		if (o instanceof Long) {
			Long t = (Long) o;
			return new Date(t.longValue());
		}
		throw new RuntimeException("invalid time object:" + o);
	}

	public static String format(long period) {
		long dayUnit = 86400L;
		long hourUnit = 3600L;
		long minUnit = 60L;
		String result = MessageFormat.format("{0}天{1}小时{2}分钟", new Object[] { Long.valueOf(period / dayUnit),
				Long.valueOf(period % dayUnit / hourUnit), Long.valueOf(period % hourUnit / minUnit) });

		return result;
	}

	public static double dateDiff(String datepart, Date startdate, Date enddate) {
		if ((datepart == null) || ("".equals(datepart))) {
			String info = "DateUtil.dateDiff()方法非法参数值：" + datepart;
			throw new IllegalArgumentException(info);
		}

		double days = (enddate.getTime() - startdate.getTime()) / 86400000.0D;

		if (datepart.equals("yy")) {
			days /= 365.0D;
		} else if (datepart.equals("MM")) {
			days /= 30.0D;
		} else {
			if (datepart.equals("dd"))
				return days;

			String info = "DateUtil.dateDiff()方法非法参数值：" + datepart;
			throw new IllegalArgumentException(info);
		}
		return days;
	}
	
	/**
	 * 计算时间差
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static long timeReduce(String time1,String time2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try {
			Date t1=df.parse(time1);
			Date t2=df.parse(time2);
			return (t1.getTime()-t2.getTime())/1000l;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static String currentStr() {
		return currentStr("yyyy-MM-dd HH:mm:ss");
	}

	public static String currentStr(String dateFormat) {
		return convert(new Date(), dateFormat);
	}

	public static long getTimeMillis(String dateStr) {
		return convert(dateStr, "yyyy-MM-dd HH:mm:ss").getTime();
	}

	public static Date getFirstMonthDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(5, calendar.getActualMinimum(5));
		return calendar.getTime();
	}

	public static Date getFirstDayNextMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(5, calendar.getActualMinimum(5));
		calendar.set(11, 0);
		calendar.set(12, 0);
		calendar.set(13, 0);
		calendar.add(2, 1);
		return calendar.getTime();
	}

	public static Date getFirstDayCurMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(5, calendar.getActualMinimum(5));
		calendar.set(11, 0);
		calendar.set(12, 0);
		calendar.set(13, 0);
		calendar.set(14, 0);
		return calendar.getTime();
	}

	public static Date getFirstDayCurYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(6, 1);
		calendar.set(11, 0);
		calendar.set(12, 0);
		calendar.set(13, 0);
		calendar.set(14, 0);
		return calendar.getTime();
	}

	public static Date getFirstDayCurYear(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, 1, 1);
		calendar.set(6, 1);
		calendar.set(11, 0);
		calendar.set(12, 0);
		calendar.set(13, 0);
		calendar.set(14, 0);
		return calendar.getTime();
	}

	public static Date getLastMonthDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(5, calendar.getActualMaximum(5));
		return calendar.getTime();
	}

	public static String addDate(String datepart, int number, String dateStr) {
		Date dt = convert(dateStr);
		Date date = addDate(datepart, number, dt);
		return convert(date);
	}
	
	public static String formartTime(Long time) {
		if(time==null) {
			return null;
		}
		Long hour=time/3600;
		Long temp=time%3600;
		Long minute=temp/60;
		Long second=temp%60;
		return (hour>0?hour+"小时，":"")+(minute>0?minute+"分，":"")+(second+"秒");
	}

	/**
	 * 返回当前日期字符串 yyMMdd
	 *
	 * @return
	 */
	public static String getSimpleCurrentDateStr() {
		return LocalDate.now().format(SIMPLE_DATE_FORMATTER);
	}


	public static String ymhmFormatString(LocalDate date){
		if(date == null){
			date = LocalDate.now();
		}
		return date.format(Y_M_H_M);
	}
	public static String ymhmFormatString(){
		return  ymhmFormatString(null);
	}
}
