package cftc.marketchart;

public class MarketCurrentData {

	private String releaseDate;
	private Double usdIndex;
	private Double us10y;
	private Double spx500;
	private Double dow30;
	private Double nasdaq;
	
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public Double getUsdIndex() {
		return usdIndex;
	}
	public void setUsdIndex(Double usdIndex) {
		this.usdIndex = usdIndex;
	}
	public Double getUs10y() {
		return us10y;
	}
	public void setUs10y(Double us10y) {
		this.us10y = us10y;
	}
	public Double getSpx500() {
		return spx500;
	}
	public void setSpx500(Double spx500) {
		this.spx500 = spx500;
	}
	public Double getDow30() {
		return dow30;
	}
	public void setDow30(Double dow30) {
		this.dow30 = dow30;
	}
	public Double getNasdaq() {
		return nasdaq;
	}
	public void setNasdaq(Double nasdaq) {
		this.nasdaq = nasdaq;
	}
}
