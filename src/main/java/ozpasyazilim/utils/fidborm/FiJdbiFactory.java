package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlStatements;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import ozpasyazilim.utils.log.Loghelper;

public class FiJdbiFactory {

	public static Jdbi createJdbi(String server, String dbName, String user, String pass) {

		if (!DbConfig.checkDriverClassMicrosoftJdbc()) {
			String message = String.format("Sql Sürücü Kütüphanesi bulunamadı.");
			Loghelper.get(FiJdbiFactory.class).error(message);
			return null;
		}

		String url = DbConfig.getUrlMicrosoftJdbcSqlServer(server, dbName);

		Jdbi jdbi = Jdbi.create(url, user, pass);
		jdbi.installPlugin(new SqlObjectPlugin());
		jdbi.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);

		if (jdbi == null) {
			String uyari = String.format("Server: %s Db: %s için bağlantı bilgilerinde hata var.", (server == null ? "" : server), (dbName == null ? "" : dbName));
			Loghelper.get(FiJdbiFactory.class).error(uyari);
			//System.out.println(uyari);
		}

		return jdbi;
	}

}
