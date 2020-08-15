package cftc.product;

import static cftc.utils.Constants.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.chart2.XChartDocument;
import com.sun.star.chart2.XFormattedString;
import com.sun.star.chart2.XTitle;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XCellRange;
import com.sun.star.table.XTableCharts;
import com.sun.star.table.XTableChartsSupplier;
import com.sun.star.uno.UnoRuntime;

import cftc.model.CftcInstrument;
import cftc.sheet.AddYearSheet;
import jloputility.Calc;
import jloputility.Chart2;
import jloputility.Lo;

public abstract class AbstractProductDocumentHandler extends AddYearSheet {

	protected abstract String getChartsTemplatePath();
	
	public void addProductDocument(CftcInstrument cftc) throws Exception {
		
		String year = "" + CURRENT_YEAR;
		
		//copy data template commodity/forex
		copyTemplateToAnalysis(cftc);
		//rename data sheet and analysis sheet
		String analysisFilePath = cftc.getAnalysisFilePath();
		XSpreadsheetDocument analysisDocument = loadDestDocument(analysisFilePath);
		
		//rename data sheet
		XSpreadsheet dataSheet = getSpreadsheet(analysisDocument, 1);
		String dataName = year + DATA_SHEET;
		setSheetName(dataSheet, dataName);
		
		//rename analysis sheet
		XSpreadsheet analysisSheet = getSpreadsheet(analysisDocument, 2);
		String analysisName = year+ ANALYSIS_SHEET;
		setSheetName(analysisSheet, analysisName);
		
		//copy data sheet
		String[] filters = cftc.getFilters();
		XSpreadsheetDocument sourceDocument = loadCftcSourceDocument(year);
		XSpreadsheet srcSheet = getSpreadsheet(sourceDocument, 0);
		int rows = updateDataSheet(srcSheet, dataSheet, filters, year);
				
		//remove extra template rows - analysis sheet
		deleteNoDataRows(analysisSheet, rows);
		
		//remove extra template rows - analysis sheet
//		deleteTemplateDataRows(analysisSheet);
		
		//remove extra template rows - data sheet
		deleteTemplateDataRows(dataSheet);
		
		//clear inventory, change and price columns - analysis sheet
		clearInventoryChangePrice(analysisSheet, cftc);
		
		//headers
		addInstrumentHeaders(analysisSheet, cftc);
		
		//copy charts template commodity/forex
		copyTemplateToCharts(cftc);
		
		//rename charts data sheet and charts sheet
		String chartsFilePath = cftc.getChartsFilePath();
		XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
		
		XSpreadsheet chartsDataSheet = getSpreadsheet(chartsDocument, 0);
		String chartsDataName = "" + CURRENT_YEAR + CHARTS_DATA_SHEET;
		setSheetName(chartsDataSheet, chartsDataName);
		
		XSpreadsheet chartsSheet = getSpreadsheet(chartsDocument, 1);
		String chartsName = "" + CURRENT_YEAR + CHARTS_SHEET;
		setSheetName(chartsSheet, chartsName);
		
		//copy and reverse analysis sheet to charts-data sheet
		updateChartsDataSheet(analysisSheet, chartsDataSheet);
		
		// replace the base year ref.
		updateChartsSheet(chartsDocument, year, BASE_YEAR);
		
		//clear inventory, change and price columns - charts data sheet
		clearInventoryChangePrice(chartsDataSheet, cftc);
		
		//clear extra template rows - charts data sheet
		clearRowsAtAndAfter(chartsDataSheet, cftc, rows + 1);
		
		//headers
		addInstrumentHeaders(chartsDataSheet, cftc);
				
		clearFormulaAtAndAfter(chartsDataSheet, cftc, rows + 1);
		
		setChartTitle(chartsSheet, cftc);
		
		Lo.closeDoc(sourceDocument);
		Lo.save(analysisDocument);
		Lo.closeDoc(analysisDocument);
		Lo.save(chartsDocument);
		Lo.closeDoc(chartsDocument);
	}

