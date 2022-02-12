package cftc.model;

import static cftc.utils.Constants.*;

import cftc.model.table.EiaTableName;

public class NgNewYork extends Energy {
	
	public String getAnalysisFilePath() {
		
		return preanalysisUrl + "ng-newyork.ods";
	}
	
	public String getChartsFilePath() {
		
		return productsUrl + "ng-newyork-charts.ods";
	}

	public String[] getFilters() {
		
		return new String[] {"NATURAL GAS - NEW YORK MERCANTILE EXCHANGE", "NAT GAS NYME - NEW YORK MERCANTILE EXCHANGE"};
	}
	
	public String getInventoryHistoryXlsFilename() {
		return NG_HISTORY_XLS_FILENAME;
	}
	
	public String getInventoryHistoryCsvFilename() {
		return "inventoryNg.csv";
	}
	
	public String getInventoryHistoryStagingTableName() {
		return EiaTableName.EIA_STAGING_NG.getName();
	}

	public String getInstrumentName() {
		return InstrumentName.NG.name();
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
		return 7;
	}

	public int getInventoryHistoryWeekEndingColumn() {
		return 0;
	}

	public int getInventoryHistoryInventoryColumn() {
		return 9;
	}
	
	public int getInventoryHistorySourceSheet() {
		return 0;
	}
	
	public String getChartTitle() {
		return "Natural Gas - NYMEX";
	}
	
	public String getNetLongImageFilePath(String year) {
		return chartsDirectory + year + "/ng-ny-net-long.png";
	}
}
