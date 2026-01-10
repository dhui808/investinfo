package cftc.sheet;

import cftc.LO;
import cftc.model.CftcInstrument;
import cftc.model.InstrumentCategoryVisitable;
import cftc.model.InstrumentName;
import cftc.model.NgHenryHub;

public class TestAddYearSheet {

	public static void main(String[] args) throws Exception {

		CftcInstrument cftc = new NgHenryHub();
		String year = "2024";
		
		AddYearSheet addPreviousYearSheetForex = new AddYearSheetForex();
		AddYearSheet addPreviousYearSheetCommodity = new AddYearSheetCommodity();
		InstrumentCategoryVisitorAddYearSheet visitorAddPreviousYearSheet = new InstrumentCategoryVisitorAddYearSheet(
				addPreviousYearSheetForex, addPreviousYearSheetCommodity);
		
		AddYearSheet addPreviousYearSheet = InstrumentCategoryVisitable.accept(cftc.getCategory(),
				visitorAddPreviousYearSheet);
		
		LO.connect();
		
		addPreviousYearSheet.addDataAnalysis(cftc, year);
		addPreviousYearSheet.addDataChartsCurrentYear(cftc, year);
		
		LO.disconnect();
	}

}
