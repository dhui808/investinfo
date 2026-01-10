package cftc.sheet;

import static cftc.utils.Constants.*;

import java.util.List;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.chart.ChartDataRowSource;
import com.sun.star.chart2.ScaleData;
import com.sun.star.chart2.XAxis;
import com.sun.star.chart2.XChartDocument;
import com.sun.star.chart2.XDataSeries;
import com.sun.star.chart2.data.XDataProvider;
import com.sun.star.chart2.data.XDataSequence;
import com.sun.star.chart2.data.XDataSink;
import com.sun.star.chart2.data.XDataSource;
import com.sun.star.chart2.data.XLabeledDataSequence;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.sheet.XCellRangeData;
import com.sun.star.sheet.XCellRangeFormula;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.CellRangeAddress;
import com.sun.star.table.XCellRange;
import com.sun.star.table.XTableCharts;
import com.sun.star.table.XTableChartsSupplier;
import com.sun.star.uno.UnoRuntime;

import cftc.model.CftcInstrument;
import jloputility.Calc;
import jloputility.Chart2;
import jloputility.Lo;
import jloputility.Props;

public abstract class AddYearSheet extends AbstractHandleYearSheet {
	
	/**
	 * Adds data and analysis for the specified instrument only.
	 * 
	 * @throws Exception
	 * @throws RuntimeException
	 */
	public void addDataAnalysis(CftcInstrument cftc, String year) throws Exception {

		XSpreadsheetDocument sourceDocument = loadCftcSourceDocument(year);
		XSpreadsheet srcSheet = getSpreadsheet(sourceDocument, 0);

		String destFilePath = cftc.getAnalysisFilePath();
		String[] filters = cftc.getFilters();
		XSpreadsheetDocument destDocument = loadDestDocument(destFilePath);

		XSpreadsheet xSheet = getDataSpreadsheetByYear(destDocument, year);
		if (null != xSheet) {
			System.out.println("Sheet " + year + " already exists for " + cftc.getInstrumentName());
			Lo.closeDoc(destDocument);
			Lo.closeDoc(sourceDocument);
			return;
		}

		System.out.println("Add data and analysis Sheet " + year + " for " + cftc.getInstrumentName());
		
		int idx = calculateNewDataSheetIndex(destDocument, year);
		//for the year before the current year but after TEMPLATE_BASE_YEAR, temporarily uses TEMPLATE_BASE_YEAR as the data sheet name.
		String dataSheetName = getDataSheetTempName(year);
		XSpreadsheet destDataSheet = insertSheet(destDocument, dataSheetName, idx); 
		int dataRows = updateDataSheet(srcSheet, destDataSheet, filters, year);
		
		String templatePath = getTemplatePath();
		XSpreadsheetDocument templateDocument = loadTemplateDocument(templatePath);
		XSpreadsheet templateSheet = getSpreadsheet(templateDocument, 2);
		XSpreadsheet destAnalysisSheet = insertSheet(destDocument, year + ANALYSIS_SHEET, idx + 1);
		updateAnalysisSheet(templateSheet, destAnalysisSheet, year);

		//now rename the data sheet back to the correct one
		String dataName = year + DATA_SHEET;
		setSheetName(destDataSheet, dataName);
		
		//remove extra template rows - analysis sheet
		deleteNoDataRows(destAnalysisSheet, dataRows);
				
		// copy instrument-specific column headers
		addInstrumentHeaders(destAnalysisSheet, cftc);

		Lo.save(destDocument);
		Lo.closeDoc(destDocument);
		Lo.closeDoc(templateDocument);

		Lo.closeDoc(sourceDocument);
	}

	/**
	 * Returns the TEMPLATE_BASE_YEAR if the year is between the BASE_YEAR (current year) and the TEMPLATE_BASE_YEAR. Otherwise returns year.
	 * @param year
	 * @return
	 */
	private String getDataSheetTempName(String year) {

		return Integer.valueOf(BASE_YEAR) > Integer.valueOf(year) && Integer.valueOf(year) > Integer.valueOf(TEMPLATE_BASE_YEAR)? TEMPLATE_BASE_YEAR + DATA_SHEET : year + DATA_SHEET;
	}

