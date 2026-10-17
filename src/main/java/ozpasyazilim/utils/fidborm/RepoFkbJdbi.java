package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.returntypes.Fdr;

/**
 * Jdbi ile sorgu çalıştıracak Genel Repo
 * <p>
 * Her db ve tablo ile çalışır
 */
public class RepoFkbJdbi extends AbsRepoFkbJdbi {

    public RepoFkbJdbi(Jdbi jdbi) {
        super(jdbi);
    }

    /**
     *
     * @param txTableName
     * @return Fdr-FkbList
     */
    public Fdr selAll(String txTableName) {
        String sql = FiQugenCls.selectAllSimple(txTableName);
        FiQuery fiQuery = new FiQuery(sql);
        return jdSelectAsFkbList(fiQuery);
    }
}
