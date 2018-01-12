package simulator.util;

import java.util.StringTokenizer;

public class Time {
	/**
	 * @param tm
	 * @return convert time in string to minutes
	 */
	public static double convertTimeMin(String tm) {
		StringTokenizer strTkTime = new StringTokenizer(tm, ":");
		int hr = Integer.parseInt(strTkTime.nextToken());
		int min = Integer.parseInt(strTkTime.nextToken());
		return (60 * hr) + min;
	}

	/**
	 * Convert time in long format to string format.
	 * 
	 * @param time
	 *            long
	 * @return time in string format.
	 */
	public static String timeToString(long time) {
		int hr = (int) Math.floor(time / 60);
		int min = (int) time % 60;
		if (min < 10) {
			return new String(hr + ":0" + min);
		}
		return new String(hr + ":" + min);
	}

	/**
	 * Convert time in double format to string format.
	 * 
	 * @param time
	 * @return time in string format.
	 */
	public static String timeToString(double time) {

		double tm = time;

		int TimeHr = (int) tm / 60;
		tm = (tm - TimeHr * 60) * 60;

		int TimeMin = (int) tm / 60;
		double TimeSec = tm - TimeMin * 60;

		String fstr;
		if (TimeHr < 10)
			fstr = "0" + TimeHr + ":";
		else
			fstr = TimeHr + ":";

		if (TimeMin < 10)
			fstr = fstr + "0" + TimeMin + ":";
		else
			fstr = fstr + TimeMin + ":";

		if (TimeSec < 10)
			fstr = fstr + "0" + (int) TimeSec;
		else
			fstr = fstr + (int) TimeSec;

		return fstr;

	}

	public static int getIntegerFromTimeString(String timeString) {
		if (timeString.isEmpty())
			return -1;
		int hhmmss = Integer.parseInt(timeString);
		int ss = hhmmss % 100;
		hhmmss = hhmmss / 100;
		int mm = hhmmss % 100;
		hhmmss = hhmmss / 100;
		int hh = hhmmss % 100;

		int time = hh * 3600 + mm * 60 + ss;
		// System.out.println("TimeString is " + timeString + " and time is "
		// + time);
		return time;
	}

	public static String getTimeStringFromInteger(int time) {
		if (time < 0) {
			time += 24 * 60 * 60;
		}
		int tempTime = time;
		int hh = time / 3600;
		time = time - hh * 3600;
		int mm = time / 60;
		time = time - mm * 60;
		int ss = time;
		// System.out.println(hh + " " + mm + " " + ss);
		String timeString = "";
		if (hh < 10)
			timeString += "0" + hh;
		else
			timeString += "" + hh;

		if (mm < 10)
			timeString += "0" + mm;
		else
			timeString += "" + mm;

		if (ss < 10)
			timeString += "0" + ss;
		else
			timeString += "" + ss;

		// if (timeString.equalsIgnoreCase("00000-30"))
		// timeString = "234930";
		// System.out.println("Time is " + tempTime + " and timeString is "
		// + timeString);
		return timeString;
	}

}
