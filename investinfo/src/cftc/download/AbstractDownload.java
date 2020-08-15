package cftc.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class AbstractDownload {
	
	public static Path download(String sourceURL, String targetDirectory) throws IOException {
	    
		URL url = new URL(sourceURL);
	    String fileName = sourceURL.substring(sourceURL.lastIndexOf('/') + 1, sourceURL.length());
	    Path targetPath = new File(targetDirectory + File.separator + fileName).toPath();
	    Files.copy(openStream(url), targetPath, StandardCopyOption.REPLACE_EXISTING);

	    return targetPath;
	}
	
	public static InputStream openStream(URL url) {
		try {
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");

			return httpcon.getInputStream();
		} catch (IOException e) {
			String error = e.toString();
			throw new RuntimeException(e);
		}
	}
	
	public static void downloadFromUrl(URL url, String localFilename) throws IOException {
	    InputStream is = null;
	    FileOutputStream fos = null;

	    try {
	        URLConnection urlConn = url.openConnection();//connect

	        is = urlConn.getInputStream();               //get connection inputstream
	        fos = new FileOutputStream(localFilename);   //open outputstream to local file

	        byte[] buffer = new byte[4096];              //declare 4KB buffer
	        int len;

	        //while we have available data, continue downloading and storing to local file
	        while ((len = is.read(buffer)) > 0) {  
	            fos.write(buffer, 0, len);
	        }
	    } finally {
	        try {
	            if (is != null) {
	                is.close();
	            }
	        } finally {
	            if (fos != null) {
	                fos.close();
	            }
	        }
	    }
	}
}
