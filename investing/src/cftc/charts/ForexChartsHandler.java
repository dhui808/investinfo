package cftc.charts;

import java.util.List;

import cftc.model.CftcInstrument;
import cftc.model.ProductList;

public class ForexChartsHandler extends AbstractChartsHandler {

	protected String getSourceFilename() {
		return null;
	}

	protected List<CftcInstrument> getProductList() {
		return ProductList.getForexProductList();
	}

	protected int getSourceColumnLength() {
		return 0;
	}

}
