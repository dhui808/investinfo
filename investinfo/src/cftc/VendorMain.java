package cftc;

import static cftc.utils.Constants.CURRENT_YEAR;

import java.util.Date;
import java.util.List;

import cftc.charts.AbstractChartsHandler;
import cftc.charts.CommodityChartsHandler;
import cftc.charts.ForexChartsHandler;
import cftc.charts.InstrumentCategoryVisitorChartsHandler;
import cftc.download.DownloadCftc;
import cftc.marketchart.MarketAnalysis;
import cftc.model.CftcInstrument;
import cftc.model.InstrumentCategory;
import cftc.model.InstrumentCategoryVisitable;
import cftc.model.InstrumentName;
import cftc.model.InstrumentUtils;
import cftc.product.AbstractProductDocumentHandler;
import cftc.product.EnergyDocumentHandler;
import cftc.product.ForexDocumentHandler;
import cftc.product.InventoryHandler;
import cftc.product.MetalDocumentHandler;
import cftc.product.PriceAndIndexHandler;
import cftc.sheet.AddYearSheet;
import cftc.sheet.AddYearSheetCommodity;
import cftc.sheet.AddYearSheetForex;
import cftc.sheet.InstrumentCategoryVisitorAddYearSheet;
import cftc.tachart.TechnicalAnalysis;
import cftc.utils.CftcProperties;
import cftc.utils.DateUtils;
import cftc.utils.PrepareFolders;
import cftc.utils.UnzipCftc;
import cftc.vendor.VendorName;

public abstract class VendorMain {

	protected CftcHistoryService cftcHistoryService = new CftcHistoryService();
	protected InventoryHistoryService inventoryHistoryService = new InventoryHistoryService();

	protected AddYearSheet addPreviousYearSheetForex = new AddYearSheetForex();
	protected AddYearSheet addPreviousYearSheetCommodity = new AddYearSheetCommodity();
	protected InstrumentCategoryVisitorAddYearSheet visitorAddPreviousYearSheet = new InstrumentCategoryVisitorAddYearSheet(
			addPreviousYearSheetForex, addPreviousYearSheetCommodity);

	protected AbstractChartsHandler commodityChartsHandler = new CommodityChartsHandler();
	protected AbstractChartsHandler forexChartsHandler = new ForexChartsHandler();
	protected InstrumentCategoryVisitorChartsHandler visitorChartsHandler = new InstrumentCategoryVisitorChartsHandler(
			forexChartsHandler, commodityChartsHandler);

	protected InventoryHandler updateSheetInventory = new InventoryHandler();
	protected UpdateEnergyAnalysis updateEnergyAnalysis = new UpdateEnergyAnalysis();

	protected UpdateCommodifiesAnalysis updateCommodifiesAnalysis;
	protected UpdateForexAnalysis updateForexAnalysis;
	protected PriceAndIndexHistoryService priceAndIndexHistoryService;
	protected PriceAndIndexHandler updateSheetPriceIndex;

	protected VendorName vendorName;
	
	protected String[] yearsNeedAdjusting;

	protected MarketAnalysis marketAnalysis = new MarketAnalysis();
	
	protected CftcFinancialHistoryService cftcFinancialHistoryService = new CftcFinancialHistoryService();
	protected TechnicalAnalysis taAnalysis = new TechnicalAnalysis();
	
