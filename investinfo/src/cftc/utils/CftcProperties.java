package cftc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CftcProperties {
	
	private static Map<String, String> dbMap;
	
	public static Map<String, String> getCftcProperties() {
		
		if (null != dbMap) {
			return dbMap;
		}
		
		dbMap = new HashMap<String, String>();
		InputStream inputStream = null;
		
		try {
			Properties prop = new Properties();
			String propFileName = "application.properties";
 
			inputStream = CftcProperties.class.getClassLoader().getResourceAsStream(propFileName);
 
			prop.load(inputStream);
 
			String user = prop.getProperty("user");
			String pwd = prop.getProperty("password");
			String databaseName = prop.getProperty("database_name");
			String yearsNeedAdjusting = prop.getProperty("years_need_adjusting");
 
			dbMap.put("user", user);
			dbMap.put("password", pwd);
			dbMap.put("databaseName", databaseName);
			dbMap.put("yearsNeedAdjusting", yearsNeedAdjusting);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot load database properties file.");
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return dbMap;
	}
	
	public static String[] getYearsNeedAdjusting() {
		
		String years = getCftcProperties().get("yearsNeedAdjusting");
		
		return years.split(",");
	}
}
