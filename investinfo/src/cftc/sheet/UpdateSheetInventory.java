package cftc.sheet;

import java.util.List;
import java.util.ListIterator;

import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;

import cftc.dao.InventoryDao;
import cftc.model.CftcInstrument;
import cftc.model.InstrumentCategory;
import cftc.model.InventoryDto;
import cftc.model.ProductList;
import cftc.model.table.EiaTableName;
import jloputility.Lo;

public abstract class UpdateSheetInventory extends AbstractHandleYearSheet {

	protected InventoryDao dao;
	
	public UpdateSheetInventory() {
		dao = new InventoryDao();
	}
	
	protected void updateInventory(String year) throws Exception {

		List<CftcInstrument> productList = getProductList();
		
		for (CftcInstrument cftc : productList) {
			
			updateInventory(cftc, year);;
		}
	}
	
	protected void updateInventory(CftcInstrument cftc, String year) throws Exception {
		
		if (!InstrumentCategory.ENERGY.equals(cftc.getCategory())) {
			System.out.println(cftc.getInstrumentName() + " is not in energy category and does not have inventory.");
			return;
		}
		
		List<InventoryDto> inventoryList = dao.retrieveInventory(EiaTableName.EIA_HISTORY.getName(), cftc.getInstrumentName(), year);
		InventoryDto lastYearInv = dao.retrieveLastYearInventory(EiaTableName.EIA_HISTORY.getName(), cftc.getInstrumentName(), year);
		
		String destFilePath = cftc.getAnalysisFilePath();
		XSpreadsheetDocument destDocument = loadDestDocument(destFilePath);
		
		int idx = calculateNewDataSheetIndex(destDocument, year);
		
		XSpreadsheet destSheet2 = getSpreadsheet(destDocument, idx + 1);
		int rows = getNumberOfRows(destSheet2);
		int invColumnIdx = cftc.getAnalysisInventoryColumnIndex();
		
		XCellRange xRange = destSheet2.getCellRangeByPosition(invColumnIdx, 1, invColumnIdx + 1, rows - 1);
		com.sun.star.sheet.XCellRangeData destData = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				xRange);
		
		Object[][] xDataArray = getInventoryArray(inventoryList, lastYearInv, destData.getDataArray().length, cftc);
		
		//update inventory and change for charts data sheet
		String chartsFilePath = cftc.getChartsFilePath();
		XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
		XSpreadsheet destSheet3 = getSpreadsheet(chartsDocument, idx - 1);
		int invChartsColumnIdx =  cftc.getChartsInventoryColumnIndex();
		XCellRange xRange3 = destSheet3.getCellRangeByPosition(invChartsColumnIdx, 1, invChartsColumnIdx + 1, rows - 1);
		com.sun.star.sheet.XCellRangeData destData3 = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				xRange3);
		
		Object[][] invDataArray = reverseArray(xDataArray);
		destData3.setDataArray(invDataArray);
		
		Lo.save(destDocument);
		Lo.closeDoc(destDocument);
		Lo.save(chartsDocument);
		Lo.closeDoc(chartsDocument);
	}
	
	private Object[][] getInventoryArray(List<InventoryDto> list, InventoryDto lastYearInv, int rows, CftcInstrument cftc) {
		
		if (list.size() != rows) {
			System.out.println("data size from database does not match sheet rows for instrument " + cftc.getInstrumentName());
			removeNewerInventoryEntries(list, rows);
		}
		
		Object[][] array = new Object[list.size()][2];
		Integer lastInv = (null == lastYearInv)? list.get(list.size() - 1).inventory : lastYearInv.inventory;
		
		for (int i = list.size() - 1; i >= 0; i--) {
			InventoryDto dto = list.get(i);
			array[i][0] = dto.inventory;
			array[i][1] = dto.inventory - lastInv;
			lastInv = dto.inventory;
		}
		
		return array;
	}
	
	private void removeNewerInventoryEntries(List<InventoryDto> list, int rows) {
		
		ListIterator<InventoryDto> it = list.listIterator();
		while (it.hasNext()) {
			
			InventoryDto pid = it.next();
			
			System.out.println("The latest inventory data from database is removed:" + pid.releaseTuesdayDate);
			
			if (list.size() == rows) {
				break;
			}
			
            it.remove();
		}
	}

	protected String getSourceFilename() {
		 return "f_year.xls";
	}

	protected List<CftcInstrument> getProductList() {
		return ProductList.getEnergyProductList();
	}

	protected int getSourceColumnLength() {
		return 188;
	}

	@Override
	protected int getTemplateColumnLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected String getTemplatePath() {
		// TODO Auto-generated method stub
		return null;
	}
}
