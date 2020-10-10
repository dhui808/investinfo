package cftc;

import java.util.List;

import cftc.model.PriceIndexDto;
import cftc.vendor.VendorName;
import cftc.vendor.investingcom.InvestingComPriceIndexHistoryService;

public class TestPriceAndIndexHistoryService {
	
	public static void main(String[] args) throws RuntimeException, Exception {
		
		PriceAndIndexHistoryService service = new InvestingComPriceIndexHistoryService();
		List<PriceIndexDto> list = service.retrieveLatestWeeklyClosePriceIndex(VendorName.INVESTING_COM);
		service.updateAllPriceIndexHistory();
	}
}
