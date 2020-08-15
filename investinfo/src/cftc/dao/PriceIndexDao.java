package cftc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cftc.model.PriceIndexDto;

public abstract class PriceIndexDao extends AbstractDao {
	
	protected abstract String[] getVendorPriceIndexTablenames();
	
	/**
	 * Clears staging tables and history tables.
	 */
	
	public void clearStagingAndHistoryTables()  throws Exception {

		String[] tablenames = getVendorPriceIndexTablenames();
		
	    String[] loadInventoryCsvFileQueries = new String[tablenames.length];

		for (int i = 0; i < tablenames.length; i++) {
			loadInventoryCsvFileQueries[i] = "DELETE FROM " + tablenames[i];
		}
		
		executeStatementUpdate(loadInventoryCsvFileQueries);
	}
	
	/**
	 * Loads the .csv Price/Index history file into the staging table.
	 *  
	 * @throws Exception 
	 */
	public void loadInventoryHistoryStaging(String csvFilePath, String table, boolean includingVolume) throws Exception {

	    String loadInventoryCsvFile = "LOAD DATA LOCAL INFILE '" + csvFilePath + "' INTO TABLE " + table + " FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n' IGNORE 1 ROWS (date_str, price, open, high, low," + (includingVolume? " vol," : "")  + " change_percentage) ";
	    
		executeStatementUpdate(loadInventoryCsvFile);
	}
	
	public void loadPriceIndexdesHistory(String stagingTableName, String historyTableName, String instrument) throws Exception {

	    String extractInventoryHistory = "REPLACE INTO " + historyTableName +" SELECT date_format(ADDDATE(str_to_date(source.date_str,'%M %d, %Y'), 2), '%Y%m%d'), '" + instrument + "', date_format(str_to_date(source.date_str,'%M %d, %Y'), '%Y%m%d'), REPLACE(source.price, ',', '')  from " + stagingTableName + " source";
		
	    executeStatementUpdate(extractInventoryHistory);
	}
	
	public void updatePriceIndexdesHistory(String historyTablename, String tuesdayDate, String instrument, Float price) throws Exception {

		String updatePriceHistory = "REPLACE INTO " + historyTablename + " VALUES(" + tuesdayDate + ", '" + instrument + "', date_format(SUBDATE(str_to_date(" + tuesdayDate + ", '%Y%m%d'), 2), '%Y%m%d'), " + price + ")";

		executeStatementUpdate(updatePriceHistory);
	}
	
	/**
	 * Loads the latest weekly close price or index into the database.
	 *  
	 * @throws Exception 
	 */
	public void updateLatestWeeklyPriceIndex(String historyTablename, List<PriceIndexDto> priceList) throws Exception {

	    String insertPriceIndexRecord = "REPLACE INTO " + historyTablename + " VALUES (?, ?, ?, ?)";
            
        for (PriceIndexDto dto : priceList) {
        	
			CftcUpdateCallback<PreparedStatement> preparedStatementCallback = preparedStatement -> {

				preparedStatement.setString(1, dto.releaseTuesdayDate);
				preparedStatement.setString(2, dto.instrument);
				preparedStatement.setString(3, dto.weekStartDate);
				preparedStatement.setDouble(4, dto.price);
			};
        	
        	executePreparedStatementUpdate(insertPriceIndexRecord, preparedStatementCallback);
        }
	}
		
	/**
	 * Retrieves the close price or index for the specified instrument and the year.
	 *  
	 * @param instrument
	 * @param year
	 * @return price or index
	 * @throws Exception 
	 */
	public List<PriceIndexDto> retrievePriceIndex(String tablename, String instrument, String year) throws Exception {
		
		String query = "SELECT release_week_tuesday, close_price from " + tablename + "  where instrument = '" + instrument + "' and release_week_tuesday like '" + year + "%' order by release_week_tuesday desc";
		
		CftcQueryCallback<ResultSet, PriceIndexDto> resultSetCallback = resultSet -> {
			
			String releaseWeekTuesday = resultSet.getString("release_week_tuesday");
			Double price = resultSet.getDouble("close_price");
			
			PriceIndexDto result = new PriceIndexDto();
			result.price = price;
			result.releaseTuesdayDate = releaseWeekTuesday;
			
			return result;
		};
		
		List<PriceIndexDto> priceList = executeStatementQuery(query, resultSetCallback);

        return priceList;
	}
	
	/**
	 * Retrieves the close price or index the week corresponding to the cftc release date as specified by the parameter cftcDate.
	 *  
	 * @param weekStartDate
	 * @return Map of instrument to price or index
	 * @throws Exception 
	 */
	public Map<String, Double> retrievePriceIndex(String tablename, String cftcDate) throws Exception {
		
		String query = "SELECT instrument, close_price from " + tablename + " where release_week_tuesday = " + cftcDate;
	    
	    CftcQueryCallback<ResultSet, PriceIndexDto> resultSetCallback = resultSet -> {
	    	
	    	String instrument = resultSet.getString("instrument");
			Double price = resultSet.getDouble("close_price");
			
			PriceIndexDto result = new PriceIndexDto();
			result.price = price;
			result.instrument = instrument;
			
			return result;
		};
		
		List<PriceIndexDto> list = executeStatementQuery(query, resultSetCallback);
		Map<String, Double> priceMap = new HashMap<String, Double>();   

		for (PriceIndexDto dto : list) {
			priceMap.put(dto.instrument, dto.price);
		}

        return priceMap;
	}	
	
	/**
	 * Updates the update_date table with the latest update date.
	 *  
	 * @throws Exception 
	 */
	public void updateDate(String vendor, String weekStartDate) throws Exception {
	 
	    String insertPriceIndexRecord = "INSERT INTO update_date VALUES ('" + vendor + "', '" +  weekStartDate + "') ON DUPLICATE KEY UPDATE update_date='" + weekStartDate + "'";

	    executeStatementUpdate(insertPriceIndexRecord);
	}
	
	/**
	 * Retrieves the latest update date for vendor.
	 *  
	 * @throws Exception 
	 */
	public String retrieveUpdateDate(String vendor) throws Exception {
		
		String query = "SELECT update_date FROM update_date WHERE vendor='" + vendor + "'";
		 
		CftcQueryCallback<ResultSet, String> resultSetCallback = resultSet -> {
			String result = resultSet.getString(1);
			return result;
		};
		
		String updateDate = executeStatementQuerySingleResult(query, resultSetCallback);
            
        return updateDate;
	}
	
	public void updateFirstCtfcReleaseDate(String historyTablename, String firstReleaseDate, String firstReleaseTuesdayDate, String instrument) throws Exception {

	    String updateNgInventoryHistory = "UPDATE " + historyTablename + " SET release_week_tuesday = " + firstReleaseDate + " WHERE instrument = '" + instrument + "' and release_week_tuesday = '" + firstReleaseTuesdayDate + "'";

		executeStatementUpdate(updateNgInventoryHistory);
	}
}
