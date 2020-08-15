package cftc.dao;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cftc.model.InventoryDto;
import cftc.model.table.EiaTableName;

public class InventoryDao extends AbstractDao {

	/**
	 * Clears staging tables and history tables.
	 */
	
	public void clearStagingAndHistoryTables()  throws Exception {
		
	    EiaTableName[] tables = EiaTableName.values();
	    String[] loadInventoryCsvFileQueries = new String[tables.length];

		for (int i = 0; i < tables.length; i++ ) {
			loadInventoryCsvFileQueries[i] = "DELETE FROM " + tables[i].getName();
		}
		
		executeStatementUpdate(loadInventoryCsvFileQueries);
	}
	
	/**
	 * Loads the .csv inventory history file into the staging table.
	 *  
	 * @throws Exception 
	 */
	public void loadInventoryHistoryStaging(String csvFilePath, String table) throws Exception {

	    String loadInventoryCsvFile = "LOAD DATA LOCAL INFILE '" + csvFilePath + "' INTO TABLE " + table + " FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' (week_ending, inventory) ";

	    executeStatementUpdate(loadInventoryCsvFile);
	}

	public void loadInventoryHistory(String stagingTableName, String historyTableName, String instrument) throws Exception {

	    String extractInventoryHistory = "INSERT INTO " + historyTableName +" SELECT date_format(ADDDATE(str_to_date(source.week_ending, '%Y%m%d'), 4), '%Y%m%d'), '" + instrument + "', source.week_ending, source.inventory  from " + stagingTableName + " source";

		executeStatementUpdate(extractInventoryHistory);
	}
	
	public void updateInventoryHistory(String historyTablename, String tuesdayDate, String instrument, String inventory) throws Exception {

	    String updateNgInventoryHistory = "REPLACE INTO " + historyTablename + " VALUES(" + tuesdayDate + ", '" + instrument + "', date_format(SUBDATE(str_to_date(" + tuesdayDate + ", '%Y%m%d'), 4), '%Y%m%d'), " + inventory + ")";

		executeStatementUpdate(updateNgInventoryHistory);
	}
	
	public Map<String, Integer> retrieveInventory(String tablename, String cftcDate) throws Exception {
		
		String query = "SELECT instrument, inventory from " + tablename + " where release_week_tuesday = " + cftcDate;
	    
	    CftcQueryCallback<ResultSet, InventoryDto> resultSetCallback = resultSet -> {
	    	
	    	String instrument = resultSet.getString("instrument");
			Integer inventory = resultSet.getInt("inventory");
			
			InventoryDto result = new InventoryDto();
			result.inventory = inventory;
			result.instrument = instrument;
			
			return result;
		};
		
		List<InventoryDto> list = executeStatementQuery(query, resultSetCallback);
		Map<String, Integer> inventoryMap = new HashMap<String, Integer>();   

		for (InventoryDto dto : list) {
			inventoryMap.put(dto.instrument, dto.inventory);
		}

        return inventoryMap;
	}
	
	public List<InventoryDto> retrieveInventory(String tablename, String instrument, String year) throws Exception {
		
		String query = "SELECT release_week_tuesday, inventory from " + tablename + " where instrument = '" + instrument + "' and release_week_tuesday like '" + year + "%' order by release_week_tuesday desc";
	    
	    CftcQueryCallback<ResultSet, InventoryDto> resultSetCallback = resultSet -> {
	    	
	    	String releaseWeekTuesday = resultSet.getString("release_week_tuesday");
			Integer inventory = resultSet.getInt("inventory");
			
			InventoryDto result = new InventoryDto();
			result.inventory = inventory;
			result.releaseTuesdayDate = releaseWeekTuesday;
			
			return result;
		};
		
		List<InventoryDto> list = executeStatementQuery(query, resultSetCallback);

        return list;
	}
	
	/**
	 * Returns the inventory for the last release of the last year. Returns null if not found. 
	 * 
	 * @param tablename
	 * @param instrument
	 * @param year
	 * @return
	 * @throws Exception
	 */
	public InventoryDto retrieveLastYearInventory(String tablename, String instrument, String year) throws Exception {
		
		String lastYear = Integer.toString(Integer.parseInt(year) - 1);
		
		List<InventoryDto> list = retrieveInventory(tablename, instrument, lastYear);

        return list.size() > 0? list.get(0) : null;
	}
	
	public void updateFirstCtfcReleaseDate(String historyTablename, String firstReleaseDate, String firstReleaseTuesdayDate, String instrument) throws Exception {

	    String updateNgInventoryHistory = "UPDATE " + historyTablename + " SET release_week_tuesday = " + firstReleaseDate + " WHERE instrument = '" + instrument + "' and release_week_tuesday = '" + firstReleaseTuesdayDate + "'";

		executeStatementUpdate(updateNgInventoryHistory);
	}
}
