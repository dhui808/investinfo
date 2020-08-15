package cftc.charts;

import cftc.model.InstrumentCategoryVisitor;

public class InstrumentCategoryVisitorChartsHandler implements InstrumentCategoryVisitor<AbstractChartsHandler> {

	private AbstractChartsHandler commodityChartsHandler;
	private AbstractChartsHandler forexChartsHandler;
	
	public InstrumentCategoryVisitorChartsHandler(AbstractChartsHandler forexChartsHandler,
			AbstractChartsHandler commodityChartsHandler) {
		
		this.forexChartsHandler = forexChartsHandler;
		this.commodityChartsHandler = commodityChartsHandler;
	}
	
	// InstrumentCategoryVisitor methods
	public AbstractChartsHandler visitForex() throws Exception {
		
		return forexChartsHandler;
	}

	public AbstractChartsHandler vistEnergy() throws Exception {
		
		return commodityChartsHandler;
	}

	public AbstractChartsHandler visitMetal() throws Exception {
		
		return commodityChartsHandler;
	}
	// End InstrumentCategoryVisitor methods

}
