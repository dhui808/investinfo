package cftc.tachart;

import static cftc.utils.Constants.productsUrl;
import static cftc.utils.Constants.templateUrl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

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
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XCell;
import com.sun.star.table.XTableCharts;
import com.sun.star.table.XTableChartsSupplier;
import com.sun.star.table.XTableRows;
import com.sun.star.uno.UnoRuntime;

import cftc.AbstractCftcAnalysis;
import cftc.InvestInfoException;
import cftc.dao.InventoryDao;
import cftc.dao.PriceIndexDao;
import cftc.model.CftcInstrument;
import cftc.model.ProductList;
import cftc.utils.DateUtils;
import cftc.vendor.VendorName;
import cftc.vendor.investingcom.InvestingComPriceIndexDao;
import jloputility.Calc;
import jloputility.Chart2;
import jloputility.Lo;
import jloputility.Props;

public class TechnicalAnalysis extends AbstractCftcAnalysis {

	private int maxColumnIndex = 6;
	private TechnicalAnalysisDao marketDao = new TechnicalAnalysisDao();
	private PriceIndexDao dao = new InvestingComPriceIndexDao();
	
	@Override
	protected String getSourceFilename() {
		
		return null;
	}

	@Override
	protected List<CftcInstrument> getProductList() {
		
		return ProductList.getTechnicalAnalysisProductList();
	}

	@Override
	protected int getSourceColumnLength() {
		
		return 0;
	}

	public void createTechnicalAnalysisChart() throws Exception {
		
		TechnicalAnalysisData marketData = marketDao.retrieveAllTechnicalAnalysisData();
		XSpreadsheetDocument chartsDocument = createTaDocumentFromTemplate();
		XSpreadsheet chartsDataSheet = getTaDataSheet(chartsDocument);

		//copy marketData to charts-data sheet
		updateTechanicalAnalysisChartsDataSheet(marketData, chartsDataSheet);
		
		Lo.save(chartsDocument);
		Lo.closeDoc(chartsDocument);
	}

