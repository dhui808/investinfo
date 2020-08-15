package cftc.charts;

import java.util.List;

import cftc.model.CftcInstrument;
import cftc.model.ProductList;

public class CommodityChartsHandler extends AbstractChartsHandler {

	protected String getSourceFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<CftcInstrument> getProductList() {
		return ProductList.getCommodityProductList();
	}

	protected int getSourceColumnLength() {
		// TODO Auto-generated method stub
		return 0;
	}

}
