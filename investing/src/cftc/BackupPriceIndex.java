package cftc;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cftc.model.CftcInstrument;
import cftc.model.ProductList;
import cftc.utils.Constants;

/**
 * @deprecated
 * Backs up the close price or index of the previous week in case we need to run update
 * price again.
 *
 */
public class BackupPriceIndex {
	
	public static void backup() throws IOException {
		//do not back up price if it is after 2:00 PM Sunday EST
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		System.out.println("day:" + day + ", hour:" + hour);
		if((Calendar.SUNDAY == day && 14 <= hour) || 
				(Calendar.SUNDAY < day && Calendar.FRIDAY > day) ||
				(Calendar.FRIDAY == day && 18  > hour)) {
			
			System.out.println("Should back up before 2:00 PM Sunday or after Friday 6:00 PM.");
			return;
		}
		Map<String, Double> priceMap = new HashMap<String, Double>();
		List<CftcInstrument> productList = null;
		
		productList = ProductList.getCommodityProductList();
		updatePriceMap(priceMap, productList);
		
		productList = ProductList.getForexProductList();
		updatePriceMap(priceMap, productList);
		
		ObjectMapper mapper = new ObjectMapper();
		File json = new File(Constants.backupFilePath);
		mapper.writeValue(json, priceMap);
		
		System.out.println("Back up prices to " + Constants.backupFilePath);
	}
	
	private static void updatePriceMap(Map<String, Double> pirceMap, List<CftcInstrument> productList) throws IOException {
		for (CftcInstrument cftc : productList) {
			//Double price = cftc.fetchPriceIndex();
			//pirceMap.put(cftc.getInstrumentName(), price);
		}
	}
	
	public static Map<String, Double> getLastWeekClosePriceIndex() throws JsonParseException, JsonMappingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();

		System.out.println("Read prices from " + Constants.backupFilePath);
		
		Map<String, Double> pirceMap = mapper.readValue(
				new File(Constants.backupFilePath), 
				new TypeReference<Map<String, Double>>() {
		});
		
		return pirceMap;
	}
}
