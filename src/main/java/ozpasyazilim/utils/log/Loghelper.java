package ozpasyazilim.utils.log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.ErrorHandler;
import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.core.FiException;

import static org.apache.log4j.Logger.getLogger;

public class Loghelper {

	//private static final Logger logGlobal = Logger.getLogger("GLOBAL");

	public static Logger get(Class clazz) {
		return Logger.getLogger(clazz);
	} // //LogManager.getLogger(logClass);

	public static Logger getInstance(Class clazz) {
		return Logger.getLogger(clazz);
	}

	//public static Logger get(String loggerAlias) {
	//return Logger.getLogger(loggerAlias);
	//}

	public static void installLogger(Boolean boDebugModeEnabled) {
		System.out.println("Logger(v1) yüklenecek. Loghelper.installLogger(boDebugMode)");

		if (FiBoolean.isTrue(boDebugModeEnabled)) { // || Loghelper.DEBUG_MODE
			PropertyConfigurator.configure("log4jd.properties");
			//System.out.println("Logger installing...(debug mode) (Log4jd)");
			Loghelper.get(Loghelper.class).info("Log4jd (Debug) Mode");
		} else {
			//System.out.println("Logger installing...(production mode) (Log4jp)");
			PropertyConfigurator.configure("log4jp.properties");
			Loghelper.get(Loghelper.class).info("Log4jd (Production) Mode");
		}

	}

	public static void main(String[] args) {
		Loghelper.installLogger(true);
		//loggerr.info("Logging an INFO-level message");
	}

	public static void debugException(Class logClass, Exception e) {
		Loghelper.get(logClass).debug("Hata (Exception): " + FiException.exceptionToStrMain(e));
	}

	public static void errorException(Class logClass, Exception e) {
		Loghelper.get(logClass).error("Hata (Exception): " + FiException.exceptionToStrMain(e));
	}

	public static void errorLog(Class logClass, String message) {
		Loghelper.get(logClass).error(message);
	}

	public static void debugLog(Class clazz, String message) {
		Loghelper.get(clazz).debug(message);
	}

	public static void debug(Class clazz, String message) {
		Loghelper.get(clazz).debug(message);
	}
	public static void logexceptionOnlyMail(String message) {
		Loghelper.get(Loghelper.class).debug(message);
	}

	public static void logexceptionMessageMail(String message) {
		Loghelper.get(Loghelper.class).debug(message);
	}

}