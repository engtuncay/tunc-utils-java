package ozpasyazilim.utils.fidborm;

import org.apache.commons.beanutils.PropertyUtils;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ozpasyazilim.utils.core.FiReflection;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class FiBeanNestedRowMapper<T> implements RowMapper<T> {

	private final Class<T> type;
	List<String> listPrefixes;

	public FiBeanNestedRowMapper(Class<T> type) {
		this.type = type;
	}

	public FiBeanNestedRowMapper(Class<T> type, String... prefixes) {
		this.type = type;
		this.listPrefixes = Arrays.asList(prefixes);
	}

	@Override
	public T map(ResultSet rs, StatementContext ctx) throws SQLException {

		List<String> columnNames = FiReflectionMapperUtil.getColumnNamesOrginal(rs);

		T ent = construct();

		//Map<String, FiField> mapFields = FiProperty.mapFieldVsFiField(type);

		columnNames.forEach(columnName -> {
			try {
				Object obj = rs.getObject(columnName);

				// sütun ismi prefix ile başlıyor ise column isminin başına prefix i ekler.
				if (getListPrefixes() != null) {
					for (String prefix : getListPrefixes()) {
						// FIXME tek regex düşültülebilr
						if (columnName.contains("__")) continue;

						if (columnName.matches("^" + prefix + ".+")) {
							columnName = prefix + "__" + columnName;
						}
					}
				}

				//if (columnName.contains("__")) {
				columnName = columnName.replaceFirst("__", ".");
				//}

				if (obj instanceof BigDecimal) {
					//Class propClass = PropertyUtils.getPropertyType(ent, columnName);
					Class propClass = FiReflection.getFieldClassType(type,columnName);

					if (propClass.getSimpleName().equals("Double")) {
						// FIXME null sa eğer
						obj = ((BigDecimal) obj).doubleValue();
					}
				}

				// veritabanında short olan alan , integer tipine bağlanmışsa , tip değişikliği yapılır
				if (obj instanceof Short) {
					Class propClass = PropertyUtils.getPropertyType(ent, columnName);

					//FiField fiField = mapFields.getOrDefault(columnName, null);
//					if (fiField != null && fiField.getTypeSimpleName().equals("Integer")) {
//						obj = ((Short) obj).intValue();
//					}

					if (propClass.getSimpleName().equals("Integer")) {
						obj = ((Short) obj).intValue();
					}

				}

				PropertyUtils.setNestedProperty(ent, columnName, obj);

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

	public List<String> getListPrefixes() {
		return listPrefixes;
	}

	public void setListPrefixes(List<String> listPrefixes) {
		this.listPrefixes = listPrefixes;
	}
}