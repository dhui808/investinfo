package cftc.wao;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cftc.vendor.VendorName;

public abstract class PriceIndexWao {
	
	protected abstract AbstractHtmlExtractor getHtmlExtractor();
	
	protected List<VendorWebModel> vendorProductsModels;
	
	public PriceIndexWao() throws JsonParseException, JsonMappingException, IOException {
		loadVendorProducts();
	}
	
	public void loadVendorProducts() throws JsonParseException, JsonMappingException, IOException {
		vendorProductsModels = VendorProductsJsonParser.getVendorProducts();
	}

	/**
	 * Fetches the current price/index (not necessarily the week close price/index) from the specified vendor 
	 * @param vendor
	 * @return
	 * @throws Exception
	 */
	public Map<String, Double> fetchPriceIndex(VendorName vendorName) throws Exception {
		
		Map<String, Double> priceMap = new HashMap<String, Double>(); 
		
		Map<String, String> productUrlMap = null;
		String baseUrl = null;
		
		for(VendorWebModel v : vendorProductsModels) {
			if (v.getVendorName().toUpperCase().equals(vendorName.name())) {
				productUrlMap = v.getProducts();
				baseUrl = v.getBaseUrl();
				break;
			}
		}
		
		AbstractHtmlExtractor htmlExtractor = getHtmlExtractor();
		
		for (String key : productUrlMap.keySet()) {
			String instrument = key.toUpperCase();
			String url = baseUrl + productUrlMap.get(key);
			String price = htmlExtractor.fetchPriceOrIndexFromUrl(url);
			priceMap.put(instrument, Double.valueOf(price.replace(",", "")));
		}
		
		return priceMap;
	}
}
