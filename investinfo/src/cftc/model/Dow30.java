package cftc.model;

import static cftc.utils.Constants.*;

public class Dow30 extends Forex {
	
	public String getAnalysisFilePath() {
		
		return preanalysisUrl + "dow30.ods";
	}
	
	public String getChartsFilePath() {
		
		return productsUrl + "dow30-charts.ods";
	}

	public String[] getFilters() {
		
		return new String[]{"DOW JONES INDUSTRIAL AVG- x $5 - CHICAGO BOARD OF TRADE"};
	}

	public String getInstrumentName() {
		return InstrumentName.DOW30.name();
	}
	
	public int getPricePrecision() {
		return 4;
	}

	public String[] getIndexPriceInventoryColumns() {
		return new String[] {"Dow30 Index"};
	}
	
	public String getChartTitle() {
		return "Dow30 Index";
	}

	public String getNetLongImageFilePath(String year) {
		return chartsDirectory + year + "/dow30-net-long.png";
	}
}
