package cftc.dao;

import java.util.Map;

import cftc.vendor.VendorName;
import cftc.vendor.investingcom.InvestingComPriceIndexDao;

public class TestPriceIndexDao {

	public static void main(String[] args) throws Exception {
		PriceIndexDao dao = new InvestingComPriceIndexDao();
		
		Map<String, Double> priceMap = dao.retrievePriceIndex("investing_com_history", "20181127");
		
		for (String instrument : priceMap.keySet()) {
			System.out.println("dao.retrievePriceIndex(\"20181127\"):instrument: " + instrument + " price: " + priceMap.get(instrument));
		}
		
		String updateDate = dao.retrieveUpdateDate(VendorName.INVESTING_COM.getName());
		System.out.println("dao.retrieveUpdateDate(VendorName.INVESTING_COM.getName()):" + updateDate);
		
		dao.updatePriceIndexdesHistory("investing_com_history", "20181113", "NG", 4.377f);
	}

}
