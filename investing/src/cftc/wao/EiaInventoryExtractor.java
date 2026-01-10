package cftc.wao;

import java.io.IOException;

import cftc.model.CftcInstrument;

public class EiaInventoryExtractor {

	public static String[] retrieveInventory(CftcInstrument instrument) throws IOException {
		
		switch(instrument.getInstrumentName()) {
			case "NG": return EiaNgInventoryExtractor.retrieveNgInventory();
			case "OIL": return EiaOilInventoryExtractor.retrieveOilInventory();
			default: throw new RuntimeException("Invalid instrument:" + instrument.getInstrumentName());
		}
	}
}
