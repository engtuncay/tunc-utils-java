package ozpasyazilim.utils.log;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.core.FiException;

import java.util.Set;

import static org.apache.log4j.Logger.getLogger;

public class Loghelper {
	private static final Logger logGlobal = Logger.getLogger("GLOBAL");

	private static Set<String> setSingle;
	private Class clazz;

	// factory method
	public static Logger get(Class clazz) {
		return LogManager.getLogger(clazz);
	}

	public static void installLogger(Boolean debugmode) {
		System.out.println("Logger Util yüklenecek. Logger.installLogger()");

		if (FiBoolean.isTrue(debugmode)) { // || Loghelper.DEBUG_MODE
			PropertyConfigurator.configure("log4jd.properties");
			//System.out.println("Logger installing...(debug mode) (Log4jd)");
			Loghelper.get(Loghelper.class).info("Log4jd (Debug) Mode");
		} else {
			//System.out.println("Logger installing...(production mode) (Log4jp)");
			PropertyConfigurator.configure("log4jp.properties");
			Loghelper.get(Loghelper.class).info("Log4jd (Production) Mode");
		}

	}

	// ???
	public Loghelper(Class clazz) {
		setClazz(clazz);
	}

	public static void main(String[] args) {
		Loghelper.installLogger(true);
		//loggerr.info("Logging an INFO-level message");
	}

	public static Logger get() {
		return logGlobal;
	}

	public static void setupDebugMode(Boolean debugmode) {
		installLogger(true);
	}

	public static void info(String message, String classname) {
		logGlobal.log(classname, Level.INFO, message, null);
	}

	public static void info(Object classobj, String message) {
		logGlobal.log(classobj.getClass().getName(), Level.INFO, message + " at " + classobj.getClass().getSimpleName(),null);
	}

	// mail ayar propertysi eklenip yapılabilir
	public static void logexceptionOnlyMail(String message) {
		// Loghelper.getInstance(Loghelper.class).info(message);
		//
		// Thread t = new Thread() {
		// public void run() {
		//
		// String hostname = "Unknown";
		//
		// try {
		//      InetAddress addr;
		//      addr = InetAddress.getLocalHost();
		//      hostname = addr.getHostName();
		// } catch (UnknownHostException ex) {
		//      Loghelper.getInstance(this.getClass()).error("Hostname can not be resolved");
		// }
		//
		//  //XIM log hatalarını tntpcx mail göndermez
		//  if (hostname.equals("tntpcx")) return;
		//
		//  ModelMail mail = new ModelMail();
		//  mail.sendmailfrombug(message, ModelMailaddressOzpasentegre.adminmail, "HATA FROM:" + hostname);
		//  }
		//};
		//
		// t.start();
	}

	public static Logger get(String loggerAlias) {
		return Logger.getLogger(loggerAlias);
	}

	public static void debugException(Class logClass, Exception e) {
		Loghelper.get(logClass).debug("Hata (Exception): " + FiException.exceptionToStrMain(e));
	}

	public static void errorException(Class logClass, Exception e) {
		Loghelper.get(logClass).error("Hata (Exception): " + FiException.exceptionToStrMain(e));
	}

	public static void errorException(Class logClass, Exception e, String message) {
		Loghelper.get(logClass).error(message);
		Loghelper.get(logClass).error("Hata (Exception): " + FiException.exceptionToStrMain(e));
	}

	public static void debugLogSta(Class logClass, String message) {
		Loghelper.get(logClass).debug(message);
	}

	public static Logger getInstance(Class logClass) {
		return LogManager.getLogger(logClass);
	}

	public void debugLog(String message) {
		LogManager.getLogger(getClazz()).debug(message);
	}

	public static void errorLog(Class logClass, String message) {
		Loghelper.get(logClass).error(message);
	}

	public static void info(Class clazz, String message) {
		Loghelper.get(clazz).info(message);
	}

	public static void warn(Class clazz, String message) {
		Loghelper.get(clazz).warn(message);
	}

	public static void debugLog(Class clazz, String message) {
		Loghelper.get(clazz).debug(message);
	}

	public static void error(Class clazz, String message) {
		Loghelper.get(clazz).error(message);
	}

	public static void debugStatic(String message) {
		logGlobal.debug(message);
	}

	public static void debugStatic(String message, Exception ex) {
		logGlobal.debug("Statik Log: Hata: " + message + "\n\n" + FiException.exceptionToStrMain(ex));
	}

	// ??????
	public static Loghelper getLogHelper(Class clazz) {
		return new Loghelper(clazz);
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public Class getClazz() {
		return clazz;
	}

	public Logger getLogger() {
		return Logger.getLogger(getClazz());
	}

}

// static private FileHandler fileTxt;
// static private SimpleFormatter formatterTxt;
// static private FileHandler fileHTML;
// static private Formatter formatterHTML;

//	public static void logSingle(String key, Class logClass, String message) {
//		if (getSetSingle().contains(key)) {
//			return;
//		} else {
//			getSetSingle().add(key);
//			debugLog(logClass, message);
//		}
//	}

//	public static Set<String> getSetSingle() {
//		if (setSingle == null) {
//			setSingle = new HashSet<>();
//		}
//		return setSingle;
//	}
