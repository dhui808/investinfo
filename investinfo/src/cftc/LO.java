package cftc;

import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

import jloputility.Lo;

/**
 * 
 * LibreOffice functionality
 *
 */
public abstract class LO {

	protected static com.sun.star.uno.XComponentContext mxRemoteContext;
	protected static com.sun.star.lang.XMultiComponentFactory mxRemoteServiceManager;
	
	public static XComponentLoader aLoader;

	public static void connect() {
//		if (mxRemoteContext == null && mxRemoteServiceManager == null) {
//			try {
//				// First step: get the remote office component context
//				mxRemoteContext = com.sun.star.comp.helper.Bootstrap.bootstrap();
//				System.out.println("Connected to a running office ...");
//	
//				mxRemoteServiceManager = mxRemoteContext.getServiceManager();
//	
//				aLoader = UnoRuntime.queryInterface(XComponentLoader.class, mxRemoteServiceManager
//						.createInstanceWithContext("com.sun.star.frame.Desktop", mxRemoteContext));
//			} catch (Exception e) {
//				System.err.println("ERROR: can't get a component context from a running office ...");
//				e.printStackTrace();
//				System.exit(1);
//			}
//		}
		aLoader = Lo.loadOffice();
	}
	
	public static void disconnect() {

        // call office to terminate itself
//        try {
//
//            // get remote context
//            XComponentContext xRemoteContext = mxRemoteContext;
//
//            // get desktop to terminate office
//            Object desktop = xRemoteContext.getServiceManager().createInstanceWithContext("com.sun.star.frame.Desktop",xRemoteContext);
//            XDesktop xDesktop = (XDesktop) UnoRuntime.queryInterface(XDesktop.class, desktop);
//            xDesktop.terminate();
//        }
//        catch (Exception e) {
//            System.out.println("failed to terminate libreoffice.");
//        }
		Lo.closeOffice();
    }
}
