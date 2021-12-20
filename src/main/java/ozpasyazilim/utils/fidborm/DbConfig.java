package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.FiException;
import ozpasyazilim.utils.log.Loghelper;

public class DbConfig {


	public static Boolean checkDriverClassJdbcMicrosoft(){

		String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

		try {
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			Loghelper.get(DbConfig.class).info("Hata :" + FiException.exceptiontostring(e));
			System.out.println("ERROR: Sürücü Yüklenemedi");
			return false;
		}

		return true;

	}

	public static String getUrlJdbcSqlServer(String server, String dbName) {

		return "jdbc:sqlserver://" + server + ";databaseName=" + dbName;
	}

	public static String getUrlJtdsSqlServer(String server, String dbName) {
		return String.format("jdbc:jtds:sqlserver://%s/%s;autoCommit=false;",server,dbName);
	}

}
