package cftc;

import static cftc.utils.Constants.CURRENT_YEAR;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;

import cftc.dao.CftcDao;
import cftc.model.CftcInstrument;
import cftc.model.ProductList;
import cftc.model.table.CftcTableName;
import cftc.tachart.CftcForexRecord;
import cftc.tachart.TechnicalAnalysisDao;
import cftc.utils.DateUtils;
import cftc.utils.UnzipCftc;
import jloputility.Lo;

public class CftcFinancialHistoryService extends AbstractCftcAnalysis {

	private TechnicalAnalysisDao dao = new TechnicalAnalysisDao();
	
	/**
	 * Adds the latest CFTC financial records to cftc_release_financial_history table.
	 * @throws Exception
	 */
	public void addCftcFinancialHistory() throws Exception {
		
		String year = "" + CURRENT_YEAR;

		updateCftcFinancial(year, true);
	}
	
	/**
	 * Update the full year for cftc_release_financial_history.
	 * @param year
	 * @throws Exception
	 */
	public void updateCftcFinancialHistory(String year) throws Exception {
		
		updateCftcFinancial(year, false);
	}

	private void updateCftcFinancial(String year, boolean currentOnly) throws Exception {
		
		UnzipCftc.unzipCftcFilesYear(year);

		XSpreadsheetDocument sourceDocument = loadCftcSourceDocument(year);
		XSpreadsheet srcSheet = getSpreadsheet(sourceDocument, 0);
		
		List<CftcForexRecord> cftcForexRecords = retrieveCftcFinancialHistory(srcSheet, year, currentOnly);
		
		dao.updateCftcFinancialHistory(CftcTableName.CFTC_RELEASE_FINANCIAL_HISTORY.getName(), cftcForexRecords);
		
		Lo.closeDoc(sourceDocument);
	}
	
	private List<CftcForexRecord> retrieveCftcFinancialHistory(XSpreadsheet srcSheet, String year, boolean currentOnly) throws Exception {
		
		List<CftcInstrument> cftcInstruments = ProductList.getTechnicalAnalysisProductList();
		List<CftcForexRecord> cftcForexRecords = new ArrayList<CftcForexRecord>();
		
		for (CftcInstrument inst: cftcInstruments) {
			
			String instrument = inst.getInstrumentName();
			String[] filters = inst.getFilters();
			XCellRange xRange = getAllCftcRows(srcSheet, filters, year);
		
			com.sun.star.sheet.XCellRangeData xData = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				xRange);
			Object[][] xDataArray = xData.getDataArray();
		
			for (Object[] row: xDataArray) {
				
				CftcForexRecord record = new CftcForexRecord();
				
				String releaseDate = DateUtils.converCftcDateIntoFormatedDate(row[1].toString().substring(0, 6));
				Integer dealerLong = Float.valueOf(row[8].toString()).intValue();
				Integer dealerShort = Float.valueOf(row[9].toString()).intValue();
				Integer assetMgrLong = Float.valueOf(row[11].toString()).intValue();
				Integer assetMgrShort = Float.valueOf(row[12].toString()).intValue();
				Integer levMoneyLong = Float.valueOf(row[14].toString()).intValue();
				Integer levMoneyShort = Float.valueOf(row[15].toString()).intValue();
				Integer otherLong = Float.valueOf(row[17].toString()).intValue();
				Integer otherShort = Float.valueOf(row[18].toString()).intValue();
				
				record.setReleaseDate(releaseDate);
				record.setInstrument(instrument);
				record.setAssetMgrLong(assetMgrLong);
				record.setAssetMgrShort(assetMgrShort);
				record.setDealerLong(dealerLong);
				record.setDealerShort(dealerShort);
				record.setLevMoneyLong(levMoneyLong);
				record.setLevMoneyShort(levMoneyShort);
				record.setOtherLong(otherLong);
				record.setOtherShort(otherShort);
				
				cftcForexRecords.add(record);
				
				// Add only the latest cftc release
				if (currentOnly) break;
			}
		}
		
		return cftcForexRecords;
	}
	
	public void updateAllReleaseHistory() throws Exception {
		
		LocalDate ld = LocalDate.now();
		int year = ld.getYear();
		
		for (int i = year; i > 2010; i--) {
			updateCftcFinancialHistory(String.valueOf(i));
		}
	}

	protected String getSourceFilename() {
		return "FinFutYY.xls";
	}

	protected List<CftcInstrument> getProductList() {
		// TODO Auto-generated method stub
		return null;
	}

	protected int getSourceColumnLength() {
		return 19;
	}
	
}
