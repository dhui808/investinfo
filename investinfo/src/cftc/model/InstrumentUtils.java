package cftc.model;

import java.util.Arrays;
import java.util.List;

public class InstrumentUtils {

	public static List<CftcInstrument> getCftcInstrument(String product) {
		
		InstrumentName instrument = InstrumentName.valueOf(product.toUpperCase());
		CftcInstrument[] cftcArray = InstrumentUtils.getCftcInstrument(instrument);
		
		return Arrays.asList(cftcArray);
	}
	
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
		case "USD_JPY":
			cftc = new CftcInstrument[] {new YenFutures()};
			break;
		case "SPX500":
			cftc = new CftcInstrument[] {new SPX500()};
			break;
		case "NASDAQ":
			cftc = new CftcInstrument[] {new NASDAQ()};
			break;
		case "DOW30":
			cftc = new CftcInstrument[] {new Dow30()};
			break;
		case "US10Y":
			cftc = new CftcInstrument[] {new Us10YearNotes()};
			break;
		default:
			throw new RuntimeException("Invalid instrument: " + instrument.name());
		}
		
		return cftc;
	}
}
