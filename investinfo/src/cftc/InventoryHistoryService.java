package cftc;

import static cftc.utils.Constants.STAGING_EIA_PATH;
import static cftc.utils.Constants.downloadEiaUrl;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import com.sun.star.sheet.XCellRangeFormula;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;

import cftc.dao.CftcDao;
import cftc.dao.InventoryDao;
import cftc.download.DownloadEia;
import cftc.model.CftcInstrument;
import cftc.model.InventoryDto;
import cftc.model.Energy;
import cftc.model.InstrumentName;
import cftc.model.NgHenryHub;
import cftc.model.OilLightSweet;
import cftc.model.ProductList;
import cftc.model.table.CftcTableName;
import cftc.model.table.EiaTableName;
import cftc.utils.DateUtils;
import cftc.wao.EiaNgInventoryExtractor;
import cftc.wao.EiaOilInventoryExtractor;
import jloputility.Lo;

/**
 * 
 * Extracts and saves the inventory history from EIA .xls file into .csv file. Then load the .csv file into database
 * staging table. Finally, the inventory history is extracted into inventory history table. 
 *
 */
public class InventoryHistoryService extends AbstractCftcAnalysis {
	
	private InventoryDao inventoryDao = new InventoryDao();

	public void updateAllInventoryHistory() throws Exception {
		updateNgInventoryHistory();
		updateOilInventoryHistory();
	}
	
	public void loadAllInventoryHistory() throws Exception {
		
		DownloadEia.downloadNgInventoryHistory();
		DownloadEia.downloaOilInventoryHistory();
		
		inventoryDao.clearStagingAndHistoryTables();
		
		loadInventoryHistory(new NgHenryHub());
		loadInventoryHistory(new OilLightSweet());
	}
	
	private void loadInventoryHistory(Energy commodity) throws Exception {
		
		String sourceUrl = downloadEiaUrl + commodity.getInventoryHistoryXlsFilename();
		XSpreadsheetDocument sourceDocument = loadSourceDocument(sourceUrl);
		int scrSheetIndex = commodity.getInventoryHistorySourceSheet();
		XSpreadsheet srcSheet = getSpreadsheet(sourceDocument, scrSheetIndex);
		
		int rows = getNumberOfRows(srcSheet);
		
		int firstRow = commodity.getInventoryHistoryFirstRow();
		int dateColumn = commodity.getInventoryHistoryWeekEndingColumn();
		int inventoryColumn = commodity.getInventoryHistoryInventoryColumn();
		com.sun.star.table.XCellRange srcCellRange = srcSheet.getCellRangeByPosition(dateColumn, firstRow, inventoryColumn, rows - 1);
		XCellRangeFormula cf = Lo.qi(XCellRangeFormula.class, srcCellRange);
		
		String[][] data = cf.getFormulaArray();
		
		Lo.closeDoc(sourceDocument);
		
		StringBuilder sb = new StringBuilder();
		
		//save in a .csv file
		String csvFile = STAGING_EIA_PATH + "/" + commodity.getInventoryHistoryCsvFilename();
		PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(csvFile)));
		for (int row = 0; row < data.length; row ++) {
			String date = data[row][dateColumn];
			String inventory = data[row][inventoryColumn];
			
			sb.setLength(0);
			
			date = DateUtils.converExcelDateIntoFormatedDate(date);
			sb.append(date).append(",").append(inventory);
			ps.println(sb.toString());
		}
		
		ps.flush();
		ps.close();
		
		//load .csv into database staging table.
		String stagingTableName = commodity.getInventoryHistoryStagingTableName();
		inventoryDao.loadInventoryHistoryStaging(csvFile, stagingTableName);
		
		//extract into history table.
		String historyTableName = commodity.getInventoryHistoryTableName();
		inventoryDao.loadInventoryHistory(stagingTableName, historyTableName, commodity.getInstrumentName());
	}
	
	private void updateNgInventoryHistory() throws Exception {
		String[] ngInv = EiaNgInventoryExtractor.retrieveNgInventory();
		String cftcDate = DateUtils.getLatestEiaReleaseTuesdayDate();
		String ngHistoryTablename =  EiaTableName.EIA_HISTORY.getName();
		String instrument = InstrumentName.NG.name();
		System.out.println("NG:"+ cftcDate + ", " + ngInv[0]);
		inventoryDao.updateInventoryHistory(ngHistoryTablename, cftcDate, instrument, ngInv[0]);
	}
	
	private void updateOilInventoryHistory() throws Exception {
		String[] ngInv = EiaOilInventoryExtractor.retrieveOilInventory();
		String cftcDate = DateUtils.getLatestEiaReleaseTuesdayDate();
		String oilHistoryTablename =  EiaTableName.EIA_HISTORY.getName();
		String instrument = InstrumentName.OIL.name();
		System.out.println("OIL:"+ cftcDate + ", " + ngInv[0]);
		inventoryDao.updateInventoryHistory(oilHistoryTablename, cftcDate, instrument, ngInv[0]);
	}
	
	@Override
	protected String getSourceFilename() {
		 return "f_year.xls";
	}

	protected List<CftcInstrument> getProductList() {
		return ProductList.getEnergyProductListUnique();
	}

	@Override
	protected int getSourceColumnLength() {
		return 188;
	}

	/**
	 * Sometimes CFTC release is against Monday instead of Tuesday positions.
	 * @param year
	 * @throws RuntimeException
	 * @throws Exception
	 */
	public void adjustInventoryHistory(String year) throws Exception {

		List<CftcInstrument> productList = getProductList();
		
		CftcDao cftcDao = new CftcDao();
		
		String firstReleaseDate = cftcDao.retrieveFirstReleaseDate(CftcTableName.CFTC_RELEASE_HISTORY.getName(), year);
		String lastYear = String.valueOf(Integer.parseInt(year) - 1);
		String lastReleaseDateLastYear = cftcDao.retrieveLastReleaseDate(CftcTableName.CFTC_RELEASE_HISTORY.getName(), lastYear);
		
		System.out.println("CFTC first relase date for " + year + ": " + firstReleaseDate);
		System.out.println("CFTC last relase date for " + lastYear + ": " + lastReleaseDateLastYear);
		
		for (CftcInstrument cftc : productList) {
			
			List<InventoryDto> inventoryList = inventoryDao.retrieveInventory(EiaTableName.EIA_HISTORY.getName(), cftc.getInstrumentName(), year);
			String firstReleaseTuesdayDate = inventoryList.get(inventoryList.size() - 1).releaseTuesdayDate;
			
			System.out.println("Datebase first relase date for " + year + ": " + firstReleaseTuesdayDate);
			
			if (!firstReleaseDate.equals(firstReleaseTuesdayDate)) {
				
				inventoryDao.updateFirstCtfcReleaseDate(EiaTableName.EIA_HISTORY.getName(), lastReleaseDateLastYear, firstReleaseTuesdayDate, cftc.getInstrumentName());
			}
			
			//TODO is it possible that the last release against positions in Wednesday?
		}
	}
}
