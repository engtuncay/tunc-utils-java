package ozpasyazilim.utils.core;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.ReflectionUtils;
import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.fidborm.FiEntity;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiCol;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

public class FiReflection {

	public static void main(String[] args) {

//		PersonEntityTest personEntityTest = new PersonEntityTest("Ali", "Kaya");
//
//		PersonEntityTest personEntityTest3 = new PersonEntityTest("Mehmet", "Kaya");
//
//		personEntityTest.setPerson(personEntityTest3);
//
//		PersonEntityTest personEntityTest2 = clazzClone(personEntityTest);
//
//		FiConsole.printFieldsNotNull(personEntityTest2);

	}


	public static <T> Object getProperty(T entity, String field) {

		//if (entity == null) Loghelperr.getInstance(FiProperty.class).debug("entity null");
		//if (field == null) Loghelperr.getInstance(FiProperty.class).debug("field null");

		Object objectt = null;
		try {
			objectt = PropertyUtils.getProperty(entity, field);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return objectt;
	}

	public static String getSimpleTypeName(Class<?> aClass) {
		return aClass.getSimpleName();
	}

	public static <T> Object getPropertyy(T entity, String field) {

		//if (entity == null) Loghelperr.getInstance(FiProperty.class).debug("entity null");
		//if (field == null) Loghelperr.getInstance(FiProperty.class).debug("field null");

		Object objectt = null;
		try {
			objectt = PropertyUtils.getProperty(entity, field);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return objectt;
	}

	public static <T> Object getPropertyNested(T entity, String field) {

		//if (entity == null) Loghelperr.getInstance(FiProperty.class).debug("entity null");
		//if (field == null) Loghelperr.getInstance(FiProperty.class).debug("field null");

		Object objectt = null;
		try {
			objectt = PropertyUtils.getNestedProperty(entity, field);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return objectt;
	}

	public static <T> Class getPropertyType(T objectt, String fieldName) {

		//if (objectt == null) Loghelperr.getInstance(getClass()).debug("Object Null");
		//if (fieldName == null) Loghelperr.getInstance(getClass()).debug("FieldName Null");

		Class clazz = null;

		// olmayan property için hata dönmedi !!!!
		try {
			PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(objectt, fieldName);
			if (propertyDescriptor == null) return null;
			clazz = propertyDescriptor.getPropertyType();
			//clazz = PropertyUtils.getPropertyType(objectt, fieldName);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return clazz;
	}

	public static <T> Boolean setter(IFiCol fiCol, T objectt, String value) {

		if (objectt == null) return false;
		if (value == null) return false;

		try {

			Object cellvalue = null;

			String strCellvalue = value;  //fiCol.getHeader();

			if (fiCol.getColType() == OzColType.String) {
				cellvalue = strCellvalue;
			} else if (fiCol.getColType() == OzColType.Double) {
				cellvalue = FiNumber.strToDouble(strCellvalue);
			} else if (fiCol.getColType() == OzColType.Date) {
				cellvalue = FiDate.strToDateGeneric2(strCellvalue);
			} else if (fiCol.getColType() == OzColType.Integer) {
				cellvalue = Integer.parseInt(strCellvalue);
			} else { // eğer tip belirtilmemişse String olarak varsayılır
				cellvalue = strCellvalue;
			}

			PropertyUtils.setProperty(objectt, fiCol.getFieldName(),cellvalue ); //cellvalue == null ? strCellvalue : cellvalue
			return true;
		} catch (Exception e) {
			Loghelper.get(FiReflection.class).debug(FiException.exceptiontostring(e));
		}
//		} catch (IllegalAccessException e) {
//			Loghelper.get(FiReflection.class).debug(FiException.exceptiontostring(e));
//		} catch (InvocationTargetException e) {
//			Loghelper.get(FiReflection.class).debug(FiException.exceptiontostring(e));
//		} catch (NoSuchMethodException e) {
//			Loghelper.get(FiReflection.class).debug(FiException.exceptiontostring(e));
//		}
		return false;
	}


	public static <R> R generateObject(Class<R> clazz) {

		R objectt = null;
		try {
			objectt = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return objectt;

	}

	public static <T> Boolean setterByString(FiCol fiTableCol, T objectt, String value) {

		if (objectt == null) return false;
		if (value == null) return false;

		try {

			String strCellvalue = value;  //ozTableCol.getHeader();

			Object cellvalue = convertObjectByOzColType(fiTableCol.getColType(), strCellvalue);

			PropertyUtils.setProperty(objectt, fiTableCol.getFieldName(), cellvalue == null ? strCellvalue : cellvalue);
			return true;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return false;

	}

	public static Object convertObjectByOzColType(OzColType ozColType, String strValue) {

		Object cellvalue = null;

		if (ozColType == OzColType.Double) {
			cellvalue = FiNumber.strToDouble(strValue);
		}

		if (ozColType == OzColType.Date) {
			cellvalue = FiDate.strToDateGeneric2(strValue);
		}

		if (ozColType == OzColType.Integer) {
			cellvalue = Integer.parseInt(strValue);
		}

		return cellvalue;
	}


	public static <T> Boolean setter(T objectt, IFiCol ozTableCol, Object value) {

		try {
			PropertyUtils.setProperty(objectt, ozTableCol.getFieldName(), value);
			return true;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return false;

	}

	public static <T> Boolean setter(T objectt, String fieldName, Object value) {

		try {
			PropertyUtils.setProperty(objectt, fieldName, value);
			return true;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return false;

	}

	public static <E> List<E> bindEntity(List<Map<String, String>> listDataMap, List<FiCol> listColumns, Class<E> entityclass) {

		List<E> list = new ArrayList<>();

		for (Iterator iterator = listDataMap.iterator(); iterator.hasNext(); ) {

			Map<String, String> map = (Map<String, String>) iterator.next();

			E entity = null;
			try {
				entity = entityclass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}


			for (int i = 0; i < listColumns.size(); i++) {

				FiCol fiTableCol = listColumns.get(i);

				// Excelden gelen veride bu sütun yoksa atlanır
				if (!map.containsKey(fiTableCol.getHeaderName())) {
					// FIXME bir defa gösterilmeli
					//Loghelper.getInstance(getClass()).debug(" Sütun bulunamadı:" + ozTableCol.getHeader());
					continue;
				}

				try {
					Object cellvalue = map.get(fiTableCol.getHeaderName());

					if (cellvalue != null && ((String) cellvalue).isEmpty()) cellvalue = null;

					if (cellvalue != null && fiTableCol.getColType() == OzColType.Date) {

						String strDate = cellvalue.toString();
						//Loghelper.getInstance(getClass()).debug(" tarih:" + cellvalue.toString());
						cellvalue = FiDate.strToDateGeneric2(strDate);

					}

					if (cellvalue != null && fiTableCol.getColType() == OzColType.Integer) {
						cellvalue = Integer.parseInt(cellvalue.toString());
					}

					if (cellvalue != null && fiTableCol.getColType() == OzColType.Double) {
						cellvalue = FiNumber.strToDouble(cellvalue.toString());
					}

					PropertyUtils.setProperty(entity, fiTableCol.getFieldName().trim(), cellvalue);

				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}

			if (entity != null) list.add(entity);


		}

		return list;
	}

	public static <E> E bindKeyBeanEntity(FiKeyBean keyBean, Class<E> entityclass) {
		return bindKeyBeanToEntityMain(keyBean, entityclass, false);
	}

	public static <E> E bindKeyBeanToEntityMain(FiKeyBean keyBean, Class<E> entityclass, Boolean boDoNotShowNotSuchMethodException) {

		E entity = createObject(entityclass);

		E finalEntity = entity;
		keyBean.forEach((key, value) -> {

			try {
				Object cellvalue = value;
				PropertyUtils.setProperty(finalEntity, key, cellvalue);

			} catch (IllegalAccessException e) {
				Loghelper.get(FiReflection.class).debug(FiException.exceptionIfToStr(e));
			} catch (InvocationTargetException e) {
				Loghelper.get(FiReflection.class).debug(FiException.exceptionIfToStr(e));
			} catch (NoSuchMethodException e) {
				if (!FiBoolean.isTrue(boDoNotShowNotSuchMethodException)) {
					Loghelper.get(FiReflection.class).debug("Objenin Metodu Yok:" + key);
				}
			}

		});

		return entity;
	}

	public static <E> E createObject(Class<E> entityclass) {
		E entity = null;
		try {
			entity = entityclass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return entity;
	}

	/**
	 * @param clazz
	 * @return
	 */
	public static Map<String, FiField> getFieldsAsMap(Class clazz) {

		if (clazz == null) return new HashMap<>();

		Field[] fields = clazz.getDeclaredFields();
		Map<String, FiField> mapFields = new HashMap<>();

		for (Field field : fields) {

			FiField fiField = new FiField();

			fiField.setName(field.getName());
			fiField.setClassNameSimple(field.getType().getSimpleName());

			mapFields.put(field.getName(), fiField);
		}

		return mapFields;

	}

	public static <T extends Object> String getSimpleClassName(T object) {
		if (object instanceof Integer) return Integer.class.getSimpleName();

		if (object instanceof Double) return Double.class.getSimpleName();

		if (object instanceof String) return String.class.getSimpleName();

		if (object instanceof List) return List.class.getSimpleName();

		return null;
	}

	/**
	 * Nested field is supported. Seperator is dot (.)
	 *
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Class<?> getFieldClassType(Class clazz, String fieldName) {
		Field field = null;
		try {
			if (fieldName.contains(".")) {
				//Loghelperr.getInstance(FiProperty.class).debug("Field:"+fieldName);

				String[] split = fieldName.split("\\.");
				//Loghelperr.getInstance(FiProperty.class).debug("Split Size:"+split.length);
				if (split.length < 2) return null;

				String nestedEntityName = split[0];
				String nestedFieldName = split[1];

				if (FiString.isEmpty(nestedEntityName) || FiString.isEmpty(nestedFieldName)) return null;

				Field fieldNestedEntiy = clazz.getDeclaredField(nestedEntityName);
				//Loghelperr.getInstance(FiProperty.class).debug("Field Class:"+fieldNestedEntiy.getType().getSimpleName());
				Field fieldNested = fieldNestedEntiy.getType().getDeclaredField(nestedFieldName);
				return fieldNested.getType();

			} else {

				final Class<?>[] clazzField = {null};
				ReflectionUtils.doWithFields(clazz, field1 -> {
//					Loghelper.get(FiReflection.class).debug("Field:"+field1.getName());
					if (field1.getName().equals(fieldName)) {
						clazzField[0] = field1.getType();
					}
				});
				//field = clazz.getDeclaredField(fieldName);
				//Class<?> clazzField = field.getType(); // Class object for java.lang.String
				// return clazzField;
				return clazzField[0];
			}

		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

//	public static Class<?> getFieldTypeNested(Class clazz, String fieldName) {
//		Field field = null;
//		try {
//			if (fieldName.contains(".")) {
//
//				String[] split = fieldName.split(".");
//				String nestedEntityName = split[0];
//				String nestedFieldName = split[1];
//
//				Field fieldNestedEntiy = clazz.getDeclaredField(nestedEntityName);
//				Field fieldNested = fieldNestedEntiy.getClass().getDeclaredField(nestedFieldName);
//				return fieldNested.getType();
//
//			} else {
//				field = clazz.getDeclaredField(fieldName);
//				Class<?> clazzField = field.getType(); // Class object for java.lang.String
//				return clazzField;
//			}
//
//
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}


	public static <T> Boolean setterNested(T bean, String fieldName, Object value) {

		try {
			PropertyUtils.setNestedProperty(bean, fieldName, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static <T> T cloneObject(T t) { //throws InstantiationException, IllegalAccessException, NoSuchFieldException

		Class<?> clazzRoot = t.getClass();

		Object newInstance = null;

		try {
			newInstance = clazzRoot.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		Field[] fieldsClone = newInstance.getClass().getDeclaredFields();

		for (Field fieldClone : fieldsClone) {
			fieldClone.setAccessible(true);

			// statik ve final değişkenler kopyalamadık
			if (checkStatic(fieldClone) || checkFinal(fieldClone)) {
				continue;
			}

			try {
				fieldClone.set(newInstance, getFieldContent(t, fieldClone.getName()));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}

		return (T) newInstance;

	}

	public static <T> T cloneObjectByClass(Object t, Class<T> clazz) {
		//throws InstantiationException, IllegalAccessException, NoSuchFieldException

		Class clazzRoot = clazz;

		Object newInstance = null;

		try {
			newInstance = clazzRoot.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		Field[] fieldsClone = clazz.getDeclaredFields();

		for (Field fieldClone : fieldsClone) {
			fieldClone.setAccessible(true);

			// statik ve final değişkenler kopyalamadık
			if (checkStatic(fieldClone) || checkFinal(fieldClone)) {
				continue;
			}

			try {
				fieldClone.set(newInstance, getProperty(t, fieldClone.getName()));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}

		return (T) newInstance;
	}

	public static Object getFieldContent(Object entityObject, String fieldName) { //throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException

		Field declaredField = null;

		try {
			declaredField = entityObject.getClass().getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		declaredField.setAccessible(true);

		Object objValue = null;

		try {
			objValue = declaredField.get(entityObject);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return objValue;
	}


	// FIXME field ile control edilebilir - optionala çevrilebilr
	public static boolean hasProperty(Object entity, String field) {

		Object objectt = null;
		try {
			objectt = PropertyUtils.getProperty(entity, field);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * statik ve final değişkenler kopyalamadık
	 * <br>
	 *
	 * @param fromObj
	 * @param toObj
	 */
	public static void copyFields(Object fromObj, Object toObj) {

		Field[] fieldsToObj = toObj.getClass().getDeclaredFields();

		for (Field fieldToObj : fieldsToObj) {

			// statik ve final değişkenler kopyalamadık
			if (checkStatic(fieldToObj) || checkFinal(fieldToObj)) {
				continue;
			}

			fieldToObj.setAccessible(true);

			try {
				fieldToObj.set(toObj, getFieldContent(fromObj, fieldToObj.getName()));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

	}

	public static Boolean checkStatic(Field field) {
		return Modifier.isStatic(field.getModifiers());
	}

	public static Boolean checkFinal(Field field) {
		return Modifier.isFinal(field.getModifiers());
	}

	public static <PrmClazz> List<PrmClazz> cloneList(List<PrmClazz> prmClazzList) {
		List<PrmClazz> listClone = new ArrayList<>();

		for (PrmClazz prmClazz : prmClazzList) {
			listClone.add(cloneObject(prmClazz));
		}
		return listClone;
	}

	public static <EntClazz> Object getCandId(EntClazz next, Class<EntClazz> clazz) {

		List<FiField> listFiFieldsCandId = FiEntity.getListFieldsCandId(clazz);

		if (listFiFieldsCandId.size() == 1) {
			Object property = getProperty(next, listFiFieldsCandId.get(0).getName());
			return property;
		}
		return null;
	}

	public static Boolean setProperty(Object objEntity, String fieldName, Object objValue) {

		try {
			PropertyUtils.setProperty(objEntity, fieldName, objValue);
			return true;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		}

	}
}
