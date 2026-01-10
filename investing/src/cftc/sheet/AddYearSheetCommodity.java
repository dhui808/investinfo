package cftc.sheet;

import static cftc.utils.Constants.templateUrl;

import java.util.List;

import cftc.model.CftcInstrument;
import cftc.model.ProductList;

public class AddYearSheetCommodity extends AddYearSheet {

	protected String getTemplatePath() {
		return templateUrl + "template-commodity.ods";
	}

	protected int getTemplateColumnLength() {
		return 18;
	}

	protected String getSourceFilename() {
		 return "f_year.xls";
	}

	protected List<CftcInstrument> getProductList() {
		return ProductList.getCommodityProductList();
	}

	protected int getSourceColumnLength() {
		return 188;
	}

}
