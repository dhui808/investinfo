package cftc.wao;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * vender/vendor.jso is read into this object.
 *
 */
public class VendorWebModel {
	String vendorName;
	String baseUrl;
	Map<String, String> products;
	
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public Map<String, String> getProducts() {
		return products;
	}
	public void setProducts(Map<String, String> products) {
		this.products = products;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
