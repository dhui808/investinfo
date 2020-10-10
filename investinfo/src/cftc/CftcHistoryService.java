package cftc;

import java.time.LocalDate;
import java.util.List;

import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;

import cftc.dao.CftcDao;
import cftc.model.CftcInstrument;
import cftc.model.ProductList;
import cftc.model.table.CftcTableName;
import cftc.utils.DateUtils;
import cftc.utils.UnzipCftc;
import jloputility.Lo;

public class CftcHistoryService extends AbstractCftcAnalysis {

	private CftcDao dao = new CftcDao();
	
	public void updateCftcReleaseHistory(String year) throws Exception {
		
		UnzipCftc.unzipCftcFilesYear(year);

		XSpreadsheetDocument sourceDocument = loadCftcSourceDocument(year);
		XSpreadsheet srcSheet = getSpreadsheet(sourceDocument, 0);
		
		String[] filters = ProductList.getEnergyProductListUnique().get(0).getFilters();//any instrument will do
		XCellRange xRange1 = getAllCftcRows(srcSheet, filters, year);
		
		com.sun.star.sheet.XCellRangeData xData = UnoRuntime.queryInterface(com.sun.star.sheet.XCellRangeData.class,
				xRange1);
		Object[][] xDataArray = xData.getDataArray();
		
		String[] releaseDates = new String[xDataArray.length];
		
		for (int i = 0; i < xDataArray.length; i++) {
			releaseDates[i] = DateUtils.converCftcDateIntoFormatedDate(xDataArray[i][1].toString().substring(0, 6));
		}
		
		dao.updateCftcReleaseHistory(CftcTableName.CFTC_RELEASE_HISTORY.getName(), year, releaseDates);
		
		Lo.closeDoc(sourceDocument);
	}
	
	public void updateAllReleaseHistory() throws Exception {
		
		LocalDate ld = LocalDate.now();
		int year = ld.getYear();
		
		for (int i = year; i >= 2010; i--) {
			updateCftcReleaseHistory(String.valueOf(i));
		}
	}

	protected String getSourceFilename() {
		return "f_year.xls";
	}

	protected List<CftcInstrument> getProductList() {
		// TODO Auto-generated method stub
		return null;
	}

	protected int getSourceColumnLength() {
		return 2;
	}
	
}
