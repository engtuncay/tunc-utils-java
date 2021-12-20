package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.fidbanno.*;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.core.FiReflection;
import ozpasyazilim.utils.table.FiCol;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class FiEntityHelper {


	/**
	 * Transient alanlar dahil degil.
	 * <br>
	 * Dahil olan Alanlar (short) <br>
	 * Standar alan dahil edilir sadece <br>
	 * id,fiId,fieldName,nullable,fiCandId1,fiSelect,fiFirstInsert
	 *
	 * @param clazz
	 * @return
	 */
	public static List<FiField> getListFiFieldsShortWithId(Class clazz) {

		Field[] fields = clazz.getDeclaredFields(); // returns all members including private members but not inherited members.

		//To filter only the public fields from the above list (based on requirement) use below code:
		//List<Field> fieldList = Arrays.asList(fields).stream().filter(field -> Modifier.isPublic(field.getModifiers())).collect(
		//Collectors.toList());

		//List<Field> fieldListFilterAnno = Arrays.asList(fields).stream().filter(field -> !field.isAnnotationPresent(Transient.class))
		//.collect(Collectors.toList());

		List<FiField> listFields = new ArrayList<>();

		for (Field field : fields) {

			if (field.isAnnotationPresent(Transient.class)) continue;
			if (field.isAnnotationPresent(FiTransient.class)) continue;
			// Static alanlar alınmaz
			if (Modifier.isStatic(field.getModifiers())) continue;

			FiField fiField = new FiField();

			listFields.add(setupFiFieldShort(field, fiField));

		}

		return listFields;

	}

	/**
	 * Transient Dahil Edilmedi
	 *
	 * @param clazz
	 * @return
	 */
	public static Map<String, FiField> getMapFiFieldsShort(Class clazz) {
		return getMapFiFieldsShortMain(clazz, null);
	}

	/**
	 * Extra anno lar dahil edildi. Comment gibi
	 *
	 * @param clazz
	 * @return
	 */
	public static Map<String, FiField> getMapFiFieldsExtra(Class clazz) {
		return getMapFiFieldsShortMain(clazz, true);
	}

	/**
	 * Transient Dahil Edilmedi
	 *
	 * @param clazz
	 * @param includeExtra
	 * @return
	 */
	public static Map<String, FiField> getMapFiFieldsShortMain(Class clazz, Boolean includeExtra) {

		Field[] fields = clazz.getDeclaredFields(); // returns all members including private members but not inherited members.

		Map<String, FiField> mapFiFields = new HashMap<>();

		for (Field field : fields) {

			if (field.isAnnotationPresent(Transient.class)) continue;
			if (field.isAnnotationPresent(FiTransient.class)) continue;
			// Static alanlar alınmaz
			if (Modifier.isStatic(field.getModifiers())) continue;

			FiField fiField = new FiField();
			setupFiFieldShort(field, fiField);

			if (FiBoolean.isTrue(includeExtra)) {
				assignFiFieldExtraRelatedDb(field, fiField);
			}

			mapFiFields.put(fiField.getName(), fiField);

		}

		return mapFiFields;

	}

	public static List<FiField> getListFiFieldsShortWithNotID(Class clazz) {

		Field[] fields = clazz.getDeclaredFields();

		List<FiField> listFields = new ArrayList<>();

		for (Field field : fields) {

			if (field.isAnnotationPresent(Transient.class)) continue;
			if (field.isAnnotationPresent(FiTransient.class)) continue;
			if (field.isAnnotationPresent(Id.class)) continue;
			if (field.isAnnotationPresent(FiId.class)) continue;
			// Static alanlar alınmaz
			if (Modifier.isStatic(field.getModifiers())) continue;

			FiField fiField = new FiField();
			listFields.add(setupFiFieldShort(field, fiField));

		}

		return listFields;

	}

	/**
	 * Not Include Transient Fields
	 *
	 * @param clazz
	 * @return
	 */
	public static List<FiField> getListFiFieldsSummary(Class clazz) {
		return getListFiFieldsSummaryMain(clazz, null, null);
	}

	public static List<FiField> getListFiFieldsCandId(Class clazz) {
		List<FiField> listFiFieldsSummary = getListFiFieldsSummary(clazz, false);

		List<FiField> fiFieldList = new ArrayList<>();

		for (FiField fiField : listFiFieldsSummary) {
			if (FiBoolean.isTrue(fiField.getBoCandidateId1())) {
				fiFieldList.add(fiField);
			}
		}

		return fiFieldList;
	}

	public static List<FiField> getListFiFieldsCandId2(Class clazz) {

		List<FiField> listFiFieldsSummary = getListFiFieldsSummary(clazz, false);

		List<FiField> fiFieldList = new ArrayList<>();

		for (FiField fiField : listFiFieldsSummary) {
			if (FiBoolean.isTrue(fiField.getBoCandidateId2())) {
				fiFieldList.add(fiField);
			}
		}

		return fiFieldList;
	}

	public static List<FiField> getListFiFieldsSummary(Class clazz, Boolean includeTransient) {
		return getListFiFieldsSummaryMain(clazz, includeTransient, null);
	}

	/**
	 * Transient Dahil Edilmedi
	 *
	 * @param clazz
	 * @return
	 */
	public static List<FiField> getListFiFieldsExtra(Class clazz) {
		return getListFiFieldsSummaryMain(clazz, false, true);
	}

	/**
	 * Normal şartlarda transient alanlar eklenmez, özel olarak belirtmek lazım
	 *
	 * @param clazz
	 * @return
	 */
	public static List<FiField> getListFiFieldsSummaryMain(Class clazz, Boolean includeTransient, Boolean includeExtra) {

		Field[] fields = clazz.getDeclaredFields();
		List<FiField> listFields = new ArrayList<>();

		for (Field field : fields) {

			// transient dahil edilmemişse atlasın
			if (!FiBoolean.isTrue(includeTransient)) {
				if (field.isAnnotationPresent(Transient.class)) continue;
				if (field.isAnnotationPresent(FiTransient.class)) continue;
			}

			// Static alanlar alınmaz
			if (Modifier.isStatic(field.getModifiers())) continue;

			FiField fiField = new FiField();
			setupFiFieldForSummaryAttributes(field, fiField);

			if (FiBoolean.isTrue(includeExtra)) {
				assignFiFieldExtraRelatedDb(field, fiField);
			}

			listFields.add(fiField);
		}

		return listFields;

	}


	public static List<FiField> getListFieldsNotNullable(Class clazz, Boolean includeTransient) {

		List<FiField> listFields = getListFiFieldsSummary(clazz, includeTransient);

		return listFields.stream().filter(fiField -> {
			if (FiBoolean.isFalse(fiField.getNullable())) return true;
			return false;
		}).collect(toList());

	}

	public static List<FiField> getListFiFieldsAll(Class clazz) {

		Field[] fields = clazz.getDeclaredFields(); // returns all members including private members but not inherited members.

		List<FiField> listFields = new ArrayList<>();

		for (Field field : fields) {

			if (field.isAnnotationPresent(Transient.class)) continue;

			if (field.isAnnotationPresent(FiTransient.class)) continue;

			FiField fiField = new FiField();
			listFields.add(setupFiFieldForAllAttributes(field, fiField));

		}

		return listFields;

	}

	public static FiField setupFiFieldShort(Field field, FiField fiField) {

		if (fiField == null) fiField = new FiField();

		assignFiFieldReflection(field, fiField);
		assignFiFieldRelatedDb(field, fiField);

//		if (field.isAnnotationPresent(FiColumn.class)) {
//
//			FiColumn column = field.getAnnotation(FiColumn.class);
//
//			//if (!FiString.isEmpty(column.label())) fiField.setLabel(column.label());
//			if (FiBoolean.isFalse(column.isNullable())) fiField.setNullable(false);
//
//		}

		return fiField;
	}

	public static void assignFiFieldReflection(Field field, FiField fiField) {

		fiField.setName(field.getName());

		if (fiField.getDbFieldName() == null) fiField.setDbFieldName(field.getName());

		fiField.setClassNameSimple(field.getType().getSimpleName());

	}

	public static void assignFiFieldExtraRelatedDb(Field field, FiField fiField) {

		if (field.isAnnotationPresent(FiComment.class)) {

			FiComment column = field.getAnnotation(FiComment.class);

			if (!FiString.isEmpty(column.txComment())) {
				fiField.setTxComment(column.txComment());
			}

		}
	}

	public static void assignFiFieldRelatedDb(Field field, FiField fiField) {

		if (field.isAnnotationPresent(Column.class)) {

			Column column = field.getAnnotation(Column.class);

			fiField.setPrecision(column.precision());
			fiField.setScale(column.scale());
			fiField.setLength(column.length());
			fiField.setUnique(column.unique());
			fiField.setNullable(column.nullable()); // def:true (in interface)
			fiField.setColCustomType(column.columnDefinition());

			if (!FiString.isEmptyTrim(column.name())) {
				fiField.setDbFieldName(column.name());
			} else {
				fiField.setDbFieldName(field.getName());
			}

		}

		if (field.isAnnotationPresent(FiColumn.class)) {

			FiColumn anno = field.getAnnotation(FiColumn.class);

			fiField.setPrecision(anno.precision());
			fiField.setScale(anno.scale());
			fiField.setLength(anno.length());
			fiField.setUnique(anno.isUnique());
			fiField.setNullable(anno.isNullable());
			//if (FiBoolean.isFalse(anno.isNullable())) fiField.setNullable(false);
			fiField.setColCustomType(anno.colCustomTypeDefinition());
			fiField.setDefaultValue(anno.defaultValue());
			fiField.setColDefinitionExtra(anno.colDefinitionExtra());
			fiField.setBoUtfSupport(anno.isUnicodeSupport());
			fiField.setBoExcludeFromAutoColList(anno.boExcludeFromAutoColList());

			if (!FiString.isEmpty(anno.label())) fiField.setLabel(anno.label());
			if (anno.collation() != FiCollation.Default) fiField.setFiCollation(anno.collation());
			if (!FiString.isEmpty(anno.typeName())) fiField.setTypeName(anno.typeName());
			if (anno.defaultUpdateField()) fiField.setBoDefaultUpdateField(true);

			if (!FiString.isEmptyTrim(anno.name())) {
				fiField.setDbFieldName(anno.name());
			} else {
				fiField.setDbFieldName(field.getName());
			}

		}

		if (field.isAnnotationPresent(FiNotNull.class)) {
			fiField.setNullable(false);
		}

		if (field.isAnnotationPresent(FiWhere1.class)) {
			fiField.setBoWhere1(true);
		}

		if (field.isAnnotationPresent(Temporal.class)) {
			fiField.setTemporalType(field.getAnnotation(Temporal.class).value());
		}

		if (field.isAnnotationPresent(ColDefinitionExtra.class)) {
			fiField.setColDefinitionExtra(field.getAnnotation(ColDefinitionExtra.class).value());
		}

		// other related db

		if (field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(FiTransient.class)) {
			fiField.setTransient(true);
		}

		// Id alanlar için ortak tanımlamalar
		if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(FiId.class)) {
			fiField.setBoIdField(true);
			fiField.setBoDtoField(true);
		}

		// FiId ye özel bilgiler set edilir
		if (field.isAnnotationPresent(FiId.class)) {
			FiId anno = field.getAnnotation(FiId.class);
			fiField.setIdGenerationType(anno.idGenerationType());
		}


		if (field.isAnnotationPresent(FiCandId1.class)) {
			fiField.setBoCandidateId1(true);
			//FIXME nullable false olmayabilir , çoklu candid ise
			fiField.setNullable(false);
			fiField.setBoDtoField(true);
		}

		if (field.isAnnotationPresent(FiCandId2.class)) {
			fiField.setBoCandidateId2(true);
			fiField.setBoDtoField(true);
		}

		if (field.isAnnotationPresent(FiSelect2.class)) {
			fiField.setBoFiSelect(true);
		}

		if (field.isAnnotationPresent(FiSelect1.class)) {
			fiField.setBoFiSelect1(true);
		}

		if (field.isAnnotationPresent(FiOnlyFirstInsert.class)) {
			fiField.setBoOnlyFirstInsert(true);
		}

		if (field.isAnnotationPresent(FiScopeIdField.class)) {
			fiField.setBoScopeIdField(true);
		}

		if (field.isAnnotationPresent(FiDto.class)) {
			fiField.setBoDtoField(true);
		}

		if (field.isAnnotationPresent(FiSeperatedField.class)) {
			fiField.setBoSeperatedField(true);
		}

		if (field.isAnnotationPresent(FiFirmField.class)) {
			fiField.setBoFirmField(true);
		}

		if (field.isAnnotationPresent(FiDefaultUpdate.class)) {
			fiField.setBoDefaultUpdateField(true);

			FiDefaultUpdate anno = field.getAnnotation(FiDefaultUpdate.class);

			if (!FiString.isEmpty(anno.typeName())) {
				fiField.setTypeName(anno.typeName());
			}
		}

		if (field.isAnnotationPresent(FiCusFieldUserId.class)) {
			fiField.setBoCusFieldUserId(true);
			fiField.setBoDefaultUpdateField(true);
		}

		if (field.isAnnotationPresent(FiCusFieldDtChange.class)) {
			fiField.setBoCusFieldDtChange(true);
			fiField.setBoDefaultUpdateField(true);
		}

		if (field.isAnnotationPresent(FiCusFieldDtCreate.class)) {
			fiField.setBoCusFieldDtCreate(true);
			fiField.setBoOnlyFirstInsert(true);
		}

		if (field.isAnnotationPresent(FiCombo.class)) {
			fiField.setBoComboField(true);
		}

		if (field.isAnnotationPresent(FiInsertMaxPlus.class)) {
			fiField.setBoInsertMaxPlus(true);
		}

		if (field.isAnnotationPresent(FiGuid.class)) {
			fiField.setBoGuidField(true);
		}


	}

	public static FiField setupFiFieldForSummaryAttributes(Field field, FiField fiField) {

		assignFiFieldReflection(field, fiField);
		assignFiFieldRelatedDb(field, fiField);

//		if (field.isAnnotationPresent(FiColumn.class)) {
//
//			FiColumn column = field.getAnnotation(FiColumn.class);
//
//			if (!FiString.isEmpty(column.label())) fiField.setLabel(column.label());
//
//			if (FiBoolean.isFalse(column.isNullable())) fiField.setNullable(false);
//
//		}

		return fiField;
	}

	public static FiField setupFiFieldForAllAttributes(Field field, FiField fiField) {

		assignFiFieldReflection(field, fiField);
		assignFiFieldRelatedDb(field, fiField);

		return fiField;
	}

	public static List<FiField> getListFiFieldsNotNull(Class clazz, Object objectt) {

		Field[] fields = clazz.getDeclaredFields(); // returns all members including private members but not inherited members.

		List<FiField> listFields = new ArrayList<>();

		for (Field field : fields) {

			if (field.isAnnotationPresent(Transient.class)) continue;
			if (field.isAnnotationPresent(FiTransient.class)) continue;
			// Static alanlar alınmaz
			if (Modifier.isStatic(field.getModifiers())) continue;

			Object fieldValue = FiReflection.getProperty(objectt, field.getName());

			if (fieldValue != null) {

				FiField fiField = new FiField();

//				fiField.setName(field.getName());
//
//				//System.out.println(" Field:"+field.getName());
//				if (field.isAnnotationPresent(FiCandId1.class)) {
//					//System.out.println("fiid selected true");
//					fiField.setBoCandidateId1(true);
//				}
//
//				if (field.isAnnotationPresent(FiId.class) || field.isAnnotationPresent(Id.class)) {
//					//System.out.println("fiid selected true");
//					fiField.setBoIdField(true);
//				}

				listFields.add(setupFiFieldShort(field, fiField));

			}

		}

		return listFields;

	}

	public static List<FiField> getListFiFieldsNotNullWithId(Class clazz, Object objectt) {

		Field[] fields = clazz.getDeclaredFields(); // returns all members including private members but not inherited members.

		List<FiField> listFields = new ArrayList<>();

		for (Field field : fields) {

			if (field.isAnnotationPresent(Transient.class)) continue;
			if (field.isAnnotationPresent(FiTransient.class)) continue;
			// Static alanlar alınmaz
			if (Modifier.isStatic(field.getModifiers())) continue;

			//if (field.isAnnotationPresent(Id.class)) continue;

			Object fieldValue = FiReflection.getProperty(objectt, field.getName());

			if (fieldValue != null) {

				FiField fiField = new FiField();

//				fiField.setName(field.getName());
//
//				//System.out.println(" Field:"+field.getName());
//				if (field.isAnnotationPresent(FiCandId1.class)) {
//					//System.out.println("fiid selected true");
//					fiField.setBoCandidateId1(true);
//				}
//
//				if (field.isAnnotationPresent(FiId.class) || field.isAnnotationPresent(Id.class)) {
//					//System.out.println("fiid selected true");
//					fiField.setBoIdField(true);
//				}

				listFields.add(setupFiFieldShort(field, fiField));

			}

		}

		return listFields;

	}

	public static List<FiField> getListFiFieldsNotNullWithCandId1(Class clazz, Object objectt) {

		List<FiField> listFields = new ArrayList<>();

		for (Field field : clazz.getDeclaredFields()) {

			if (field.isAnnotationPresent(Transient.class)) continue;
			if (field.isAnnotationPresent(FiTransient.class)) continue;
			// Static alanlar alınmaz
			if (Modifier.isStatic(field.getModifiers())) continue;

			Object fieldValue = FiReflection.getProperty(objectt, field.getName());

			if (fieldValue != null || field.isAnnotationPresent(FiCandId1.class)) {
				listFields.add(setupFiFieldShort(field, null));
			}
		}

		return listFields;

	}

	public static <T> Boolean checkIdFieldsNullOrFull(T entity, Class<T> entityClass) {

		List<FiField> listFiFieldsShort = getListFiFieldsShortWithId(entityClass);

		//Stream<FiField> listIdFields = listFiFieldsShort.stream().filter(fiField -> FiBoolean.isTrue(fiField.getBoIdField()));

		Boolean isNull = null;

		//listIdFields.forEach(fiField -> {
		for (FiField field : listFiFieldsShort) {

			if (FiBoolean.isTrue(field.getBoIdField())) {
				//Loghelperr.staticLogDebug("Id Fiedl"+field.getName());
				Object idValue = FiReflection.getPropertyNested(entity, field.getName());

				if (isNull == null && idValue != null) isNull = false;
				if (idValue == null) isNull = true;

			}
		}

		return isNull;

	}

	public static <T> Boolean assignIdFields(T fromEntity, T toEntity, Class<T> entityClass) {

		List<FiField> listFiFieldsShort = getListFiFieldsShortWithId(entityClass);

		//Stream<FiField> listIdFields = listFiFieldsShort.stream().filter(fiField -> FiBoolean.isTrue(fiField.getBoIdField()));

		Boolean boResult = null;

		//listIdFields.forEach(fiField -> {
		for (FiField field : listFiFieldsShort) {

			if (FiBoolean.isTrue(field.getBoIdField())) {
				Loghelper.debugStatic("Id Field:" + field.getName());
				Object idValue = FiReflection.getPropertyNested(fromEntity, field.getName());
				boResult = FiReflection.setterNested(toEntity, field.getName(), idValue);
			}
		}

		return boResult;

	}

	public static String[] getIdFields(Class clazz) {
		List<String> listFiFieldsShort = getListIdFields(clazz);
		return FiString.convertListToArray(listFiFieldsShort);
	}

	public static List<String> getListIdFields(Class entityClazz) {

		List<String> listIdFields = new ArrayList<>();

		for (FiField field : getListFiFieldsShortWithId(entityClazz)) {

			if (FiBoolean.isTrue(field.getBoIdField())) {
				//Loghelperr.staticLogDebug("Id Field:"+field.getName());
				listIdFields.add(field.getName());
			}
		}

		return listIdFields;
	}

	public static List<String> getListDbFieldName(List<FiField> fieldListFilterAnno) {
		return fieldListFilterAnno.stream().map(fiField -> fiField.getDbFieldName()).collect(toList());
	}

	public static List<String> getListFieldName(List<FiField> fieldListFilterAnno) {
		return fieldListFilterAnno.stream().map(fiField -> fiField.getName()).collect(toList());
	}

	public static String getIdFieldSingle(List<Field> fieldListFilterAnno) {

		for (Field field : fieldListFilterAnno) {
			if (field.isAnnotationPresent(Id.class)) {
				return field.getName();
			}
		}
		return null;
	}

	public static <NC> Boolean checkNullFields(NC entity, List<FiField> listNotNullFields) {

		Boolean found = false;

		for (FiField fiField : listNotNullFields) {

			if (FiReflection.getProperty(entity, fiField.getName()) == null) {
				fiField.setNullCheck(true);
				found = true;
			}

		}

		return found;

	}

	public static List<FiCol> getListFiTableCol(Class entityClass) {
		return FiCol.convertListFiField(getListFiFieldsSummary(entityClass));
	}

	public static List<FiCol> getListFiTableColWithTransient(Class entityClass) {
		return FiCol.convertListFiField(getListFiFieldsSummary(entityClass, true));
	}
}
