package cftc.sheet;

import static cftc.utils.Constants.templateUrl;

import java.util.List;

import cftc.model.CftcInstrument;
import cftc.model.ProductList;

public class AddYearSheetForex extends AddYearSheet {

	protected String getTemplatePath() {
		return templateUrl + "template-forex.ods";
	}

	protected int getTemplateColumnLength() {
		return 22;
	}

	protected String getSourceFilename() {
		return "FinFutYY.xls";
	}

	protected List<CftcInstrument> getProductList() {
		return ProductList.getForexProductList();
	}

	protected int getSourceColumnLength() {
		return 84;
	}

}
