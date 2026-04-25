package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.datatypes.FiKeybean;
import ozpasyazilim.utils.datatypes.FkbList;
import ozpasyazilim.utils.returntypes.Fdr;

/**
 * Jdbi ile sorgu çalıştıracak Genel Repo
 * <p>
 * Her db ve tablo ile çalışır
 */
public class RepoDdFkbJdbi extends AbsRepoFkbJdbi {

    public RepoDdFkbJdbi(Jdbi jdbi) {
        super(jdbi);
    }

    public Fdr<FkbList> selAll(String txTableName) {
        String sql = FiQugen.selectAllSimple(txTableName);
        return jdSelectListFkb1BindMapMain(sql, null);
    }

    public Fdr<FiKeybean> checkTable(FiKeybean fkbParams) {

        String sql = "SELECT CASE WHEN EXISTS (\n" +
            "  SELECT 1 FROM sys.tables WHERE name =  @ddTxTableName AND schema_id = SCHEMA_ID('dbo')\n" +
            "  ) THEN 1\n" +
            "  ELSE 0\n" +
            "  END AS ddLnCount";

        return jdSelectFkbSingleBindMapMain(sql, fkbParams);
    }


}
