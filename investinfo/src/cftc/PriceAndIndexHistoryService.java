package cftc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sun.star.uno.RuntimeException;

import cftc.dao.CftcDao;
import cftc.dao.PriceIndexDao;
import cftc.model.CftcInstrument;
import cftc.model.PriceIndexDto;
import cftc.model.ProductList;
import cftc.model.table.CftcTableName;
import cftc.utils.DateUtils;
import cftc.utils.UnzipCftc;
import cftc.vendor.VendorName;
import cftc.vendor.investingcom.InvestingComTablename;
import cftc.wao.PriceIndexWao;

public abstract class PriceAndIndexHistoryService {
	
	protected PriceIndexWao wao;
	
	protected PriceIndexDao dao;
	
	public abstract void updateAllPriceIndexHistory() throws Exception;
	
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
	
	public List<PriceIndexDto> retrieveLatestWeeklyClosePriceIndex(VendorName vendor) throws Exception {
		
		//do not retrieve price/index if it is after 2:00 PM Sunday EST when the markets may be open.
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		
		List<PriceIndexDto> priceList = new ArrayList<PriceIndexDto>();
		
		System.out.println("day:" + day + ", hour:" + hour);
		
		if((Calendar.SUNDAY == day && 14 <= hour) || 
				(Calendar.SUNDAY < day && Calendar.FRIDAY > day) ||
				(Calendar.FRIDAY == day && 18  > hour)) {
			
			System.out.println("Should retrieve price/index before 2:00 PM Sunday and after Friday 6:00 PM.");
			return priceList;
		}
		
		String cftcDate = DateUtils.getLatestReleaseTuesdayDate();
		String startDate =  DateUtils.getWeekStartDate(cftcDate);
		
		priceList = retrievePriceList(vendor, cftcDate, startDate);
		
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
	
	public void adjustPriceHistory(String year) throws Exception {
		
		UnzipCftc.unzipCftcFilesYear(year);
		
		List<CftcInstrument> productList = ProductList.getAllProductList();
		
		CftcDao cftcDao = new CftcDao();
		
		String firstReleaseDate = cftcDao.retrieveFirstReleaseDate(CftcTableName.CFTC_RELEASE_HISTORY.getName(), year);
		String lastYear = String.valueOf(Integer.parseInt(year) - 1);
		String lastReleaseDateLastYear = cftcDao.retrieveLastReleaseDate(CftcTableName.CFTC_RELEASE_HISTORY.getName(), lastYear);
		
		System.out.println("CFTC first relase date for " + year + ": " + firstReleaseDate);
		System.out.println("CFTC last relase date for " + lastYear + ": " + lastReleaseDateLastYear);
		
		for (CftcInstrument cftc : productList) {
			
			List<PriceIndexDto> priceList = dao.retrievePriceIndex(getHistoryTablename(), cftc.getInstrumentName(), year);
			String firstReleaseTuesdayDate = priceList.get(priceList.size() - 1).releaseTuesdayDate;
			
			System.out.println("Datebase first relase date for " + year + ": " + firstReleaseTuesdayDate);
			
			if (!firstReleaseDate.equals(firstReleaseTuesdayDate)) {
				
				dao.updateFirstCtfcReleaseDate(getHistoryTablename(), lastReleaseDateLastYear, firstReleaseTuesdayDate, cftc.getInstrumentName());
			}
			
			//TODO is it possible that the last release against positions in Wednesday?
		}
	}
	
}