	private void copyTemplateToAnalysis(CftcInstrument cftc) throws IOException {
		
		String templatePath = getTemplatePath().substring(8);//remove file:///
		String analysisFilePath = cftc.getAnalysisFilePath().substring(8);
		File templateFile = new File(templatePath);
		File analysisFile = new File(analysisFilePath);
		
		Files.copy(templateFile.toPath(), analysisFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	private void copyTemplateToCharts(CftcInstrument cftc) throws IOException {
		
		String chartsTemplatePath = getChartsTemplatePath().substring(8);//remove file:///
		String chartsFilePath = cftc.getChartsFilePath().substring(8);
		File chartsTemplateFile = new File(chartsTemplatePath);
		File chartsFile = new File(chartsFilePath);

		Files.copy(chartsTemplateFile.toPath(), chartsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	protected void clearInventoryChangePrice(XSpreadsheet sheet, CftcInstrument cftc) throws IndexOutOfBoundsException {
		
		int inventoryColumnIndex = cftc.getInventoryPriceChangeColumnIndex();//assume change and price column follow inventory column.
		int inventoryColumnLength = cftc.getInventoryPriceChangeColumnLength();
		XCellRange xRange = sheet.getCellRangeByPosition(inventoryColumnIndex, 1, inventoryColumnIndex + inventoryColumnLength - 1, 53);
		com.sun.star.sheet.XCellRangeData xData = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				xRange);
		Object[][] xDataArray = xData.getDataArray();
		
		for (int i = 0; i < xDataArray.length; i++) {
			for (int j = 0; j < xDataArray[0].length; j++) {
				xDataArray[i][j] = "";
			}
		}
		
		xData.setDataArray(xDataArray);
	}
	
	protected void clearRowsAtAndAfter(XSpreadsheet sheet, CftcInstrument cftc, int idx) throws IndexOutOfBoundsException {
		
		int inventoryColumnIndex = cftc.getInventoryPriceChangeColumnIndex();//assume change and price column follow inventory column.
		XCellRange xRange = sheet.getCellRangeByPosition(0, idx, inventoryColumnIndex - 1, 53);
		com.sun.star.sheet.XCellRangeData xData = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				xRange);
		Object[][] xDataArray = xData.getDataArray();
		
		for (int i = 0; i < xDataArray.length; i++) {
			for (int j = 0; j < xDataArray[0].length; j++) {
				xDataArray[i][j] = "";
			}
		}
		
		xData.setDataArray(xDataArray);
	}
	
	protected void clearFormulaAtAndAfter(XSpreadsheet sheet, CftcInstrument cftc, int idx) throws IndexOutOfBoundsException {
		
		int calculatedColumnIndex = cftc.getFirstCalculatedColumnIndex();
		int calculatedColumnslength = cftc.getCalculatedColumnsLength();
		XCellRange xRange = sheet.getCellRangeByPosition(calculatedColumnIndex, idx, calculatedColumnIndex + calculatedColumnslength - 1, 53);
		com.sun.star.sheet.XCellRangeFormula xData = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeFormula.class,
				xRange);
		String[][] xDataArray = xData.getFormulaArray();
		
		for (int i = 0; i < xDataArray.length; i++) {
			for (int j = 0; j < xDataArray[0].length; j++) {
				xDataArray[i][j] = "";
			}
		}
		
		xData.setFormulaArray(xDataArray);
	}
	
	protected void setChartTitle(XSpreadsheet chartsSheet, CftcInstrument cftc) {

		XTableChartsSupplier chartsSupplier = Lo.qi(XTableChartsSupplier.class, chartsSheet);
		XTableCharts tableCharts = chartsSupplier.getCharts();
		String[] chartNames = tableCharts.getElementNames();

		for (String chartName : chartNames) {
			setChartTitleByChartName(chartsSheet, chartName, cftc);
		}
	}
	
	private void setChartTitleByChartName(XSpreadsheet chartsSheet, String chartName, CftcInstrument cftc) {
		XChartDocument chartDoc = Chart2.getChartDoc(chartsSheet, chartName);
		
		String title = cftc.getChartTitle();
		
		XTitle xTitle = Chart2.getTitle(chartDoc);
		XFormattedString[] titleArray = xTitle.getText();
		titleArray[0].setString(title);
		xTitle.setText(titleArray);
	}
	
	private void clearBackgroundColourAtAndAfter(XSpreadsheet sheet, CftcInstrument cftc, int idx) throws IndexOutOfBoundsException, IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException {
		
		int inventoryColumnIndex = cftc.getInventoryPriceChangeColumnIndex();//assume change and price column follow inventory column.
		XCellRange xRange = sheet.getCellRangeByPosition(0, idx, inventoryColumnIndex - 1, 53);
		XPropertySet xCellPropSet = (XPropertySet)
			    UnoRuntime.queryInterface(XPropertySet.class, xRange);
		
		
		xCellPropSet.setPropertyValue("CellBackColor", Integer.valueOf(0xffffff));
	}
}