	/**
	 * Adds data and analysis for all instruments
	 * 
	 * @throws Exception
	 * @throws RuntimeException
	 */
	public void addDataAnalysis(String year) throws Exception {

		List<CftcInstrument> productList = getProductList();

		for (CftcInstrument cftc : productList) {

			addDataAnalysis(cftc, year);
		}
	}
	
	public void addDataChartsPreviousYear(CftcInstrument cftc, String year) throws Exception {
		addDataCharts(cftc, year, "" + CURRENT_YEAR, BASE_YEAR_CHARTS_DATA_SHEET, BASE_YEAR_CHARTS_SHEET);
	}
	
	public void addDataChartsCurrentYear(CftcInstrument cftc, String year) throws Exception {
		addDataCharts(cftc, year, "" + (CURRENT_YEAR - 1), PREVIOUS_YEAR_CHARTS_DATA_SHEET, PREVIOUS_YEAR_CHARTS_SHEET);
	}
	
	/**
	 * Adds chart data and chart sheets for the specified instrument .
	 * 
	 * @throws Exception
	 * @throws RuntimeException
	 */
	public void addDataCharts(CftcInstrument cftc, String year, String baseYear, String baseYearChartsDataSheet, String baseYearChartsSheet) throws Exception {

		String analysisFilePath = cftc.getAnalysisFilePath();
		XSpreadsheetDocument analysisDocument = loadDestDocument(analysisFilePath);
		XSpreadsheet xSheet = getAnalysisSpreadsheetByYear(analysisDocument, year);

		String chartsFilePath = cftc.getChartsFilePath();
		XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
		XSpreadsheet chartsDataSheet = getChartsDataSpreadsheetByYear(chartsDocument, year);

		if (null != chartsDataSheet) {
			System.out.println("Charts data Sheet " + year + " already exists for " + cftc.getInstrumentName());
			Lo.closeDoc(analysisDocument);
			Lo.closeDoc(chartsDocument);
			return;
		}

		System.out.println("Add Charts data Sheet " + year + " for " + cftc.getInstrumentName());
		
		int idx = calculateNewChartsDataSheetIndex(analysisDocument, year);
		chartsDocument.getSheets().copyByName(baseYearChartsDataSheet, year + CHARTS_DATA_SHEET, (short) (idx));
		chartsDataSheet = getChartsDataSpreadsheetByYear(chartsDocument, year);
		int rows = updateChartsDataSheet(xSheet, chartsDataSheet);

		//remove extra template rows - charts data sheet
		deleteNoDataRows(chartsDataSheet, rows);
		
		chartsDocument.getSheets().copyByName(baseYearChartsSheet, year + CHARTS_SHEET, (short) (idx + 1));
		updateChartsSheet(chartsDocument, year, baseYear);

		Lo.save(analysisDocument);
		Lo.closeDoc(analysisDocument);
		Lo.save(chartsDocument);
		Lo.closeDoc(chartsDocument);
	}
	
	/**
	 * Adds chart data and chart sheets for all instruments.
	 * 
	 * @throws Exception
	 * @throws RuntimeException
	 */
	public void addDataChartsPreviousYear(String year) throws Exception {

		List<CftcInstrument> productList = getProductList();

		for (CftcInstrument cftc : productList) {

			addDataChartsPreviousYear(cftc, year);
		}
	}

	public void addDataChartsCurrentYear(String year) throws Exception {

		List<CftcInstrument> productList = getProductList();

		for (CftcInstrument cftc : productList) {

			addDataChartsCurrentYear(cftc, year);
		}
	}
	
	private XSpreadsheet insertSheet(XSpreadsheetDocument destDocument, String name, int idx) {
		System.out.println("Insert sheet " + name + ", inde " + idx);
		XSpreadsheet destDataSheet = Calc.insertSheet(destDocument, name, (short) idx);
		return destDataSheet;
	}

	/**
	 * Returns the number of data rows (excluding header row).
	 * 
	 * @param srcSheet
	 * @param destSheet
	 * @param filter
	 * @param year
	 * @return
	 * @throws RuntimeException
	 * @throws Exception
	 */
	protected int updateDataSheet(XSpreadsheet srcSheet, XSpreadsheet destSheet, String[] filters, String year)
			throws Exception {

		// copy the first row - headers
		XCellRange xRange0 = srcSheet.getCellRangeByPosition(0, 0, getSourceColumnLength() - 1, 0);
		com.sun.star.sheet.XCellRangeData xData0 = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				xRange0);
		Object[][] xDataArray0 = xData0.getDataArray();

