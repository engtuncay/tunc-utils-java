package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlStatements;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import ozpasyazilim.utils.configmisc.ServerConfig;
import ozpasyazilim.utils.log.Loghelper;

public class FiJdbiFactory {

    public static Jdbi createJdbi(ServerConfig serverConfig) {
        return createJdbi(serverConfig.getServer(), serverConfig.getServerDb(), serverConfig.getServerUser(), serverConfig.getServerKey());
    }

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

    public static Jdbi createJdbiToMasterDb(String server, String user, String pass) {

        if (!DbConfig.checkDriverClassMicrosoftJdbc()) {
            String message = String.format("Sql Sürücü Kütüphanesi bulunamadı.");
            Loghelper.get(FiJdbiFactory.class).error(message);
            return null;
        }

        String url = DbConfig.getUrlMicrosoftJdbcSqlServerWoutDb(server);

        Jdbi jdbi = Jdbi.create(url, user, pass);
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);
        // Configuration implements JdbiConfig --> configuration class'ı olabilir
        if (jdbi == null) {
            String uyari = String.format("Server: %s için bağlantı bilgilerinde hata var.", (server == null ? "" : server));
            Loghelper.get(FiJdbiFactory.class).error(uyari);
            //System.out.println(uyari);
        }

        return jdbi;
    }

}
