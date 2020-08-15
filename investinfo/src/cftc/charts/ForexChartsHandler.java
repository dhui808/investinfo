package cftc.charts;

import java.util.List;

import cftc.model.CftcInstrument;
import cftc.model.ProductList;

public class ForexChartsHandler extends AbstractChartsHandler {

	protected String getSourceFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<CftcInstrument> getProductList() {
		return ProductList.getForexProductList();
	}

	protected int getSourceColumnLength() {
		// TODO Auto-generated method stub
		return 0;
	}

}