		com.sun.star.table.XCellRange destHeaderCellRange = destSheet.getCellRangeByPosition(0, 0,
				xDataArray0[0].length - 1, 0);
		com.sun.star.sheet.XCellRangeData destHeaderData = UnoRuntime
				.queryInterface(com.sun.star.sheet.XCellRangeData.class, destHeaderCellRange);

		destHeaderData.setDataArray(xDataArray0);

		// copy data rows
		XCellRange xRange1 = getAllCftcRows(srcSheet, filters, year);
		com.sun.star.sheet.XCellRangeData xData = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				xRange1);
		Object[][] xDataArray = xData.getDataArray();

		com.sun.star.table.XCellRange destCellRange = destSheet.getCellRangeByPosition(0, 1, xDataArray[0].length - 1,
				xDataArray.length);
		com.sun.star.sheet.XCellRangeData destData = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				destCellRange);

		destData.setDataArray(xDataArray);
		
		return xDataArray.length;
	}

	protected XSpreadsheet updateAnalysisSheet(XSpreadsheet templateSheet, XSpreadsheet destSheet, String year )
			throws Exception {

		XCellRange xRange0 = templateSheet.getCellRangeByPosition(0, 0, getTemplateColumnLength() - 1, 53);
		XCellRangeFormula crFormula0 = Lo.qi(XCellRangeFormula.class, xRange0);
		String[][] xFormulaArray0 = crFormula0.getFormulaArray();

		com.sun.star.table.XCellRange destHeaderCellRange = destSheet.getCellRangeByPosition(0, 0,
				getTemplateColumnLength() - 1, 53);
		XCellRangeFormula crFormula = Lo.qi(XCellRangeFormula.class, destHeaderCellRange);

		crFormula.setFormulaArray(xFormulaArray0);
		//for the year before the current year but after TEMPLATE_BASE_YEAR, do not apply formula.
		if (Integer.valueOf(year).intValue() <= Integer.valueOf(TEMPLATE_BASE_YEAR) || Integer.valueOf(year).intValue() == Integer.valueOf(BASE_YEAR)) {
			applyTemplateFormula(crFormula, year);
		}

		// child may need destSheet
		return destSheet;
	}

	private void applyTemplateFormula(XCellRangeFormula destRange, String year) throws IndexOutOfBoundsException {

		String[][] formula = destRange.getFormulaArray();
		for (int row = 0; row < formula.length; row++) {
			for (int col = 0; col < formula[row].length; col++) {
				formula[row][col] = formula[row][col].replace(TEMPLATE_BASE_YEAR, year);
			}
		}

		destRange.setFormulaArray(formula);
	}

	protected void addInstrumentHeaders(XSpreadsheet destAnalysisSheet, CftcInstrument cftc)
			throws IndexOutOfBoundsException {
		String[] headers = cftc.getIndexPriceInventoryColumns();
		int index = getTemplateColumnLength();
		int headersLen = headers.length;

		com.sun.star.table.XCellRange destHeaderCellRange = destAnalysisSheet.getCellRangeByPosition(index, 0,
				index + headersLen - 1, 0);
		XCellRangeData crFormula = Lo.qi(XCellRangeData.class, destHeaderCellRange);
		String[][] formulaArray = new String[1][headersLen];
		
		for (int i = 0; i < headersLen; i++) {
			formulaArray[0][i] = headers[i];
		}
		crFormula.setDataArray(formulaArray);
	}

	public void removeDataAnalysis(CftcInstrument cftc, String year) throws Exception {
		
		String destFilePath = cftc.getAnalysisFilePath();
		XSpreadsheetDocument destDocument = loadDestDocument(destFilePath);

		XSpreadsheet xSheet = getDataSpreadsheetByYear(destDocument, year);
		if (null != xSheet) {
			Calc.removeSheet(destDocument, year + DATA_SHEET);
			Calc.removeSheet(destDocument, year + ANALYSIS_SHEET);
		} else {
			System.out.println("Sheet " + year + " does not exist.");
		}

		Lo.save(destDocument);
		Lo.closeDoc(destDocument);
	}
	
	public void removeDataAnalysis(String year) throws Exception {

		List<CftcInstrument> productList = getProductList();

		for (CftcInstrument cftc : productList) {
			
			removeDataAnalysis(cftc, year);
		}
	}
	
	public void removeCharts(CftcInstrument cftc, String year) throws Exception {
		
		String destFilePath = cftc.getChartsFilePath();
		XSpreadsheetDocument destDocument = loadDestDocument(destFilePath);

		XSpreadsheet xSheet = getChartsDataSpreadsheetByYear(destDocument, year);
		if (null != xSheet) {
			Calc.removeSheet(destDocument, year + CHARTS_DATA_SHEET);
			Calc.removeSheet(destDocument, year + CHARTS_SHEET);
		} else {
			System.out.println("Sheet " + year + " does not exist.");
		}

		Lo.save(destDocument);
		Lo.closeDoc(destDocument);
	}
	
	public void removeCharts(String year) throws Exception {

		List<CftcInstrument> productList = getProductList();

		for (CftcInstrument cftc : productList) {
			
			removeCharts(cftc, year);
		}
	}

	/**
	 * Updates the charts data sheet except the header row. Returns the number of data rows (excluding header row).
	 * 
	 * @param analysisSheet
	 * @param chartsDataSheet
	 * @return charts data sheet rows
	 * @throws RuntimeException
	 * @throws Exception
	 */
	protected int updateChartsDataSheet(XSpreadsheet analysisSheet, XSpreadsheet chartsDataSheet)
			throws Exception {

		int rows = getNumberOfRows(analysisSheet);
		XCellRange xRange0 = analysisSheet.getCellRangeByPosition(0, 1, getTemplateColumnLength() - 1, rows - 1);
		com.sun.star.sheet.XCellRangeData crFormula0 = Lo.qi(com.sun.star.sheet.XCellRangeData.class, xRange0);
		Object[][] xFormulaArray0 = crFormula0.getDataArray();

		Object[][] reversedFormulaArray = reverseArray(xFormulaArray0);
		com.sun.star.table.XCellRange destHeaderCellRange = chartsDataSheet.getCellRangeByPosition(0, 1,
				getTemplateColumnLength() - 1, rows - 1);
		com.sun.star.sheet.XCellRangeData crFormula = Lo.qi(com.sun.star.sheet.XCellRangeData.class,
				destHeaderCellRange);

		crFormula.setDataArray(reversedFormulaArray);

		String netLongColumnCellRange = "Y2:AD" + String.valueOf(rows);
		com.sun.star.table.XCellRange dcRange = chartsDataSheet.getCellRangeByName(netLongColumnCellRange);
		XCellRangeFormula crForm = Lo.qi(XCellRangeFormula.class, dcRange);
		applyFormula(crForm);

		return rows - 1;
	}

	// Applies formula from 1st data row (row2) to other rows
	protected void applyFormula(XCellRangeFormula destRange) throws IndexOutOfBoundsException {

		String rowStr = null;

		String[][] formula = destRange.getFormulaArray();
		for (int row = 1; row < formula.length; row++) {
			rowStr = String.valueOf(row + 2);
			for (int col = 0; col < formula[0].length; col++) {
				formula[row][col] = formula[0][col].replace("2", rowStr);
			}
		}
		destRange.setFormulaArray(formula);
	}

	/**
	 * Replaces the base year references in the charts to the specified year.
	 * 
	 * @param year
	 */
	protected void updateChartsSheet(XSpreadsheetDocument chartsDocument, String year, String baseYear) {

		XSpreadsheet chartsSheet = getChartsSpreadsheetByYear(chartsDocument, year);
		XSpreadsheet chartsDataSheet = getChartsDataSpreadsheetByYear(chartsDocument, year);
		
		XTableChartsSupplier chartsSupplier = Lo.qi(XTableChartsSupplier.class, chartsSheet);
		XTableCharts tableCharts = chartsSupplier.getCharts();
		String[] chartNames = tableCharts.getElementNames();

		for (String chartName : chartNames) {
			updateChartByName(chartsSheet, chartsDataSheet, chartName, year, baseYear);
		}
	}
	
	private void updateChartByName(XSpreadsheet chartsSheet, XSpreadsheet chartsDataSheet, String chartName, String year, String baseYear) {
		XChartDocument chartDoc = Chart2.getChartDoc(chartsSheet, chartName);

		XDataProvider dp = chartDoc.getDataProvider();
		
		XDataSeries[] ds = Chart2.getDataSeries(chartDoc);

		for (int i = 0; i < ds.length; i++) {
			
			XDataSeries dsi = ds[i];
			
			// y values
			XDataSource xDataSource = (XDataSource) UnoRuntime.queryInterface(XDataSource.class, dsi);
			XLabeledDataSequence[] xLabeledDS = xDataSource.getDataSequences();
			XLabeledDataSequence xLabeledDS0 = xLabeledDS[0];
			XDataSequence xDSLabel = xLabeledDS0.getLabel();
			XDataSequence xDSValues = xLabeledDS0.getValues();
			
			String valuesRange = xDSValues.getSourceRangeRepresentation().replace(baseYear, year);
			String labelRange = xDSLabel.getSourceRangeRepresentation().replace(baseYear, year);
			
			XDataSequence dataSeq = dp.createDataSequenceByRangeRepresentation(valuesRange);
			XDataSequence labelSeq = dp.createDataSequenceByRangeRepresentation(labelRange);
			XPropertySet dsProps = Lo.qi(XPropertySet.class, dataSeq);
			Props.setProperty(dsProps, "Role", "values-y"); // specify data role (type)
			
			xLabeledDS0.setValues(dataSeq);
			xLabeledDS0.setLabel(labelSeq);
			
			XDataSink dataSink = Lo.qi(XDataSink.class, dsi);
			dataSink.setData(xLabeledDS);
		}
			
		// categories
		XAxis axis = Chart2.getAxis(chartDoc, Chart2.X_AXIS, 0);
		ScaleData sd = axis.getScaleData();
		XLabeledDataSequence cat = sd.Categories;
		XDataSequence catValues = cat.getValues();
		String catValuesRange = catValues.getSourceRangeRepresentation().replace(baseYear, year);
		XDataSequence catDataSeq = dp.createDataSequenceByRangeRepresentation(catValuesRange);
		XPropertySet catDsProps = Lo.qi(XPropertySet.class, catDataSeq);
		Props.setProperty(catDsProps, "Role", "categories"); // specify data role (type)
		
		cat.setValues(catDataSeq);
	}

	/**
	 * Assume the data sheet has at least one row with the valid data. The template data rows are removed.
	 * @param sheet
	 */
	protected void deleteTemplateDataRows(XSpreadsheet sheet) {
		
		XCellRange xRange = sheet.getCellRangeByName("B2"); //first data row
		com.sun.star.sheet.XCellRangeData cellRangedata = Lo.qi(com.sun.star.sheet.XCellRangeData.class, xRange);
		Object[][] xData = cellRangedata.getDataArray();
		String a1 = xData[0][0].toString();
		String year = a1.substring(0, 2);
		
		XCellRange xRange0 = sheet.getCellRangeByName("B3:B54");
		com.sun.star.sheet.XCellRangeData cellRangedata0 = Lo.qi(com.sun.star.sheet.XCellRangeData.class, xRange0);
		Object[][] xData0 = cellRangedata0.getDataArray();
		
		for (int i = 0; i < xData0.length; i++) {
			
			Object obj = xData0[i][0];
			if ("".equals(obj)) {
				break;
			}
			
			if (!year.equals(obj.toString().substring(0, 2))) {
				
				//delete rows at and after row index i + 2
				deleteRowsAtAndAfter(sheet, i + 2);
				
				break;
			}
		}
	}

	/**
	 * Deletes rows that are larger than the data rows
	 * @param sheet
	 * @param dataRows
	 */
	protected void deleteNoDataRows(XSpreadsheet sheet, int dataRows) {
		
		//delete rows at and after row index dataRows + 1
		deleteRowsAtAndAfter(sheet, dataRows + 1);
	}

	protected void deleteRowsAtAndAfter(XSpreadsheet sheet, int idx) {
		
		for (int i = 53; i >= idx; i--) {
			Calc.deleteRow(sheet, i);
		}
	}
	
	protected void setSheetName(XSpreadsheet sheet, String name) {
		Calc.setSheetName(sheet, name);
	}
	
}
