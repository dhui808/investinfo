package cftc.marketchart;

import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import cftc.dao.AbstractDao;
import cftc.dao.CftcQueryCallback;
import cftc.model.InstrumentName;
import cftc.model.PriceIndexDto;
import cftc.utils.DateUtils;

public class MarketDao extends AbstractDao {

	public MarketData retrieveAllMarketData() throws Exception {
		
		MarketData marketData = new MarketData();
	
		List<String> dateList = new ArrayList<String>();
		List<Double> usdIndexList = new ArrayList<Double>();
		List<Double> us10yIndexList = new ArrayList<Double>();
		List<Double> spx500IndexList = new ArrayList<Double>();
		List<Double> dow30IndexList = new ArrayList<Double>();
		List<Double> nasdaqIndexList = new ArrayList<Double>();
		List<Double> goldPriceList = new ArrayList<Double>();

		retrieveIndexAndDate(InstrumentName.USD_INDEX, usdIndexList, dateList);
		retrieveIndex(InstrumentName.DOW30, dow30IndexList);
		retrieveIndex(InstrumentName.NASDAQ, nasdaqIndexList);
		retrieveIndex(InstrumentName.SPX500, spx500IndexList);
		retrieveIndex(InstrumentName.US10Y, us10yIndexList, 10);
		retrieveIndex(InstrumentName.GOLD, goldPriceList);
		
		marketData.setReleaseDateList(dateList);
		marketData.setDow30List(dow30IndexList);
		marketData.setNasdaqList(nasdaqIndexList);
		marketData.setSpx500List(spx500IndexList);
		marketData.setUs10yList(us10yIndexList);
		marketData.setUsdIndexList(usdIndexList);
		marketData.setGoldPriceList(goldPriceList);
		
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
	
	public MarketCurrentData retrieveCurrentMarketData() throws Exception {
		
        String formatDateTime = DateUtils.getLatestReleaseSundayDate();
        
		String query = "SELECT instrument, close_price, week_starting from investing_com_history where instrument in ('USD_INDEX','US10Y', 'SPX500','NASDAQ', 'GOLD', 'DOW30') and week_starting = '" + formatDateTime + "' order by instrument desc";
	    
	    CftcQueryCallback<ResultSet, PriceIndexDto> resultSetCallback = resultSet -> {
	    	
			Double price = resultSet.getDouble("close_price");
			String instrument = resultSet.getString("instrument");
			
			PriceIndexDto result = new PriceIndexDto();
			result.price = price;
			result.instrument = instrument;
			
			return result;
		};
		
		List<PriceIndexDto> list = executeStatementQuery(query, resultSetCallback);
		
		MarketCurrentData marketCurrentData =  new MarketCurrentData();
		marketCurrentData.setUsdIndex(list.get(0).price);
		marketCurrentData.setUs10y(list.get(1).price * 10);
		marketCurrentData.setSpx500(list.get(2).price);
		marketCurrentData.setNasdaq(list.get(3).price);
		marketCurrentData.setGoldPrice(list.get(4).price);
		marketCurrentData.setDow30(list.get(5).price);
		marketCurrentData.setReleaseDate(formatDateTime);
		
		return marketCurrentData;
	}

}
