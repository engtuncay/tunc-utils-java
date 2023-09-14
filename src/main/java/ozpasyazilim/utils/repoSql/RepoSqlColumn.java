package ozpasyazilim.utils.repoSql;

import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.entitysql.EntSqlColumn;
import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.fidborm.AbsRepoGenJdbi;
import ozpasyazilim.utils.returntypes.Fdr;

import java.util.List;

public class RepoSqlColumn extends AbsRepoGenJdbi<EntSqlColumn> {

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

		return jdSelectListBindMapMainNtn(sql, fiKeyBean);
	}

	public Fdr<List<EntSqlColumn>> selectColumnsAllDetailed(String txTableName) {

		FiKeyBean fiKeyBean = FiKeyBean.bui().buiPut("txTableName", txTableName);

		String sql = "--sq202307081736\n" +
				"--$ver 1\n" +
				"SELECT c.TABLE_NAME, c.COLUMN_NAME,c.DATA_TYPE\n" +
				"--, c.Column_default\n" +
				"--,c.character_maximum_length, c.numeric_precision, c.is_nullable\n" +
				",CASE WHEN pk.COLUMN_NAME IS NOT NULL THEN 'PK' ELSE '' END AS TX_KEY_TYPE\n" +
				"FROM INFORMATION_SCHEMA.COLUMNS c\n" +
				"LEFT JOIN (\n" +
				"  SELECT ku.TABLE_CATALOG,ku.TABLE_SCHEMA,ku.TABLE_NAME,ku.COLUMN_NAME\n" +
				"  FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS tc\n" +
				"  INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS ku\n" +
				"      ON tc.CONSTRAINT_TYPE = 'PRIMARY KEY' \n" +
				"      AND tc.CONSTRAINT_NAME = ku.CONSTRAINT_NAME\n" +
				"         )   pk \n" +
				"ON  c.TABLE_CATALOG = pk.TABLE_CATALOG\n" +
				"  AND c.TABLE_SCHEMA = pk.TABLE_SCHEMA\n" +
				"  AND c.TABLE_NAME = pk.TABLE_NAME\n" +
				"  AND c.COLUMN_NAME = pk.COLUMN_NAME\n" +
				"WHERE c.TABLE_NAME = @txTableName\n" +
				"ORDER BY c.TABLE_SCHEMA,c.TABLE_NAME, c.ORDINAL_POSITION ";

		return jdSelectListBindMapMainNtn(sql, fiKeyBean);
	}
}
