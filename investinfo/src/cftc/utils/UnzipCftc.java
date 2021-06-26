package cftc.utils;

import static cftc.utils.Constants.CFTC_COMMODITY_FILE;
import static cftc.utils.Constants.CFTC_FOREX_FILE;
import static cftc.utils.Constants.CURRENT_YEAR;
import static cftc.utils.Constants.archiveCftcDirectory;
import static cftc.utils.Constants.cftcSourceDirectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipCftc {
    
	public static String projectBasePath = CftcProperties.getInvestinfoBasePath();
	
	public static void unzipCftcFilesByDate(String date) {
    	
		String year = date.substring(0, 4);
    	unzipCftcFilesYear(year);
    }

    public static void unzipCurrentYearCftcFiles() {
    	
    	unzipCftcFilesYear("" + CURRENT_YEAR);
    }

    public static void unzipCftcFilesYear(String year) {
    	
    	String yearPath = year + "/";
    	String zipfileFolder =  projectBasePath + archiveCftcDirectory;
    	String outputFolder = projectBasePath + cftcSourceDirectory + yearPath;
    	
    	//unzip ng
    	String ngInputFile = zipfileFolder + "fut_disagg_xls_" + year + ".zip";
    	boolean ngNeedUnzip = needUnzipFile(CFTC_COMMODITY_FILE, outputFolder, ngInputFile, year);
    	if (ngNeedUnzip) {
    		UnzipCftc.unzipIt(ngInputFile, outputFolder);
    	} else {
    		System.out.println(ngInputFile + " does not need to be unzipped.");
    	}
    	
    	//unzip forex
    	String forexInputFile = zipfileFolder + "fut_fin_xls_" + year + ".zip";
    	boolean forexNeedUnzip = needUnzipFile(CFTC_FOREX_FILE, outputFolder, forexInputFile, year);
    	if (forexNeedUnzip) {
    		UnzipCftc.unzipIt(forexInputFile, outputFolder);
    	} else {
    		System.out.println(forexInputFile + " does not need to be unzipped.");
    	}
    }
    
    private static boolean needUnzipFile(String inFile, String outputFolder, String zipFilePath, String year) {
    	
    	boolean fileExists = fileExists(inFile, outputFolder);
    	if (!fileExists) return true;
    	
    	if (year.equals("" + CURRENT_YEAR)) {
    		
    		File zipFile = new File(zipFilePath);
    		long zipFileLastModified = zipFile.lastModified();
			
    		File file = new File(outputFolder + inFile);
    		long lastModified = file.lastModified();
    		
    		long diff = lastModified - zipFileLastModified;
    		
    		if (diff < 100) {
    			return true;
    		}
    	}
    	
    	// if it is the first week of the year, we need to download file.
    	if ( 1 == LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)) {
    		return true;
    	}
    	
    	return false;
    }

    private static boolean fileExists(String inFile, String outputFolder) {
    	
    	File file = new File(outputFolder + inFile);
    	
    	boolean fileExists = file.exists();
    	
    	return fileExists;
    }

    /**
     * Unzip it
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    static void unzipIt(String zipFile, String outputFolder){

     byte[] buffer = new byte[1024];

     try{

    	//create output directory is not exists
    	File folder = new File(outputFolder);
    	if(!folder.exists()){
    		folder.mkdir();
    	}

    	//get the zip file content
    	ZipInputStream zis =
    		new ZipInputStream(new FileInputStream(zipFile));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();

    	while(ze!=null){

    	   String fileName = ze.getName();
           File newFile = new File(outputFolder + File.separator + fileName);

           System.out.println("file unzip : "+ newFile.getAbsoluteFile());

            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            while ((len = zis.read(buffer)) > 0) {
       		fos.write(buffer, 0, len);
            }

            fos.close();
            ze = zis.getNextEntry();
    	}

        zis.closeEntry();
    	zis.close();

    	System.out.println("Done unzip " + zipFile);

    }catch(IOException ex){
       ex.printStackTrace();
    }
   }
}
