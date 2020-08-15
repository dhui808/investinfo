package cftc;

import java.util.List;

import com.sun.star.beans.XPropertySet;
import com.sun.star.chart2.ScaleData;
import com.sun.star.chart2.XAxis;
import com.sun.star.chart2.XChartDocument;
import com.sun.star.chart2.XDataSeries;
import com.sun.star.chart2.data.XDataProvider;
import com.sun.star.chart2.data.XDataSequence;
import com.sun.star.chart2.data.XDataSource;
import com.sun.star.chart2.data.XLabeledDataSequence;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XTableCharts;
import com.sun.star.table.XTableChartsSupplier;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.UnoRuntime;

import cftc.model.CftcInstrument;
import jloputility.Chart2;
import jloputility.Lo;
import jloputility.Props;

public class TestCharts extends AbstractCftcAnalysis {

	public static void main(String[] args) throws Exception {
		TestCharts testCharts = new TestCharts();
		try {
			LO.connect();
			testCharts.listCharts();
		} finally {
			LO.disconnect();
		}
		
		System.exit(0);
	}

	public void listCharts() throws Exception {
		String productsUrl = cftc.utils.Constants.productsUrl;
		String chartsFilePath = productsUrl + "ng-ny-charts.ods";
		
		XSpreadsheetDocument chartsDocument = loadDestDocument(chartsFilePath);
		XSpreadsheet destSheet3 = getSpreadsheet(chartsDocument,3);
		
		XTableChartsSupplier chartsSupplier = 
                Lo.qi(XTableChartsSupplier.class, destSheet3);
		XTableCharts tableCharts = chartsSupplier.getCharts();
		String[] chartNames = tableCharts.getElementNames();
		for (String s : chartNames) {
			System.out.println(s);
		}
		
		XChartDocument chartDoc = Chart2.getChartDoc(destSheet3, chartNames[0]);

		XDataSeries[] ds = Chart2.getDataSeries(chartDoc);
		
		chartDoc.getDataProvider();
		chartDoc.getFirstDiagram();
		
		XDataSeries ds1 = ds[0];
		System.out.println("ds.length:" + ds.length);
		XDataSource xDataSource = (XDataSource) UnoRuntime.queryInterface(
		         XDataSource.class, ds1 );
		XLabeledDataSequence[] xLabeledDataSequence = xDataSource.getDataSequences();
		System.out.println("xLabeledDataSequence.length:" + xLabeledDataSequence.length);
		XLabeledDataSequence xLabeledDataSequence_2 = xLabeledDataSequence[0];
	    XLabeledDataSequence xLabeledDataSequence_3 = (XLabeledDataSequence) UnoRuntime.queryInterface(
	         XLabeledDataSequence.class, xLabeledDataSequence_2 );
	    XDataSequence xDataSequence = xLabeledDataSequence_3.getLabel();
	      
	    XDataSequence xDataSequence_2 = xLabeledDataSequence_3.getValues();
	      
	    XPropertySet xPropertySet = (XPropertySet) UnoRuntime.queryInterface(
	         XPropertySet.class, xDataSequence_2 );
	    String sRole = AnyConverter.toString(xPropertySet.getPropertyValue( "Role" ));
	      
	    String sSourceRangeRepresentation = xDataSequence_2.getSourceRangeRepresentation();
	    
	    for (int i = 0; i < xPropertySet.getPropertySetInfo().getProperties().length; i++) {
	    	System.out.println("xPropertySet.getPropertySetInfo().getProperties():" + xPropertySet.getPropertySetInfo().getProperties()[i].Name);
	    }
	    System.out.println("Role:" + sRole);
	    System.out.println("xDataSequence.getSourceRangeRepresentation():" + xDataSequence.getSourceRangeRepresentation());
	    System.out.println("sSourceRangeRepresentation:" + sSourceRangeRepresentation);
	    System.out.println("xDataSequence.getSourceRangeRepresentation():" + xDataSequence.getSourceRangeRepresentation());
	    
	    
	    String s1 = sSourceRangeRepresentation.replace("2018", "2017");
	    String s2 = xDataSequence.getSourceRangeRepresentation().replace("2018", "2017");
	    XDataProvider dp = chartDoc.getDataProvider();
	    XDataSequence dataSeq =  
	              dp.createDataSequenceByRangeRepresentation(s1);
	    System.out.println("dataSeq.getSourceRangeRepresentation()" + dataSeq.getSourceRangeRepresentation());
	    XDataSequence labelSeq =  
                dp.createDataSequenceByRangeRepresentation(s2);
	    System.out.println("labelSeq.getSourceRangeRepresentation()" + labelSeq.getSourceRangeRepresentation());
	    XPropertySet dsProps = Lo.qi(XPropertySet.class, dataSeq);
	    Props.setProperty(dsProps, "Role", "values-y");  //specify data role (type)
	    
	    Chart2.showDataSourceArgs(chartDoc, xDataSource);
	    //xLabeledDataSequence_2.setValues(xDataSequence_2);
	    xLabeledDataSequence_2.setValues(dataSeq);
	    xLabeledDataSequence_2.setLabel(labelSeq);
	   
	    //categories
	    XAxis axis = Chart2.getAxis(chartDoc, Chart2.X_AXIS, 0);
	    ScaleData sd = axis.getScaleData();
	    XLabeledDataSequence cat = sd.Categories;
	    XDataSequence catVal = cat.getValues();
	    //XDataSequence catLabel = cat.getLabel();
	    String dataRange = catVal.getSourceRangeRepresentation().replace("2018", "2017");
	    //String labelDataRange = catLabel.getSourceRangeRepresentation().replace("2018", "2017");
	    XDataSequence catDataSeq =  
	              dp.createDataSequenceByRangeRepresentation(dataRange);
	    System.out.println("catDataSeq.getSourceRangeRepresentation()" + catDataSeq.getSourceRangeRepresentation());
	    //XDataSequence catLabelSeq =  
        //      dp.createDataSequenceByRangeRepresentation(labelDataRange);
	    XPropertySet catDsProps = Lo.qi(XPropertySet.class, catDataSeq);
	    Props.setProperty(catDsProps, "Role", "categories");  //specify data role (type)
	    //cat.setLabel(catLabelSeq);
	    cat.setValues(catDataSeq);
	    
	    Lo.save(chartsDocument);
		Lo.closeDoc(chartsDocument);
		Lo.closeOffice();
	}
	@Override
	protected String getSourceFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<CftcInstrument> getProductList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getSourceColumnLength() {
		// TODO Auto-generated method stub
		return 0;
	}

}
