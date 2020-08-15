package cftc.vendor.investingcom;

public enum InvestingComFilename {
	
	NG("Natural Gas Futures Historical Data.csv", true),
	OIL("Crude Oil WTI Futures Historical Data.csv", true),
	GOLD("Gold Futures Historical Data.csv", true),
	USD_INDEX("US Dollar Index Futures Historical Data.csv", true),
	USD_CAD("USD_CAD Historical Data.csv", false);
	
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
