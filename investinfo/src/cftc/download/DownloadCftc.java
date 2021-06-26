package cftc.download;

import static cftc.utils.Constants.forexZipFilename;
import static cftc.utils.Constants.ngZipFilename;
import static cftc.utils.Constants.targetCftcDirectory;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import cftc.utils.CftcProperties;

public class DownloadCftc extends AbstractDownload {
	
	private static final String cftcURL = "https://www.cftc.gov/files/dea/history/";
	
	//if there is a date specified, zip filename should use the year specified. 
	public static void downloadCftcZipFiles(boolean forceDownload, String date) throws IOException {
		String year = date.substring(0, 4);
		downloadCftcZipFilesByYear(forceDownload, year);
	}
	
	public static void downloadCftcZipFilesByYear(boolean forceDownload, String year) throws IOException {

		String ngfilename = "fut_disagg_xls_" + year + ".zip";
		String forexFilename = "fut_fin_xls_" + year + ".zip";
		downloadCftcZipFilesByFilename(forceDownload, ngfilename, forexFilename);
	}
	
	public static void downloadCftcZipFiles(boolean forceDownload) throws IOException {
        
		downloadCftcZipFilesByFilename(forceDownload, ngZipFilename, forexZipFilename);
	}
	
	public static void downloadCftcZipFilesByFilename(boolean forceDownload, String ngfilename, String forexFilename) throws IOException {
		//download ng
        String ngSourceURL = cftcURL + ngfilename;
        String projectBasePath = CftcProperties.getInvestinfoBasePath();
        String cftcDirectory = projectBasePath + targetCftcDirectory;
        
        boolean ngDownloaded = zipFileDownloaded(ngfilename, cftcDirectory);
        
        if (!ngDownloaded || forceDownload) {
        	System.out.println("Downloading " + ngfilename + "...");
        	download(ngSourceURL, cftcDirectory);
        }
        
        //download forex
        String forexSourceURL = cftcURL + forexFilename;
        
        boolean forexDownloaded = zipFileDownloaded(forexFilename, cftcDirectory);
        
        if (!forexDownloaded || forceDownload) {
        	System.out.println("Downloading " + forexFilename + "...");
        	download(forexSourceURL, cftcDirectory);
        }
	}
	
	private static boolean zipFileDownloaded(String zipFilename, String targetDirectory) throws IOException {
		
		boolean downloaded = false;
		
		File file = new File( targetDirectory + zipFilename);
		
		if (file.exists()) {
			
			long lastModified = file.lastModified();
			Date in = new Date(lastModified);
			LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
			
			downloaded = !needNewDownload(ldt);
		}
		
		System.out.println(zipFilename + " downloaded? " + downloaded);
		
		return downloaded;
	}
	
	private static boolean needNewDownload(LocalDateTime lastUpdate) {
		
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime fridayThisWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY))
				.withHour(15).withMinute(30).withSecond(5);
		
		if (lastUpdate.isAfter(fridayThisWeek)) {
			return false;
		}
		
		// new release available
		if (now.isAfter(fridayThisWeek)) {
			return true;
		}
		
		return false;
	}
}
