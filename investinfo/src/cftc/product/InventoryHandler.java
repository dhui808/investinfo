package cftc.product;

import com.sun.star.uno.RuntimeException;

import cftc.model.CftcInstrument;
import cftc.sheet.UpdateSheetInventory;

public class InventoryHandler extends UpdateSheetInventory {

	public void updateInventory(String year) throws RuntimeException, Exception {
		
		super.updateInventory(year);
	}
	
	public void updateInventory(CftcInstrument cftc, String year) throws RuntimeException, Exception {
		
		super.updateInventory(cftc, year);
	}
}
