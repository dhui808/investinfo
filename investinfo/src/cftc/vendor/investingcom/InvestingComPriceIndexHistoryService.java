package cftc.vendor.investingcom;

import static cftc.utils.Constants.STAGING_INVESTING_COM_PATH;

import cftc.PriceAndIndexHistoryService;
import cftc.model.CftcInstrument;
import cftc.vendor.VendorName;

public class InvestingComPriceIndexHistoryService extends PriceAndIndexHistoryService {

	public InvestingComPriceIndexHistoryService() throws Exception {
		wao = new InvestingComPriceIndexWao();
		dao = new InvestingComPriceIndexDao();
	}
	public void updateAllPriceIndexHistory() throws Exception {
		updatePriceIndexHistory(VendorName.INVESTING_COM);
	}
	
	public void loadAllPriceIndexHistory() throws Exception {
		//download .csv files from investing.com into database staging tables
		
		//clear all staging and history tables
		dao.clearStagingAndHistoryTables();
		
		//load price/Index history .csv files into staging tables.
		loadCsvIntoStagingTables();
		
		//extract price/index data into investing.com history tables
		loadPriceIndexIntoHistoryTables();
	}

	private void loadCsvIntoStagingTables() throws Exception {
		
		for (InvestingComFilename filename : InvestingComFilename.values()) {
		
			String csvFilePath = STAGING_INVESTING_COM_PATH + "/" + filename.getCsvName();
			InvestingComTablename tablename = Enum.valueOf(InvestingComTablename.class, filename.name());
			dao.loadInventoryHistoryStaging(csvFilePath, tablename.getTablename(), filename.isInclugingVolume());
		}
	}

	
	private void loadPriceIndexIntoHistoryTables() throws Exception {
		
		String history = InvestingComTablename.HISTORY.name();
		String historyTablename = InvestingComTablename.HISTORY.getTablename();
		for (InvestingComTablename tablename : InvestingComTablename.values()) {
			
			String instrument = tablename.name();
			
			if (history.equals(instrument)) {
				continue;
			}
			
			dao.loadPriceIndexdesHistory(tablename.getTablename(), historyTablename, instrument);
		}
		
	}

	protected String getHistoryTablename() {
		return InvestingComTablename.HISTORY.getTablename();
	}
	
	@Override
	public void loadPriceIndexHistoryForProduct(String product) throws Exception {
		//download .csv files from investing.com into database staging table
		
		InvestingComTablename icTablename = InvestingComTablename.valueOf(product.toUpperCase());
		
		//clear all staging and history table
		dao.clearStagingAndHistoryTable(icTablename.getTablename());
		
		//load price/Index history .csv files into staging table.
		loadCsvIntoStagingTable(icTablename);
		
		//extract price/index data into investing.com history table
		loadPriceIndexIntoHistoryTable(icTablename);
		
	}
	
	private void loadCsvIntoStagingTable(InvestingComTablename icTablename) throws Exception {
		
		InvestingComFilename filename = InvestingComFilename.valueOf(icTablename.name());
		String csvFilePath = STAGING_INVESTING_COM_PATH + "/" + filename.getCsvName();
		dao.loadInventoryHistoryStaging(csvFilePath, icTablename.getTablename(), filename.isInclugingVolume());
		
	}
	
	private void loadPriceIndexIntoHistoryTable(InvestingComTablename icTablename) throws Exception {
		
		String historyTablename = InvestingComTablename.HISTORY.getTablename();
		dao.loadPriceIndexdesHistory(icTablename.getTablename(), historyTablename, icTablename.name());
		
	}
}
