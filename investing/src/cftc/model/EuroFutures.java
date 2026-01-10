package cftc.model;

import static cftc.utils.Constants.*;

public class EuroFutures extends Forex {
	
	public String getAnalysisFilePath() {
		
		return preanalysisUrl + "euro.ods";
	}
	
	public String getChartsFilePath() {
		
		return productsUrl + "euro-charts.ods";
	}

	public String[] getFilters() {
		
		return new String[]{"EURO FX - CHICAGO MERCANTILE EXCHANGE"};
	}

	public String getInstrumentName() {
		return InstrumentName.EURO_FX.name();
	}
	
	public int getPricePrecision() {
		return 4;
	}

	public String[] getIndexPriceInventoryColumns() {
		return new String[] {"EURO FX"};
	}
	
	public String getChartTitle() {
		return "EURO FX";
	}

	public String getNetLongImageFilePath(String year) {
		return chartsDirectory + year + "/euro-net-long.png";
	}
}