	private void updateTechanicalAnalysisChartsDataSheet(TechnicalAnalysisData marketData, XSpreadsheet chartsDataSheet) throws Exception {

		int rows = marketData.getEurusdList().size();
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
	
	private Object[][] createFormulaArray(TechnicalAnalysisData marketData) {

		int rows = marketData.getEurusdList().size();
		Object[][] xFormulaArray = new Object[rows][maxColumnIndex];
		
		List<CftcForexAnalysisRecord> eurusdList = marketData.getEurusdList();
		
		for (int i = 0; i < rows; i++) {
			CftcForexAnalysisRecord rec = eurusdList.get(i);
			xFormulaArray[i][0] = rec.getReleaseDate();
			xFormulaArray[i][1] = rec.getPrice();
			xFormulaArray[i][2] = rec.getDealerNetLong();
			xFormulaArray[i][3] = rec.getAssetMgrNetLong();
			xFormulaArray[i][4] = rec.getLevMoneyNetLong();
			xFormulaArray[i][5] = rec.getOtherNetLong();
		}
		
		return xFormulaArray;
	}

	// Creates ta document from template.
	private XSpreadsheetDocument createTaDocumentFromTemplate() {
		
		try {
			copyTemplateToTaChart();
		} catch (IOException e) {
			e.printStackTrace();
			throw new InvestInfoException("Error in createTaDocumentFromTemplate().");
		}
		
		XSpreadsheetDocument chartsDocument = loadTaDocument();

		return chartsDocument;
	}

	private XSpreadsheetDocument loadTaDocument() {
		
		try {
			String chartsFilePath = getTaChartFilePath();
			XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
			return chartsDocument;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvestInfoException("Error in loadTaDocument().");
		}
	}
	
	private XSpreadsheet getTaDataSheet(XSpreadsheetDocument chartsDocument) {
		
		return getTaSheet(chartsDocument, 0);
	}

	private XSpreadsheet getTaChartSheet(XSpreadsheetDocument chartsDocument) {
		
		return getTaSheet(chartsDocument, 1);
	}

	private XSpreadsheet getTaSheet(XSpreadsheetDocument chartsDocument, int index) {
		
		try {
			XSpreadsheet chartsDataSheet = getSpreadsheet(chartsDocument, index);
			return chartsDataSheet;
		} catch (IndexOutOfBoundsException | WrappedTargetException e) {
			e.printStackTrace();
			throw new InvestInfoException("Error in getTaSheet().");
		}
	}
	
	public void updateTaChart(String date) throws Exception {
		
		TechnicalAnalysisData marketCurrentData = marketDao.retrieveCurrentFinancialAnalysisData(date);
		XSpreadsheetDocument chartsDocument = loadTaDocument();
		XSpreadsheet chartsDataSheet = getTaDataSheet(chartsDocument);

		//copy marketCurrentData to charts-data sheet
		appendTechanicalAnalysisChartsDataSheet(marketCurrentData, chartsDataSheet);
		
		XSpreadsheet chartsSheet = getTaChartSheet(chartsDocument);
		updateMarketChartSheet(chartsSheet);
		
		Lo.save(chartsDocument);
		Lo.closeDoc(chartsDocument);
	}
	
	private void appendTechanicalAnalysisChartsDataSheet(TechnicalAnalysisData marketData, XSpreadsheet chartsDataSheet) throws Exception {

		Object[][] xFormulaArray0 = createFormulaArray(marketData);
		int row = 1 + getNumberOfRows(chartsDataSheet);
		
		com.sun.star.table.XCellRange destHeaderCellRange = chartsDataSheet.getCellRangeByName(getLastRowCellRange(row));
		com.sun.star.sheet.XCellRangeData crFormula = Lo.qi(com.sun.star.sheet.XCellRangeData.class,
				destHeaderCellRange);

		// not sure why need to copy data to another array.
		Object[][] x = new Object[1][maxColumnIndex];
		
		x[0]=xFormulaArray0[0];

		
	    crFormula.setDataArray(x);
	}
	
	public void deleteByDate(String date) throws Exception {
	
		XSpreadsheetDocument chartsDocument = loadTaDocument();
		XSpreadsheet chartsDataSheet = getTaDataSheet(chartsDocument);
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
	
	private void updateMarketChartSheet(XSpreadsheet chartsSheet) {
		
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

	private void copyTemplateToTaChart() throws IOException {
		
		String chartsTemplatePath = getTaChartTemplatePath().substring(8);//remove file:///
		String chartsFilePath = getTaChartFilePath().substring(8);
		File chartsTemplateFile = new File(chartsTemplatePath);
		File chartsFile = new File(chartsFilePath);

		Files.copy(chartsTemplateFile.toPath(), chartsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	private String getTaChartTemplatePath() {
		return templateUrl + "template-ta-charts.ods";
	}
	
	private String getTaChartFilePath() {
		return productsUrl + "ta-charts.ods";
	}
	
	public void upatePriceOrIndexInSpreadsheet(VendorName vendor, String date) throws Exception {
		
		List<CftcInstrument> productList = getProductList();
		
		String cftcDate =(null == date)? DateUtils.getCurrentWeekTuesdayDate() : date;
		String startDate =  DateUtils.getWeekStartDate(cftcDate);
		
		String tablename = "cftc_forex_view";
		Map<String, Double> priceMap = dao.retrievePriceIndex(tablename, cftcDate);
		
		if (0 == priceMap.size()) {
			System.out.println("No price/index data for " + startDate);
			return;
		}
		
		String chartsFilePath = getTaChartFilePath();
		XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
		
		for (CftcInstrument cftc : productList) {
			Double price = priceMap.get(cftc.getInstrumentName());
			XSpreadsheet destSheet3 = getSpreadsheet(chartsDocument, 0);
			int row = getNumberOfRows(destSheet3);
			Calc.setVal(destSheet3, getChartsPriceCellName(row), price);
			
			System.out.println(cftc.getInstrumentName() + " : " + price);
		}
		
		// updates technical analysis chart
		XSpreadsheet chartsSheet = getTaChartSheet(chartsDocument);
		updateMarketChartSheet(chartsSheet);
		
		Lo.save(chartsDocument);
		Lo.closeDoc(chartsDocument);
	}
	
	protected String getLastRowCellRange(int row) {
		return "A" + row + ":F" + row;
	}

	protected String getChartsPriceCellName(int row) {
		return "B" + row;
	}
}
