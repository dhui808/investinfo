package cftc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class CftcDao extends AbstractDao {

	public void updateCftcReleaseHistory(String historyTablename, String year, String[] releaseDates) throws Exception {

		String insertCftcReleaseDate = "REPLACE INTO " + historyTablename + " VALUES (?)";
        
        for (String date : releaseDates) {
        	
			CftcUpdateCallback<PreparedStatement> preparedStatementCallback = preparedStatement -> {

				preparedStatement.setString(1, date);
			};
        	
        	executePreparedStatementUpdate(insertCftcReleaseDate, preparedStatementCallback);
        }
	}
	
	public String retrieveFirstReleaseDate(String tablename, String year) throws Exception {
		
		List<String> list = retrieveReleaseDates(tablename, year);

        return list.size() > 0? list.get(list.size() - 1) : null;
	}

	public String retrieveLastReleaseDate(String tablename, String year) throws Exception {
		
		List<String> list = retrieveReleaseDates(tablename, year);

        return list.size() > 0? list.get(0) : null;
	}
	
	private List<String> retrieveReleaseDates(String tablename, String year) throws Exception {
		
		String query = "SELECT release_date from " + tablename + " where release_date like '" + year + "%' order by release_date desc";
	    
	    CftcQueryCallback<ResultSet, String> resultSetCallback = resultSet -> {
	    	
	    	String releaseDate = resultSet.getString("release_date");
			
			return releaseDate;
		};
		
		List<String> list = executeStatementQuery(query, resultSetCallback);

        return list;
	}
}
