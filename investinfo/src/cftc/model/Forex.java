package cftc.model;

public abstract class Forex extends CftcInstrument {
	
	public InstrumentCategory getCategory() {
		return InstrumentCategory.FOREX;
	}
	
	public int getAnalysisPriceColumnIndex() {
		return 22;
	}
	
	public int getChartsPriceColumnIndex() {
		return 22;
	}
	
	public int getInventoryPriceChangeColumnIndex() {
		return 22;
	}
	
	public int getInventoryPriceChangeColumnLength() {
		return 2;
	}
}
