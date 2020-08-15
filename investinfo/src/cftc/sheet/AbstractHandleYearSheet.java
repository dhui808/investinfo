package cftc.sheet;

import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;

import cftc.AbstractCftcAnalysis;
import static cftc.utils.Constants.*;

public  abstract class AbstractHandleYearSheet extends AbstractCftcAnalysis {


	/**
	 * Returns the data sheet index for the specified year. All latest years after the specified year must have been added.
	 * @param xDocument
	 * @param year
	 * @return
	 */
	protected int calculateNewDataSheetIndex(XSpreadsheetDocument xDocument, String year) {
		int idx = 1;
		
		XSpreadsheet xSheet = null;
		int currentYear = CURRENT_YEAR;
		int i = 0;
		
		try {
			xSheet = getSpreadsheet(xDocument, 1);
			//assume the sheets from the current up to year are already exist
			i = Integer.valueOf(year);
		} catch (Exception e) {
			System.out.println("sheet 1 does not exist.");
			i = currentYear;
		}
		
		idx = 1 + 2 * (currentYear - i);
		
		return idx;
	}

	/**
	 * Returns the charts data sheet index for the specified year. All latest years after the specified year must have been added.
	 * @param xDocument
	 * @param year
	 * @return
	 */
	protected int calculateNewChartsDataSheetIndex(XSpreadsheetDocument xDocument, String year) {
		
		int i = Integer.valueOf(year);
		int currentYear = CURRENT_YEAR;
		int idx = 2 * (currentYear - i);
		
		return idx;
	}
	
	protected Object[][] reverseArray(Object[][] src) {
		
		int length = src.length;
		
		Object[][] reversedFormulaArray = new Object[length][src[0].length];
		
		for (int i = length - 1; i >= 0; i--) {
			reversedFormulaArray[length - 1 - i] = src[i];
		}
		
		return reversedFormulaArray;
	}

	protected abstract String getTemplatePath();

	protected abstract int getTemplateColumnLength();
}
