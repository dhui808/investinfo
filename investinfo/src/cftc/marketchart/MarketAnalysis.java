package cftc.marketchart;

import static cftc.utils.Constants.productsUrl;
import static cftc.utils.Constants.templateUrl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import com.sun.star.beans.Property;
import com.sun.star.beans.XPropertySet;
import com.sun.star.chart2.ScaleData;
import com.sun.star.chart2.XAxis;
import com.sun.star.chart2.XChartDocument;
import com.sun.star.chart2.XDataSeries;
import com.sun.star.chart2.data.XDataProvider;
import com.sun.star.chart2.data.XDataSequence;
import com.sun.star.chart2.data.XDataSource;
import com.sun.star.chart2.data.XLabeledDataSequence;
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

import cftc.AbstractCftcAnalysis;
import cftc.InvestInfoException;
import cftc.dao.PriceIndexDao;
import cftc.model.CftcInstrument;
import cftc.model.ProductList;
import cftc.utils.CftcException;
import cftc.utils.DateUtils;
import cftc.vendor.VendorName;
import cftc.vendor.investingcom.InvestingComPriceIndexDao;
import jloputility.Calc;
import jloputility.Chart2;
import jloputility.Lo;
import jloputility.Props;

public class MarketAnalysis extends AbstractCftcAnalysis {

	private int maxColumnIndex = 7;
	private MarketDao marketDao = new MarketDao();
	private PriceIndexDao dao = new InvestingComPriceIndexDao();
	
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
		XSpreadsheet chartsDataSheet = getMarketDataSheet(chartsDocument);

		//copy marketData to charts-data sheet
		updateMarketChartsDataSheet(marketData, chartsDataSheet);
		
