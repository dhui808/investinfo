package cftc.model;

import com.sun.star.uno.RuntimeException;

public class InstrumentUtils {

	
	public static CftcInstrument[] getCftcInstrument(InstrumentName instrument) {
		
		CftcInstrument[] cftc = null;
		
		switch (instrument.name()) {
		case "GOLD":
			cftc = new CftcInstrument[] {new Gold()};
			break;
		case "NG":
			cftc = new CftcInstrument[] {new NgHenryHub(), new NgNewYork()};
			break;
		case "OIL":
			cftc = new CftcInstrument[] {new OilLightSweet()};
			break;
		case "USD_CAD":
			cftc = new CftcInstrument[] {new CadFutures()};
			break;
		case "USD_INDEX":
			cftc = new CftcInstrument[] {new UsdIndex()};
			break;
		case "EURO_FX":
			cftc = new CftcInstrument[] {new EuroFutures()};
			break;
		default:
			throw new RuntimeException("Invalid instrument: " + instrument.name());
		}
		
		return cftc;
	}
}
