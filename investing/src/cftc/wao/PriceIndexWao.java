package cftc.wao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cftc.model.InstrumentName;
import cftc.vendor.VendorName;

public abstract class PriceIndexWao {
	
	protected abstract AbstractHtmlExtractor getHtmlExtractor();
	
	protected List<VendorWebModel> vendorProductsModels;
	
	private static List<String> datasetList = new ArrayList<String>(7);
	
	static {
		datasetList.add(InstrumentName.NG.name());
		datasetList.add(InstrumentName.OIL.name());
		datasetList.add(InstrumentName.GOLD.name());
		datasetList.add(InstrumentName.US10Y.name());
		datasetList.add(InstrumentName.DOW30.name());
		datasetList.add(InstrumentName.SPX500.name());
		datasetList.add(InstrumentName.NASDAQ.name());
	}
	
	private static List<String> datasetList2 = new ArrayList<String>(3);
	
	static {
		datasetList2.add(InstrumentName.EURO_FX.name());
		datasetList2.add(InstrumentName.USD_CAD.name());
		datasetList2.add(InstrumentName.USD_JPY.name());
		datasetList2.add(InstrumentName.USD_INDEX.name());
	}
	
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
			String price = null;
			
			if (datasetList.contains(instrument)) {
				price = htmlExtractor.fetchPriceOrIndexFromUrlByDataSet(url);
			} else if (datasetList2.contains(instrument)) {
				price = htmlExtractor.fetchPriceOrIndexFromUrlByDataSetSpan(url);
			} else {
				price = htmlExtractor.fetchPriceOrIndexFromUrl(url);
			}
			
			priceMap.put(instrument, Double.valueOf(price.replace(",", "")));
		}
		
		return priceMap;
	}
	
	public Map<String, Double> fetchPriceIndex(VendorName vendorName, boolean currentPrice) throws Exception {
		
		Map<String, Double> priceMap = new HashMap<String, Double>(); 
		
		Map<String, String> productUrlMap = null;
		
		for(VendorWebModel v : vendorProductsModels) {
			if (v.getVendorName().toUpperCase().equals(vendorName.name())) {
				productUrlMap = v.getProducts();
				break;
			}
		}
		
		AbstractHtmlExtractor htmlExtractor = getHtmlExtractor();
		
		for (String key : productUrlMap.keySet()) {
			String instrument = key.toUpperCase();
			String price = htmlExtractor.fetchPriceOrIndex(instrument, currentPrice);
			priceMap.put(instrument, Double.valueOf(price.replace(",", "")));
		}
		
		return priceMap;
	}
}
