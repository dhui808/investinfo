package cftc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cftc.dao.CftcDao;
import cftc.dao.PriceIndexDao;
import cftc.model.CftcInstrument;
import cftc.model.InstrumentUtils;
import cftc.model.PriceIndexDto;
import cftc.model.ProductList;
import cftc.model.table.CftcTableName;
import cftc.utils.DateUtils;
import cftc.utils.UnzipCftc;
import cftc.vendor.VendorName;
import cftc.wao.PriceIndexWao;

public abstract class PriceAndIndexHistoryService {
	
	protected PriceIndexWao wao;
	
	protected PriceIndexDao dao;
	
	public abstract void updateAllPriceIndexHistory() throws Exception;

	public abstract void updateAllPriceIndexHistory(String date) throws Exception;
	
	public abstract void loadAllPriceIndexHistory() throws Exception;

	public abstract void loadPriceIndexHistoryForProduct(String product) throws Exception;
	
	protected abstract String getHistoryTablename();
	
	protected void updatePriceIndexHistory(VendorName vendor) throws Exception {
		
		List<PriceIndexDto> priceList = retrieveLatestWeeklyClosePriceIndex(vendor);
		
		if (0 == priceList.size()) {
			return;
		}
		
		
		String updateDate = dao.retrieveUpdateDate(vendor.getName());
		String priceDate = priceList.get(0).weekStartDate;
		
//		if (StringUtils.equals(updateDate, priceDate)) {
//			System.out.println("price/index already updated for " + vendor + ", date:" + priceDate);
//			return;
//		}
		
		dao.updateLatestWeeklyPriceIndex(vendor.getPriceHistoryTablename(), priceList);
		dao.updateDate(vendor.getName(), priceDate);
	}

	protected void updatePriceIndexHistory(VendorName vendor, String date) throws Exception {
		
		List<PriceIndexDto> priceList = retrieveLatestWeeklyClosePriceIndex(vendor, date);
		
		if (0 == priceList.size()) {
			return;
		}
		
		
		String updateDate = dao.retrieveUpdateDate(vendor.getName());
		String priceDate = priceList.get(0).weekStartDate;
		
//		if (StringUtils.equals(updateDate, priceDate)) {
//			System.out.println("price/index already updated for " + vendor + ", date:" + priceDate);
//			return;
//		}
		
		dao.updateLatestWeeklyPriceIndex(vendor.getPriceHistoryTablename(), priceList);
		dao.updateDate(vendor.getName(), priceDate);
	}
	
	public List<PriceIndexDto> retrieveLatestWeeklyClosePriceIndex(VendorName vendor) throws Exception {
		
		return retrieveLatestWeeklyClosePriceIndex(vendor, null);
	}
	
	public List<PriceIndexDto> retrieveLatestWeeklyClosePriceIndex(VendorName vendor, String date) throws Exception {
		
		//do not retrieve price/index if it is after 2:00 PM Sunday EST when the markets may be open.
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		
		List<PriceIndexDto> priceList = new ArrayList<PriceIndexDto>();
		
		System.out.println("day:" + day + ", hour:" + hour);
		
		String cftcDate = null;
		String startDate =  null;
		String previousCftDate = null;
		
		if((Calendar.SUNDAY == day && 14 <= hour) || 
				(Calendar.SUNDAY < day && Calendar.FRIDAY > day) ||
				(Calendar.FRIDAY == day && 18  > hour)) {
			
			if (null == date) {
				//for the latest cftc release before 5:00 PM Friday or after 2:00 PM Sunday
				System.out.println("Should retrieve price/index before 2:00 PM Sunday and after Friday 6:00 PM.");
				return priceList;
			}
			
			System.out.println("Fetch last week close price / index.");
			
			cftcDate = DateUtils.getCurrentWeekTuesdayDate();
			if (Calendar.SUNDAY == day && 14 <= hour) {
				previousCftDate = cftcDate;
			} else {
				previousCftDate = DateUtils.getPreviousWeekTuesdayDate(cftcDate);
			}
			
			startDate =  DateUtils.getWeekStartDate(previousCftDate);
			
			priceList = retrievePriceList(vendor, cftcDate, startDate, false);
			
		} else {
		
			cftcDate = DateUtils.getLatestReleaseTuesdayDate();
			startDate =  DateUtils.getWeekStartDate(cftcDate);
			
			priceList = retrievePriceList(vendor, cftcDate, startDate);
		
		}
		
		return priceList;
	}
	
