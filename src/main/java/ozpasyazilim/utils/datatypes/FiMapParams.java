package ozpasyazilim.utils.datatypes;

import javafx.beans.property.StringProperty;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.fidborm.FiQuery;
import ozpasyazilim.utils.log.Loghelper;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static ozpasyazilim.utils.fidborm.FiQuery.fhrConvertSqlForMultiParamByTemplate2;

/**
 * Sql Sorgularında named parametrelere bind etmek için kullanılır.
 */
public class FiMapParams extends FiMapStro {

	Set<String> activateParamSet;
	Set<String> deActivateParamSet;

	public FiMapParams() {
	}

	public FiMapParams(Map<? extends String, ?> m) {
		super(m);
	}


	public static FiMapParams build() {
		return new FiMapParams();
	}

	public FiMapParams buildPut(Object fieldName, Object value) {
		if (fieldName == null) return this;

		this.put(fieldName.toString(), value);
		return this;
	}

	public FiMapParams buildPutIfNotNull(Object fieldName, Object value) {
		if (fieldName == null) return this;
		if (value == null) return this;

		this.put(fieldName.toString(), value);
		return this;
	}

//	public FiMapParams buildPutIfNotEmpty(Object fieldName, Object value) {
//		putIfNotEmpty(fieldName,value,false);
//		return this;
//	}

//	public FiMapParams buildPutIfNotEmpty(Object fieldName, Object value, Boolean addPercentage) {
//		putIfNotEmpty(fieldName, value, addPercentage);
//		return this;
//	}

	public FiMapParams putIfNotEmpty(Object fieldName, Object value) {
		return putIfNotEmpty(fieldName, value, null);
	}

	public FiMapParams putIfNotEmpty(Object fieldName, Object value, Boolean addPercentage) {
		if (fieldName == null) return this;
		if (FiType.isEmptyGen(value)) return this;

		if (FiBoolean.isTrue(addPercentage)) {
			if(value instanceof String){
				this.put(fieldName.toString(), "%"+ value +"%");
			}else {
				this.put(fieldName.toString(), value);
			}
		}else{
			this.put(fieldName.toString(), value);
		}
		return this;
	}

	public void puto(Object field, Object value) {
		this.put(field.toString(), value);
	}

	public void add(Object field, Object value) {
		this.put(field.toString(), value);
	}

	public FiMapParams buildMultiParam(Object fieldName, List<Integer> listData) {

		Integer index = -1;
		for (Integer listDatum : listData) {
			index++;
			put(fieldName.toString() + index.toString(), listData.get(index));
		}
		return this;
	}

	public void putIfNotNull(Object objKey, Object value) {
		if (value != null) {
			this.put(objKey.toString(), value);
		}
	}


	/**
	 * String boş olmamalı
	 * <p>
	 * Collection size 0 olmamalı
	 * <p>
	 * Diger objeler null olmamalı
	 *
	 * @param objKey
	 * @param value
	 * @param sql
	 * @return
	 */
	public FiMapParams bindAndActivateIfNotEmpty(Object objKey, Object value, StringProperty sql) {
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
	 *
	 * @param objKey
	 * @param value
	 * @param sql
	 * @param addPercentage
	 */
	public FiMapParams bindAndActivateIfNotEmpty(Object objKey, Object value, StringProperty sql, Boolean addPercentage) {
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
		}else{
//			Loghelper.get(getClass()).debug("Aktive edilmedi Param:" + objKey.toString());
		}
		return this;
	}

	public FiMapParams bindAndActivateListParamIfNotEmpty(Object objKey, List value, StringProperty spSql) {

		if (FiCollection.isNotEmpty(value)) {
			String param = objKey.toString();
			addMultiParams(spSql, param, (List) value, this);
			FiQuery.fsmActivateOptParamForProp(spSql, param);
		}

		return this;
	}

	private void addMultiParams(StringProperty sqlQuery, String param, List listData, FiMapParams mapBind) {

		if (FiCollection.isEmpty(listData)) return;

		Map<String, Object> mapParamsNew = new HashMap<>();

		Integer index = -1;
		for (Object listDatum : listData) {
			index++;
			String paramNameByTemplate = FiQuery.genTemplateMultiParam(param, index);
			Object value = listData.get(index);
			mapParamsNew.put(paramNameByTemplate, value);
		}

		FiQuery.fhrConvertSqlForMultiParamByTemplate2(sqlQuery, param, listData.size());

		mapBind.remove(param);
		mapBind.putAll(mapParamsNew);

	}

	public FiMapParams bind(Object key, Object value) {
		if (key == null) return this;
		put(key.toString(), value);
		return this;
	}

	public void deActivateParam(Object sqlParam, StringProperty spSql) {
		if (sqlParam == null) return;
		FiQuery.fhrDeActivateOptParam(spSql, sqlParam.toString());
	}

	public void activateParam(Object sqlParam, StringProperty spSql) {
		if (sqlParam == null) return;
		FiQuery.fsmActivateOptParamForProp(spSql, sqlParam.toString());
	}

	public Set<String> getActivateParamSet() {
		if (activateParamSet == null) {
			activateParamSet = new HashSet<>();
		}
		return activateParamSet;
	}

	public void setActivateParamSet(Set<String> activateParamSet) {
		this.activateParamSet = activateParamSet;
	}

	public Set<String> getDeActivateParamSet() {
		if (deActivateParamSet == null) {
			deActivateParamSet = new HashSet<>();
		}
		return deActivateParamSet;
	}

	public void setDeActivateParamSet(Set<String> deActivateParamSet) {
		this.deActivateParamSet = deActivateParamSet;
	}
}
