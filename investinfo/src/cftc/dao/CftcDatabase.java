package cftc.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CftcDatabase {
	public static Map<String, String> getDatabaseProperties() {
		InputStream inputStream = null;
		Map<String, String> dbMap = new HashMap();
		
		try {
			Properties prop = new Properties();
			String propFileName = "database.properties";
 
			inputStream = CftcDatabase.class.getClassLoader().getResourceAsStream(propFileName);
 
			prop.load(inputStream);
 
			String user = prop.getProperty("user");
			String pwd = prop.getProperty("password");
			String databaseName = prop.getProperty("database_name");
 
			dbMap.put("user", user);
			dbMap.put("password", pwd);
			dbMap.put("databaseName", databaseName);
			
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
}
