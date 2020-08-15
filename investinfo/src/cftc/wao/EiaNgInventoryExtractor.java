package cftc.wao;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;

public class EiaNgInventoryExtractor {

	/**
		EIA Natural Gas Storage Data
		Total (08/17/18): 2,435 Bcf 
		Total (08/10/18): 2,387 Bcf 
		Net change: 48 Bcf 
		Implied flow: 48 Bcf 
		Year ago stocks: 3,119 Bcf
		% change from year ago: -21.9 % 
		5-year avg stocks: 3,034 Bcf
		% change from 5-year avg: -19.7 % 
	 */
	public static String[] retrieveNgInventory() throws IOException {

		URL url = new URL("http://ir.eia.gov/ngs/wngsr.txt");
		String content = IOUtils.toString(new InputStreamReader(url.openStream()));

		
		StringTokenizer lineSt = new StringTokenizer(content, "\n");
		Map<String, String> tokenMap = new HashMap<String, String>();
		
		while(lineSt.hasMoreTokens()) {
			
			String line  = lineSt.nextToken();
			if (!line.contains(":")) continue;
			
			StringTokenizer st = new StringTokenizer(line, ":");
			String key = null;
			String token = null;
			
			while(st.hasMoreTokens()) {
				token = st.nextToken();
				if (null == key) {
					key = token;
				} else {
					tokenMap.put(key, token);
				}
			}
		}
		
		//extract current inventory
		Map<String, String> invMap = new HashMap<String, String>();
		for (String key : tokenMap.keySet()) {
			if (key.startsWith("Total")) {
				invMap.put(key, tokenMap.get(key));
			}
		}
		
		//there two items in invMap
		Object[] keyTotals = invMap.keySet().toArray();
		String total = null;
		String change = null;
		String total1 = keyTotals[0].toString();
		String year1 = total1.substring(total1.lastIndexOf('/') + 1, total1.lastIndexOf('/') + 3);
		String total2 = keyTotals[1].toString();
		String year2 = total2.substring(total2.lastIndexOf('/') + 1, total2.lastIndexOf('/') + 3);
		if (year1.compareTo(year2) > 0) {
			total = invMap.get(keyTotals[0].toString());
		} else if (year1.compareTo(year2) < 0) {
			total = invMap.get(keyTotals[1].toString()); 	
		} else if (keyTotals[0].toString().compareTo(keyTotals[1].toString()) > 0) {
			total = invMap.get(keyTotals[0].toString());
		} else {
			total = invMap.get(keyTotals[1].toString());
		}
		
		//remove non-digits
		total = total.replaceAll("[^\\d.]", "");
		
		//inventory change
		change = tokenMap.get("Implied flow");
		change = change.replaceAll("[^\\d.]", "");
		
		//assemble the array
		String[] inventory = new String[2];
		inventory[0] = total;
		inventory[1] = change;
		
		return inventory;
	}
}
