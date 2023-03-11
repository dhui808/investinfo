package cftc.vendor.investingcom;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cftc.wao.AbstractHtmlExtractor;

public class InvestingComHtmlExtractor extends AbstractHtmlExtractor {
	
	@Override
	public String fetchPriceOrIndex(String instrument, boolean currentPrice) throws IOException {
		
		String url = InvestingComHistoryDataUrl.valueOf(instrument).getUrl();
		
		return fetchLastWeekPriceOrIndexFromUrl(url, currentPrice);
	}
	
//	@Override
//	public String fetchPriceOrIndex(String instrument) throws IOException {
//		switch(instrument) {
//			case "NG": return fetchNgPrice();
//			case "OIL": return fetchOilLightSweetPrice();
//			case "GOLD": return fetchGoldPrice();
//			case "USD_INDEX": return fetchUsdIndex();
//			case "USD_CAD": return fetchCadFutures();
//			case "EURO_FX": return fetchEuroFutures();
//			case "USD_JPY": return fetchYenFutures();
//			case "SPX500": return fetchSPX500Index();
//			case "NASDAQ": return fetchNASDAQIndex();
//			case "DOW30": return fetchDow30Index();
//			case "US10Y": return fetchUs10YFutures();
//			default: return null;
//		}
//	}
	
//	public String fetchNgPrice() throws IOException {
//				
//		return fetchPriceOrIndexFromUrlByDataSet("https://www.investing.com/commodities/natural-gas");
//	}
//	
//	public String fetchOilLightSweetPrice() throws IOException {
//				
//		return fetchPriceOrIndexFromUrlByDataSet("https://www.investing.com/commodities/crude-oil");
//	}
//	
//	public String fetchGoldPrice() throws IOException {
//		
//		return fetchPriceOrIndexFromUrlByDataSet("https://www.investing.com/commodities/gold");
//	}
//	
//	public  String fetchUsdIndex() throws IOException {
//		
//		return fetchPriceOrIndexFromUrl("https://www.investing.com/currencies/us-dollar-index");
//	}
//	
//	public String fetchCadFutures() throws IOException {
//		
//		return fetchPriceOrIndexFromUrl("https://www.investing.com/currencies/usd-cad");
//	}
//
//	public String fetchEuroFutures() throws IOException {
//		
//		return fetchPriceOrIndexFromUrl("https://www.investing.com/currencies/eur-usd");
//	}
//	
//	public String fetchYenFutures() throws IOException {
//		
//		return fetchPriceOrIndexFromUrl("https://www.investing.com/currencies/usd-jpy");
//	}
//
//	public String fetchSPX500Index() throws IOException {
//		
//		return fetchPriceOrIndexFromUrlByDataSet("https://www.investing.com/indices/us-spx-500");
//	}
//
//	public String fetchNASDAQIndex() throws IOException {
//		
//		return fetchPriceOrIndexFromUrlByDataSet("https://www.investing.com/indices/nasdaq-composite");
//	}
//	
//	public String fetchDow30Index() throws IOException {
//		
//		return fetchPriceOrIndexFromUrlByDataSet("https://www.investing.com/indices/us-30");
//	}
//	
//	public String fetchUs10YFutures() throws IOException {
//		
//		return fetchPriceOrIndexFromUrl("https://www.investing.com/rates-bonds/u.s.-10-year-bond-yield");
//	}
	
	public String fetchPriceOrIndexFromUrl(String url) throws IOException {
		
		Document doc = Jsoup.connect(url).get();
		Element content = doc.getElementById("last_last");
		String price = content.text();
				
		return price;
	}
	
	public String fetchPriceOrIndexFromUrlByDataSet(String url) throws IOException {
		
		Document doc = Jsoup.connect(url).get();
		Element content = doc.getElementsByAttributeValue("data-test", "instrument-price-last").get(0);
		String price = content.text();
				
		return price;
	}
	
	public String fetchLastWeekPriceOrIndexFromUrl(String url, boolean currentPrice) throws IOException {
		
		int index = currentPrice? 0 : 1;
		Document doc = Jsoup.connect(url).get();
		Element content = doc.getElementById("curr_table");
		Elements tbodys = content.getElementsByTag("tbody");
		Element tbody = tbodys.get(0);
		Elements trs = tbody.getElementsByTag("tr");
		Element tr = trs.get(index);//it has to be before GMT 12:00 AM Monday, aka. before Sunday 8:00 PM EST. Complicated. Won't fix now.
		Elements tds = tr.getElementsByTag("td");
		Element td = tds.get(1);
		
		String price = td.text();
				
		return price;
	}
}
