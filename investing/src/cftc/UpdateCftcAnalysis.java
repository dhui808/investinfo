package cftc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.chart2.ScaleData;
import com.sun.star.chart2.XAxis;
import com.sun.star.chart2.XChartDocument;
import com.sun.star.chart2.XDataSeries;
import com.sun.star.chart2.data.XDataProvider;
import com.sun.star.chart2.data.XDataSequence;
import com.sun.star.chart2.data.XDataSink;
import com.sun.star.chart2.data.XDataSource;
import com.sun.star.chart2.data.XLabeledDataSequence;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.sheet.XCellRangeFormula;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.table.XTableCharts;
import com.sun.star.table.XTableChartsSupplier;
import com.sun.star.table.XTableRows;
import com.sun.star.uno.UnoRuntime;

import cftc.dao.InventoryDao;
import cftc.dao.PriceIndexDao;
import cftc.model.CftcInstrument;
import cftc.utils.DateUtils;
import cftc.vendor.VendorName;

import static cftc.utils.Constants.*;
import jloputility.Calc;
import jloputility.Chart2;
import jloputility.Lo;
import jloputility.Props;

public abstract class UpdateCftcAnalysis extends AbstractCftcAnalysis {
	
	protected PriceIndexDao dao;
	
	/**
	 * Updates data and analysis
	 * @throws Exception 
	 * @throws RuntimeException 
	 */
	public void updateDataAnalysisByDate(String date) throws Exception {
		String year = date.substring(0, 4);
		updateDataAnalysis(year);
	}
	
	private void updateDataAnalysis(String year) throws Exception {
		
		XSpreadsheetDocument sourceDocument = loadCftcSourceDocument(year);
		XSpreadsheet srcSheet = getSpreadsheet(sourceDocument, 0);
		
		List<CftcInstrument> productList = getProductList();
		
		for (CftcInstrument cftc : productList) {
			
			String destFilePath = cftc.getAnalysisFilePath();
			String[] filters = cftc.getFilters();
			XSpreadsheetDocument destDocument = loadDestDocument(destFilePath);
			updateData(srcSheet, destDocument, filters);
			updateAnalysis(destDocument, filters);
			
			Lo.save(destDocument);
			Lo.closeDoc(destDocument);
		}
		
		Lo.closeDoc(sourceDocument);
	}

	public void updateDataAnalysis() throws Exception {
		
		String year = "" + CURRENT_YEAR;
		updateDataAnalysis(year);
	}
	
	/**
	 * Updates charts
	 * @throws Exception 
	 * @throws RuntimeException 
	 */
	public void updateCharts() throws Exception {
		
		List<CftcInstrument> productList = getProductList();
		
		for (CftcInstrument cftc : productList) {
			
			String destFilePath = cftc.getAnalysisFilePath();
			XSpreadsheetDocument analysisDocument = loadDestDocument(destFilePath);
			XSpreadsheet analysisSheet = getSpreadsheet(analysisDocument, 2);
			
			String chartsFilePath = cftc.getChartsFilePath();
			XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
			updateCharts(analysisSheet, chartsDocument);
			updateChartsNetLong(chartsDocument);
			updateChartsDataRange(chartsDocument);
			
			Lo.save(chartsDocument);
			Lo.closeDoc(chartsDocument);
			Lo.closeDoc(analysisDocument);
		}
	}
	
