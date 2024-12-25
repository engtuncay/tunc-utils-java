package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.datatypes.FkbList;
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

    public Fdr<FkbList> selAll(String txTableName) {
        String sql = FiQugen.selectAllSimple(txTableName);
        return jdSelectListFkb1BindMapMain(sql, null);
    }
}
