package cftc.product;

import java.util.List;

import cftc.model.CftcInstrument;
import cftc.model.ProductList;

public class MetalDocumentHandler extends CommodityDocumentHandler {

	protected List<CftcInstrument> getProductList() {
		
		return ProductList.getMetalProductList();
	}

}
