package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlStatements;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import ozpasyazilim.utils.log.Loghelper;

public class FiJdbiFactory {

	public static Jdbi createJdbi(String server, String dbName, String user, String pass) {

		if (!DbConfig.checkDriverClassJtdsSqlServerDriver()) return null;

		String url = DbConfig.getUrlJtdsSqlServer(server, dbName);

		Jdbi jdbi = Jdbi.create(url, user, pass);
		jdbi.installPlugin(new SqlObjectPlugin());
		jdbi.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);

		if(jdbi==null){
			String uyari= "Server:" + (server==null?"":server) + " Db: "+ (dbName==null?"":dbName) + " için bağlantı bilgilerinde hata var.";
			Loghelper.get(FiJdbiFactory.class).debug(uyari);
			//System.out.println(uyari);
		}

		return jdbi;
	}

//	public Jdbi createJdbi(String dbName) {
//
//		if (new DbConfig().checkDriverClassJdbcMicrosoft()) return null;
//
//		String url = new DbConfig().getUrlJdbcSqlServer(server, dbName);
//
//		Jdbi jdbi = Jdbi.create(url, user, pass);
//		jdbi.installPlugin(new SqlObjectPlugin());
//
//		return jdbi;
//	}

}
