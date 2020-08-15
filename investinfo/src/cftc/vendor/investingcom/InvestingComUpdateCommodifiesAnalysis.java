package cftc.vendor.investingcom;

import cftc.UpdateCommodifiesAnalysis;

public class InvestingComUpdateCommodifiesAnalysis extends UpdateCommodifiesAnalysis {

	public InvestingComUpdateCommodifiesAnalysis() {
		dao = new InvestingComPriceIndexDao();
	}
}
