package cftc.vendor.investingcom;

import cftc.UpdateForexAnalysis;

public class InvestingComUpdateForexAnalysis extends UpdateForexAnalysis {
	
	public InvestingComUpdateForexAnalysis() {
		dao = new InvestingComPriceIndexDao();
	}
}
