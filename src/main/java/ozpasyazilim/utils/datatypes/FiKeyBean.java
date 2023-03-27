package ozpasyazilim.utils.datatypes;

import javafx.beans.property.StringProperty;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.fidborm.FiEntity;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.fidborm.FiQuery;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.table.FiCol;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Map [String,Object] türünde HashMap tipidir.
 * <p>
 * Kullanımları :
 * <p>
 * Sql Sorgularında named parametrelere bind etmek için kullanılır.
 * <p>
 * Bir nevi key-object tipinde array dir.
 */
public class FiKeyBean extends LinkedHashMap<String, Object> {

	public FiKeyBean() {
	}

	public FiKeyBean(Map<? extends String, ?> m) {
		super(m);
	}

	public FiKeyBean(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public FiKeyBean(int initialCapacity) {
		super(initialCapacity);
	}

	public FiKeyBean(int initialCapacity, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
	}

	public static FiKeyBean build() {
		return new FiKeyBean();
	}

	// üst sınıftan alamaz, üst sınıf FiKeyBean dönüyor
	public FiKeyBean buildPut(Object fieldName, Object value) {
		if (fieldName == null) return this;

		this.put(fieldName.toString(), value);
		return this;
	}

	public FiKeyBean buildPutIfNotNull(Object fieldName, Object value) {
		if (fieldName == null) return this;
		if (value == null) return this;

		this.put(fieldName.toString(), value);
		return this;
	}

	public FiKeyBean bind(Object key, Object value) {
		if (key == null) return this;
		put(key.toString(), value);
		return this;
	}

	public FiKeyBean putIfNotEmpty(Object fieldName, Object value, Boolean addPercentage) {
		if (fieldName == null) return this;
		if (FiType.isEmptyGen(value)) return this;

		if (FiBoolean.isTrue(addPercentage)) {
			if (value instanceof String) {
				this.put(fieldName.toString(), "%" + value + "%");
			} else {
				this.put(fieldName.toString(), value);
			}
		} else {
			this.put(fieldName.toString(), value);
		}
		return this;
	}

	public FiKeyBean putObjTos(Object field, Object value) {
		this.put(field.toString(), value);
		return this;
	}


	/**
	 * String boş olmamalı
	 * <p>
	 * Collection size 0 olmamalı
	 * <p>
	 * Diger objeler null olmamalı
	 * <p>
	 * FiQuery kullan
	 *
	 * @param objKey
	 * @param value
	 * @param sql
	 * @return
	 */
	@Deprecated
	public FiKeyBean bindAndActivateIfNotEmpty(Object objKey, Object value, StringProperty sql) {
		bindAndActivateIfNotEmpty(objKey, value, sql, null);
		return this;
	}

	/**
	 * Use StringProperty
	 *
	 * @param objKey
	 * @param value
	 * @param sql
	 */
	@Deprecated
	public void bindAndActivateIfNotEmpty(Object objKey, Object value, AtomicReference<String> sql) {
		if (value != null) {
			this.put(objKey.toString(), value);
			sql.set(FiQuery.activateOptParamMain(sql.get(), objKey.toString()));
		}
	}

	/**
	 * String boş olmamalı
	 * <p>
	 * Collection size 0 olmamalı
	 * <p>
	 * Diger objeler null olmamalı
	 * <p>
	 * Use FiQuery
	 *
	 * @param objKey
	 * @param value
	 * @param sql
	 * @param addPercentage
	 */
	@Deprecated
	public FiKeyBean bindAndActivateIfNotEmpty(Object objKey, Object value, StringProperty sql, Boolean addPercentage) {
		if (value != null) {
			if (value instanceof String) {
				if (FiString.isEmpty((String) value)) {
					Loghelper.get(getClass()).debug("Aktive edilmedi Param:" + objKey.toString());
					return this;
				}
				if (FiBoolean.isTrue(addPercentage)) value = "%" + value.toString() + "%";
			}

			if (value instanceof Collection) {
				if (FiCollection.isEmpty((Collection) value)) return this;
			}

//			Loghelper.get(getClass()).debug("Aktive edildi Param:" + objKey.toString());
			this.put(objKey.toString(), value);
			FiQuery.fsmActivateOptParamForProp(sql, objKey.toString());
		} else {
//			Loghelper.get(getClass()).debug("Aktive edilmedi Param:" + objKey.toString());
		}
		return this;
	}

	/**
	 * Included Not Null Fields
	 * <p>
	 * Included Transient Fields
	 *
	 * @param entity
	 * @param clazz
	 * @return
	 */
	public FiKeyBean genFiMapParamsDb(Object entity, Class clazz) {

		FiKeyBean fiKeyBean = new FiKeyBean();
		Field[] fields = clazz.getDeclaredFields(); // returns all members including private members but not inherited members.

		for (Field field : fields) {

//			if (field.isAnnotationPresent(Transient.class)) continue;
//			if (field.isAnnotationPresent(FiTransient.class)) continue;
			// Static alanlar alınmaz
			if (Modifier.isStatic(field.getModifiers())) continue;

			Object fieldValue = FiReflection.getProperty(entity, field.getName());

			if (fieldValue != null) {
				FiField fiField = FiEntity.setupFiFieldBasic(field, null);

				if (FiBoolean.isTrue(fiField.getBoFilterLike())) {
					String txValue = (String) fieldValue;
					txValue = "%" + txValue + "%";
					fiKeyBean.add(fiField.getDbFieldName(), txValue);
				} else {
					fiKeyBean.add(fiField.getDbFieldName(), fieldValue);
				}


			}

		}

		return fiKeyBean;
	}

	public Integer getFullParamCount() {
		Collection<Object> values = this.values();
		Integer lnCount = 0;
		for (Object value : values) {
			if (value != null) lnCount++;
		}
		return lnCount;
	}

	public void add(Object field, Object value) {
		this.put(field.toString(), value);
	}

	public Object getAsObj(Object txKey) {
		if (txKey == null) return null;
		return get(txKey.toString());
	}

	public String getAsString(FiCol fiCol) {
		if (fiCol == null || FiString.isEmpty(fiCol.getFieldName())) return null;

		if (containsKey(fiCol.getFieldName())) {
			return (String) get(fiCol.getFieldName());
		}

		return null;
	}

	public Date getAsDate(FiCol fiCol) {
		if (fiCol == null || FiString.isEmpty(fiCol.getFieldName())) return null;

		if (containsKey(fiCol.getFieldName())) {
			return (Date) get(fiCol.getFieldName());
		}
		return null;
	}

	public Boolean getAsBoolean(FiCol fiCol) {
		if (fiCol == null || FiString.isEmpty(fiCol.getFieldName())) return null;

		if (containsKey(fiCol.getFieldName())) {
			if (get(fiCol.getFieldName()) instanceof Boolean) {
				return (Boolean) get(fiCol.getFieldName());
			}
		}

		return null;
	}


	public void copyParamByFiColOrEmptyStr(FiCol fiColFrom, FiCol fiColTo) {
		putObjTos(fiColTo, getOrDefault(fiColFrom.toString(), ""));
	}

}