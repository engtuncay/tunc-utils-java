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
 * String,Object türünde HashMap'dir.
 * <p>
 * Kullanımları
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

	public void putObj(Object field, Object value) {
		this.put(field.toString(), value);
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

	public Object getByObj(Object txKey) {
		if (txKey == null) return null;
		return get(txKey.toString());
	}

	public String  getAsString(FiCol fiCol) {
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

	public Boolean  getAsBoolean(FiCol fiCol) {
		if (fiCol == null || FiString.isEmpty(fiCol.getFieldName())) return null;

		if (containsKey(fiCol.getFieldName())) {
			if(get(fiCol.getFieldName()) instanceof Boolean){
				return (Boolean) get(fiCol.getFieldName());
			}
		}

		return null;
	}


}

//public class FiKeyBean extends HashMap<String, Object> {
//
//	public FiKeyBean buildPut(Object fieldName, Object value) {
//		if (fieldName == null) return this;
//		this.put(fieldName.toString(), value);
//		return this;
//	}
//	public FiKeyBean buildPutIfNotNull(Object fieldName, Object value) {
//		if (fieldName == null) return this;
//		if (value == null) return this;
//
//		this.put(fieldName.toString(), value);
//		return this;
//	}
//
//	public void puto(Object field, Object value) {
//		this.put(field.toString(), value);
//	}
//
//	public void add(Object field, Object value) {
//		this.put(field.toString(), value);
//	}
//
//	public void putIfNotNull(Object objKey, Object value) {
//		if (value != null) {
//			this.put(objKey.toString(), value);
//		}
//	}
//	public FiKeyBean bind(Object key, Object value) {
//		if (key == null) return this;
//		put(key.toString(), value);
//		return this;
//	}
//
//	public Object getAfterCheck(Object txKey) {
//		if(FiString.isEmptyToString(txKey)) return null;
//
//		if (this.containsKey(txKey.toString())) {
//			return get(txKey.toString());
//		}else{
//			return null;
//		}
//
//	}

//	public FiMapParams buildPutIfNotEmpty(Object fieldName, Object value) {
//		putIfNotEmpty(fieldName,value,false);
//		return this;
//	}

//	public FiMapParams buildPutIfNotEmpty(Object fieldName, Object value, Boolean addPercentage) {
//		putIfNotEmpty(fieldName, value, addPercentage);
//		return this;
//	}

//	Set<String> activateParamSet;
//	Set<String> deActivateParamSet;

//	public void add(Object field, Object value) {
//		this.put(field.toString(), value);
//	}

//	public void putIfNotNull(Object objKey, Object value) {
//		if (value != null) {
//			this.put(objKey.toString(), value);
//		}
//	}

//	public FiMapParams bindAndActivateListParamIfNotEmpty(Object objKey, List value, StringProperty spSql) {
//
//		if (FiCollection.isNotEmpty(value)) {
//			String param = objKey.toString();
//			addMultiParams(spSql, param, (List) value, this);
//			FiQuery.fsmActivateOptParamForProp(spSql, param);
//		}
//
//		return this;
//	}

//	private void addMultiParams(StringProperty sqlQuery, String param, List listData, FiKeyBean mapBind) {
//
//		if (FiCollection.isEmpty(listData)) return;
//
//		Map<String, Object> mapParamsNew = new HashMap<>();
//
//		Integer index = -1;
//		for (Object listDatum : listData) {
//			index++;
//			String paramNameByTemplate = FiQuery.genTemplateMultiParam(param, index);
//			Object value = listData.get(index);
//			mapParamsNew.put(paramNameByTemplate, value);
//		}
//
//		FiQuery.fhrConvertSqlForMultiParamByTemplate2(sqlQuery, param, listData.size());
//
//		mapBind.remove(param);
//		mapBind.putAll(mapParamsNew);
//
//	}


//	public void deActivateParam(Object sqlParam, StringProperty spSql) {
//		if (sqlParam == null) return;
//		FiQuery.fhrDeActivateOptParam(spSql, sqlParam.toString());
//	}

//	public void activateParam(Object sqlParam, StringProperty spSql) {
//		if (sqlParam == null) return;
//		FiQuery.fsmActivateOptParamForProp(spSql, sqlParam.toString());
//	}

//	public Set<String> getActivateParamSet() {
//		if (activateParamSet == null) {
//			activateParamSet = new HashSet<>();
//		}
//		return activateParamSet;
//	}

//	public void setActivateParamSet(Set<String> activateParamSet) {
//		this.activateParamSet = activateParamSet;
//	}

//	public Set<String> getDeActivateParamSet() {
//		if (deActivateParamSet == null) {
//			deActivateParamSet = new HashSet<>();
//		}
//		return deActivateParamSet;
//	}

//	public void setDeActivateParamSet(Set<String> deActivateParamSet) {
//		this.deActivateParamSet = deActivateParamSet;
//	}