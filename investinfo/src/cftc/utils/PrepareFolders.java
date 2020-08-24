package cftc.utils;

import static cftc.utils.Constants.*;

import java.io.File;

public class PrepareFolders {
	
	private static File downloadCftcFolder = new File(DOWNLOAD_CFTC_PATH);
	private static File downloadEiaFolder = new File(DOWNLOAD_EIA_PATH);
	private static File preanalysisFolder = new File(PREANALYSIS_PATH);
	private static File productsFolder = new File(PRODUCTS_PATH);
	private static File cftcSourceFolder = new File(cftcSourceUrl.substring(0, cftcSourceUrl.length() - 1));
	private static File stagingEiaFolder = new File(STAGING_EIA_PATH);
	private static File stagingInvestingComFolder = new File(STAGING_INVESTING_COM_PATH);
	private static File inventoryFolder = new File(inventoryUrl.substring(0, inventoryUrl.length() - 1));
	
	public static void prepareFolders() {

		if (!downloadCftcFolder.exists() ) {
			downloadCftcFolder.mkdirs();
		}
		
		if (!downloadEiaFolder.exists() ) {
			downloadEiaFolder.mkdirs();
		}
		
		if (!preanalysisFolder.exists() ) {
			preanalysisFolder.mkdirs();
		}
		
		if (!productsFolder.exists() ) {
			productsFolder.mkdirs();
		}
		
		if (!cftcSourceFolder.exists() ) {
			cftcSourceFolder.mkdirs();
		}
		
		if (!stagingEiaFolder.exists() ) {
			stagingEiaFolder.mkdirs();
		}
		
		if (!stagingInvestingComFolder.exists() ) {
			stagingInvestingComFolder.mkdirs();
		}
		
		if (!inventoryFolder.exists() ) {
			inventoryFolder.mkdirs();
		}
	}
}
