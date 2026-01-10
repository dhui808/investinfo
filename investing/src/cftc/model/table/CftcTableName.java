package cftc.model.table;

public enum CftcTableName {

	CFTC_RELEASE_HISTORY("cftc_release_history"),
	CFTC_RELEASE_FINANCIAL_HISTORY("cftc_release_financial_history");
	
	private String tableName;

	CftcTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getName() {
		return tableName;
	}
}
