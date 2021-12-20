package ozpasyazilim.utils.fidborm;

import org.apache.commons.beanutils.PropertyUtils;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ozpasyazilim.utils.core.FiReflection;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class FiBeanMapper<T> implements RowMapper<T> {

	private final Class<T> type;

	public FiBeanMapper(Class<T> type) {
		this.type = type;
	}

	@Override
	public T map(ResultSet rs, StatementContext ctx) throws SQLException {

		List<String> columnNames = FiReflectionMapperUtil.getColumnNamesOrginal(rs);

		T ent = construct();

		Map<String, FiField> mapFields = FiReflection.getFieldsAsMap(type);

		columnNames.forEach(columnName -> {
			try {
				Object obj = rs.getObject(columnName);
				Number number = null;

				if (obj instanceof Short) {
					FiField fiField = mapFields.getOrDefault(columnName, null);
					if (fiField != null && fiField.getClassNameSimple().equals("Integer")){
						obj = ((Short) obj).intValue();
					}
				}

				PropertyUtils.setProperty(ent, columnName, obj);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		return ent;
	}

	private T construct() {
		try {
			return type.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format("A bean, %s, was mapped "
					+ "which was not instantiable", type.getName()), e);
		}
	}
}