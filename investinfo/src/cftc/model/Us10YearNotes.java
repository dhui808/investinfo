package cftc.model;

import static cftc.utils.Constants.*;

public class Us10YearNotes extends Forex {
	
	public String getAnalysisFilePath() {
		
		return preanalysisUrl + "us10y.ods";
	}
	
	public String getChartsFilePath() {
		
		return productsUrl + "us10y-charts.ods";
	}

	public String[] getFilters() {
		
		return new String[]{"10-YEAR U.S. TREASURY NOTES - CHICAGO BOARD OF TRADE", "UST 10Y NOTE - CHICAGO BOARD OF TRADE"};
	}

	public String getInstrumentName() {
		return InstrumentName.US10Y.name();
	}
	
	public int getPricePrecision() {
		return 3;
	}

	public String[] getIndexPriceInventoryColumns() {
		return new String[] {"US 10Y Yield"};
	}
	
	public String getChartTitle() {
		return "US 10 Year Notes Yield";
	}

	public String getNetLongImageFilePath(String year) {
		return chartsDirectory + year + "/usd10y.png";
	}
}
