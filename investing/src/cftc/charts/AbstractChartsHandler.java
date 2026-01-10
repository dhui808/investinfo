package cftc.charts;

import static cftc.utils.Constants.CURRENT_YEAR;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;

import cftc.AbstractCftcAnalysis;
import cftc.model.CftcInstrument;
import jloputility.Chart2;
import jloputility.Images;
import jloputility.Lo;

public abstract class AbstractChartsHandler extends AbstractCftcAnalysis {
	
	public void exportChartAsImage(String year) throws Exception {

		List<CftcInstrument> productList = getProductList();

		for (CftcInstrument cftc : productList) {

			exportChartAsImage(cftc, year);
		}
	}
	
	public void exportChartAsImage(CftcInstrument cftc, String year) throws Exception {

		if (null == year) {
			exportChartAsImage(cftc);
		} else {
			exportChartAsImageByYear(cftc, year);
		}
	}
	
	private void exportChartAsImage(CftcInstrument cftc) throws Exception {
		
		for (int year = CURRENT_YEAR; year > 2010; year--) {
			
			exportChartAsImageByYear(cftc, "" + year);
		}
	}

	private void exportChartAsImageByYear(CftcInstrument cftc, String year) throws Exception {
		
		String chartsFilePath = cftc.getChartsFilePath();
		XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
		XSpreadsheet chartsSheet = getChartsSpreadsheetByYear(chartsDocument, year);

		BufferedImage bi = CftcChart2.getChartImages(chartsSheet).get(0);
		
		// defect in Chart2.getChartImage or its dependency. has to call another time
		if (null == bi) {
			bi = CftcChart2.getChartImages(chartsSheet).get(0);
		}
		
		String imgPath = cftc.getNetLongImageFilePath(year);
		String chartsFolder = imgPath.substring(0, imgPath.lastIndexOf("/"));
		File folder = new File(chartsFolder);
		
		if (!folder.exists()) {
			folder.mkdir();
		}
		
		Images.saveImage( bi, imgPath);
		
		Lo.closeDoc(chartsDocument);
	}
}
