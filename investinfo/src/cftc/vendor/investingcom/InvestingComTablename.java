package cftc.vendor.investingcom;

public enum InvestingComTablename {
	NG("investing_com_staging_ng"),
	OIL("investing_com_staging_oil_wti"),
	GOLD("investing_com_staging_gold"),
	USD_INDEX("investing_com_staging_usd_index"),
	USD_CAD("investing_com_staging_usd_cad"),
	HISTORY("investing_com_history");
	
	private String tablename;

	InvestingComTablename(String tablename) {
		this.tablename = tablename;
	}
	
	public String getTablename() {
		return tablename;
	}
}
