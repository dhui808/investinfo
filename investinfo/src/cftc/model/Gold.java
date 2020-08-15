package cftc.model;

import static cftc.utils.Constants.*;

public class Gold extends Metal {

	public String getAnalysisFilePath() {
		
		return preanalysisUrl + "gold.ods";
	}
	
	public String getChartsFilePath() {
		
		return productsUrl + "gold-charts.ods";
	}

	public String[] getFilters() {
		return new String[] {"GOLD - COMMODITY EXCHANGE INC."};
	}

	public String getInstrumentName() {
		return InstrumentName.GOLD.name();
	}

	public String[] getIndexPriceInventoryColumns() {
		return new String[] {"", "", "Price"};
	}

	public String getChartTitle() {
		return "Gold";
	}
	
	public String getNetLongImageFilePath(String year) {
		return chartsDirectory + year + "/gold-net-long.png";
	}
}
