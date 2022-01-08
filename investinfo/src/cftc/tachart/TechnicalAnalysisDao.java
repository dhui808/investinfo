package cftc.tachart;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cftc.dao.AbstractDao;
import cftc.dao.CftcQueryCallback;
import cftc.dao.CftcUpdateCallback;
import cftc.marketchart.MarketCurrentData;
import cftc.model.InstrumentName;
import cftc.model.PriceIndexDto;
import cftc.utils.DateUtils;

public class TechnicalAnalysisDao extends AbstractDao {

	public void updateCftcFinancialHistory(String historyTablename, List<CftcForexRecord> cftcRecords) throws Exception {
        
        for (CftcForexRecord taData : cftcRecords) {
        	updateCftcFinancialHistory(historyTablename, taData);
        }
	}
	
	private void updateCftcFinancialHistory(String historyTablename, CftcForexRecord taData) throws Exception {

		String insertCftcReleaseDate = "REPLACE INTO " + historyTablename + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		CftcUpdateCallback<PreparedStatement> preparedStatementCallback = preparedStatement -> {

			preparedStatement.setString(1, taData.getReleaseDate());
			preparedStatement.setString(2, taData.getInstrument());
			preparedStatement.setInt(3, taData.getDealerLong());
			preparedStatement.setInt(4, taData.getDealerShort());
			preparedStatement.setInt(5, taData.getAssetMgrLong());
			preparedStatement.setInt(6, taData.getAssetMgrShort());
			preparedStatement.setInt(7, taData.getLevMoneyLong());
			preparedStatement.setInt(8, taData.getLevMoneyShort());
			preparedStatement.setInt(9, taData.getOtherLong());
			preparedStatement.setInt(10, taData.getOtherShort());
		};
        	
        executePreparedStatementUpdate(insertCftcReleaseDate, preparedStatementCallback);
	}

	public TechnicalAnalysisData retrieveAllTechnicalAnalysisData() throws Exception {
		
		TechnicalAnalysisData marketData = new TechnicalAnalysisData();
		
		List<CftcForexAnalysisRecord> eurusdList = new ArrayList<CftcForexAnalysisRecord>();


		eurusdList = retrieveIndex(InstrumentName.EURO_FX);
		
		marketData.setEurusdList(eurusdList);
		
		return marketData;
	}
	
	private List<CftcForexAnalysisRecord> retrieveIndex(InstrumentName instrumentName) throws Exception {
		
		String instrument = instrumentName.name();
		String query = "SELECT * from cftc_forex_view where instrument = '" + instrument + "' order by release_week_tuesday asc";
	    
	    CftcQueryCallback<ResultSet, CftcForexAnalysisRecord> resultSetCallback = resultSet -> {
	    	
	    	String date = resultSet.getString("release_week_tuesday");
			Float price = resultSet.getFloat("close_price");
			Integer dealerNetLong = resultSet.getInt("dealer_net_long");
			Integer assetMgrNetLong = resultSet.getInt("asset_mgr_net_long");
			Integer leMoneyNetLong = resultSet.getInt("lev_money_net_long");
			Integer otherNetLong = resultSet.getInt("other_net_long");
			
			CftcForexAnalysisRecord result = new CftcForexAnalysisRecord();
			
			result.setInstrument(instrument);
			result.setReleaseDate(date);
			result.setPrice(price);;
			result.setAssetMgrNetLong(assetMgrNetLong);
			result.setDealerNetLong(dealerNetLong);
			result.setLevMoneyNetLong(leMoneyNetLong);
			result.setOtherNetLong(otherNetLong);
			
			return result;
		};
		
		List<CftcForexAnalysisRecord> list = executeStatementQuery(query, resultSetCallback);
		
		return list;
	}
	
	public TechnicalAnalysisData retrieveCurrentFinancialAnalysisData(String formatDateTime) {
        	    
		String query = "SELECT * from cftc_forex_view where instrument in ('EURO_FX') and release_week_tuesday = '" + formatDateTime + "'";
	    
	    CftcQueryCallback<ResultSet, CftcForexAnalysisRecord> resultSetCallback = resultSet -> {
	    	
	    	String date = resultSet.getString("release_week_tuesday");
	    	String instrument = resultSet.getString("instrument");
			Float price = resultSet.getFloat("close_price");
			Integer dealerNetLong = resultSet.getInt("dealer_net_long");
			Integer assetMgrNetLong = resultSet.getInt("asset_mgr_net_long");
			Integer leMoneyNetLong = resultSet.getInt("lev_money_net_long");
			Integer otherNetLong = resultSet.getInt("other_net_long");
			
			CftcForexAnalysisRecord result = new CftcForexAnalysisRecord();
			
			result.setInstrument(instrument);
			result.setReleaseDate(date);
			result.setPrice(price);;
			result.setAssetMgrNetLong(assetMgrNetLong);
			result.setDealerNetLong(dealerNetLong);
			result.setLevMoneyNetLong(leMoneyNetLong);
			result.setOtherNetLong(otherNetLong);
			
			return result;
		};
		
		List<CftcForexAnalysisRecord> list = executeStatementQuery(query, resultSetCallback);
		
		TechnicalAnalysisData marketCurrentData =  new TechnicalAnalysisData();
		
		for (CftcForexAnalysisRecord record : list) {
			List<CftcForexAnalysisRecord> l = new ArrayList<CftcForexAnalysisRecord>();
			l.add(record);
			if ("EURO_FX".equals(record.getInstrument())) {
				marketCurrentData.setEurusdList(l);
			}
		}
		
		return marketCurrentData;
	}
}
