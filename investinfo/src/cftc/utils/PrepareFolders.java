package cftc.utils;

import static cftc.utils.Constants.*;

import java.io.File;

public class PrepareFolders {
	
	private static File downloadFolder = new File(downloadUrl.substring(0, downloadUrl.length() - 1));
	private static File downloadCftcFolder = new File(downloadCftcUrl.substring(0, downloadCftcUrl.length() - 1));
	private static File downloadEiaFolder = new File(downloadEiaUrl.substring(0, downloadEiaUrl.length() - 1));
	private static File preanalysisFolder = new File(preanalysisUrl.substring(0, preanalysisUrl.length() - 1));
	private static File productsFolder = new File(productsUrl.substring(0, productsUrl.length() - 1));
	private static File cftcSourceFolder = new File(cftcSourceUrl.substring(0, cftcSourceUrl.length() - 1));
	private static File stagingEiaFolder = new File(STAGING_EIA_PATH);
	private static File stagingInvestingComFolder = new File(STAGING_INVESTING_COM_PATH);
	private static File inventoryFolder = new File(inventoryUrl.substring(0, inventoryUrl.length() - 1));
	
	public static void prepareFolders() {
		
		if (!downloadFolder.exists() ) {
			downloadFolder.mkdirs();
			downloadCftcFolder.mkdirs();
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
