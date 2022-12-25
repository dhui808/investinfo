package cftc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cftc.InvestInfoException;
import cftc.utils.CftcProperties;

public class AbstractDao {

	protected String dbUrl;

	public AbstractDao() {
		Map<String, String> dbMap = CftcProperties.getCftcProperties();
		dbUrl = "jdbc:mysql://localhost/" + dbMap.get("databaseName") + "?"
	            + "user=" + dbMap.get("user") + "&password=" + dbMap.get("password")
	            + "&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true&allowLoadLocalInfile=true";
	}
	protected void executeStatementUpdate(String query) throws Exception{
		executeStatementUpdate(new String[] {query});
	}
	
	protected void executeStatementUpdate(String[] queries) throws Exception {
		
		Connection connect = null;
	    Statement statement = null;
	    ResultSet resultSet = null;

		try {

			connect = createConnection();
			statement = connect.createStatement();
			for (String query : queries ) {
				System.out.println("query=" + query);
				int count = statement.executeUpdate(query);
				System.out.println(count + " records loaded.");
			}
			
		} catch (Exception e) {
			throw e;
		} finally {
			close(resultSet, statement, connect);
		}
	}
	
	protected <T> T executeStatementQuerySingleResult(String query, CftcQueryCallback<ResultSet, T> resultSetCallback) throws Exception {
		
		List<T> list = executeStatementQuery(query, resultSetCallback);
		
		if (0 == list.size()) {
			return null;
		}
		
		return list.get(0);
	}

	protected <T> List<T> executeStatementQuery(String query, CftcQueryCallback<ResultSet, T> resultSetCallback)  {
		
		Connection connect = null;
	    Statement statement = null;
	    ResultSet resultSet = null;
	    T result = null;

		try {

            connect = createConnection();
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            
            List<T> list = new ArrayList<T>();
            
            while(resultSet.next()) {
            	result = resultSetCallback.accept(resultSet);
            	list.add(result);
            }
            
            return list;
            
        } catch (Exception e) {
            throw new InvestInfoException(e);
        } finally {
        	close(resultSet, statement, connect);
        }
	}
	
	protected void executePreparedStatementUpdate(String query, CftcUpdateCallback<PreparedStatement> preparedStatementCallback) throws Exception {
		
		Connection connect = null;
	    PreparedStatement preparedStatement = null;

		try {

            connect = createConnection();
            preparedStatement = connect.prepareStatement(query);

            preparedStatementCallback.accept(preparedStatement);

            preparedStatement.executeUpdate();
            
        } catch (Exception e) {
            throw e;
        } finally {
        	close(null, preparedStatement, connect);
        }
	}
	
	protected <T> T executePreparedStatementQuery(String query, CftcUpdateCallback<PreparedStatement> preparedStatementCallback,
			CftcQueryCallback<ResultSet, T> resultSetCallback) throws Exception {
		
		Connection connect = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    T result = null;

		try {

            connect = createConnection();
            preparedStatement = connect.prepareStatement(query);

            preparedStatementCallback.accept(preparedStatement);

            resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {
            	result = resultSetCallback.accept(resultSet);
            }
            
            return result;
            
        } catch (Exception e) {
            throw e;
        } finally {
        	close(resultSet, preparedStatement, connect);
        }
	}
	
	private Connection createConnection() throws Exception {
		
		Connection connect = null;
		
		try {
            // Setup the connection with the DB
            connect = DriverManager.getConnection(dbUrl);
		 } catch (Exception e) {
	            throw e;
	     }
		
		return connect;
	}
	
	private void close(ResultSet resultSet, Statement statement, Connection connect) {
		
    	try {
    		
    		if (resultSet != null) {
				resultSet.close();
			}

    		if (statement != null) {
    			statement.close();
            }
    		
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
}