package cftc.model;

import static cftc.utils.Constants.*;

public class YenFutures extends Forex {
	
	public String getAnalysisFilePath() {
		
		return preanalysisUrl + "yen.ods";
	}
	
	public String getChartsFilePath() {
		
		return productsUrl + "yen-charts.ods";
	}

	public String[] getFilters() {
		
		return new String[]{"JAPANESE YEN - CHICAGO MERCANTILE EXCHANGE"};
	}

	public String getInstrumentName() {
		return InstrumentName.USD_JPY.name();
	}
	
	public boolean needInverseRatio() {
		return true;
	}
	
	public int getPricePrecision() {
		return 2;
	}

	public String[] getIndexPriceInventoryColumns() {
		return new String[] {"JPYUSD", "USDJPY"};
	}
	
	public String getChartTitle() {
		return "JPY/USD";
	}

	public String getNetLongImageFilePath(String year) {
		return chartsDirectory + year + "/yen-net-long.png";
	}
}