	public void parse(String[] args, List<String> argList) {

		Date start = new Date();

		try {
			LO.connect();
			parseAndExecute(args, argList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LO.disconnect();
		}

		Date end = new Date();

		System.out.println("Time cost:" + (end.getTime() - start.getTime()) / 1000 + " sec.");
	}

	private void parseAndExecute(String[] args, List<String> argList) throws Exception {

		String instrument = null;
		boolean allyears = false;
		String category = null;
		String year = null;
		String date = null;
		int idxIns = argList.indexOf("-instrument");
		int idxAllYears = argList.indexOf("-allyears");
		int idxCat = argList.indexOf("-category");
		int idxYear = argList.indexOf("-year");
		int idxDate = argList.indexOf("-date");

		if (idxIns >= 0) {
			instrument = argList.get(idxIns + 1);
		}

		if (idxAllYears >= 0) {
			allyears = true;
		}

		if (idxCat >= 0) {
			category = argList.get(idxCat + 1);
		}

		if (idxYear >= 0) {
			year = argList.get(idxYear + 1);
		}

		if (idxDate >= 0) {
			date = argList.get(idxDate + 1);
		}

		if (null != instrument && null != category) {
			System.out.println("Should not specify both instrument and category.");
			return;
		}

		PrepareFolders.prepareFolders();

		yearsNeedAdjusting = CftcProperties.getYearsNeedAdjusting();
		
		// add documents
		if (argList.contains("ad") || argList.contains("adddoc")) {

			if (null != instrument) {
				addProduct(instrument, allyears);
			} else if (null != category) {
				addProductCategory(category, allyears);
			} else {
				addAllProducts(allyears);
			}

			return;
		}

		// remove sheets
		if (argList.contains("rs") || argList.contains("removesheet")) {

			if (idxYear < 0) {
				System.out.println("The year must be specified for deleting the sheet.");
				return;
			}

			if (null != instrument) {
				removeSheetByInstrumentYear(instrument, year);
			} else if (null != category) {
				removeSheetByCategoryYear(category, year);
			} else {
				removeSheetByYear(year);
			}

			return;
		}

		// add sheets
		if (argList.contains("as") || argList.contains("addsheet")) {

			if (idxYear < 0) {
				System.out.println("The year must be specified for adding the sheet.");
				return;
			}

			if (null != instrument) {
				addSheetByInstrumentYear(instrument, year);
			} else if (null != category) {
				addSheetByCategoryYear(category, year);
			} else {
				addSheetByYear(year);
			}

			return;
		}

		int idxHistory = argList.indexOf("-history");
		int idxLoad = argList.indexOf("-load");
		int idxUpdate = argList.indexOf("-update");
		int idxAdjust = argList.indexOf("-adjust");
		int idxCreate = argList.indexOf("-create");
		
		// update inventory
		if (argList.contains("i") || argList.contains("inventory")) {

			if (idxHistory >= 0) {
				if (idxLoad >= 0) {
					System.out.println("Load commodity inventory history into database");
					loadCommodityInventoryHistory();
				} else if (idxUpdate >= 0) {
					System.out.println("Update commodity inventory history in database");
					updateCommodityInventoryHistory();
				} else {
					System.out.println("Should specify either -load or -update option.");
					return;
				}
			} else if (idxAdjust >= 0) {
				System.out.println("adjust inventory history for " + year);
				adjustInventoryHistory(year);
			} else if (idxYear >= 0) {
				System.out.println("add inventory to sheet " + year);
				addSheetInventory(year);
			} else {
				System.out.println("update commodity inventory in spreadsheet");
				upateInventoryInSpreadsheet(date);
			}

			return;
		}

		// update price/index
		if (argList.contains("p") || argList.contains("price")) {

			if (idxHistory >= 0) {
				if (idxLoad >= 0) {
					if (null != instrument) {
						System.out.println("Load Price/Index history into database for instrument " + instrument);
						loadPriceIndexHistoryForProduct(instrument);
					} else {
						System.out.println("Load Price/Index history into database");
						loadPriceIndexHistory();
					}
				} else if (idxUpdate >= 0) {
					System.out.println("Update price/index history in database");
					updatePriceIndexHistory();
				} else {
					System.out.println("Should specify either -load or -update option.");
					return;
				}
			} else if (idxAdjust >= 0) {
				System.out.println("adjust price/index history for " + year);
				adjustPriceHistory(year);
			} else if (idxYear >= 0) {
				System.out.println("add Price/index to sheet " + year);
				addSheetPrice(year);
			} else {
				System.out.println("update price/index in spreadsheet");
				upatePriceOrIndexInSpreadsheet(date);
			}

			return;
		}

		// delete a row for a specific date
		if (argList.contains("d") || argList.contains("delete")) {

			int idxDelete = argList.indexOf("d");

			if (idxDelete < 0) {
				idxDelete = argList.indexOf("delete");
			}

			String deleteDate = argList.get(idxDelete + 1);

			System.out.println("delete a row for " + deleteDate);

			deleteCftcRowByDate(deleteDate);

			return;
		}

		// update CFTC release history
		if (argList.contains("cftc")) {

			if (idxYear >= 0) {
				System.out.println("add CFTC release history for " + year);
				addCftcHistory(year);
			} else {
				System.out.println("add CFTC release history for all years.");
				addAllCftcHistory();
			}

			return;
		}

		// export charts
		if (argList.contains("c") || argList.contains("chart")) {
			if (null != instrument) {
				exportChartByInstrumentYear(instrument, year);
			} else if (null != category) {
				exportChartByCategoryYear(category, year);
			} else {
				exportChartByYear(year);
			}

			return;
		}
		
		// creates/updates market chart
		if (argList.contains("mc") || argList.contains("marketchart")) {
			// creates market chart
			createMarketChart();

			return;
		}
		
		// creates/updates technical analysis data
		if (argList.contains("ta") || argList.contains("technicalanalysis")) {
			if (idxYear >= 0) {
				System.out.println("add technical analysis history for " + year);
				cftcFinancialHistoryService.updateCftcFinancialHistory(year);
			} else {
				System.out.println("add technical analysis history for all years.");
				cftcFinancialHistoryService.updateAllReleaseHistory();
			}

			return;
		}
		
		// creates/updates technical analysis chart
		if (argList.contains("tac") || argList.contains("technicalanalysischart")) {
			// creates ta chart
			taAnalysis.createTechnicalAnalysisChart();

			return;
		}
				
		// update all sheets with the latest CFTC release.
		boolean forceDownload = false;
		if (argList.contains("-f") || argList.contains("-forcedownload")) {
			forceDownload = true;
		}

		System.out.println("update all, force download? " + forceDownload);
		String cftcDate = (null == date) ? DateUtils.getCurrentWeekTuesdayDate() : date;

		updateAll(forceDownload, cftcDate);
	}

	public void updateAll(boolean forceDownload, String date) throws Exception {

		DownloadCftc.downloadCftcZipFiles(forceDownload, date);
		UnzipCftc.unzipCftcFilesByDate(date);

		// load the latest inventory into the database
		updateCommodityInventoryHistory();

		// loads the latest price to the database
		updatePriceIndexHistory();

		updateCommodifiesAnalysis.updateDataAnalysisByDate(date);
		updateCommodifiesAnalysis.updateCharts();
		updateCommodifiesAnalysis.upatePriceOrIndexInSpreadsheet(vendorName, date);
		updateEnergyAnalysis.upateInventoryInSpreadsheet(date);

		updateForexAnalysis.updateDataAnalysisByDate(date);
		updateForexAnalysis.updateCharts();
		updateForexAnalysis.upatePriceOrIndexInSpreadsheet(vendorName, date);
		
		//add cftc release history
		cftcHistoryService.addCftcReleaseHistory();
		
		// updates market chart
		marketAnalysis.updateMarketChart();
		
		// updates ta data
		cftcFinancialHistoryService.addCftcFinancialHistory();
				
		// updates ta chart
		taAnalysis.updateTaChart();
	}

	public void upatePriceOrIndexInSpreadsheet(String date) throws Exception {

		updatePriceIndexHistory(date);

		updateCommodifiesAnalysis.upatePriceOrIndexInSpreadsheet(vendorName, date);

		updateForexAnalysis.upatePriceOrIndexInSpreadsheet(vendorName, date);
	}

	public void upateInventoryInSpreadsheet(String date) throws Exception {

		updateEnergyAnalysis.upateInventoryInSpreadsheet(date);
	}

	public void deleteCftcRowByDate(String date) throws Exception {

		updateCommodifiesAnalysis.deleteByDate(date);
		updateForexAnalysis.deleteByDate(date);
		marketAnalysis.deleteByDate(date);
		taAnalysis.deleteByDate(date);
	}

	public void addSheet(CftcInstrument cftc, String year) throws Exception {

		DownloadCftc.downloadCftcZipFilesByYear(false, year);
		UnzipCftc.unzipCftcFilesYear(year);

		AddYearSheet addPreviousYearSheet = InstrumentCategoryVisitable.accept(cftc.getCategory(),
				visitorAddPreviousYearSheet);
		addPreviousYearSheet.addDataAnalysis(cftc, year);
		if (("" + CURRENT_YEAR).equals(year)) {
			addPreviousYearSheet.addDataChartsCurrentYear(cftc, year);
		} else {
			addPreviousYearSheet.addDataChartsPreviousYear(cftc, year);
		}

		updateSheetInventory.updateInventory(cftc, year);
		updateSheetPriceIndex.updatePriceOrIndex(cftc, year);
	}

	public void addSheetByInstrumentYear(String product, String year) throws Exception {

		InstrumentName instrument = InstrumentName.valueOf(product.toUpperCase());

		addSheetByInstrumentYear(instrument, year);
	}

	private void addSheetByInstrumentYear(InstrumentName instrument, String year) throws Exception {

		CftcInstrument[] cftcArray = InstrumentUtils.getCftcInstrument(instrument);

		for (CftcInstrument cftc : cftcArray) {

			addSheet(cftc, year);
		}
	}

	public void addSheetByCategoryYear(String category, String year) throws Exception {

		InstrumentCategory cat = InstrumentCategory.valueOf(category.toUpperCase());

		for (InstrumentName inst : InstrumentName.values()) {
			if (inst.getCategory().equals(cat)) {
				addSheetByInstrumentYear(inst, year);
			}
		}
	}

	public void addSheetByYear(String year) throws Exception {

		DownloadCftc.downloadCftcZipFilesByYear(false, year);
		UnzipCftc.unzipCftcFilesYear(year);

		if (("" + CURRENT_YEAR).equals(year)) {
			addPreviousYearSheetCommodity.addDataAnalysis(year);
			addPreviousYearSheetCommodity.addDataChartsCurrentYear(year);

			addPreviousYearSheetForex.addDataAnalysis(year);
			addPreviousYearSheetForex.addDataChartsCurrentYear(year);
		} else {
			addPreviousYearSheetCommodity.addDataAnalysis(year);
			addPreviousYearSheetCommodity.addDataChartsPreviousYear(year);

			addPreviousYearSheetForex.addDataAnalysis(year);
			addPreviousYearSheetForex.addDataChartsPreviousYear(year);
		}

		updateSheetInventory.updateInventory(year);
		updateSheetPriceIndex.updatePriceOrIndex(year);
	}

	public void addSheetInventory(String year) throws Exception {

		updateSheetInventory.updateInventory(year);
	}

	public void addSheetPrice(String year) throws Exception {

		updateSheetPriceIndex.updatePriceOrIndex(year);
	}

	public void removeSheetByInstrumentYear(String product, String year) throws Exception {

		InstrumentName instrument = InstrumentName.valueOf(product.toUpperCase());

		removeSheetByInstrumentYear(instrument, year);
	}

	private void removeSheetByInstrumentYear(InstrumentName instrument, String year)
			throws Exception {

		CftcInstrument[] cftcArray = InstrumentUtils.getCftcInstrument(instrument);
		AddYearSheet addPreviousYearSheet = null;

		for (CftcInstrument cftc : cftcArray) {

			addPreviousYearSheet = InstrumentCategoryVisitable.accept(cftc.getCategory(), visitorAddPreviousYearSheet);
			addPreviousYearSheet.removeDataAnalysis(cftc, year);
			addPreviousYearSheet.removeCharts(cftc, year);
		}
	}

	public void removeSheetByCategoryYear(String category, String year) throws Exception {

		InstrumentCategory cat = InstrumentCategory.valueOf(category.toUpperCase());

		for (InstrumentName inst : InstrumentName.values()) {
			if (inst.getCategory().equals(cat)) {
				removeSheetByInstrumentYear(inst, year);
			}
		}
	}

	public void removeSheetByYear(String year) throws Exception {

		addPreviousYearSheetCommodity.removeDataAnalysis(year);
		addPreviousYearSheetCommodity.removeCharts(year);

		addPreviousYearSheetForex.removeDataAnalysis(year);
		addPreviousYearSheetForex.removeCharts(year);
	}

	public void loadPriceIndexHistory() throws Exception {

		priceAndIndexHistoryService.loadAllPriceIndexHistory();
		priceAndIndexHistoryService.adjustPriceHistory("2013");
		priceAndIndexHistoryService.adjustPriceHistory("2019");
		//priceAndIndexHistoryService.adjustPriceHistory(yearsNeedAdjusting);
	}

	public void loadPriceIndexHistoryForProduct(String product) throws Exception {

		priceAndIndexHistoryService.loadPriceIndexHistoryForProduct(product);
		priceAndIndexHistoryService.adjustPriceHistory("2013", product);
		priceAndIndexHistoryService.adjustPriceHistory("2019", product);
		//priceAndIndexHistoryService.adjustPriceHistory(yearsNeedAdjusting);
	}

	/**
	 * Update price / index for the latest cftc release week after 6:00 PM Friday and before
	 * 2:00 PM Sunday
	 *  
	 * @throws Exception
	 */
	public void updatePriceIndexHistory() throws Exception {

		priceAndIndexHistoryService.updateAllPriceIndexHistory();
	}
	
	/**
	 * Update price / index for the week before the latest cftc release week before 6:00 PM Friday
	 * and of the latest cftc release week or after 2:00 PM Sunday of the coming cftc release week.
	 *  
	 * @throws Exception
	 */
	private void updatePriceIndexHistory(String date) throws Exception {
		
		priceAndIndexHistoryService.updateAllPriceIndexHistory(date);
		
	}
	
	public void loadCommodityInventoryHistory() throws Exception {

		inventoryHistoryService.loadAllInventoryHistory();
		inventoryHistoryService.adjustInventoryHistory("2013");
		inventoryHistoryService.adjustInventoryHistory("2019");
		//inventoryHistoryService.adjustInventoryHistory(yearsNeedAdjusting);
	}

	public void updateCommodityInventoryHistory() throws Exception {

		InventoryHistoryService loadHistory = new InventoryHistoryService();
		loadHistory.updateAllInventoryHistory();
	}

	/**
	 * Adjusts inventory history, considering CFTC release may not report the
	 * instrument positions for Tuesday.
	 * 
	 * @throws Exception
	 */
	public void adjustInventoryHistory(String year) throws Exception {

		inventoryHistoryService.adjustInventoryHistory(year);
	}

	/**
	 * Adjusts inventory history, considering CFTC release may not report the
	 * instrument positions for Tuesday.
	 * 
	 * @throws Exception
	 */
	public void adjustPriceHistory(String year) throws Exception {

		priceAndIndexHistoryService.adjustPriceHistory(year);
	}

	public void addCftcHistory(String year) throws Exception {

		cftcHistoryService.updateCftcReleaseHistory(year);
	}

	public void addAllCftcHistory() throws Exception {

		cftcHistoryService.updateAllReleaseHistory();
	}

	private void addCftcProduct(CftcInstrument cftc) throws Exception {

		DownloadCftc.downloadCftcZipFiles(false);
		UnzipCftc.unzipCurrentYearCftcFiles();
		String year = "" + CURRENT_YEAR;

		AbstractProductDocumentHandler productDocumentHanlder = getProductDocumentHandler(cftc.getCategory().name());

		productDocumentHanlder.addProductDocument(cftc);
		updateSheetPriceIndex.updatePriceOrIndex(cftc, year);
		updateSheetInventory.updateInventory(cftc, year);
	}

	public void addProduct(String product, boolean allyears) throws Exception {

		InstrumentName instrument = InstrumentName.valueOf(product.toUpperCase());
		CftcInstrument[] cftcArray = InstrumentUtils.getCftcInstrument(instrument);

		for (CftcInstrument cftc : cftcArray) {

			addCftcProduct(cftc);
		}

		if (allyears) {
			for (int year = CURRENT_YEAR - 1; year > 2010; year--) {
				for (CftcInstrument cftc : cftcArray) {
					addSheet(cftc, "" + year);
				}
			}
		}
	}

	public void addProductCategory(String category, boolean allyears) throws Exception {

		InstrumentCategory cat = InstrumentCategory.valueOf(category.toUpperCase());

		for (InstrumentName inst : InstrumentName.values()) {
			if (inst.getCategory().equals(cat)) {
				addProduct(inst.name(), allyears);
			}
		}
	}

	public void addAllProducts(boolean allyears) throws Exception {

		for (InstrumentCategory category : InstrumentCategory.values()) {
			addProductCategory(category.name(), allyears);
		}
		
		createMarketChart();
		
		cftcFinancialHistoryService.updateAllReleaseHistory();
		taAnalysis.createTechnicalAnalysisChart();
	}

	private AbstractProductDocumentHandler getProductDocumentHandler(String category) {

		AbstractProductDocumentHandler productDocumentHanlder = null;

		switch (category.toUpperCase()) {
		case "METAL":
			productDocumentHanlder = new MetalDocumentHandler();
			break;
		case "ENERGY":
			productDocumentHanlder = new EnergyDocumentHandler();
			break;
		case "FOREX":
			productDocumentHanlder = new ForexDocumentHandler();
			break;
		default:
			throw new RuntimeException("Invalid produc categoty: " + category);
		}

		return productDocumentHanlder;
	}

	public void exportChartByInstrumentYear(String product, String year) throws Exception {

		InstrumentName instrument = InstrumentName.valueOf(product.toUpperCase());

		exportChartByInstrumentYear(instrument, year);
	}

	private void exportChartByInstrumentYear(InstrumentName instrument, String year)
			throws Exception {

		CftcInstrument[] cftcArray = InstrumentUtils.getCftcInstrument(instrument);

		for (CftcInstrument cftc : cftcArray) {

			exportChartByInstrumentYear(cftc, year);
		}
	}

	public void exportChartByInstrumentYear(CftcInstrument cftc, String year) throws Exception {

		AbstractChartsHandler chatrsHandler = InstrumentCategoryVisitable.accept(cftc.getCategory(),
				visitorChartsHandler);
		chatrsHandler.exportChartAsImage(cftc, year);
	}

	public void exportChartByCategoryYear(String category, String year) throws Exception {

		InstrumentCategory cat = InstrumentCategory.valueOf(category.toUpperCase());

		for (InstrumentName inst : InstrumentName.values()) {
			if (inst.getCategory().equals(cat)) {
				exportChartByInstrumentYear(inst, year);
			}
		}
	}

	public void exportChartByYear(String year) throws Exception {

		commodityChartsHandler.exportChartAsImage(year);

		forexChartsHandler.exportChartAsImage(year);
	}
	
	private void createMarketChart() throws Exception {
		
		marketAnalysis.createMarketChart();
	}
}
