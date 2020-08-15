package cftc.charts;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.sun.star.drawing.XDrawPage;
import com.sun.star.drawing.XDrawPageSupplier;
import com.sun.star.drawing.XShape;
import com.sun.star.graphic.XGraphic;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.uno.Exception;

import jloputility.Chart2;
import jloputility.FileIO;
import jloputility.Images;
import jloputility.Lo;
import jloputility.Props;

public class CftcChart2 extends Chart2 {

	  public static List<XShape> getChartShapes(XSpreadsheet sheet)
	  {
	    //get draw page supplier for chart sheet 
	    XDrawPageSupplier pageSupplier = Lo.qi(XDrawPageSupplier.class, sheet); 
	    XDrawPage drawPage = pageSupplier.getDrawPage(); 
	    int numShapes = drawPage.getCount(); 
	    // System.out.println("No. of shapes: " + numShapes);
	    XShape shape = null;
	    String classID;
	    List<XShape> xshapeList = new ArrayList<XShape>(numShapes);
	    
	    for (int i=0; i < numShapes; i++) {
	      try {
	        shape = Lo.qi(XShape.class, drawPage.getByIndex(i)); 
	        xshapeList.add(shape);
	      }
	      catch(Exception e) {}
	    }

	    return xshapeList;
	  } 
	  
	  public static BufferedImage getChartImage(XShape chartShape)
	  {
	    if (chartShape == null) {
	      System.out.println("Could not find a chart");
	      return null;
	    }

	    //System.out.println("Shape type: " + chartShape.getShapeType());
	    //Props.showObjProps("Shape", chartShape);
	    XGraphic graphic = Lo.qi( XGraphic.class, 
	                             Props.getProperty(chartShape, "Graphic") );
	    if (graphic == null) {
	      System.out.println("No chart graphic found");
	      return null;
	    }

	    String tempFnm = FileIO.createTempFile("png");
	    if (tempFnm == null) {
	      System.out.println("Could not create a temporary file for the graphic");
	      return null;
	    }

	    Images.saveGraphic(graphic, tempFnm, "png");
	    BufferedImage im = Images.loadImage(tempFnm);
	    FileIO.deleteFile(tempFnm);
	    return im;
	  }
	  
	  public static List<BufferedImage> getChartImages(XSpreadsheet sheet)
	  {
	    List<XShape> chartShapes = CftcChart2.getChartShapes(sheet);

	    List<BufferedImage> chartImages = new ArrayList<BufferedImage>(chartShapes.size());
	    
	    for (XShape chartShape : chartShapes) {
	    	BufferedImage im = CftcChart2.getChartImage(chartShape);
	    	chartImages.add(im);
	    }
	    
	    return chartImages;
	  }
}
