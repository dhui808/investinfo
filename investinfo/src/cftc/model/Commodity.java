package cftc.model;

import cftc.model.table.EiaTableName;

public abstract class Commodity extends CftcInstrument {
	
	public String getInventoryHistoryTableName() {
		return EiaTableName.EIA_HISTORY.getName();
	}
	
	public int getAnalysisInventoryColumnIndex() {
		return 18;
	}
	
	public int getAnalysisPriceColumnIndex() {
		return 20;
	}
	
	public int getChartsInventoryColumnIndex() {
		return 18;
	}
	
	public int getChartsPriceColumnIndex() {
		return 20;
	}
	
	public int getInventoryPriceChangeColumnIndex() {
		return 18;
	}
	
	public int getInventoryPriceChangeColumnLength() {
		return 3;
	}
}
