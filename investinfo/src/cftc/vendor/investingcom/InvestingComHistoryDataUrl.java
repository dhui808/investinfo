package cftc.vendor.investingcom;

public enum InvestingComHistoryDataUrl {
	NG("https://www.investing.com/commodities/natural-gas-historical-data"),
	OIL("https://www.investing.com/commodities/crude-oil-historical-data"),
	GOLD("https://www.investing.com/commodities/gold-historical-data"),
	USD_INDEX("https://www.investing.com/currencies/us-dollar-index-historical-data"),
	USD_CAD("https://www.investing.com/currencies/usd-cad-historical-data"),
	EURO_FX("https://www.investing.com/currencies/eur-usd-historical-data"),
	SPX500("https://www.investing.com/indices/us-spx-500-historical-data"),
	NASDAQ("https://www.investing.com/indices/nasdaq-composite-historical-data"),
	DOW30("https://www.investing.com/indices/us-30-historical-data"),
	US10Y("https://www.investing.com/rates-bonds/u.s.-10-year-bond-yield-historical-data");
	
	private String url;

	InvestingComHistoryDataUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
}
