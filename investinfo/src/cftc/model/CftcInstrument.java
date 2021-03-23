package cftc.model;

import static cftc.utils.Constants.productsUrl;

public abstract class CftcInstrument {
	
	public abstract String getAnalysisFilePath();
	public abstract String getChartsFilePath();
	public abstract String getNetLongImageFilePath(String year);
	public abstract String[] getFilters();
	public abstract InstrumentCategory getCategory();
	
	/**
	 * getInstrumentName matches the partition in mysql.sql and the instrument column in extract_price.sql
	 */
	public abstract String getInstrumentName();
	/**
	 * Returns the instrument-specific columns like index, price, inventory, change, etc.
	 * @return
	 */
	public abstract String[] getIndexPriceInventoryColumns();
	
	public abstract String getChartTitle();
	
	/**
	 * Example: need CADUSD from USDCAD
	 * @return
	 */
	public boolean needInverseRatio() {
		return false;
	}
	
	public int getPricePrecision() {
		return 2;
	}
	
	/**
	 * Assume the calculated columns starting from the same index for both analysis and charts data.
	 * @return
	 */
	public int getFirstCalculatedColumnIndex() {
		return 24;
	}
	
	public int getCalculatedColumnsLength() {
		return 6;
	}
	
	public String[] getAnalysisInventoryCellName() {
		throw new UnsupportedOperationException();
	}
	
	public String[] getChartsInventoryCellName(int row) {
		throw new UnsupportedOperationException();
	}
	
	public int getAnalysisInventoryColumnIndex() {
		throw new UnsupportedOperationException();
	}
	
	public int getChartsInventoryColumnIndex() {
		throw new UnsupportedOperationException();
	}
	
	public int getAnalysisPriceColumnIndex() {
		throw new UnsupportedOperationException();
	}
	
	public int getChartsPriceColumnIndex() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Assume both analysis and charts data have the same columns for a cftc instrument
	 * @return
	 */
	public int getInventoryPriceChangeColumnIndex() {
		throw new UnsupportedOperationException();
	}
	
	public int getInventoryPriceChangeColumnLength() {
		throw new UnsupportedOperationException();
	}
}
