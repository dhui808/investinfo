package cftc.utils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;

public class DateUtils {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public static boolean isCurrentYear(String year) {
		LocalDate ld = LocalDate.now();
		int yearInt = ld.getYear();
		
		return yearInt == Integer.valueOf(year);
	}
	
	/**
	 * To be in the safe side, this method returns the number of the year which might be 1 larger than the actual value.
	 * @param year
	 * @return
	 */
	public static int getNumberOfWeeks(String year) {
		
		if (isCurrentYear(year)) {
			LocalDate ld = LocalDate.now();
			return 1 + ld.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		} else {
			return 53;
		}
		

	}
	
	/**
	 * Returns the date of the Sunday of the week corresponding to the CFTC date cftcDate, which is Tuesday.
	 * This is the date the investing.com weekly close price is defined against.
	 * 
	 * @param cftcDate
	 * @return
	 */
	public static String getWeekStartDate(String cftcDate) {
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate ld = LocalDate.parse(cftcDate, df);
		LocalDate thisSunday = ld.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
		String sun = thisSunday.format(df);
		
		return sun;
	}
	
	/**
	 * Returns the date of the Friday of the week corresponding to the CFTC date cftcDate, which is Tuesday.
	 * This is the date the EIA inventory is defined against.
	 * 
	 * @param cftcDate
	 * @return
	 */
	public static String getWeekEndDate(String cftcDate) {
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate ld = LocalDate.parse(cftcDate, df);
		LocalDate thisFriday = ld.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
		String fri = thisFriday.format(df);
		
		return fri;
	}
	
	/**
	 * From 6:00 PM of this week's CFTC release date (mostly Friday) to 6:00 PM (exclusive) of the next CFTC release date, returns
	 * the date string in yyyyMMdd format of this week's Tuesday. Otherwise returns the Tuesday of the last week.  
	 * @return
	 */
	public static String getLatestReleaseTuesdayDate() {
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate ld = LocalDate.now();
		LocalDate thisFriday = ld.with(DayOfWeek.FRIDAY);
		LocalDate thisTuesday = ld.with(DayOfWeek.TUESDAY);
		LocalDate lastWeekTuesday = thisTuesday.minusWeeks(1);
		
		LocalDate latestReleaseTuesday = null;
		String dateStr = null;
		
		int result = ld.compareTo(thisFriday);
		if (result > 0) {
			//Sat.
			latestReleaseTuesday = thisTuesday;
		} else if (result < 0){
			//Sun. thru Thu.
			latestReleaseTuesday = lastWeekTuesday;
		} else {
			//today's Friday
			LocalDateTime now = LocalDateTime.now();
			if (now.toLocalTime().isAfter(LocalTime.of(18, 0))){
				latestReleaseTuesday = thisTuesday;
			} else {
				latestReleaseTuesday = lastWeekTuesday;
			}
		}
		
		dateStr = latestReleaseTuesday.format(df);
		System.out.println("latestReleaseTuesday:" + dateStr);
		return dateStr;
	}
	
	/**
	 * Returns the Sunday (week starting day) of the specified Tuesday date.
	 * 
	 *  @param date The Tuesday date.
	 * @return
	 */
	public static String getReleaseSundayDate(String tuesday) {
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");

		LocalDate localDate = LocalDate.parse(tuesday, df);
		String dateStr = localDate.minusDays(2).format(df);
		
		return dateStr;
	}
	
	/**
	 * From 10:30 AM of this week's EIA release date (mostly Thursday) to 10:30 AM (exclusive) of the next EIA release date, returns
	 * the date string in yyyyMMdd format of this week's Tuesday. Otherwise returns the Tuesday of the last week.  
	 * @return
	 */
	public static String getLatestEiaReleaseTuesdayDate() {
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate ld = LocalDate.now();
		LocalDate thisThursday = ld.with(DayOfWeek.THURSDAY);
		LocalDate thisTuesday = ld.with(DayOfWeek.TUESDAY);
		LocalDate lastWeekTuesday = thisTuesday.minusWeeks(1);
		
		LocalDate latestReleaseTuesday = null;
		String dateStr = null;
		
		int result = ld.compareTo(thisThursday);
		if (result > 0) {
			//Fri., Sat. Sun. this week
			latestReleaseTuesday = thisTuesday;
		} else if (result < 0){
			//last Fri. thru Wed. this week
			latestReleaseTuesday = lastWeekTuesday;
		} else {
			//today's Thursday
			LocalDateTime now = LocalDateTime.now();
			if (now.toLocalTime().isAfter(LocalTime.of(10, 30))){
				latestReleaseTuesday = thisTuesday;
			} else {
				latestReleaseTuesday = lastWeekTuesday;
			}
		}
		
		dateStr = latestReleaseTuesday.format(df);
		
		return dateStr;
	}
	
	/**
	 * Returns the Tuesday date of the current week (string from the Sunday).
	 * @return
	 */
	public static String getCurrentWeekTuesdayDate() {
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate ld = LocalDate.now();
		LocalDate thisTuesday = ld.with(DayOfWeek.TUESDAY);
		String dateStr = null;
		
		dateStr = thisTuesday.format(df);
		
		return dateStr;
	}


	public static String getPreviousWeekTuesdayDate(String currentTuesdayDate) {
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate ld = getCurrentTuesdayLocalDate(currentTuesdayDate);
		LocalDate previousTuesday = ld.with(DayOfWeek.TUESDAY).minusWeeks(1);
		String dateStr = null;
		
		dateStr = previousTuesday.format(df);
		
		return dateStr;
	}
	
	/**
	 * 
	 * @param currentTuesdayDate yyyyMMdd
	 */
	private static LocalDate getCurrentTuesdayLocalDate(String currentTuesdayDate) {
		
		if (null == currentTuesdayDate) {
			return LocalDate.now();
		}
		
		String y = currentTuesdayDate.substring(0, 4);
		String m = currentTuesdayDate.substring(4, 6);
		String d = currentTuesdayDate.substring(6);
		
		return LocalDate.of(Integer.valueOf(y), Integer.valueOf(m), Integer.valueOf(d));
	}
	
	/**
	 * Excel date is stored as the number of days since Jan. 1, 1900.
	 * Converts the days since Janual 1, 1970 to date in the format of yyyyMMdd.
	 */
	
	public static String converExcelDateIntoFormatedDate(String excelDate) {
		Calendar cal = Calendar.getInstance();
		cal.set(1900, 0, 1);
		long l = (Long.valueOf(excelDate) - 2)* 24 * 60 * 60 * 1000 + cal.getTime().getTime();
		
		return sdf.format(l);
	}
	
	/**
	 * Converts the CFTC date format yymmdd to date in the format of 20yymmdd.
	 */
	
	public static String converCftcDateIntoFormatedDate(String cftcDate) {
		
		return "20" + cftcDate;
	}
}
