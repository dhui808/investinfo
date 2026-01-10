package cftc;

import java.util.List;

import cftc.model.CftcInstrument;
import cftc.model.ProductList;

public class UpdateEnergyAnalysis extends UpdateCommodifiesAnalysis {

	protected List<CftcInstrument> getProductList() {

		return ProductList.getEnergyProductList();
	}
}
