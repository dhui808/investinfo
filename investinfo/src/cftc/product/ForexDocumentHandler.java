package cftc.product;

import static cftc.utils.Constants.templateUrl;

import java.util.List;

import cftc.model.CftcInstrument;
import cftc.model.ProductList;

public class ForexDocumentHandler extends AbstractProductDocumentHandler {

	protected String getSourceFilename() {
		return "FinFutYY.xls";//forex
	}

	protected List<CftcInstrument> getProductList() {
		return ProductList.getForexProductList();
	}

	protected int getSourceColumnLength() {
		return 84;
	}

	protected String getTemplatePath() {
		return templateUrl + "template-forex.ods";
	}

	protected int getTemplateColumnLength() {
		return 22;
	}

	protected String getChartsTemplatePath() {
		return templateUrl + "template-forex-charts.ods";
	}

}
