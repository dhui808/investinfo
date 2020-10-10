package cftc;

import static cftc.utils.Constants.CFTC_MAX_LINES;
import static cftc.utils.Constants.cftcSourceUrl;

import java.util.List;

import com.sun.star.beans.PropertyValue;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XIndexAccess;
import com.sun.star.container.XNameAccess;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.UnoRuntime;

import cftc.model.CftcInstrument;
import cftc.utils.DateUtils;
import jloputility.Calc;

public abstract class AbstractCftcAnalysis {

	/**
	 * Load CFTC source spreadsheet document.
	 * 
	 * @return The XSpreadsheetDocument interface of the document.
	 */
	protected XSpreadsheetDocument loadCftcSourceDocument(String year) throws Exception {
	
		String sourceFile = cftcSourceUrl + year + "/" + getSourceFilename();
		
		return loadSourceDocument(sourceFile);
	}

	/**
	 * Load source spreadsheet document.
	 * 
	 * @return The XSpreadsheetDocument interface of the document.
	 */
	protected XSpreadsheetDocument loadSourceDocument(String sourceUrl) throws Exception {
		com.sun.star.beans.PropertyValue[] propertyValue = new com.sun.star.beans.PropertyValue[2];
		propertyValue[0] = new com.sun.star.beans.PropertyValue();
		propertyValue[0].Name = "Hidden";
		propertyValue[0].Value = Boolean.TRUE;
		propertyValue[1] = new PropertyValue();
		propertyValue[1].Name = "ReadOnly";
		propertyValue[1].Value = Boolean.TRUE;
	
		System.out.println("Load source document " + sourceUrl);
		
		String sourceFile = sourceUrl;
		XComponent sourceXComponent = LO.aLoader.loadComponentFromURL(sourceFile, "_default", 0,
				propertyValue);
	
		return UnoRuntime.queryInterface(XSpreadsheetDocument.class, sourceXComponent);
	}
	
	/**
	 * Load source spreadsheet document.
	 * 
	 * @return The XSpreadsheetDocument interface of the document.
	 */
	protected XSpreadsheetDocument loadDestDocument(String destFilePath) throws Exception {
		com.sun.star.beans.PropertyValue[] propertyValue = new com.sun.star.beans.PropertyValue[2];
		propertyValue[0] = new com.sun.star.beans.PropertyValue();
		propertyValue[0].Name = "Hidden";
		propertyValue[0].Value = Boolean.TRUE;
		propertyValue[1] = new PropertyValue();
		propertyValue[1].Name = "Overwrite";
		propertyValue[1].Value = Boolean.TRUE;
	
		System.out.println("Load dest document " + destFilePath);
		
		XComponent destXComponent = LO.aLoader.loadComponentFromURL(destFilePath, "_default", 0,
				propertyValue);
	
		return UnoRuntime.queryInterface(XSpreadsheetDocument.class, destXComponent);
	}

	/**
	 * Load tempalte spreadsheet document as specified by templatePath.
	 * 
	 * @return The XSpreadsheetDocument interface of the document.
	 */
	protected XSpreadsheetDocument loadTemplateDocument(String templatePath) throws Exception {
		com.sun.star.beans.PropertyValue[] propertyValue = new com.sun.star.beans.PropertyValue[2];
		propertyValue[0] = new com.sun.star.beans.PropertyValue();
		propertyValue[0].Name = "Hidden";
		propertyValue[0].Value = Boolean.TRUE;
		propertyValue[1] = new PropertyValue();
		propertyValue[1].Name = "ReadOnly";
		propertyValue[1].Value = Boolean.FALSE;
	
		System.out.println("Load template document " + templatePath);
		
		XComponent destXComponent = LO.aLoader.loadComponentFromURL(templatePath, "_default", 0,
				propertyValue);
	
		return UnoRuntime.queryInterface(XSpreadsheetDocument.class, destXComponent);
	}
	
