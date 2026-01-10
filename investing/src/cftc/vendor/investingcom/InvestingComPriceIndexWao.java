package cftc.vendor.investingcom;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cftc.wao.AbstractHtmlExtractor;
import cftc.wao.PriceIndexWao;

public class InvestingComPriceIndexWao extends PriceIndexWao {

	public InvestingComPriceIndexWao() throws JsonParseException, JsonMappingException, IOException {
		super();
	}
	
	protected AbstractHtmlExtractor getHtmlExtractor() {
		return new InvestingComHtmlExtractor();
	}

}
