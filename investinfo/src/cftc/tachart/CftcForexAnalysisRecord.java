package cftc.tachart;

public class CftcForexAnalysisRecord {

	private String releaseDate;
	private String instrument;
	private Integer dealerNetLong;
	private Integer assetMgrNetLong;
	private Integer levMoneyNetLong;
	private Integer otherNetLong;
	private Float price;
	
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public Integer getDealerNetLong() {
		return dealerNetLong;
	}
	public void setDealerNetLong(Integer dealerNetLong) {
		this.dealerNetLong = dealerNetLong;
	}
	public Integer getAssetMgrNetLong() {
		return assetMgrNetLong;
	}
	public void setAssetMgrNetLong(Integer assetMgrNetLong) {
		this.assetMgrNetLong = assetMgrNetLong;
	}
	public Integer getLevMoneyNetLong() {
		return levMoneyNetLong;
	}
	public void setLevMoneyNetLong(Integer levMoneyNetLong) {
		this.levMoneyNetLong = levMoneyNetLong;
	}
	public Integer getOtherNetLong() {
		return otherNetLong;
	}
	public void setOtherNetLong(Integer otherNetLong) {
		this.otherNetLong = otherNetLong;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
}
