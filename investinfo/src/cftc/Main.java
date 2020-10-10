package cftc;

import java.util.ArrayList;
import java.util.List;

import cftc.vendor.VendorName;

public class Main {

	public static void main(String[] args) throws RuntimeException, Exception {
		
		List<String> argList = buildArgumentList(args);
		VendorMain vendorMain = new InvestingComMain();
		
		if (args.length == 1 && ("help".equalsIgnoreCase(args[0]) || "-help".equalsIgnoreCase(args[0]))) {
			
			printHelp();
			
			System.exit(0);
		}
		
		if (argList.contains("-v") || argList.contains("-vendor")) {
			
			int index = argList.indexOf("-v");
			
			if (index < 0) {
				index = argList.indexOf("-vendor");
			}
			
			String vendor = argList.get(index + 1);
			if (VendorName.INVESTING_COM.getName().equals(vendor)) {
			}
			//TODO add other vendors
		}
		
		vendorMain.parse(args, argList);
		
		System.exit(0);
	}

	private static List<String> buildArgumentList(String[] args) {
		
		List<String> argList = new ArrayList<String>();
		
		for (String s : args) {
			argList.add(s.toLowerCase());
		}
		
		return argList;
	}
	
	private static void printHelp() {
		
		System.out.println("Usage: ");
		System.out.println("run help");

		System.out.println("run -instrument gold adddoc");
		System.out.println("run -category forex adddoc");
		System.out.println("run adddoc");
		System.out.println("run -allyears -instrument ng adddoc");
		System.out.println("run -allyears -category energy adddoc");
		System.out.println("run -allyears adddoc");

		System.out.println("run -instrument gold -year 2017 rs");
		System.out.println("run -category energy -year 2017 rs");
		System.out.println("run -year 2017 rs");

		System.out.println("run -instrument gold -year 2017 as");
		System.out.println("run -category energy -year 2017 as");
		System.out.println("run -year 2017 as");

		System.out.println("run -history -update i");
		System.out.println("run -history -load i");
		System.out.println("run i");
		System.out.println("run -year 2018 i");
		System.out.println("run -year 2013 -adjust i");

		System.out.println("run -history -update p");
		System.out.println("run -history -load p");
		System.out.println("run p");
		System.out.println("run -year 2018 p");
		System.out.println("run -year 2013 -adjust p");

		System.out.println("run");

		System.out.println("run d 181218");

		System.out.println("run -year 2018 cftc");
		System.out.println("run cftc");
		
		System.out.println();
		System.out.println("Options:");
		System.out.println("-f, -forcedownload");
		System.out.println("-v investing_com");
		System.out.println("-vendor investing_com");
		System.out.println("-help");	
	}
}