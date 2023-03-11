package cftc.wao;

import java.io.IOException;

public abstract class AbstractHtmlExtractor {

	public abstract String fetchPriceOrIndexFromUrl(String url) throws IOException;
	public abstract String fetchPriceOrIndexFromUrlByDataSet(String url) throws IOException;
//	public abstract String fetchPriceOrIndex(String instrument) throws IOException;
	public abstract String fetchPriceOrIndex(String instrument, boolean currentPrice) throws IOException;
	
//	public String fetchPriceOrIndex(InstrumentName instrument) throws IOException {
//		return fetchPriceOrIndex(instrument.name());
//	}
//	
//	public String fetchPriceOrIndex(CftcInstrument instrument) throws IOException {
//		return fetchPriceOrIndex(instrument.getInstrumentName());
//	}
}
