package cftc;

import cftc.vendor.VendorName;
import cftc.vendor.investingcom.InvestingComPriceIndexHistoryService;
import cftc.vendor.investingcom.InvestingComUpdateCommodifiesAnalysis;
import cftc.vendor.investingcom.InvestingComUpdateForexAnalysis;
import cftc.vendor.investingcom.InvestingComUpdateSheetPriceIndex;
import jloputility.Lo;

public class InvestingComMain extends VendorMain {

	public InvestingComMain() throws Exception {
		updateCommodifiesAnalysis = new InvestingComUpdateCommodifiesAnalysis();
		updateForexAnalysis = new InvestingComUpdateForexAnalysis();
		priceAndIndexHistoryService = new InvestingComPriceIndexHistoryService();
		updateSheetPriceIndex = new InvestingComUpdateSheetPriceIndex();
		vendorName = VendorName.INVESTING_COM;
	}
	
	/**
	 * Adjusts inventory history, considering CFTC release may not report the instrument positions for Tuesday.
	 * @throws Exception
	 */
	public void adjustPriceHistory(String year) throws Exception {
		
		try {
			PriceAndIndexHistoryService loadHistory = new InvestingComPriceIndexHistoryService();
			loadHistory.adjustPriceHistory(year);

		} finally {
			Lo.closeOffice();
		}
	}
}
