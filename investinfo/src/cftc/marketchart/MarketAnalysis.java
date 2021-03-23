package cftc.marketchart;

import static cftc.utils.Constants.productsUrl;
import static cftc.utils.Constants.templateUrl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;

import cftc.AbstractCftcAnalysis;
import cftc.InvestInfoException;
import cftc.model.CftcInstrument;
import cftc.model.ProductList;
import jloputility.Lo;

public class MarketAnalysis extends AbstractCftcAnalysis {

	private MarketDao marketDao = new MarketDao();
	
	@Override
	protected String getSourceFilename() {
		
		return null;
	}

	@Override
	protected List<CftcInstrument> getProductList() {
		
		return ProductList.getMarketProductList();
	}

	@Override
	protected int getSourceColumnLength() {
		
		return 0;
	}

	public void createMarketChart() throws Exception {
		
		MarketData marketData = marketDao.retrieveAllMarketData();
		XSpreadsheetDocument chartsDocument = createMarketDocument();
		XSpreadsheet chartsDataSheet = createMarketDataSheet(chartsDocument);

		//copy marketData to charts-data sheet
		updateMarketChartsDataSheet(marketData, chartsDataSheet);
		
		Lo.save(chartsDocument);
		Lo.closeDoc(chartsDocument);
	}

	private void updateMarketChartsDataSheet(MarketData marketData, XSpreadsheet chartsDataSheet) throws Exception {

		int rows = marketData.getReleaseDateList().size();
		Object[][] xFormulaArray0 = createFormulaArray(marketData);

		com.sun.star.table.XCellRange destHeaderCellRange = chartsDataSheet.getCellRangeByPosition(0, 0,
				5, rows - 1);
		com.sun.star.sheet.XCellRangeData crFormula = Lo.qi(com.sun.star.sheet.XCellRangeData.class,
				destHeaderCellRange);

		// not sure why need to copy data to another array.
		Object[][] x = new Object[rows][6];
		
		for (int i=0;i<rows;i++) {
			x[i]=xFormulaArray0[i];
		}
		
	    crFormula.setDataArray(x);
	}
	
	private Object[][] createFormulaArray(MarketData marketData) {

		int rows = marketData.getReleaseDateList().size();
		Object[][] xFormulaArray = new Object[1 + rows][6];
		
		xFormulaArray[0][0] = "Week Start Date";
		xFormulaArray[0][1] = "USD Index";
		xFormulaArray[0][2] = "US 10Y Yield * 1000";
		xFormulaArray[0][3] = "SPX500";
		xFormulaArray[0][4] = "Dow30";
		xFormulaArray[0][5] = "NASDAQ";
		
		List<String> releaseDateList = marketData.getReleaseDateList();
		List<Double> usdIndexList = marketData.getUsdIndexList();
		List<Double> us10yList = marketData.getUs10yList();
		List<Double> spx500List = marketData.getSpx500List();
		List<Double> dow30List = marketData.getDow30List();
		List<Double> nasdaqList = marketData.getNasdaqList();
		
		for (int i = 1; i < rows; i++) {
			xFormulaArray[i][0] = releaseDateList.get(i);
			xFormulaArray[i][1] = usdIndexList.get(i);
			xFormulaArray[i][2] = us10yList.get(i);
			xFormulaArray[i][3] = spx500List.get(i);
			xFormulaArray[i][4] = dow30List.get(i);
			xFormulaArray[i][5] = nasdaqList.get(i);
		}
		
		return xFormulaArray;
	}

	// Creates market document from template.
	private XSpreadsheetDocument createMarketDocument() {
		
		try {
			copyTemplateToMarketChart();
		} catch (IOException e) {
			e.printStackTrace();
			throw new InvestInfoException("Error in copyTemplateToMarketChart().");
		}
		
		try {
			String chartsFilePath = getMarketChartsFilePath();
			XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
			return chartsDocument;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvestInfoException("Error in loadDestDocument().");
		}
	}
	
	private XSpreadsheet createMarketDataSheet(XSpreadsheetDocument chartsDocument) {
		
		try {
			XSpreadsheet chartsDataSheet = getSpreadsheet(chartsDocument, 0);
			return chartsDataSheet;
		} catch (IndexOutOfBoundsException | WrappedTargetException e) {
			e.printStackTrace();
			throw new InvestInfoException("Error in getSpreadsheet().");
		}
	}

	public void updateMarketChart() {
		

	}

	private void copyTemplateToMarketChart() throws IOException {
		
		String chartsTemplatePath = getMarketChartsTemplatePath().substring(8);//remove file:///
		String chartsFilePath = getMarketChartsFilePath().substring(8);
		File chartsTemplateFile = new File(chartsTemplatePath);
		File chartsFile = new File(chartsFilePath);

		Files.copy(chartsTemplateFile.toPath(), chartsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	private String getMarketChartsTemplatePath() {
		return templateUrl + "template-market-charts.ods";
	}
	
	private String getMarketChartsFilePath() {
		return productsUrl + "market-charts.ods";
	}
}
