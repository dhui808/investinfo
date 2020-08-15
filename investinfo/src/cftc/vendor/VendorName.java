package cftc.vendor;

public enum VendorName {
	INVESTING_COM("investing_com", "investing_com_history");
	
	private String name;
	private String priceHistoryTablename;
	
	VendorName(String name, String priceHistoryTablename) {
		this.name = name;
		this.priceHistoryTablename = priceHistoryTablename;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPriceHistoryTablename() {
		return this.priceHistoryTablename;
	}
}
