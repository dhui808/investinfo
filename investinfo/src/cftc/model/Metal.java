package cftc.model;

public abstract class Metal extends Commodity {
	
	public InstrumentCategory getCategory() {
		return InstrumentCategory.METAL;
	}
}
