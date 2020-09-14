package cftc;

import java.io.IOException;

import cftc.vendor.investingcom.InvestingComHtmlExtractor;

public class TestHtmlExtractor {
	public static void main(String[] args) throws IOException {
		String price = new InvestingComHtmlExtractor().fetchNgPrice();
		System.out.println("NG price:" + price);
		price =  new InvestingComHtmlExtractor().fetchOilLightSweetPrice();
		System.out.println("OIL price:" + price);
		price =  new InvestingComHtmlExtractor().fetchCadFutures();
		System.out.println("USDCAD price:" + price);
		price =  new InvestingComHtmlExtractor().fetchUsdIndex();
		System.out.println("USD Index:" + price);
		price =  new InvestingComHtmlExtractor().fetchEuroFutures();
		System.out.println("Euro FX:" + price);
	}
}
