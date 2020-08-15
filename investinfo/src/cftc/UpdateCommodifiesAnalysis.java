package cftc;

import java.util.List;

import cftc.model.CftcInstrument;
import cftc.model.ProductList;

public abstract class UpdateCommodifiesAnalysis extends UpdateCftcAnalysis {

	protected List<CftcInstrument> getProductList() {
		
		return ProductList.getCommodityProductList();
	}
	
	protected String getSourceFilename() {
		 return "f_year.xls";//ng
	}
	
	protected String getSourceCellRange() {
		return "A2:GF2";//ng
	}
	
	protected String getLastRowCellRange(int row) {
		return "A" + row + ":R" + row;//ng
	}
	
	protected String getDestStartCellRange() {
		return "A1:R1";//ng
	}
	
	protected String getDestEndCellRange() {
		return "A2:R2";//ng
	}
	
	protected String getAnalysisPriceCellName() {
		return "U2";//ng
	}

	protected String getChartsPriceCellName(int row) {
		return "U" + row;
	}
	
	protected int getSourceColumnLength() {
		return 188;//ng
	}
	
	protected int getAnalysisSheetSourceColumnLength() {
		return 18;//ng
	}
}
