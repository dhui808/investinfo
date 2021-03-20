package cftc.model;

public enum InstrumentName {
	
	NG(InstrumentCategory.ENERGY),
	OIL(InstrumentCategory.ENERGY),
	GOLD(InstrumentCategory.METAL),
	USD_INDEX(InstrumentCategory.FOREX),
	USD_CAD(InstrumentCategory.FOREX),
	EURO_FX(InstrumentCategory.FOREX),
	SPX500(InstrumentCategory.FOREX),
	NASDAQ(InstrumentCategory.FOREX),
	DOW30(InstrumentCategory.FOREX),
	US10Y(InstrumentCategory.FOREX);
	
	private InstrumentCategory category;

	InstrumentName(InstrumentCategory category) {
		this.category = category;
	}
	
	public InstrumentCategory getCategory() {
		return category;
	}
}
