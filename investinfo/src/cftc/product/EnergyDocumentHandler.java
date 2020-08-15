package cftc.product;

import java.util.List;

import cftc.model.CftcInstrument;
import cftc.model.ProductList;

public class EnergyDocumentHandler extends CommodityDocumentHandler {

	protected List<CftcInstrument> getProductList() {
		
		return ProductList.getEnergyProductList();
	}
}
