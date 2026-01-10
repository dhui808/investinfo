package cftc.model;

import static cftc.utils.Constants.*;

import cftc.model.table.EiaTableName;

public class NgHenryHub extends Energy {
	
	public String getAnalysisFilePath() {
		
		return preanalysisUrl + "ng-henryhub.ods";
	}
	
	public String getChartsFilePath() {
		
		return productsUrl + "ng-henryhub-charts.ods";
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
	
	public String[] getFilters() {
		
		return new String[] {"NATURAL GAS HENRY LD1", "NATURAL GAS ICE HENRY HUB", "NAT GAS ICE LD1 - ICE FUTURES ENERGY DIV"};
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
		return "Natural Gas - Henry Hub";
	}
	
	public String getNetLongImageFilePath(String year) {
		return chartsDirectory + year + "/ng-hh-net-long.png";
	}
}