	protected XSpreadsheet getSpreadsheet(XSpreadsheetDocument xDocument, int nIndex)
			throws IndexOutOfBoundsException, WrappedTargetException {
		// Collection of sheets
		XSpreadsheets xSheets = xDocument.getSheets();
		XSpreadsheet xSheet = null;

		XIndexAccess xSheetsIA = UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
		xSheet = UnoRuntime.queryInterface(XSpreadsheet.class, xSheetsIA.getByIndex(nIndex));

		return xSheet;
	}

	protected XSpreadsheet getSpreadsheet(XSpreadsheetDocument xDocument, String name) {
		// Collection of sheets
		XSpreadsheets xSheets = xDocument.getSheets();
		XSpreadsheet xSheet = null;

		XNameAccess xSheetsIA = UnoRuntime.queryInterface(XNameAccess.class, xSheets);
		try {
			xSheet = UnoRuntime.queryInterface(XSpreadsheet.class, xSheetsIA.getByName(name));
		} catch (NoSuchElementException e) {
			return null;
		} catch (WrappedTargetException e) {
			return null;
		}

		return xSheet;
	}
	
	protected XSpreadsheet getDataSpreadsheetByYear(XSpreadsheetDocument xDocument, String year) {
		return getSpreadsheet(xDocument, year + "_data");
	}

	protected XSpreadsheet getAnalysisSpreadsheetByYear(XSpreadsheetDocument xDocument, String year) {
		return getSpreadsheet(xDocument, year + "_analysis");
	}
	
	protected XSpreadsheet getChartsDataSpreadsheetByYear(XSpreadsheetDocument xDocument, String year) {
		return getSpreadsheet(xDocument, year + "_charts_data");
	}
	
	protected XSpreadsheet getChartsSpreadsheetByYear(XSpreadsheetDocument xDocument, String year) {
		return getSpreadsheet(xDocument, year + "_charts");
	}
	
	protected abstract String getSourceFilename();
	protected abstract List<CftcInstrument> getProductList();
	protected abstract int getSourceColumnLength();

	/**
	 * Returns the total number of rows in the sheet.
	 * @param xSheet
	 * @return
	 */
	protected int getNumberOfRows(XSpreadsheet xSheet) {
		
		int row;
		//Object obj = null;
		String str = null;
		XTextRange xTextRange = null;
		XCell xCell = null;
		
		for (row = 1; row <  CFTC_MAX_LINES; row++) {
			xCell = Calc.getCell(xSheet, 0, row);
			xTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xCell);
			str = xTextRange.getString();
			//obj = Calc.getVal(xSheet, 0, row);
			if (null == str || "0".equals(str) || "".equals(str)) {
				break;
			}
		}
		
		return row;
	}

	protected XCellRange getAllCftcRows(XSpreadsheet xSheet, String[] filters, String year) throws IndexOutOfBoundsException {
	
		XCell xCell = null;
		int row;
		int rows = DateUtils.getNumberOfWeeks(year);
	
		// hopefully each file maximum 90000 lines
		LOOP1:
		for (row = 1; row <  CFTC_MAX_LINES; row++) {
			xCell = xSheet.getCellByPosition(0, row);
			for (int i = 0; i < filters.length; i++) {
				if (filters[i].equals(xCell.getFormula()) || xCell.getFormula().contains(filters[i])) {
					break LOOP1;
				}
			}
		}
	
		int lastRow = (row + rows);
		int index;
		LOOP2:
		for (index = lastRow; index > row; index--) {
			xCell = xSheet.getCellByPosition(0, index);
			for (int i = 0; i < filters.length; i++) {
				if (filters[i].equals(xCell.getFormula()) || xCell.getFormula().contains(filters[i])) {
					break LOOP2;
				}
			}
		}
	
		XCellRange xRange = xSheet.getCellRangeByPosition(0, row, getSourceColumnLength() - 1, index);
	
		return xRange;
	}
}
