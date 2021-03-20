package cftc.model;

import static cftc.utils.Constants.*;

public class NASDAQ extends Forex {
	
	public String getAnalysisFilePath() {
		
		return preanalysisUrl + "nasdaq.ods";
	}
	
	public String getChartsFilePath() {
		
		return productsUrl + "nasdaq-charts.ods";
	}

	public String[] getFilters() {
		
		return new String[]{"NASDAQ-100 Consolidated - CHICAGO MERCANTILE EXCHANGE"};
	}

	public String getInstrumentName() {
		return InstrumentName.NASDAQ.name();
	}
	
	public int getPricePrecision() {
		return 2;
	}

	public String[] getIndexPriceInventoryColumns() {
		return new String[] {"NASDAQ Index"};
	}
	
	public String getChartTitle() {
		return "NASDAQ Index";
	}

	public String getNetLongImageFilePath(String year) {
		return chartsDirectory + year + "/nasdaq-net-long.png";
	}
}
