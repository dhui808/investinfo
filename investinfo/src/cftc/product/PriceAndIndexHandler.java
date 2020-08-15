package cftc.product;

import com.sun.star.uno.RuntimeException;

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
