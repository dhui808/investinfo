package cftc;

import java.util.List;

import cftc.model.CftcInstrument;
import cftc.model.ProductList;

public abstract class UpdateForexAnalysis extends UpdateCftcAnalysis {

	protected List<CftcInstrument> getProductList() {
		
		return ProductList.getForexProductList();
	}
	
	protected String getSourceFilename() {
		 return "FinFutYY.xls";//forex
	}
	
	protected String getSourceCellRange() {
		return "A2:CF2";//forex
	}
	
	protected String getDestStartCellRange() {
		return "A1:V1";//forex
	}
	
	protected String getDestEndCellRange() {
		return "A2:V2";//forex
	}
	
	protected String getAnalysisPriceCellName() {
		return "W2";//forex
	}

	protected String getChartsPriceCellName(int row) {
		return "W" + row;
	}
	
	protected String getChartsInverseRatioCellName(int row) {
		return "X" + row;
	}
	
	protected int getSourceColumnLength() {
		return 84;//forex
	}
	
	protected int getAnalysisSheetSourceColumnLength() {
		return 22;//forex
	}

	protected String getLastRowCellRange(int row) {
		return "A" + row + ":V" + row;//forex
	}
}
