package cftc.marketchart;

import java.util.List;

public class MarketData {

	private List<String> releaseDateList;
	private List<Double> usdIndexList;
	private List<Double> us10yList;
	private List<Double> spx500List;
	private List<Double> dow30List;
	private List<Double> nasdaqList;
	
	public List<String> getReleaseDateList() {
		return releaseDateList;
	}
	public void setReleaseDateList(List<String> releaseDateList) {
		this.releaseDateList = releaseDateList;
	}
	public List<Double> getUsdIndexList() {
		return usdIndexList;
	}
	public void setUsdIndexList(List<Double> usdIndexList) {
		this.usdIndexList = usdIndexList;
	}
	public List<Double> getUs10yList() {
		return us10yList;
	}
	public void setUs10yList(List<Double> us10yList) {
		this.us10yList = us10yList;
	}
	public List<Double> getSpx500List() {
		return spx500List;
	}
	public void setSpx500List(List<Double> spx500List) {
		this.spx500List = spx500List;
	}
	public List<Double> getDow30List() {
		return dow30List;
	}
	public void setDow30List(List<Double> dow30List) {
		this.dow30List = dow30List;
	}
	public List<Double> getNasdaqList() {
		return nasdaqList;
	}
	public void setNasdaqList(List<Double> nasdaqList) {
		this.nasdaqList = nasdaqList;
	}
}
