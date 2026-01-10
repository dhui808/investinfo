package cftc.model;

import static cftc.utils.Constants.*;

public class SPX500 extends Forex {
	
	public String getAnalysisFilePath() {
		
		return preanalysisUrl + "spx500.ods";
	}
	
	public String getChartsFilePath() {
		
		return productsUrl + "spx500-charts.ods";
	}

	public String[] getFilters() {
		
		return new String[]{"S&P 500 Consolidated - CHICAGO MERCANTILE EXCHANGE"};
	}

	public String getInstrumentName() {
		return InstrumentName.SPX500.name();
	}
	
	public int getPricePrecision() {
		return 2;
	}

	public String[] getIndexPriceInventoryColumns() {
		return new String[] {"SPX500 Index"};
	}
	
	public String getChartTitle() {
		return "S&P 500 Index";
	}

	public String getNetLongImageFilePath(String year) {
		return chartsDirectory + year + "/spx500-net-long.png";
	}
}
