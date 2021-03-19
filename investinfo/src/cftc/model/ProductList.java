package cftc.model;

import java.util.ArrayList;
import java.util.List;

public class ProductList {
	
	public static List<CftcInstrument> getAllProductList() {
		List<CftcInstrument> productList  = new ArrayList<CftcInstrument>();
		
		productList.addAll(getForexProductList());
		productList.addAll(getCommodityProductList());
		
		return productList;
	}
	
	public static List<CftcInstrument> getCommodityProductList() {
		List<CftcInstrument> productList  = new ArrayList<CftcInstrument>();
		
		productList.addAll(getMetalProductList());
		productList.addAll(getEnergyProductList());
		
		return productList;
	}
	
	public static List<CftcInstrument> getEnergyProductListUnique() {
		List<CftcInstrument> productList  = new ArrayList<CftcInstrument>();
		productList.add(new NgHenryHub());
		productList.add(new OilLightSweet());
		
		return productList;
	}
	
	public static List<CftcInstrument> getEnergyProductList() {
		List<CftcInstrument> productList  = new ArrayList<CftcInstrument>();
		productList.add(new NgHenryHub());
		productList.add(new NgNewYork());
		productList.add(new OilLightSweet());
		
		return productList;
	}
	
	public static List<CftcInstrument> getForexProductList() {
		
		List<CftcInstrument> productList  = new ArrayList<CftcInstrument>();
		productList.add(new UsdIndex());
		productList.add(new CadFutures());
		productList.add(new EuroFutures());
		productList.add(new Us10YearNotes());
		
		return productList;
	}
	
	public static List<CftcInstrument> getMetalProductList() {
		
		List<CftcInstrument> productList  = new ArrayList<CftcInstrument>();
		productList.add(new Gold());
		
		return productList;
	}
}
