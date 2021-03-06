package cftc.wao;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cftc.utils.Constants;

public class VendorProductsJsonParser {

	public static List<VendorWebModel> getVendorProducts() throws JsonParseException, JsonMappingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();

		System.out.println("Read vendor product from " + Constants.VENDOR_FILE_PATH);
		
		List<VendorWebModel> vendors = mapper.readValue(
				new File(Constants.VENDOR_FILE_PATH), 
				new TypeReference<List<VendorWebModel>>() {
		});
		
		return vendors;
	}
}
