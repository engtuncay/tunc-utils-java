/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ozpasyazilim.utils.jdbi;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.jdbi.v3.core.mapper.CaseStrategy;
import org.jdbi.v3.core.mapper.MapMappers;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ozpasyazilim.utils.datatypes.FiKeyBean;

/**
 * MapMapper , FiKeyBean'e uyarlandı.
 * <p>
 * Yo dawg, I heard you like maps, so I made you a mapper that maps rows into {@code Map<String,Object>}. Map
 * keys are column names, while map values are the values in those columns. Map keys are converted to lowercase by
 * default !!!.
 * <p>
 * //@see GenericMapMapperFactory
 */
public class FiKeyBeanMapper implements RowMapper<FiKeyBean> {
	/**
	 * @deprecated remove
	 */
	@Deprecated
	private final Function<StatementContext, UnaryOperator<String>> caseStrategy;

	/**
	 * Constructs a new MapMapper and delegates case control to MapMappers.
	 */
	public FiKeyBeanMapper() {
		caseStrategy = ctx -> ctx.getConfig(MapMappers.class).getCaseChange();
	}

	/**
	 * Constructs a new MapMapper
	 *
	 * @param toLowerCase if true, column names are converted to lowercase in the mapped {@link Map}. If false, nothing is done. Use the other constructor to delegate case control to MapMappers instead.
	 */
	// TODO deprecate when MapMappers.caseChange is out of beta
	public FiKeyBeanMapper(boolean toLowerCase) {
		caseStrategy = toLowerCase ? ctx -> CaseStrategy.LOCALE_LOWER : ctx -> CaseStrategy.NOP;
	}

	@Override
	public FiKeyBean map(ResultSet rs, StatementContext ctx) throws SQLException {
		return specialize(rs, ctx).map(rs, ctx);
	}

	@Override
	public RowMapper<FiKeyBean> specialize(ResultSet rs, StatementContext ctx) throws SQLException {
		final List<String> columnNames = getColumnNames(rs, caseStrategy.apply(ctx));

		return (r, c) -> {
			FiKeyBean row = new FiKeyBean(columnNames.size());

			for (int i = 0; i < columnNames.size(); i++) {
				row.put(columnNames.get(i), rs.getObject(i + 1));
			}

			return row;
		};
	}

	private static List<String> getColumnNames(ResultSet rs, UnaryOperator<String> caseChange) throws SQLException {
		// important: ordered and unique
		Set<String> columnNames = new LinkedHashSet<>();
		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();

		for (int i = 0; i < columnCount; i++) {
			String columnName = meta.getColumnName(i + 1);
			String alias = meta.getColumnLabel(i + 1);

			String name = caseChange.apply(alias == null ? columnName : alias);

			boolean added = columnNames.add(name);
			if (!added) {
				throw new RuntimeException("column " + name + " appeared twice in this resultset!");
			}
		}

		return new ArrayList<>(columnNames);
	}
}