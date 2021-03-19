package cftc.vendor.investingcom;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import cftc.wao.AbstractHtmlExtractor;

public class InvestingComHtmlExtractor extends AbstractHtmlExtractor {
	
	public String fetchPriceOrIndex(String instrument) throws IOException {
		switch(instrument) {
			case "NG": return fetchNgPrice();
			case "OIL": return fetchOilLightSweetPrice();
			case "GOLD": return fetchGoldPrice();
			case "USD_INDEX": return fetchUsdIndex();
			case "USD_CAD": return fetchCadFutures();
			case "EURO_FX": return fetchEuroFutures();
			case "US10Y": return fetchUs10YFutures();
			default: return null;
		}
	}

	public String fetchNgPrice() throws IOException {
				
		return fetchPriceOrIndexFromUrl("https://www.investing.com/commodities/natural-gas");
	}
	
	public String fetchOilLightSweetPrice() throws IOException {
				
		return fetchPriceOrIndexFromUrl("https://www.investing.com/commodities/crude-oil");
	}
	
	public String fetchGoldPrice() throws IOException {
		
		return fetchPriceOrIndexFromUrl("https://www.investing.com/commodities/gold");
	}
	
	public  String fetchUsdIndex() throws IOException {
		
		return fetchPriceOrIndexFromUrl("https://www.investing.com/quotes/us-dollar-index");
	}
	
	public String fetchCadFutures() throws IOException {
		
		return fetchPriceOrIndexFromUrl("https://www.investing.com/currencies/usd-cad");
	}

	public String fetchEuroFutures() throws IOException {
		
		return fetchPriceOrIndexFromUrl("https://www.investing.com/currencies/eur-usd");
	}

	public String fetchUs10YFutures() throws IOException {
		
		return fetchPriceOrIndexFromUrl("https://www.investing.com/rates-bonds/u.s.-10-year-bond-yield");
	}
	
	public String fetchPriceOrIndexFromUrl(String url) throws IOException {
		
		Document doc = Jsoup.connect(url).get();
		Element content = doc.getElementById("last_last");
		String price = content.text();
				
		return price;
	}
	
}
