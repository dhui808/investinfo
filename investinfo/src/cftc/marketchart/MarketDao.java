package cftc.marketchart;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cftc.dao.AbstractDao;
import cftc.dao.CftcQueryCallback;
import cftc.model.InstrumentName;
import cftc.model.PriceIndexDto;

public class MarketDao extends AbstractDao {

	public MarketData retrieveAllMarketData() throws Exception {
		
		MarketData marketData = new MarketData();
	
		List<String> dateList = new ArrayList<String>();
		List<Double> usdIndexList = new ArrayList<Double>();
		List<Double> us10yIndexList = new ArrayList<Double>();
		List<Double> spx500IndexList = new ArrayList<Double>();
		List<Double> dow30IndexList = new ArrayList<Double>();
		List<Double> nasdaqIndexList = new ArrayList<Double>();

		retrieveIndexAndDate(InstrumentName.USD_INDEX, usdIndexList, dateList);
		retrieveIndex(InstrumentName.DOW30, dow30IndexList);
		retrieveIndex(InstrumentName.NASDAQ, nasdaqIndexList);
		retrieveIndex(InstrumentName.SPX500, spx500IndexList);
		retrieveIndex(InstrumentName.US10Y, us10yIndexList, 10);
		
		marketData.setReleaseDateList(dateList);
		marketData.setDow30List(dow30IndexList);
		marketData.setNasdaqList(nasdaqIndexList);
		marketData.setSpx500List(spx500IndexList);
		marketData.setUs10yList(us10yIndexList);
		marketData.setUsdIndexList(usdIndexList);
		
		return marketData;
	}

	private void retrieveIndexAndDate(InstrumentName instrumentName, List<Double> indexList, List<String> dateList) throws Exception {
		
		String instrument = instrumentName.name();
		String query = "SELECT close_price, week_starting from investing_com_history where instrument = '" + instrument + "' order by week_starting asc";
	    
	    CftcQueryCallback<ResultSet, PriceIndexDto> resultSetCallback = resultSet -> {
	    	
	    	String weekStartDate = resultSet.getString("week_starting");
			Double price = resultSet.getDouble("close_price");
			
			PriceIndexDto result = new PriceIndexDto();
			result.weekStartDate = weekStartDate;
			result.price = price;
			
			return result;
		};
		
		List<PriceIndexDto> list = executeStatementQuery(query, resultSetCallback);
		
		for (PriceIndexDto dto : list) {
			indexList.add(dto.price);
			dateList.add(dto.weekStartDate);
		}
	}

	private void retrieveIndex(InstrumentName instrumentName, List<Double> indexList) throws Exception {
		
		String instrument = instrumentName.name();
		String query = "SELECT close_price from investing_com_history where instrument = '" + instrument + "' order by week_starting asc";
	    
	    CftcQueryCallback<ResultSet, PriceIndexDto> resultSetCallback = resultSet -> {
	    	
			Double price = resultSet.getDouble("close_price");
			
			PriceIndexDto result = new PriceIndexDto();
			result.price = price;
			
			return result;
		};
		
		List<PriceIndexDto> list = executeStatementQuery(query, resultSetCallback);
		
		for (PriceIndexDto dto : list) {
			indexList.add(dto.price);
		}
	}

	private void retrieveIndex(InstrumentName instrumentName, List<Double> indexList, int priceMultipler) throws Exception {
		
		String instrument = instrumentName.name();
		String query = "SELECT close_price from investing_com_history where instrument = '" + instrument + "' order by week_starting asc";
	    
	    CftcQueryCallback<ResultSet, PriceIndexDto> resultSetCallback = resultSet -> {
	    	
			Double price = resultSet.getDouble("close_price") * priceMultipler;
			
			PriceIndexDto result = new PriceIndexDto();
			result.price = price;
			
			return result;
		};
		
		List<PriceIndexDto> list = executeStatementQuery(query, resultSetCallback);
		
		for (PriceIndexDto dto : list) {
			indexList.add(dto.price);
		}
	}
	
	public MarketData retrieveCurrentMarketData() {
		// TODO Auto-generated method stub
		return null;
	}

}
