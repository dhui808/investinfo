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
        
        boolean ngDownloaded = zipFileDownloaded(ngfilename, targetCftcDirectory);
        
        if (!ngDownloaded || forceDownload) {
        	System.out.println("Downloading " + ngfilename + "...");
        	download(ngSourceURL, targetCftcDirectory);
        }
        
        //download forex
        String forexSourceURL = cftcURL + forexFilename;
        
        boolean forexDownloaded = zipFileDownloaded(forexFilename, targetCftcDirectory);
        
        if (!forexDownloaded || forceDownload) {
        	System.out.println("Downloading " + forexFilename + "...");
        	download(forexSourceURL, targetCftcDirectory);
        }
	}
	
	private static boolean zipFileDownloaded(String zipFilename, String targetDirectory) throws IOException {
		
		boolean downloaded = false;
		
		File file = new File( targetDirectory + zipFilename);
		
		if (file.exists()) {
			
			long lastModified = file.lastModified();
			Date in = new Date(lastModified);
			LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
			
			downloaded = !isNewCftcReleaseAvailable(ldt);
		}
		
		System.out.println(zipFilename + " downloaded? " + downloaded);
		
		return downloaded;
	}
	
	private static boolean isNewCftcReleaseAvailable(LocalDateTime lastUpdate) {
		
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime previousFriday = now.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
		LocalDateTime nextFriday = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
		
		if (lastUpdate.isBefore(previousFriday)) {
			//last update date is not Friday
			return true;
		} 
		
		if (lastUpdate.toLocalDate().isEqual(previousFriday.toLocalDate())) {
			//last update date is the previous Friday
			if (lastUpdate.toLocalTime().isBefore(LocalTime.of(15, 31))) {
				return true;
			} 
		}
		
		//at this point, last update is the last week's release. Need to check if 
		//this week's release is available
		if (now.isBefore(nextFriday)) {
			return false;
		}
		
		//today is the next Friday
		if (now.toLocalDate().isEqual(nextFriday.toLocalDate())) {
			
			if (now.toLocalTime().isBefore(LocalTime.of(15, 31))) {
				return false;
			} 
		}
		
		//Now it is either after Friday or is Friday after 3:31 PM.
		if (lastUpdate.isBefore(nextFriday)) {
			return true;
		}
		
		//last update is this Friday
		if (lastUpdate.toLocalTime().isBefore(LocalTime.of(15, 31))) {
			return true;
		}
		
		//last update is already the latest release.
		return false;
	}
}
