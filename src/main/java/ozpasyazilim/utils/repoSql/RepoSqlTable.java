package ozpasyazilim.utils.repoSql;

import ozpasyazilim.utils.entitysql.EntSqlTable;
import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.fidborm.AbsRepoGenJdbi;
import ozpasyazilim.utils.returntypes.Fdr;

import java.util.List;

public class RepoSqlTable extends AbsRepoGenJdbi<EntSqlTable> {

	public RepoSqlTable() {
	}

	public RepoSqlTable(Jdbi jdbi) {
		super(jdbi);
	}

	public Fdr<List<EntSqlTable>> selectTables() {
		//sq202202260955
		String sql = "--sq202202260955\n" +
				"SELECT *\n" +
				"FROM INFORMATION_SCHEMA.TABLES\n" +
				"WHERE TABLE_TYPE='BASE TABLE'\n";
		return jdSelectListBindMapMain(sql, null);
	}

	public Fdr<List<EntSqlTable>> selectTablesWithCount() {
		//sq202203040959
		String sql = "--sq202203040959\n" +
				"SELECT T.name AS TABLE_NAME, I.rows AS lnCount\n" +
				"FROM sys.tables AS T \n" +
				"INNER JOIN sys.sysindexes AS I ON T.object_id = I.id AND I.indid < 2 \n" +
				"ORDER  BY I.rows DESC";

		return jdSelectListBindMapMain(sql, null);
	}
}
