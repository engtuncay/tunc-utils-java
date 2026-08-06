package ozpasyazilim.utils.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.core.FiException;

import java.io.File;

public class Loghelper {

  //private static final Logger logGlobal = Logger.getLogger("GLOBAL");

  public static Logger get(Class<?> clazz) {
    return LoggerFactory.getLogger(clazz);
  }

  public static void installLogger(Boolean boDebugModeEnabled) {

    System.out.println("Logger(v1) yüklenecek. Loghelper.installLogger(boDebugMode):" + boDebugModeEnabled);

    if (FiBool.isTrue(boDebugModeEnabled)) {

      // NOTE: switched to SLF4J API. Do not depend on log4j 1.x PropertyConfigurator here.
      // PropertyConfigurator.configure("log4jd.properties");

      //Loghelper.get(Loghelper.class).info("Logger Installation (Debug Mode True)");
      System.out.println("Logger Installation (Debug Mode True)");

      // Logback yapılandırmasını root klasörden yükle
      try {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        // root klasördeki logback.xml
        configurator.doConfigure(new File("logback.xml"));
      } catch (Exception e) {
        System.err.println("Logback yapılandırması yüklenemedi: " + e.getMessage());
      }


    } else {
      //PropertyConfigurator.configure("log4jp.properties");
      System.out.println("Logger Installation (Debug Mode False)");

    }

  }

  public static void main(String[] args) {
    // For testing
    Loghelper.installLogger(true);
    //loggerr.info("Logging an INFO-level message");
  }

  public static void debugException(Class<?> logClass, Exception e) {
    Loghelper.get(logClass).debug("Hata (Exception): " + FiException.exTosMain(e));
  }

  public static void errorException(Class<?> logClass, Exception e) {
    Loghelper.get(logClass).error("Hata (Exception): " + FiException.exTosMain(e));
  }

  public static void errorLog(Class<?> logClass, String message) {
    Loghelper.get(logClass).error(message);
  }

  public static void debugLog(Class<?> clazz, String message) {
    Loghelper.get(clazz).debug(message);
  }

  public static void debug(Class<?> clazz, String message) {
    Loghelper.get(clazz).debug(message);
  }

  public static void logexceptionOnlyMail(String message) {
    Loghelper.get(Loghelper.class).debug(message);
  }

  public static void logexceptionMessageMail(String message) {
    Loghelper.get(Loghelper.class).debug(message);
  }

}