	private List<PriceIndexDto> retrievePriceList(VendorName vendor, String cftcDate, String weekStartDate) throws Exception {
		
		List<PriceIndexDto> priceList = new ArrayList<PriceIndexDto>();

		Map<String, Double> priceMap = wao.fetchPriceIndex(vendor);
		
		for (String instrumentName : priceMap.keySet()) {
			Double price = priceMap.get(instrumentName);
			PriceIndexDto dto = new PriceIndexDto();
			dto.releaseTuesdayDate= cftcDate;
			dto.weekStartDate = weekStartDate;
			dto.instrument = instrumentName;
			dto.price = Double.valueOf(price);
			
			priceList.add(dto);
		}
		
		return priceList;
	}

	private List<PriceIndexDto> retrievePriceList(VendorName vendor, String cftcDate, String weekStartDate, boolean currentPrice) throws Exception {
		
		List<PriceIndexDto> priceList = new ArrayList<PriceIndexDto>();

		Map<String, Double> priceMap = wao.fetchPriceIndex(vendor, currentPrice);
		
		for (String instrumentName : priceMap.keySet()) {
			Double price = priceMap.get(instrumentName);
			PriceIndexDto dto = new PriceIndexDto();
			dto.releaseTuesdayDate= cftcDate;
			dto.weekStartDate = weekStartDate;
			dto.instrument = instrumentName;
			dto.price = Double.valueOf(price);
			
			priceList.add(dto);
		}
		
		return priceList;
	}
	
//	public void adjustPriceHistory(String[] years) throws Exception {
//		
//		for(String year: years) {
//			adjustPriceHistory(year);
//		}
//	}
	
	public void adjustPriceHistory(String year) throws Exception {
		
		List<CftcInstrument> productList = ProductList.getAllProductList();
		adjustPriceHistory(year, productList);
	}

	public void adjustPriceHistory(String year, String product) throws Exception {
		
		List<CftcInstrument> cftcArray = InstrumentUtils.getCftcInstrument(product);
		
		adjustPriceHistory(year, cftcArray);
	}
	
	private void adjustPriceHistory(String year, List<CftcInstrument> productList) throws Exception {
		
		UnzipCftc.unzipCftcFilesYear(year, false);
		
		CftcDao cftcDao = new CftcDao();
		
		String firstReleaseDate = cftcDao.retrieveFirstReleaseDate(CftcTableName.CFTC_RELEASE_HISTORY.getName(), year);
		String lastYear = String.valueOf(Integer.parseInt(year) - 1);
		String lastReleaseDateLastYear = cftcDao.retrieveLastReleaseDate(CftcTableName.CFTC_RELEASE_HISTORY.getName(), lastYear);
		
		System.out.println("CFTC first release date for " + year + ": " + firstReleaseDate);
		System.out.println("CFTC last release date for " + lastYear + ": " + lastReleaseDateLastYear);
		
		for (CftcInstrument cftc : productList) {
			
			List<PriceIndexDto> priceList = dao.retrievePriceIndex(getHistoryTablename(), cftc.getInstrumentName(), year);
			String firstReleaseTuesdayDate = priceList.get(priceList.size() - 1).releaseTuesdayDate;
			
			System.out.println("Datebase first release date for " + cftc.getInstrumentName() + " for " + year + ": " + firstReleaseTuesdayDate);
			
			if (!firstReleaseDate.equals(firstReleaseTuesdayDate)) {
				
				dao.updateFirstCtfcReleaseDate(getHistoryTablename(), lastReleaseDateLastYear, firstReleaseTuesdayDate, cftc.getInstrumentName());
			}
			
			//TODO is it possible that the last release against positions in Wednesday?
		}
	}
}
