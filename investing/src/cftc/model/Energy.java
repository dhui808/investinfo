package cftc.model;

public abstract class Energy extends Commodity {
	
	public abstract String getInventoryHistoryXlsFilename();
	public abstract String getInventoryHistoryCsvFilename();
	public abstract String getInventoryHistoryStagingTableName();
	public abstract int getInventoryHistoryFirstRow();
	public abstract int getInventoryHistoryWeekEndingColumn();
	public abstract int getInventoryHistoryInventoryColumn();
	public abstract int getInventoryHistorySourceSheet();
	
	public InstrumentCategory getCategory() {
		return InstrumentCategory.ENERGY;
	}
}
