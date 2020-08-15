package cftc.vendor.investingcom;

import cftc.product.PriceAndIndexHandler;
import cftc.vendor.VendorName;

public class InvestingComUpdateSheetPriceIndex extends PriceAndIndexHandler {

	public InvestingComUpdateSheetPriceIndex() {
		dao = new InvestingComPriceIndexDao();
		vendorName = VendorName.INVESTING_COM;
	}
}
