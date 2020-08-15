package cftc.model;

import static cftc.utils.Constants.*;

public class UsdIndex extends Forex {
	
	public String getAnalysisFilePath() {
		
		return preanalysisUrl + "usd-index.ods";
	}

	public String getChartsFilePath() {
		
		return productsUrl + "usd-index-charts.ods";
	}

	public String[] getFilters() {
		
		return new String[]{"U.S. DOLLAR INDEX - ICE FUTURES U.S."};
	}

	public String getInstrumentName() {
		return InstrumentName.USD_INDEX.name();
	}
	
	public String[] getIndexPriceInventoryColumns() {
		return new String[] {"USD Index"};
	}
	
	public String getChartTitle() {
		return "USD Index";
	}
	
	public String getNetLongImageFilePath(String year) {
		return chartsDirectory + year + "/usd-index-net-long.png";
	}
}
