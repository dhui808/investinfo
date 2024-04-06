package cftc.wao;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EiaOilInventoryExtractor {
	
	/**
	 * Return the latest oil inventory and change compared with the previous week, as an String array.
	 * @return
	 * @throws IOException
	 */
	public static String[] retrieveOilInventory() throws IOException {
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Connection", "keep-alive");
		headers.put("Host", "www.eia.gov");
		headers.put("Sec-Fetch-Dest", "document");
		headers.put("Sec-Fetch-Mode", "navigate");
		headers.put("Sec-Fetch-Site", "none");
		headers.put("Sec-Fetch-User", "?1");
		headers.put("Upgrade-Insecure-Requests", "1");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
		
		String url = "https://www.eia.gov/dnav/pet/PET_SUM_SNDW_A_EPC0_SAX_MBBL_W.htm";
		Document doc = Jsoup.connect(url).headers(headers).get();
		Elements dataRows = doc.getElementsByClass("DataRow");
		Element currentDataRow = dataRows.first();
		Element currentInv = currentDataRow.getElementsByClass("Current2").first();
		Element previousInv = currentDataRow.getElementsByClass("DataB").last();
		
		String total = currentInv.ownText().replaceAll("[^\\d.]", "");
		String preiousTotal = previousInv.ownText().replaceAll("[^\\d.]", "");
		Integer change = Integer.parseInt(total) - Integer.parseInt(preiousTotal);
		
		String[] inventory = new String[2];
		inventory[0] = total;
		inventory[1] = change.toString();
		
		return inventory;
	}
}
