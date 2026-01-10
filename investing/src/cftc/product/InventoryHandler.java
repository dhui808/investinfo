package cftc.product;

import cftc.model.CftcInstrument;
import cftc.sheet.UpdateSheetInventory;

public class InventoryHandler extends UpdateSheetInventory {

	public void updateInventory(String year) throws Exception {
		
		super.updateInventory(year);
	}
	
	public void updateInventory(CftcInstrument cftc, String year) throws Exception {
		
		super.updateInventory(cftc, year);
	}
}
