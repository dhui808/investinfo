package cftc.wao;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cftc.vendor.VendorName;
import cftc.vendor.investingcom.InvestingComPriceIndexWao;

public class TestPriceIndexWao {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, Exception {
		Map<String, Double> map = new InvestingComPriceIndexWao().fetchPriceIndex(VendorName.INVESTING_COM);
		System.out.println(map);
	}

}
