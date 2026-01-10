package cftc.download;

import java.io.IOException;

public class DownloadEia extends AbstractDownload {
	
	private static final String ngHistoryURL = "http://ir.eia.gov/ngs/ngshistory.xls";
	private static final String oilHiistoryPath = "https://www.eia.gov/dnav/pet/hist_xls/WCESTUS1w.xls";
	private static final String targetEiaDirectory = "../download/eia";
	
	public static void downloadNgInventoryHistory() throws IOException {
        
		//download ng inventory history
    	System.out.println("Downloading NG inventory history from " + ngHistoryURL);
    	download(ngHistoryURL, targetEiaDirectory);
    	System.out.println("Downloading NG history finished.");
	}
	
	public static void downloaOilInventoryHistory() throws IOException {
        
		//download oil inventory history
    	System.out.println("Downloading oil inventory history from " + oilHiistoryPath);
    	download(oilHiistoryPath, targetEiaDirectory);
    	System.out.println("Downloading oil inventory history finished.");
	}
}
