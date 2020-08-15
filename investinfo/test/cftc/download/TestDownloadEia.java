package cftc.download;

import java.io.IOException;

public class TestDownloadEia {

	public static void main(String[] args) throws IOException {
		DownloadEia.downloadNgInventoryHistory();
		DownloadEia.downloaOilInventoryHistory();
	}

}
