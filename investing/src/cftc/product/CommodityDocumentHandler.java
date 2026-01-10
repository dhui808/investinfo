package cftc.product;

import static cftc.utils.Constants.templateUrl;

import java.util.List;

import cftc.model.CftcInstrument;
import cftc.model.ProductList;

public class CommodityDocumentHandler extends AbstractProductDocumentHandler {

	protected String getSourceFilename() {
		return "f_year.xls";
	}

	protected List<CftcInstrument> getProductList() {
		
		return ProductList.getCommodityProductList();
	}

	protected int getSourceColumnLength() {
		return 188;
	}

	protected String getTemplatePath() {
		return templateUrl + "template-commodity.ods";
	}

	protected int getTemplateColumnLength() {
		return 18;
	}

	protected String getChartsTemplatePath() {
		return templateUrl + "template-commodity-charts.ods";
	}

}
