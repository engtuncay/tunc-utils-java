package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.FiException;
import ozpasyazilim.utils.log.Loghelper;

public class DbConfig {


	public static Boolean checkDriverClassJdbcMicrosoft(){

		String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

		try {
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			Loghelper.get(DbConfig.class).info("Hata :" + FiException.exceptionToStrMain(e));
			System.out.println("ERROR: Sürücü Yüklenemedi");
			return false;
		}

		return true;

	}

	public static Boolean checkDriverClassJtdsSqlServerDriver(){

		String DRIVER_CLASS = "net.sourceforge.jtds.jdbc.Driver";

		try {
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			Loghelper.get(DbConfig.class).error("Hata :" + FiException.exceptionToStrMain(e));
			System.out.println("ERROR: Sürücü Yüklenemedi");
			return false;
		}

		return true;

	}

	public static String getUrlJdbcSqlServer(String server, String dbName) {
		//return "jdbc:sqlserver://" + server + ";databaseName=" + dbName;
		return String.format("jdbc:sqlserver://%s;databaseName=%s;autoCommit=false",server,dbName); // ;encrypt=true;trustServerCertificate=true
	}

	public static String getUrlJdbcSqlServer(String server, String dbName,String user,String pass) {
		//return "jdbc:sqlserver://" + server + ";databaseName=" + dbName;
		return String.format("jdbc:sqlserver://%s;databaseName=%s;autoCommit=false;encrypt=true;trustServerCertificate=true;user=%s;password=%s",server,dbName,user,pass); //
	}

	public static String getUrlJtdsSqlServer(String server, String dbName) {
		return String.format("jdbc:jtds:sqlserver://%s/%s;autoCommit=false;",server,dbName);
	}

}
