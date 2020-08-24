package cftc.utils;

import java.util.Calendar;

public class Constants {
	
	private static final String USER_DIR = System.getProperty("user.dir").replace('\\', '/');
	private static final String PROJECT_PATH = USER_DIR.substring(0, USER_DIR.lastIndexOf("/")); 
	private static final String PROJECT_URL = "file:///" + PROJECT_PATH + "/";
	public static final String downloadUrl = PROJECT_URL + "download/";
	public static final String downloadCftcUrl = PROJECT_URL + "download/cftc/";
	public static final String downloadEiaUrl = PROJECT_URL + "download/eia/";
	public static final String preanalysisUrl = PROJECT_URL + "preanalysis/";
	public static final String productsUrl = PROJECT_URL + "products/";
	public static final String cftcSourceUrl = PROJECT_URL + "staging/cftc/";
	public static final String cftcSourceDirectory = "../staging/cftc/";
	public static final String stagingEiaUrl = PROJECT_URL + "staging/eia/";
	public static final String stagingEiaDirectory = "../staging/eia/";
	public static final String inventoryUrl = PROJECT_URL + "inventory/";
	public static final String inventoryDirectory = "../inventory/";
	public static final String chartsUrl = PROJECT_URL + "charts/";
	public static final String chartsDirectory = "../charts/";
	public static final String templateUrl = PROJECT_URL + "investinfo/templates/";
	public static final String backupFilePath = "../preanalysis/price.json";
	public static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
	public static final String targetCftcDirectory = "../download/cftc/";
	public static final String archiveCftcDirectory = "../download/cftc/";
	public static final String ngZipFilename = "fut_disagg_xls_" + CURRENT_YEAR + ".zip";
	public static final String forexZipFilename = "fut_fin_xls_" + CURRENT_YEAR + ".zip";
	public static final String NG_HISTORY_XLS_FILENAME = "ngshistory.xls";
	public static final String OIL_HISTORY_XLS_FILENAME = "WCESTUS1w.xls";
	public static final String DOWNLOAD_CFTC_PATH = PROJECT_PATH + "/download/cftc";
	public static final String DOWNLOAD_EIA_PATH = PROJECT_PATH + "/download/eia";
	public static final String PREANALYSIS_PATH = PROJECT_PATH + "/preanalysis";
	public static final String PRODUCTS_PATH = PROJECT_PATH + "/products";
	public static final String STAGING_EIA_PATH = PROJECT_PATH + "/staging/eia";
	public static final String STAGING_INVESTING_COM_PATH = PROJECT_PATH + "/staging/investing_com";
    public static final String VENDOR_FILE_PATH = "resources/vendor.json";
    public static final String INSTRUMENTS_FILE_PATH = "resources/instruments.json";
	public static final String GENERATED_FOLDER_PATH = PROJECT_URL + "investinfo/generated";
	public static final String BASE_YEAR = "" + CURRENT_YEAR;
	public static final String TEMPLATE_BASE_YEAR = "2018";
	public static final String DATA_SHEET = "_data";
	public static final String ANALYSIS_SHEET = "_analysis";
	public static final String CHARTS_DATA_SHEET = "_charts_data";
	public static final String CHARTS_SHEET = "_charts";
	public static final String BASE_YEAR_CHARTS_DATA_SHEET = CURRENT_YEAR + CHARTS_DATA_SHEET;
	public static final String BASE_YEAR_CHARTS_SHEET = CURRENT_YEAR + CHARTS_SHEET;
	public static final String PREVIOUS_YEAR_CHARTS_DATA_SHEET = CURRENT_YEAR -1 + CHARTS_DATA_SHEET;
	public static final String PREVIOUS_YEAR_CHARTS_SHEET = CURRENT_YEAR -1 + CHARTS_SHEET;
	public static final int CFTC_MAX_LINES = 90000;
	public static final Double ZERO_DOT_ZERO = 0.0;
	public static final String CFTC_COMMODITY_FILE = "f_year.xls";
	public static final String CFTC_FOREX_FILE = "FinFutYY.xls";
}
