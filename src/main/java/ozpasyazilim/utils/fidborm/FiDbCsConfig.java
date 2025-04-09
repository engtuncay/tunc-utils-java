package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.FiException;
import ozpasyazilim.utils.log.Loghelper;

public class FiDbCsConfig {

	public static Boolean checkDriverClassMicrosoftJdbc(){

		String driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: Sürücü Yüklenemedi");
			Loghelper.get(FiDbCsConfig.class).error("Hata :" + FiException.exTosMain(e));
			return false;
		}

		return true;
	}

	public static Boolean checkDriverClassJtdsSqlServer(){

		String driverClass = "net.sourceforge.jtds.jdbc.Driver";

		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			Loghelper.get(FiDbCsConfig.class).error("Hata :" + FiException.exTosMain(e));
			System.out.println("ERROR: Sürücü Yüklenemedi");
			return false;
		}

		return true;

	}

	public static String getUrlMicrosoftJdbcSqlServer(String server, String dbName) {
		return String.format("jdbc:sqlserver://%s;databaseName=%s;autoCommit=false",server,dbName);
		// tls hatası engellemek için denendi
		// ;encrypt=true;trustServerCertificate=true
	}

	public static String getUrlMicrosoftJdbcSqlServerWoutDb(String server) {
		return String.format("jdbc:sqlserver://%s;autoCommit=false",server);
		// tls hatası engellemek için denendi
		// ;encrypt=true;trustServerCertificate=true
	}

	public static String getUrlMicrosoftJdbcSqlServerWithUserAndPass(String server, String dbName, String user, String pass) {
		return String.format("jdbc:sqlserver://%s;databaseName=%s;autoCommit=false;encrypt=true;trustServerCertificate=true;user=%s;password=%s",server,dbName,user,pass); //
	}

	public static String getUrlJtdsSqlServer(String server, String dbName) {
		return String.format("jdbc:jtds:sqlserver://%s/%s;autoCommit=false;",server,dbName);
	}

}
