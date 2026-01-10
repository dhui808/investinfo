package cftc.vendor.investingcom;

import cftc.dao.PriceIndexDao;

public class InvestingComPriceIndexDao extends PriceIndexDao {

	protected String[] getVendorPriceIndexTablenames() {

	    InvestingComTablename[] tables = InvestingComTablename.values();
	    String[] tablenames = new String[tables.length];

		for (int i = 0; i < tables.length; i++) {
			tablenames[i] = tables[i].getTablename();
		}
		
		return tablenames;
	}

	@Override
	protected String getVendorPriceIndexTablenameForProduct(String product) {
		
		InvestingComTablename ivTablename = InvestingComTablename.valueOf(product.toUpperCase());
		
		return ivTablename.getTablename();
	}

}
