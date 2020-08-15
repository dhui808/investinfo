package cftc.model.table;

public enum CftcTableName {

	CFTC_RELEASE_HISTORY("cftc_release_history");
	
	private String tableName;

	CftcTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getName() {
		return tableName;
	}
}
