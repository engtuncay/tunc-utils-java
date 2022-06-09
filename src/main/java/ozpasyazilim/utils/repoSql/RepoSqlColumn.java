package ozpasyazilim.utils.repoSql;

import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.entitysql.SqlColumn;
import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.fidborm.AbsRepoJdbi;
import ozpasyazilim.utils.returntypes.Fdr;

import java.util.List;

public class RepoSqlColumn extends AbsRepoJdbi<SqlColumn> {

	public RepoSqlColumn() {
	}

	public RepoSqlColumn(Jdbi jdbi) {
		super(jdbi);
	}

	public Fdr<List<SqlColumn>> selectColumnsAll(String txTableName) {

		FiKeyBean fiKeyBean = FiKeyBean.build().buildPut("txTableName", txTableName);

		String sql = "--sq202203031657\n" +
				"SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS \n" +
				"WHERE TABLE_NAME = @txTableName";

		return jdSelectListBindMapMain(sql, fiKeyBean);
	}
}
