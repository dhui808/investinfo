package cftc.product;

import cftc.model.CftcInstrument;
import cftc.sheet.UpdateSheetPriceIndex;

public abstract class PriceAndIndexHandler extends UpdateSheetPriceIndex {
	
	public void updatePriceOrIndex(String year) throws RuntimeException, Exception {
		
		super.updatePriceOrIndex(year);
	}
	
	public void updatePriceOrIndex(CftcInstrument cftc, String year) throws RuntimeException, Exception {
		
		super.updatePriceOrIndex(cftc, year);
	}
}
