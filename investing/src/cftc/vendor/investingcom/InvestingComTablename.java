package cftc.vendor.investingcom;

public enum InvestingComTablename {
	NG("investing_com_staging_ng"),
	OIL("investing_com_staging_oil_wti"),
	GOLD("investing_com_staging_gold"),
	USD_INDEX("investing_com_staging_usd_index"),
	USD_CAD("investing_com_staging_usd_cad"),
	EURO_FX("investing_com_staging_euro_fx"),
	USD_JPY("investing_com_staging_usd_jpy"),
	SPX500("investing_com_staging_spx500"),
	NASDAQ("investing_com_staging_nasdaq"),
	DOW30("investing_com_staging_dow30"),
	US10Y("investing_com_staging_us10y"),
	HISTORY("investing_com_history");
	
	private String tablename;

	InvestingComTablename(String tablename) {
		this.tablename = tablename;
	}
	
	public String getTablename() {
		return tablename;
	}
}
