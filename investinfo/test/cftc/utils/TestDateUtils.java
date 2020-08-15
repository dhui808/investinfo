package cftc.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class TestDateUtils {
	public static void main(String[] args) throws IOException {
		testIsCurrentYear();
		testGetNumberOfWeeks();
		testGetWeekStartDate();
		testGetLatestReleaseTuesdayDate();
		testDaysSince19700101();
	}
	
	public static void testIsCurrentYear() {
		boolean year2018 = DateUtils.isCurrentYear("2018");
		System.out.println("DateUtils.isCurrentYear(\"2018\"):" + year2018);
		
		boolean year2017 = DateUtils.isCurrentYear("2017");
		System.out.println("DateUtils.isCurrentYear(\"2017\"):" + year2017);
	}
	
	public static void testGetNumberOfWeeks() {
		int year2018 = DateUtils.getNumberOfWeeks("2018");
		System.out.println("DateUtils.getNumberOfWeeks(\"2018\"):" + year2018);
		
		int year2017 = DateUtils.getNumberOfWeeks("2017");
		
		System.out.println("DateUtils.getNumberOfWeeks(\"2017\"):" + year2017);
	}
	
	public static void testGetWeekStartDate() {
		String sun = DateUtils.getWeekStartDate("20181002");
		System.out.println("DateUtils.getWeekStartDate(\"20181002\"):" + sun);
	}
	
	public static void testGetLatestReleaseTuesdayDate() {
		String tue = DateUtils.getLatestReleaseTuesdayDate();
		System.out.println("DateUtils.getLatestReleaseTuesdayDate():" + tue);
	}
	
	public static void testDaysSince19700101() {
		Calendar cal = Calendar.getInstance();
		cal.set(2010, 0, 1);
		System.out.println("DateUtils.testDaysSince19700101():" + cal.getTime().getTime()/24/3600/1000);
	}
}
