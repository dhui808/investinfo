package cftc.vendor.investingcom;

import cftc.model.InstrumentName;

public enum InvestingComInstrument {

	NG(InstrumentName.NG.name(), InvestingComFilename.NG.getCsvName(),  InvestingComTablename.NG.getTablename(), InvestingComTablename.HISTORY.getTablename()),
	OIL(InstrumentName.OIL.name(), InvestingComFilename.OIL.getCsvName(), InvestingComTablename.OIL.getTablename(), InvestingComTablename.HISTORY.getTablename()),
	GOLD(InstrumentName.GOLD.name(), InvestingComFilename.GOLD.getCsvName(), InvestingComTablename.GOLD.getTablename(), InvestingComTablename.HISTORY.getTablename()),
	USD_INDEX(InstrumentName.USD_INDEX.name(), InvestingComFilename.USD_INDEX.getCsvName(), InvestingComTablename.USD_INDEX.getTablename(), InvestingComTablename.HISTORY.getTablename()),
	USD_CAD(InstrumentName.USD_CAD.name(), InvestingComFilename.USD_CAD.getCsvName(), InvestingComTablename.USD_CAD.getTablename(), InvestingComTablename.HISTORY.getTablename()),
	EURO_FX(InstrumentName.EURO_FX.name(), InvestingComFilename.EURO_FX.getCsvName(), InvestingComTablename.EURO_FX.getTablename(), InvestingComTablename.HISTORY.getTablename()),
	SPX500(InstrumentName.SPX500.name(), InvestingComFilename.SPX500.getCsvName(), InvestingComTablename.SPX500.getTablename(), InvestingComTablename.HISTORY.getTablename()),
	NASDAQ(InstrumentName.NASDAQ.name(), InvestingComFilename.NASDAQ.getCsvName(), InvestingComTablename.NASDAQ.getTablename(), InvestingComTablename.HISTORY.getTablename()),
	DOW30(InstrumentName.DOW30.name(), InvestingComFilename.DOW30.getCsvName(), InvestingComTablename.DOW30.getTablename(), InvestingComTablename.HISTORY.getTablename()),
	US10Y(InstrumentName.US10Y.name(), InvestingComFilename.US10Y.getCsvName(), InvestingComTablename.US10Y.getTablename(), InvestingComTablename.HISTORY.getTablename());

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