		Lo.save(chartsDocument);
		Lo.closeDoc(chartsDocument);
	}

	private void updateMarketChartsDataSheet(MarketData marketData, XSpreadsheet chartsDataSheet) throws Exception {

		int rows = marketData.getReleaseDateList().size();
		Object[][] xFormulaArray0 = createFormulaArray(marketData);

		com.sun.star.table.XCellRange destHeaderCellRange = chartsDataSheet.getCellRangeByPosition(0, 1,
				maxColumnIndex - 1, rows);
		com.sun.star.sheet.XCellRangeData crFormula = Lo.qi(com.sun.star.sheet.XCellRangeData.class,
				destHeaderCellRange);

		// not sure why need to copy data to another array.
		Object[][] x = new Object[rows][maxColumnIndex];
		
		for (int i=0;i<rows;i++) {
			x[i]=xFormulaArray0[i];
		}
		
	    crFormula.setDataArray(x);
	}
	
	private Object[][] createFormulaArray(MarketData marketData) {

		int rows = marketData.getReleaseDateList().size();
		Object[][] xFormulaArray = new Object[1 + rows][maxColumnIndex];
		
		xFormulaArray[0][0] = "Release Tue. Date";
		xFormulaArray[0][1] = "USD Index";
		xFormulaArray[0][2] = "US 10Y Yield ‰";
		xFormulaArray[0][3] = "SPX500";
		xFormulaArray[0][4] = "Dow30";
		xFormulaArray[0][5] = "NASDAQ";
		xFormulaArray[0][6] = "Gold";
		
		List<String> releaseDateList = marketData.getReleaseDateList();
		List<Double> usdIndexList = marketData.getUsdIndexList();
		List<Double> us10yList = marketData.getUs10yList();
		List<Double> spx500List = marketData.getSpx500List();
		List<Double> dow30List = marketData.getDow30List();
		List<Double> nasdaqList = marketData.getNasdaqList();
		List<Double> goldPriceList = marketData.getGoldPriceList();
		
		for (int i = 0; i < rows; i++) {
			xFormulaArray[i][0] = releaseDateList.get(i);
			xFormulaArray[i][1] = usdIndexList.get(i);
			xFormulaArray[i][2] = us10yList.get(i);
			xFormulaArray[i][3] = spx500List.get(i);
			xFormulaArray[i][4] = dow30List.get(i);
			xFormulaArray[i][5] = nasdaqList.get(i);
			xFormulaArray[i][6] = goldPriceList.get(i);
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
		
		XSpreadsheetDocument chartsDocument = loadMarketDocument();

		return chartsDocument;
	}

	private XSpreadsheetDocument loadMarketDocument() {
		
		try {
			String chartsFilePath = getMarketChartsFilePath();
			XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
			return chartsDocument;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvestInfoException("Error in loadDestDocument().");
		}
	}
	
	private XSpreadsheet getMarketDataSheet(XSpreadsheetDocument chartsDocument) {
		
		return getMarketSheet(chartsDocument, 0);
	}

	private XSpreadsheet getMarketChartsSheet(XSpreadsheetDocument chartsDocument) {
		
		return getMarketSheet(chartsDocument, 1);
	}

	private XSpreadsheet getMarketSheet(XSpreadsheetDocument chartsDocument, int index) {
		
		try {
			XSpreadsheet chartsDataSheet = getSpreadsheet(chartsDocument, index);
			return chartsDataSheet;
		} catch (IndexOutOfBoundsException | WrappedTargetException e) {
			e.printStackTrace();
			throw new InvestInfoException("Error in getSpreadsheet().");
		}
	}
	
	public void updateMarketChart(String date) throws Exception {
		
		MarketCurrentData marketCurrentData = marketDao.retrieveCurrentMarketData(date);
		XSpreadsheetDocument chartsDocument = loadMarketDocument();
		XSpreadsheet chartsDataSheet = getMarketDataSheet(chartsDocument);

		//copy marketCurrentData to charts-data sheet
		if (null != marketCurrentData) {
			updateMarketChartsDataSheet(marketCurrentData, chartsDataSheet);
		}
		
		XSpreadsheet chartsSheet = getMarketChartsSheet(chartsDocument);
		updateMarketChartsSheet(chartsSheet);
		
		Lo.save(chartsDocument);
		Lo.closeDoc(chartsDocument);
	}
	
	public void deleteByDate(String date) throws Exception {
	
		XSpreadsheetDocument chartsDocument = loadMarketDocument();
		XSpreadsheet chartsDataSheet = getMarketDataSheet(chartsDocument);
		int row = getNumberOfRows(chartsDataSheet) - 1;
		deleteRowFromSheet(chartsDataSheet, 0, row, date);
		
		Lo.save(chartsDocument);
		Lo.closeDoc(chartsDocument);
	}
	
	private void deleteRowFromSheet(XSpreadsheet xSheet, int sheetIndex, int row, String date) throws IndexOutOfBoundsException, WrappedTargetException {
		
		XCell xCell = xSheet.getCellByPosition(0, row);//date column
		if (xCell.getFormula().contains(date) || Double.parseDouble(date) == xCell.getValue()) {
			deleteRow(xSheet, row);
		}
	}
	
	private void deleteRow(XSpreadsheet sheet, int idx) {
		com.sun.star.table.XColumnRowRange crRange = Lo.qi(com.sun.star.table.XColumnRowRange.class, sheet);
		XTableRows rows = crRange.getRows();
		rows.removeByIndex(idx, 1); // remove 1 row at idx position
	}
	
	private void updateMarketChartsSheet(XSpreadsheet chartsSheet) {
		
		XTableChartsSupplier chartsSupplier = Lo.qi(XTableChartsSupplier.class, chartsSheet);
		XTableCharts tableCharts = chartsSupplier.getCharts();
		String[] chartNames = tableCharts.getElementNames();

		for (String chartName : chartNames) {
			updateChartByName(chartsSheet, chartName);
		}
	}

	private void updateChartByName(XSpreadsheet chartsSheet, String chartName) {
		
		XChartDocument chartDoc = Chart2.getChartDoc(chartsSheet, chartName);

		XDataSeries[] ds = Chart2.getDataSeries(chartDoc);

		for (int i = 0; i < ds.length; i++) {
			
			XDataSeries dsi = ds[i];
			
			// y values
			XDataSource xDataSource = (XDataSource) UnoRuntime.queryInterface(XDataSource.class, dsi);
			XLabeledDataSequence[] xLabeledDS = xDataSource.getDataSequences();
			XLabeledDataSequence xLabeledDS0 = xLabeledDS[0];
			XDataSequence xDSLabel = xLabeledDS0.getLabel();
			XDataSequence xDSValues = xLabeledDS0.getValues();
			
			String dataRange1 = getDataRange(xDSValues.getSourceRangeRepresentation());
			String dataRange2 = incrementDataRange(dataRange1);
			
			String valuesRange = xDSValues.getSourceRangeRepresentation().replace(dataRange1, dataRange2);
			String labelRange = xDSLabel.getSourceRangeRepresentation().replace(dataRange1, dataRange2);
			XDataProvider dp = chartDoc.getDataProvider();
			XDataSequence dataSeq = dp.createDataSequenceByRangeRepresentation(valuesRange);
			XDataSequence labelSeq = dp.createDataSequenceByRangeRepresentation(labelRange);
			XPropertySet dsProps = Lo.qi(XPropertySet.class, dataSeq);
			Props.setProperty(dsProps, "Role", "values-y"); // specify data role (type)
			
			xLabeledDS0.setValues(dataSeq);
			xLabeledDS0.setLabel(labelSeq);

			// categories
			XAxis axis = Chart2.getAxis(chartDoc, Chart2.X_AXIS, 0);
			ScaleData sd = axis.getScaleData();
			XLabeledDataSequence cat = sd.Categories;
			XDataSequence catValues = cat.getValues();
			String catValuesRange = catValues.getSourceRangeRepresentation().replace(dataRange1, dataRange2);
			XDataSequence catDataSeq = dp.createDataSequenceByRangeRepresentation(catValuesRange);
			XPropertySet catDsProps = Lo.qi(XPropertySet.class, catDataSeq);
			Props.setProperty(catDsProps, "Role", "categories"); // specify data role (type)
			
			cat.setValues(catDataSeq);
		}
	}
	
	private String getDataRange(String oldDataRange) {
		return oldDataRange.substring(1 + oldDataRange.lastIndexOf("$"));
	}
	
	// Increment the dataRange by 1
	private String incrementDataRange(String oldDataRange) {
		
		int range = Integer.parseInt(oldDataRange);
		String newDataRange = String.valueOf(1 + range);
		
		return newDataRange;
	}
	
	private void updateMarketChartsDataSheet(MarketCurrentData marketCurrentData, XSpreadsheet chartsDataSheet) throws Exception {
		
		int rows = getNumberOfRows(chartsDataSheet);
		Object[][] xFormulaArray0 = createFormulaArray(marketCurrentData);

		com.sun.star.table.XCellRange destHeaderCellRange = chartsDataSheet.getCellRangeByPosition(0, rows,
				maxColumnIndex - 1, rows);
		com.sun.star.sheet.XCellRangeData crFormula = Lo.qi(com.sun.star.sheet.XCellRangeData.class,
				destHeaderCellRange);

		Object[][] x = new Object[1][maxColumnIndex];
		
		x[0]=xFormulaArray0[0];

	    crFormula.setDataArray(x);
	}

	private Object[][] createFormulaArray(MarketCurrentData marketCurrentData) {
		
		Object[][] xFormulaArray = new Object[1][maxColumnIndex];
		
		xFormulaArray[0][0] = marketCurrentData.getReleaseDate();
		xFormulaArray[0][1] = marketCurrentData.getUsdIndex();
		xFormulaArray[0][2] = marketCurrentData.getUs10y();
		xFormulaArray[0][3] = marketCurrentData.getSpx500();
		xFormulaArray[0][4] = marketCurrentData.getDow30();
		xFormulaArray[0][5] = marketCurrentData.getNasdaq();
		xFormulaArray[0][6] = marketCurrentData.getGoldPrice();
		
		return xFormulaArray;
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

	public void upatePriceOrIndexInSpreadsheet(VendorName vendor, String date) throws Exception {
		
		List<CftcInstrument> productList = getProductList();
		
		String cftcDate =(null == date)? DateUtils.getCurrentWeekTuesdayDate() : date;
		String startDate =  DateUtils.getWeekStartDate(cftcDate);
		
		String tablename = vendor.getPriceHistoryTablename();
		Map<String, Double> priceMap = dao.retrievePriceIndex(tablename, cftcDate);
		
		if (0 == priceMap.size()) {
			System.out.println("No price/index data for " + startDate);
			return;
		}
		
		String chartsFilePath = getMarketChartsFilePath();
		XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
		XSpreadsheet destSheet3 = getSpreadsheet(chartsDocument, 0);
		int row = 1 + getNumberOfRows(destSheet3); //new row is not added yet
		
		//XCell cell = Calc.getCell(destSheet3, 0, row - 1);
		//cell.setFormula(cftcDate.substring(2));
		
		com.sun.star.table.XCellRange dcRange3 = destSheet3.getCellRangeByName("A" + row);
		com.sun.star.sheet.XCellRangeData crFormula = Lo.qi(com.sun.star.sheet.XCellRangeData.class,
				dcRange3);
		Object[][] xFormulaArray0 = new String[1][1];
		xFormulaArray0[0][0] = cftcDate.substring(2);
		crFormula.setDataArray(xFormulaArray0);
		
		for (CftcInstrument cftc : productList) {
			String instrument = cftc.getInstrumentName();
			Double price = priceMap.get(instrument);
			
			Calc.setVal(destSheet3, getChartsPriceCellName(row, instrument), price);
			
			System.out.println(cftc.getInstrumentName() + " : " + price);
		}
		
		// Update the market chart
		XSpreadsheet chartsSheet = getMarketChartsSheet(chartsDocument);
		updateMarketChartsSheet(chartsSheet);
		
		Lo.save(chartsDocument);
		Lo.closeDoc(chartsDocument);
	}

	private String getChartsPriceCellName(int row, String instrumentName) {
		
		String col = "";
		
		switch (instrumentName) {
			case "USD_INDEX": col = "B";break;
			case "US10Y": col = "C";break;
			case "SPX500": col = "D";break;
			case "DOW30": col = "E";break;
			case "NASDAQ": col = "F";break;
			case "GOLD": col = "G";break;
			default: throw new CftcException("Invalid instrument name:" + instrumentName);
		}
		
		return col + row;
	}
}
