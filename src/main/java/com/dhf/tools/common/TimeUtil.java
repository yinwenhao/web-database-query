package com.dhf.tools.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	
	/** 半小时的毫秒数 */
	public static final int halfhourMillisecond = 1800 * 1000;

	/** 一小时的毫秒数 */
	public static final int hourMillisecond = 3600 * 1000;

	/** 一天的毫秒数 */
	public static final int dayMillisecond = 3600 * 1000 * 24;

	public static final String timeStr = "yyyy-MM-dd HH:mm:ss";

	private static DateFormat df = new SimpleDateFormat(timeStr);

	public static String formatDateToString(Date time) {
		return df.format(time);
	}

	public static Date parseDateFromString(String dateString)
			throws ParseException {
		return df.parse(dateString);
	}

}
