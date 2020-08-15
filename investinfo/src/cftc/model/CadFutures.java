package cftc.model;

import static cftc.utils.Constants.*;

public class CadFutures extends Forex {
	
	public String getAnalysisFilePath() {
		
		return preanalysisUrl + "cad.ods";
	}
	
	public String getChartsFilePath() {
		
		return productsUrl + "cad-charts.ods";
	}

	public String[] getFilters() {
		
		return new String[]{"CANADIAN DOLLAR - CHICAGO MERCANTILE EXCHANGE"};
	}

	public String getInstrumentName() {
		return InstrumentName.USD_CAD.name();
	}
	
	public boolean needInverseRatio() {
		return true;
	}
	
	public int getPricePrecision() {
		return 4;
	}

	public String[] getIndexPriceInventoryColumns() {
		return new String[] {"CADUSD", "USDCAD"};
	}
	
	public String getChartTitle() {
		return "CAD/USD";
	}

	public String getNetLongImageFilePath(String year) {
		return chartsDirectory + year + "/cad-net-long.png";
	}
}
