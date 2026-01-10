package cftc.sheet;

import cftc.model.InstrumentCategoryVisitor;

public class InstrumentCategoryVisitorAddYearSheet implements InstrumentCategoryVisitor<AddYearSheet> {

	private AddYearSheet addPreviousYearSheetForex;
	private AddYearSheet addPreviousYearSheetCommodity;
	
	public InstrumentCategoryVisitorAddYearSheet(AddYearSheet addPreviousYearSheetForex,
			AddYearSheet addPreviousYearSheetCommodity) {
		
		this.addPreviousYearSheetForex = addPreviousYearSheetForex;
		this.addPreviousYearSheetCommodity = addPreviousYearSheetCommodity;
	}
	
	// InstrumentCategoryVisitor methods
	public AddYearSheet visitForex() throws Exception {
		
		return addPreviousYearSheetForex;
	}

	public AddYearSheet vistEnergy() throws Exception {
		
		return addPreviousYearSheetCommodity;
	}

	public AddYearSheet visitMetal() throws Exception {
		
		return addPreviousYearSheetCommodity;
	}
	// End InstrumentCategoryVisitor methods

}