	public void upatePriceOrIndexInSpreadsheet(VendorName vendor, String date) throws Exception {
			
			List<CftcInstrument> productList = getProductList();
			
			String cftcDate =(null == date)? DateUtils.getCurrentWeekTuesdayDate() : date;
			String startDate =  DateUtils.getWeekStartDate(cftcDate);
			
			Map<String, Double> priceMap = dao.retrievePriceIndex(vendor.getPriceHistoryTablename(), cftcDate);
			
			if (0 == priceMap.size()) {
				System.out.println("No price/index data for " + startDate);
				return;
			}
			
			for (CftcInstrument cftc : productList) {
				
				String destFilePath = cftc.getAnalysisFilePath();
				XSpreadsheetDocument destDocument = loadDestDocument(destFilePath);
				XSpreadsheet destSheet2 = getSpreadsheet(destDocument, 2);
				Double price = priceMap.get(cftc.getInstrumentName());
				Calc.setVal(destSheet2, getAnalysisPriceCellName(), price);
				
				String chartsFilePath = cftc.getChartsFilePath();
				XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
				XSpreadsheet destSheet3 = getSpreadsheet(chartsDocument, 0);
				int row = getNumberOfRows(destSheet3);
				Calc.setVal(destSheet3, getChartsPriceCellName(row), price);
				
				//inverseRatio
				if (cftc.needInverseRatio()) {
					// in this case, price goes to the next column to the right and the the inverse of the price occupies 
					//the original the price column
					Calc.setVal(destSheet3, getChartsInverseRatioCellName(row), price);
					int multilier = cftc.getInverseMultiplier();
					BigDecimal bd = new BigDecimal(multilier / price);
					bd = bd.setScale(cftc.getPricePrecision(), RoundingMode.HALF_UP);
					Calc.setVal(destSheet3, getChartsPriceCellName(row), bd.doubleValue());
				}
				
				System.out.println(cftc.getInstrumentName() + " : " + price);
				
				Lo.save(destDocument);
				Lo.closeDoc(destDocument);
				Lo.save(chartsDocument);
				Lo.closeDoc(chartsDocument);
			}
	}

	public void upateInventoryInSpreadsheet(String date) throws Exception {

		List<CftcInstrument> productList = getProductList();
		
		String cftcDate = (null == date)? DateUtils.getCurrentWeekTuesdayDate() : date;
		String endDate =  DateUtils.getWeekEndDate(cftcDate);
		String previousCftDate = DateUtils.getPreviousWeekTuesdayDate(cftcDate);
		
		InventoryDao dao = new InventoryDao();
		Map<String, Integer> inventoryMap = dao.retrieveInventory("eia_history", cftcDate);
		Map<String, Integer> previousInventoryMap = dao.retrieveInventory("eia_history", previousCftDate);
		
		if (0 == inventoryMap.size()) {
			System.out.println("No inventory data for " + endDate);
			return;
		}
		
		for (CftcInstrument cftc : productList) {

			String destFilePath = cftc.getAnalysisFilePath();
			XSpreadsheetDocument destDocument = loadDestDocument(destFilePath);
			XSpreadsheet destSheet2 = getSpreadsheet(destDocument, 2);
	
			//calculate the current inventory and the change.
			Integer currentInv = inventoryMap.get(cftc.getInstrumentName());
			Integer previousInv = previousInventoryMap.get(cftc.getInstrumentName());
			String[] inv = new String[2];
			inv[0] = Integer.toString(currentInv);
			inv[1] = Integer.toString(currentInv - previousInv);
			
			String[] cellNames = cftc.getAnalysisInventoryCellName();
			Calc.setVal(destSheet2, cellNames[0], inv[0]);
			Calc.setVal(destSheet2, cellNames[1], inv[1]);
	
			String chartsFilePath = cftc.getChartsFilePath();
			XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
			XSpreadsheet destSheet3 = getSpreadsheet(chartsDocument, 0);
			int row = getNumberOfRows(destSheet3);
			String[] cellNames3 = cftc.getChartsInventoryCellName(row);
			Calc.setVal(destSheet3, cellNames3[0], inv[0]);
			Calc.setVal(destSheet3, cellNames3[1], inv[1]);
	
			System.out.println(cftc.getInstrumentName() + " : " + inv[0] + ", " + inv[1]);
	
			Lo.save(destDocument);
			Lo.closeDoc(destDocument);
			Lo.save(chartsDocument);
			Lo.closeDoc(chartsDocument);
		}
	}

	protected void updateData(XSpreadsheet srcSheet, XSpreadsheetDocument destDocument, String[] filters) throws Exception {
		// copy row
		XSpreadsheet destSheet = getSpreadsheet(destDocument, 1);
		XCellRange xRange1 = getFirstCftcRow(srcSheet, filters);
		com.sun.star.sheet.XCellRangeData xData = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				xRange1);
		
