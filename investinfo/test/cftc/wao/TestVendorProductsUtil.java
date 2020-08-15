package cftc.wao;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TestVendorProductsUtil {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		List<VendorWebModel> vendors = VendorProductsJsonParser.getVendorProducts();

		System.out.println(vendors);
	}

}
