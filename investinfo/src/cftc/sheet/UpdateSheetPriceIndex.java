package cftc.sheet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ListIterator;

import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;

import cftc.dao.PriceIndexDao;
import cftc.model.CftcInstrument;
import cftc.model.PriceIndexDto;
import cftc.model.ProductList;
import cftc.vendor.VendorName;
import jloputility.Lo;

/**
 * Updates the price/index column and optional index inverse column for the whole year. 
 *
 */
public abstract class UpdateSheetPriceIndex extends AbstractHandleYearSheet {

	protected PriceIndexDao dao;
	protected VendorName vendorName;
	
	protected void updatePriceOrIndex(String year) throws RuntimeException, Exception {
		
		List<CftcInstrument> productList = getProductList();
		
		for (CftcInstrument cftc : productList) {	
			updatePriceOrIndex(cftc, year);
		}
	}

	protected void updatePriceOrIndex(CftcInstrument cftc, String year) throws RuntimeException, Exception {
		
		List<PriceIndexDto> priceList = dao.retrievePriceIndex(vendorName.getPriceHistoryTablename(), cftc.getInstrumentName(), year);
		
		String destFilePath = cftc.getAnalysisFilePath();
		XSpreadsheetDocument destDocument = loadDestDocument(destFilePath);
		
		int idx = calculateNewDataSheetIndex(destDocument, year);
		
		XSpreadsheet destSheet2 = getSpreadsheet(destDocument, idx + 1);
		int rows = getNumberOfRows(destSheet2);
		int priceColumnIdx = cftc.getAnalysisPriceColumnIndex();
		
		XCellRange xRange = destSheet2.getCellRangeByPosition(priceColumnIdx, 1, priceColumnIdx + 1, rows - 1);
		com.sun.star.sheet.XCellRangeData destData = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				xRange);
		System.out.println("" + cftc.getInstrumentName() + ", " + year + ", cftc rows=" + rows + ", price rows=" + priceList.size());
		Object[][] xDataArray = getPriceArray(priceList, cftc.needInverseRatio(), destData.getDataArray().length, cftc);
		
		destData.setDataArray(xDataArray);
		
		//update price/index in charts data sheet
		String chartsFilePath = cftc.getChartsFilePath();
		XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
		XSpreadsheet destSheet3 = getSpreadsheet(chartsDocument, idx - 1);
		int priceChartsColumnIdx =  cftc.getChartsPriceColumnIndex();
		XCellRange xRange3 = destSheet3.getCellRangeByPosition(priceChartsColumnIdx, 1, priceChartsColumnIdx + 1, rows - 1);
		com.sun.star.sheet.XCellRangeData destData3 = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				xRange3);

		Object[][] invDataArray = reverseArray(xDataArray);
		destData3.setDataArray(invDataArray);
		
		Lo.save(destDocument);
		Lo.closeDoc(destDocument);
		Lo.save(chartsDocument);
		Lo.closeDoc(chartsDocument);
	}
	
	private Object[][] getPriceArray(List<PriceIndexDto> list, boolean needInverseRatio, int rows, CftcInstrument cftc) {
		
		if (list.size() != rows) {
			System.out.println("data size from database does not match sheet rows for instrument " + cftc.getInstrumentName());
			removeNewerPriceEntries(list, rows);
		}
		
		Object[][] array = new Object[list.size()][2];
		
		for (int i = 0; i < list.size(); i++) {
			PriceIndexDto dto = list.get(i);
			Double price = dto.price;
			array[i][0] = price;
			
			if (needInverseRatio) {
				// in this case, price goes to the next column to the right and the the inverse of the price occupies 
				//the original the price column
				array[i][1] = price;
				BigDecimal bd = new BigDecimal(1 / price).setScale(4, RoundingMode.HALF_UP);
				array[i][0] = bd.doubleValue();
			} else {
				array[i][1] = "";
			}
		}
		
		return array;
	}
	
	private void removeNewerPriceEntries(List<PriceIndexDto> list, int rows) {
		
		ListIterator<PriceIndexDto> it = list.listIterator();
		while (it.hasNext()) {
			
			PriceIndexDto pid = it.next();
			
			System.out.println("The latest price/index data from database is removed:" + pid.releaseTuesdayDate);
			
			if (list.size() == rows) {
				break;
			}
			
			it.remove();
		}
	}
	
	protected String getSourceFilename() {
		 return "FinFutYY.xls";
	}

	protected List<CftcInstrument> getProductList() {
		return ProductList.getAllProductList();
	}
	
	protected int getSourceColumnLength() {
		return 84;
	}

	@Override
	protected String getTemplatePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getTemplateColumnLength() {
		// TODO Auto-generated method stub
		return 0;
	}
}
