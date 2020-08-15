package cftc.vendor.investingcom;

import cftc.model.InstrumentName;

public enum InvestingComInstrument {

	NG(InstrumentName.NG.name(), InvestingComFilename.NG.getCsvName(),  InvestingComTablename.NG.getTablename(), InvestingComTablename.HISTORY.getTablename()),
	OIL(InstrumentName.OIL.name(), InvestingComFilename.OIL.getCsvName(), InvestingComTablename.OIL.getTablename(), InvestingComTablename.HISTORY.getTablename()),
	GOLD(InstrumentName.GOLD.name(), InvestingComFilename.GOLD.getCsvName(), InvestingComTablename.GOLD.getTablename(), InvestingComTablename.HISTORY.getTablename()),
	USD_INDEX(InstrumentName.USD_INDEX.name(), InvestingComFilename.USD_INDEX.getCsvName(), InvestingComTablename.USD_INDEX.getTablename(), InvestingComTablename.HISTORY.getTablename()),
	USD_CAD(InstrumentName.USD_CAD.name(), InvestingComFilename.USD_CAD.getCsvName(), InvestingComTablename.USD_CAD.getTablename(), InvestingComTablename.HISTORY.getTablename());
	
	private String instrumentName;
	private String priceIndexCsvName;
	private String priceIndexStagingTablename;
	private String priceIndexHistoryTablename;
	
	InvestingComInstrument(String instrumentName, String priceIndexCsvName, String priceIndexStagingTablename, String priceIndexHistoryTablename) {
		this.instrumentName = instrumentName;
		this.priceIndexCsvName = priceIndexCsvName;
		this.priceIndexStagingTablename = priceIndexStagingTablename;
		this.priceIndexHistoryTablename = priceIndexHistoryTablename;
	}

	public String getInstrumentName() {
		return instrumentName;
	}

	public String getPriceIndexCsvName() {
		return priceIndexCsvName;
	}

	public String getPriceIndexStagingTablename() {
		return priceIndexStagingTablename;
	}

	public String getPriceIndexHistoryTablename() {
		return priceIndexHistoryTablename;
	}
}