		Calc.insertRow(destSheet, 1);
		com.sun.star.table.XCellRange destCellRange = destSheet.getCellRangeByName(getSourceCellRange());
		com.sun.star.sheet.XCellRangeData destData = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				destCellRange);
		
		destData.setDataArray(xData.getDataArray());
	}
	
	protected XSpreadsheet updateAnalysis(XSpreadsheetDocument destDocument, String[] filters) throws Exception {
		
		XSpreadsheet destSheet2 = getSpreadsheet(destDocument, 2);
		Calc.insertRow(destSheet2, 1);
		com.sun.star.table.XCellRange dcRange1 = destSheet2.getCellRangeByName(getDestStartCellRange());
		com.sun.star.table.XCellRange dcRange2 = destSheet2.getCellRangeByName(getDestEndCellRange());
		XCellRangeFormula crForm1 = Lo.qi(XCellRangeFormula.class, dcRange1);
		XCellRangeFormula crForm2 = Lo.qi(XCellRangeFormula.class, dcRange2);
		crForm2.setFormulaArray(crForm1.getFormulaArray());
		applyFormula(crForm2);
		
		//child may need destSheet2
		return destSheet2;
	}
	
	protected void applyFormula(XCellRangeFormula destRange) throws IndexOutOfBoundsException {
		
		String[][] formula = destRange.getFormulaArray();
		for (int col = 0; col < formula[0].length; col++) {
			formula[0][col] = formula[0][col].substring(0, formula[0][col].length() - 1) + "2";
		}
		destRange.setFormulaArray(formula);
	}
	
	protected XSpreadsheet updateCharts(XSpreadsheet analysisSheet, XSpreadsheetDocument destDocument) throws Exception {
		
		//insert to the chart sheet too
		XSpreadsheet destSheet3 = getSpreadsheet(destDocument, 0);
		int row = 1 + getNumberOfRows(destSheet3);
		Calc.insertRow(destSheet3, row);//row is row number (i.e. row index + 1)
		com.sun.star.table.XCellRange destCellRange3 = destSheet3.getCellRangeByName(getLastRowCellRange(row));
		com.sun.star.sheet.XCellRangeData destData3 = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				destCellRange3);
		XCellRange xRange3 = analysisSheet.getCellRangeByPosition(0, 1, getAnalysisSheetSourceColumnLength() - 1, 1);
		com.sun.star.sheet.XCellRangeData xData3 = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				xRange3);
		destData3.setDataArray(xData3.getDataArray());
		
		com.sun.star.table.XCellRange headerCellRange3 = destSheet3.getCellRangeByPosition(0, 0, getAnalysisSheetSourceColumnLength() - 1, 0);
		applyBackgroundColour(headerCellRange3, destCellRange3);
		
		return analysisSheet;
	}

	/**
	 * Update the chart data range so it won't increment by one each time a new row is inserted.
	 * 
	 * @param year
	 */
	private void updateChartsDataRange(XSpreadsheetDocument chartsDocument) {

		String year = "" + CURRENT_YEAR;
		XSpreadsheet chartsSheet = getChartsSpreadsheetByYear(chartsDocument, year);
		
		XTableChartsSupplier chartsSupplier = Lo.qi(XTableChartsSupplier.class, chartsSheet);
		XTableCharts tableCharts = chartsSupplier.getCharts();
		String[] chartNames = tableCharts.getElementNames();

		for (String chartName : chartNames) {
			updateChartDataRangeByName(chartsSheet, chartName);
		}
	}
	
	private void updateChartDataRangeByName(XSpreadsheet chartsSheet, String chartName) {
		XChartDocument chartDoc = Chart2.getChartDoc(chartsSheet, chartName);

		XDataProvider dp = chartDoc.getDataProvider();
		
		XDataSeries[] ds = Chart2.getDataSeries(chartDoc);

		for (int i = 0; i < ds.length; i++) {
			
			XDataSeries dsi = ds[i];
			
			// y values
			XDataSource xDataSource = (XDataSource) UnoRuntime.queryInterface(XDataSource.class, dsi);
			XLabeledDataSequence[] xLabeledDS = xDataSource.getDataSequences();
			XLabeledDataSequence xLabeledDS0 = xLabeledDS[0];
			XDataSequence xDSValues = xLabeledDS0.getValues();
			
			String valuesRange = updateDataRange(xDSValues.getSourceRangeRepresentation());
			
			XDataSequence dataSeq = dp.createDataSequenceByRangeRepresentation(valuesRange);
			XPropertySet dsProps = Lo.qi(XPropertySet.class, dataSeq);
			Props.setProperty(dsProps, "Role", "values-y"); // specify data role (type)
			
			xLabeledDS0.setValues(dataSeq);
			
			XDataSink dataSink = Lo.qi(XDataSink.class, dsi);
			dataSink.setData(xLabeledDS);
		}
			
		// categories
		XAxis axis = Chart2.getAxis(chartDoc, Chart2.X_AXIS, 0);
		ScaleData sd = axis.getScaleData();
		XLabeledDataSequence cat = sd.Categories;
		XDataSequence catValues = cat.getValues();
		String catValuesRange = updateDataRange(catValues.getSourceRangeRepresentation());
		XDataSequence catDataSeq = dp.createDataSequenceByRangeRepresentation(catValuesRange);
		XPropertySet catDsProps = Lo.qi(XPropertySet.class, catDataSeq);
		Props.setProperty(catDsProps, "Role", "categories"); // specify data role (type)
		
		cat.setValues(catDataSeq);
	}
	
	// keep the dataRange $54 always.
	private String updateDataRange(String dataRange) {
		String s = dataRange.substring(dataRange.lastIndexOf('$'));

		return dataRange.replace(s, "$54");
	}
	
	protected void updateChartsNetLong(XSpreadsheetDocument destDocument) throws Exception {
		
		XSpreadsheet destSheet3 = getSpreadsheet(destDocument, 0);
		int row = getNumberOfRows(destSheet3);
		com.sun.star.table.XCellRange dcRange1 = destSheet3.getCellRangeByName(getChartsNetLongCellRange(row - 1));
		com.sun.star.table.XCellRange dcRange2 = destSheet3.getCellRangeByName(getChartsNetLongCellRange(row));
		XCellRangeFormula crForm1 = Lo.qi(XCellRangeFormula.class, dcRange1);
		XCellRangeFormula crForm2 = Lo.qi(XCellRangeFormula.class, dcRange2);
		crForm2.setFormulaArray(crForm1.getFormulaArray());
		applyNetLongFormula(crForm2, row);
		applyPercentageNumberFormat(dcRange1, dcRange2);
	}
	
	protected void applyNetLongFormula(XCellRangeFormula destRange, int row) throws IndexOutOfBoundsException {

		String sRow = String.valueOf(row);
		String sRow_1 =String.valueOf(row-1);
		String[][] formula = destRange.getFormulaArray();
		for (int col = 0; col < formula[0].length; col++) {
			formula[0][col] = formula[0][col].replace(sRow_1, sRow);
		}
		destRange.setFormulaArray(formula);
	}
	
	protected XCellRange getFirstCftcRow(XSpreadsheet xSheet, String[] filters) throws IndexOutOfBoundsException {
		XCell xCell = null;
		int row;

		//hopefully each file maximum 90000 lines
		LOOP:
		for (row = 1; row <  CFTC_MAX_LINES; row++) {
			xCell = xSheet.getCellByPosition(0, row);
			for (int i = 0; i < filters.length; i++) {
				if (filters[i].equals(xCell.getFormula()) || xCell.getFormula().contains(filters[i])) {
					break LOOP;
				}
			}
		}

		XCellRange xRange = xSheet.getCellRangeByPosition(0, row, getSourceColumnLength() - 1, row);

		return xRange;
	}

	public void deleteByDate(String date) throws Exception {
		
		List<CftcInstrument> productList = getProductList();
		
		for (CftcInstrument cftc : productList) {
			
			//charts sheet
			String chartsFilePath = cftc.getChartsFilePath();
			XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
			XSpreadsheet xSheet = getSpreadsheet(chartsDocument, 0);
			int row = getNumberOfRows(xSheet) - 1;
			deleteRowFromSheet(xSheet, 0, row, date);
			
			Lo.save(chartsDocument);
			Lo.closeDoc(chartsDocument);
			
			String destFilePath = cftc.getAnalysisFilePath();
			XSpreadsheetDocument destDocument = loadDestDocument(destFilePath);
			
			//two sheets: data, analysis
			for (int sheetIndex = 2; sheetIndex > 0; sheetIndex--) {
				XSpreadsheet xSheet1 = getSpreadsheet(destDocument, sheetIndex);
				deleteRowFromSheet(xSheet1, sheetIndex, 1, date);
			}
			
			Lo.save(destDocument);
			Lo.closeDoc(destDocument);
		}
	}
	
	private void deleteRowFromSheet(XSpreadsheet xSheet, int sheetIndex, int row, String columnValue) throws IndexOutOfBoundsException, WrappedTargetException {
		
		XCell xCell = xSheet.getCellByPosition(1, row);//date column
		if (columnValue.equals(xCell.getFormula()) || Double.parseDouble(columnValue) == xCell.getValue()) {
			deleteRow(xSheet, row);
		}
	}
	
	private void deleteRow(XSpreadsheet sheet, int idx) {
		com.sun.star.table.XColumnRowRange crRange = Lo.qi(com.sun.star.table.XColumnRowRange.class, sheet);
		XTableRows rows = crRange.getRows();
		rows.removeByIndex(idx, 1); // remove 1 row at idx position
	}
	
	private void applyBackgroundColour(com.sun.star.table.XCellRange srcCellRange, com.sun.star.table.XCellRange destCellRange) throws IndexOutOfBoundsException, IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException {
		
		int colLength = getAnalysisSheetSourceColumnLength();
		
		for (int i = 0; i < colLength; i++) {
			com.sun.star.table.XCell srcCell = srcCellRange.getCellByPosition(i, 0);
			com.sun.star.table.XCell destCell = destCellRange.getCellByPosition(i, 0);
			com.sun.star.beans.XPropertySet srcPropSet = UnoRuntime.queryInterface( com.sun.star.beans.XPropertySet.class, srcCell);
			com.sun.star.beans.XPropertySet destPropSet = UnoRuntime.queryInterface( com.sun.star.beans.XPropertySet.class, destCell);
			
			destPropSet.setPropertyValue("CellBackColor", srcPropSet.getPropertyValue("CellBackColor"));
		}
	}
	
	//apply Percentage NumberFormat for the last cell in the row (index=4)
	private void applyPercentageNumberFormat(com.sun.star.table.XCellRange srcCellRange, com.sun.star.table.XCellRange destCellRange) throws IndexOutOfBoundsException, IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException {
		
		int colIndex = 4;
		com.sun.star.table.XCell srcCell = srcCellRange.getCellByPosition(colIndex, 0);
		com.sun.star.table.XCell destCell = destCellRange.getCellByPosition(colIndex, 0);
		com.sun.star.beans.XPropertySet srcPropSet = UnoRuntime.queryInterface( com.sun.star.beans.XPropertySet.class, srcCell);
		com.sun.star.beans.XPropertySet destPropSet = UnoRuntime.queryInterface( com.sun.star.beans.XPropertySet.class, destCell);
			
		destPropSet.setPropertyValue("NumberFormat", srcPropSet.getPropertyValue("NumberFormat"));

	}

	protected String getDateCellRange() {
		return "B2:B53";//single year date column
	}
	
	protected String getChartsInverseRatioCellName(int row) {
		//only certain forex products need to override this.
		//Otherwise it is NOT used.
		return getChartsPriceCellName(row);
	}
	
	protected String getChartsNetLongCellRange(int row) {
		return "Y" + row + ":AD" + row;
	}
	
	protected abstract String getSourceCellRange();
	protected abstract String getLastRowCellRange(int row);
	protected abstract String getAnalysisPriceCellName();
	protected abstract String getChartsPriceCellName(int row);
	protected abstract int getAnalysisSheetSourceColumnLength();
	protected abstract String getDestStartCellRange();
	protected abstract String getDestEndCellRange();
}
