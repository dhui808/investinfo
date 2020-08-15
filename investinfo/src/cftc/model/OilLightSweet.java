package cftc.model;

import static cftc.utils.Constants.*;

import cftc.model.table.EiaTableName;

public class OilLightSweet extends Energy {
	
	public String getAnalysisFilePath() {
		
		return preanalysisUrl + "oil.ods";
	}
	
	public String getChartsFilePath() {
		
		return productsUrl + "oil-charts.ods";
	}

	public String getInventoryHistoryStagingTableName() {
		return EiaTableName.EIA_STAGING_OIL.getName();
	}
	
	public String[] getFilters() {
		
		return new String[] {"CRUDE OIL, LIGHT SWEET - NEW YORK MERCANTILE EXCHANGE"};
	}
	
	public String getInventoryHistoryXlsFilename() {
		return OIL_HISTORY_XLS_FILENAME;
	}
	
	public String getInventoryHistoryCsvFilename() {
		return "inventoryOil.csv";
	}

	public String getInstrumentName() {
		return InstrumentName.OIL.name();
	}
	
	public String[] getAnalysisInventoryCellName() {
		String[] inv = {"S2", "T2"};
		return inv;
	}
	
	public String[] getChartsInventoryCellName(int row) {
		String[] inv = {"S" + row, "T" + row};
		return inv;
	}
	
	public String[] getIndexPriceInventoryColumns() {
		return new String[] {"Inventory", "Change", "Price"};
	}
	
	public int getInventoryHistoryFirstRow() {
		return 3;
	}

	public int getInventoryHistoryWeekEndingColumn() {
		return 0;
	}

	public int getInventoryHistoryInventoryColumn() {
		return 1;
	}
	
	public int getInventoryHistorySourceSheet() {
		return 1;
	}
	
	public String getChartTitle() {
		return "Crude Oil WTI - NYMEX";
	}
	
	public String getNetLongImageFilePath(String year) {
		return chartsDirectory + year + "/oil-net-long.png";
	}
}