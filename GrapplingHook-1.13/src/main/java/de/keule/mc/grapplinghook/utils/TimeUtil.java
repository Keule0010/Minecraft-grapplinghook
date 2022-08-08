package de.keule.mc.grapplinghook.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtil {
	public static String formatMillis(long millis) {
		if (millis < 1000)
			return millis + "ms";
		else
			return formatSeconds(millis / 1000);
	}

	public static String formatSeconds(long secs) {
		long hrs;
		long min;
		long sec;
		if (secs >= 3600) {
			hrs = TimeUnit.SECONDS.toHours(secs);
			min = TimeUnit.SECONDS.toMinutes(secs);
			sec = Math.round(secs % 60);
		} else if (secs >= 60) {
			hrs = 0;
			min = TimeUnit.SECONDS.toMinutes(secs);
			sec = Math.round(secs % 60);
		} else {
			hrs = 0;
			min = 0;
			sec = secs;
		}
		if (hrs == 0 && min == 0)
			return String.format("%2ds", sec);
		else if (hrs == 0)
			return String.format("%2d:%2dm", min, sec);
		else
			return String.format("%2d:%2d:%2dh", hrs, min, sec);
	}
}