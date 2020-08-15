package cftc.model.table;

public enum EiaTableName {
	EIA_STAGING_NG("eia_staging_ng"),
	EIA_STAGING_OIL("eia_staging_oil"),
	EIA_HISTORY("eia_history");
	
	private String tableName;

	EiaTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getName() {
		return tableName;
	}
}
