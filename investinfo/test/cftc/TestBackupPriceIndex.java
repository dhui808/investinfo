package cftc;

import java.io.IOException;
import java.util.Map;

public class TestBackupPriceIndex {

	public static void main(String[] args) throws IOException {
		BackupPriceIndex.backup();
		Map<String, Double> priceMap = BackupPriceIndex.getLastWeekClosePriceIndex();
		System.out.println(priceMap.toString());
	}

}
