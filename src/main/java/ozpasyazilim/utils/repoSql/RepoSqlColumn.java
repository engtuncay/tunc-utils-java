package ozpasyazilim.utils.repoSql;

import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.entitysql.EntSqlColumn;
import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.fidborm.AbsRepoJdbi;
import ozpasyazilim.utils.returntypes.Fdr;

import java.util.List;

public class RepoSqlColumn extends AbsRepoJdbi<EntSqlColumn> {

	public RepoSqlColumn() {
	}

	public RepoSqlColumn(Jdbi jdbi) {
		super(jdbi);
	}

	public Fdr<List<EntSqlColumn>> selectColumnsAll(String txTableName) {

		FiKeyBean fiKeyBean = FiKeyBean.bui().buiPut("txTableName", txTableName);

		String sql = "--sq202203031657\n" +
				"SELECT col.COLUMN_NAME,col.DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS col\n" +
				"WHERE TABLE_NAME = @txTableName";

		return jdSelectListBindMapMain(sql, fiKeyBean);
	}
}
