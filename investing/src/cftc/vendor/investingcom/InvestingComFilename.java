package cftc.vendor.investingcom;

public enum InvestingComFilename {
	
	NG("Natural Gas Futures Historical Data.csv", true),
	OIL("Crude Oil WTI Futures Historical Data.csv", true),
	GOLD("Gold Futures Historical Data.csv", true),
	USD_INDEX("US Dollar Index Futures Historical Data.csv", true),
	USD_CAD("USD_CAD Historical Data.csv", false),
	EURO_FX("EUR_USD Historical Data.csv", false),
	USD_JPY("USD_JPY Historical Data.csv", false),
	SPX500("S&P 500 Historical Data.csv", false),
	NASDAQ("NASDAQ Composite Historical Data.csv", false),
	DOW30("Dow Jones Industrial Average Historical Data.csv", false),
	US10Y("United States 10-Year Bond Yield Historical Data.csv", false);
	
	private String csvName;
	private boolean inclugingVolume;
	
	InvestingComFilename(String csvName, boolean inclugingVolume) {
		this.csvName = csvName;
		this.inclugingVolume = inclugingVolume;
	}

	public String getCsvName() {
		return csvName;
	}

	public boolean isInclugingVolume() {
		return inclugingVolume;
	}
}
