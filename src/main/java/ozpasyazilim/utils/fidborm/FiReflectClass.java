package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.FiBool;
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

/**
 * FiClass Reflection Utility Methods
 */
public class FiReflectClass {

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
    public static List<FiField> getListFieldsShortWithId(Class clazz) {

        // returns all members including private members but not inherited members.
        Field[] fields = clazz.getDeclaredFields();

        //To know the public fields :
        //Modifier.isPublic(field.getModifiers())

        List<FiField> listFields = new ArrayList<>();

        for (Field field : fields) {

            if (field.isAnnotationPresent(Transient.class)) continue;
            if (field.isAnnotationPresent(FiTransient.class)) continue;

            // Static alanlar alınmaz
            if (Modifier.isStatic(field.getModifiers())) continue;

            FiField fiField = new FiField();
            listFields.add(setupFiFieldAll(field, fiField));
        }

        return listFields;
    }

    /**
     * Transient Dahil Edilmedi
     *
     * @param clazz
     * @return
     */
    public static Map<String, FiField> getMapFieldsShort(Class clazz) {
        return getMapFieldsShortMain(clazz, null);
    }

    /**
     * Extra anno lar dahil edildi. Comment gibi
     *
     * @param clazz
     * @return
     */
    public static Map<String, FiField> getMapFieldsExtra(Class clazz) {
        return getMapFieldsShortMain(clazz, true);
    }

    /**
     * Transient Dahil Edilmedi
     *
     * @param clazz
     * @param includeExtra
     * @return
     */
    public static Map<String, FiField> getMapFieldsShortMain(Class clazz, Boolean includeExtra) {

        // returns all members including private members but not inherited members.
        Field[] fields = clazz.getDeclaredFields();

        Map<String, FiField> mapFiFields = new HashMap<>();

        for (Field field : fields) {

            if (field.isAnnotationPresent(Transient.class)) continue;
            if (field.isAnnotationPresent(FiTransient.class)) continue;
            // Static alanlar alınmaz
            if (Modifier.isStatic(field.getModifiers())) continue;

            FiField fiField = new FiField();
            setupFiFieldAll(field, fiField);

            if (FiBool.isTrue(includeExtra)) {
                assignFiFieldExtraRelatedDb(field, fiField);
            }

            mapFiFields.put(fiField.getOfcTxFieldName(), fiField);

        }

        return mapFiFields;

    }

    public static List<FiField> getListFieldsShortWithNotID(Class clazz) {

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
            listFields.add(setupFiFieldAll(field, fiField));

        }

        return listFields;

    }

    /**
     * Not Include Transient Fields
     *
     * @param clazz
     * @return
     */
    public static List<FiField> getListFieldsWoutStatic(Class clazz) {
        return getListFieldsMain(clazz, null, null);
    }

    public static List<FiField> getListFieldsCandId(Class clazz) {
        List<FiField> listFiFieldsSummary = getListFieldsMain(clazz, false, null);

        List<FiField> fiFieldList = new ArrayList<>();

        for (FiField fiField : listFiFieldsSummary) {
            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                fiFieldList.add(fiField);
            }
        }

        return fiFieldList;
    }

    public static List<FiField> getListFieldsId(Class clazz) {
        List<FiField> listFiFieldsSummary = getListFieldsMain(clazz, false, null);

        List<FiField> fiFieldList = new ArrayList<>();

        for (FiField fiField : listFiFieldsSummary) {
            if (FiBool.isTrue(fiField.getBoKeyIdField())) {
                fiFieldList.add(fiField);
            }
        }

        return fiFieldList;
    }

    public static List<FiField> getListFieldsDateSeperatorField(Class clazz) {
        List<FiField> listFiFieldsSummary = getListFieldsMain(clazz, false, null);

        List<FiField> fiFieldList = new ArrayList<>();

        for (FiField fiField : listFiFieldsSummary) {
            if (FiBool.isTrue(fiField.getBoDateSeperatorField())) {
                fiFieldList.add(fiField);
            }
        }

        return fiFieldList;
    }

    /**
     * @param clazz
     * @param boIncTransient boIncludeTransient Fields
     * @return
     */
    public static List<FiField> getListFieldsWoutStatic(Class clazz, Boolean boIncTransient) {
        return getListFieldsMain(clazz, boIncTransient, null);
    }

    public static List<FiField> getListFieldsCandId2(Class clazz) {

        List<FiField> listFiFieldsSummary = getListFieldsWoutStatic(clazz, false);
        List<FiField> fiFieldList = new ArrayList<>();

        for (FiField fiField : listFiFieldsSummary) {
            if (FiBool.isTrue(fiField.getBoCandidateId2())) {
                fiFieldList.add(fiField);
            }
        }

        return fiFieldList;
    }


    /**
     * Normal şartlarda transient alanlar eklenmez, özel olarak belirtmek lazım
     * <p>
     * WoutStatic
     *
     * @param clazz
     * @return
     */
    public static List<FiField> getListFieldsMain(Class clazz, Boolean boIncTransients, Boolean boIncludeExtra) {

        Field[] fields = clazz.getDeclaredFields();
        List<FiField> listFields = new ArrayList<>();

        for (Field field : fields) {

            // transient dahil edilmemişse atlasın
            if (!FiBool.isTrue(boIncTransients)) {
                if (field.isAnnotationPresent(Transient.class)) continue;
                if (field.isAnnotationPresent(FiTransient.class)) continue;
            }

            // Static alanlar alınmaz
            if (Modifier.isStatic(field.getModifiers())) continue;

            FiField fiField = new FiField();
            setupFiFieldAll(field, fiField);

            if (FiBool.isTrue(boIncludeExtra)) {
                assignFiFieldExtraRelatedDb(field, fiField);
            }

            listFields.add(fiField);
        }

        return listFields;
    }

    public static List<FiCol> getListFieldsMainAsFiCol(Class clazz, Boolean boIncTransients, Boolean boIncludeExtra) {

        Field[] fields = clazz.getDeclaredFields();
        List<FiCol> listFields = new ArrayList<>();

        for (Field field : fields) {

            // transient dahil edilmemişse atlasın
            if (!FiBool.isTrue(boIncTransients)) {
                if (field.isAnnotationPresent(Transient.class)) continue;
                if (field.isAnnotationPresent(FiTransient.class)) continue;
            }

            // Static alanlar alınmaz
            if (Modifier.isStatic(field.getModifiers())) continue;

            FiCol fiField = new FiCol();
            setupFiColAll(field, fiField);

            if (FiBool.isTrue(boIncludeExtra)) {
                assignFiFieldExtraRelatedDb(field, fiField);
            }

            listFields.add(fiField);
        }

        return listFields;
    }

    public static List<FiField> getListFieldsWithFiColManualWoutStaticMain(Class clazz, Boolean includeTransient, Boolean includeExtra) {

        Field[] fields = clazz.getDeclaredFields();
        List<FiField> listFields = new ArrayList<>();

        for (Field field : fields) {

            // transient dahil edilmemişse atlasın
            if (!field.isAnnotationPresent(FiColManual.class) && !FiBool.isTrue(includeTransient)) {
                if (field.isAnnotationPresent(Transient.class)) continue;
                if (field.isAnnotationPresent(FiTransient.class)) continue;
            }

            // Static alanlar alınmaz
            if (Modifier.isStatic(field.getModifiers())) continue;

            FiField fiField = new FiField();
            setupFiFieldAll(field, fiField);

            if (FiBool.isTrue(includeExtra)) {
                assignFiFieldExtraRelatedDb(field, fiField);
            }

            listFields.add(fiField);
        }

        return listFields;
    }


    public static List<FiField> getListFieldsNotNullable(Class clazz, Boolean includeTransient) {

        List<FiField> listFields = getListFieldsWoutStatic(clazz, includeTransient);

        return listFields.stream().filter(fiField -> {
            if (FiBool.isFalse(fiField.getOfcBoNullable())) return true;
            return false;
        }).collect(toList());

    }

    public static List<FiField> getListFieldsAll(Class clazz) {

        Field[] fields = clazz.getDeclaredFields(); // returns all members including private members but not inherited members.

        List<FiField> listFields = new ArrayList<>();

        for (Field field : fields) {

            if (field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(FiTransient.class)) continue;

            FiField fiField = new FiField();
            listFields.add(setupFiFieldAll(field, fiField));
        }

        return listFields;

    }

    /**
     * fieldName, dbFieldName, ClassNameSimple alanlarını doldurur.
     *
     * @param field
     * @param fiField
     */
    public static void assignFiFieldBasic(Field field, FiField fiField) {

        fiField.setOfcTxFieldName(field.getName());
        if (fiField.getOfcTxDbFieldName() == null) fiField.setOfcTxDbFieldName(field.getName());
        fiField.setClassNameSimple(field.getType().getSimpleName());

        if (field.isAnnotationPresent(FiColumn.class)) {
            FiColumn annoFiColumn = field.getAnnotation(FiColumn.class);
            if (!FiString.isEmptyTrim(annoFiColumn.name())) {
                fiField.setOfcTxDbFieldName(annoFiColumn.name());
            }

            if (FiBool.isTrue(annoFiColumn.boFilterLike())) {
                fiField.setOfcBoFilterLike(true);
            }
        }


    }

    public static void assignFiFieldBasicForFiCol(Field field, FiCol fiCol) {

        fiCol.setOfcTxFieldName(field.getName());
        if (fiCol.getOfcTxDbFieldName() == null) fiCol.setOfcTxDbFieldName(field.getName());
        fiCol.setTxClassNameSimple(field.getType().getSimpleName());

        if (field.isAnnotationPresent(FiColumn.class)) {
            FiColumn annoFiColumn = field.getAnnotation(FiColumn.class);
            if (!FiString.isEmptyTrim(annoFiColumn.name())) {
                fiCol.setOfcTxDbFieldName(annoFiColumn.name());
            }

            if (FiBool.isTrue(annoFiColumn.boFilterLike())) {
                fiCol.setOfcBoFilterLike(true);
            }
        }


    }

    public static void assignFiFieldExtraRelatedDb(Field field, FiField fiField) {
        if (field.isAnnotationPresent(FiComment.class)) {
            FiComment column = field.getAnnotation(FiComment.class);
            if (!FiString.isEmpty(column.txComment())) {
                fiField.setTxComment(column.txComment());
            }
        }


    }

    public static void assignFiFieldExtraRelatedDb(Field field, FiCol fiField) {
        if (field.isAnnotationPresent(FiComment.class)) {
            FiComment column = field.getAnnotation(FiComment.class);
            if (!FiString.isEmpty(column.txComment())) {
                fiField.setColComment(column.txComment());
            }
        }


    }

    public static void assignFiFieldDatabase(Field field, FiField fiField) {

        if (field.isAnnotationPresent(Column.class)) {
            Column annoColumn = field.getAnnotation(Column.class);
            fiField.setOfcLnPrecision(annoColumn.precision());
            fiField.setOfcLnScale(annoColumn.scale());
            fiField.setOfcLnLength(annoColumn.length());
            fiField.setOfcBoUnique(annoColumn.unique());
            fiField.setOfcBoNullable(annoColumn.nullable()); // def:true (in interface)
            fiField.setColCustomType(annoColumn.columnDefinition());

            if (!FiString.isEmptyTrim(annoColumn.name())) {
                fiField.setOfcTxDbFieldName(annoColumn.name());
            } else {
                fiField.setOfcTxDbFieldName(field.getName());
            }

        }

        if (field.isAnnotationPresent(FiColumn.class)) {

            FiColumn annoFiColumn = field.getAnnotation(FiColumn.class);

            fiField.setOfcLnPrecision(annoFiColumn.precision());
            fiField.setOfcLnScale(annoFiColumn.scale());
            fiField.setOfcLnLength(annoFiColumn.length());
            fiField.setOfcBoUnique(annoFiColumn.isUnique());
            fiField.setOfcBoNullable(annoFiColumn.isNullable());
            //if (FiBoolean.isFalse(annoFiColumn.isNullable())) fiField.setNullable(false);
            fiField.setColCustomType(annoFiColumn.colCustomTypeDefinition());
            fiField.setOfcTxDefValue(annoFiColumn.defaultValue());
            fiField.setColDefinitionExtra(annoFiColumn.colDefinitionExtra());
            fiField.setOfcBoUtfSupport(annoFiColumn.isUnicodeSupport());
            fiField.setBoExcludeFromAutoColList(annoFiColumn.boExcludeFromAutoColList());

            if (!FiString.isEmpty(annoFiColumn.label())) fiField.setOfcTxHeader(annoFiColumn.label());
            if (annoFiColumn.collation() != FiCollation.Default) fiField.setFiCollation(annoFiColumn.collation());
            if (!FiString.isEmpty(annoFiColumn.typeName())) fiField.setOfcTxTypeName(annoFiColumn.typeName());
            if (annoFiColumn.defaultUpdateField()) fiField.setBoDefaultUpdateField(true);

            if (!FiString.isEmptyTrim(annoFiColumn.name())) {
                fiField.setOfcTxDbFieldName(annoFiColumn.name());
            } else {
                fiField.setOfcTxDbFieldName(field.getName());
            }

        }

        if (field.isAnnotationPresent(FiNotNull.class)) {
            fiField.setOfcBoNullable(false);
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
            fiField.setOfcBoTransient(true);
        }

        // Id alanlar için ortak tanımlamalar
        if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(FiId.class)) {
            fiField.setBoKeyIdField(true);
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
            fiField.setOfcBoNullable(false);
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

        if (field.isAnnotationPresent(FiWhere1.class)) {
            fiField.setBoFiWhere1(true);
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

//		if (field.isAnnotationPresent(FiSeperatedField.class)) {
//			fiField.setBoSeperatedField(true);
//		}

        if (field.isAnnotationPresent(FiFirmField.class)) {
            fiField.setBoFirmField(true);
        }

        if (field.isAnnotationPresent(FiDefaultUpdate.class)) {
            fiField.setBoDefaultUpdateField(true);

            FiDefaultUpdate anno = field.getAnnotation(FiDefaultUpdate.class);

//			if (!FiString.isEmpty(anno.typeName())) {
//				fiField.setTypeName(anno.typeName());
//			}
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

        if (field.isAnnotationPresent(FiDateSeperator.class)) {
            fiField.setBoDateSeperatorField(true);
        }

        if (field.isAnnotationPresent(FiUniqGroup1.class)) {
            fiField.setOfcBoUniqGro1(true);

            FiUniqGroup1 anno = field.getAnnotation(FiUniqGroup1.class);

            if (!FiString.isEmpty(anno.name())) {
                fiField.setTxUnique1Name(anno.name());
            }

        }

    }

    public static void assignFiFieldDatabaseForFiCol(Field field, FiCol fiField) {

        if (field.isAnnotationPresent(Column.class)) {
            Column annoColumn = field.getAnnotation(Column.class);
            fiField.setOfcLnPrecision(annoColumn.precision());
            fiField.setOfcLnScale(annoColumn.scale());
            fiField.setOfcLnLength(annoColumn.length());
            fiField.setOfcBoUnique(annoColumn.unique());
            fiField.setOfcBoNullable(annoColumn.nullable()); // def:true (in interface)
            //ColCustomType
            fiField.setOfcTxColDefinition(annoColumn.columnDefinition());

            if (!FiString.isEmptyTrim(annoColumn.name())) {
                fiField.setOfcTxDbFieldName(annoColumn.name());
            } else {
                fiField.setOfcTxDbFieldName(field.getName());
            }

        }

        if (field.isAnnotationPresent(FiColumn.class)) {

            FiColumn annoFiColumn = field.getAnnotation(FiColumn.class);

            fiField.setOfcLnPrecision(annoFiColumn.precision());
            fiField.setOfcLnScale(annoFiColumn.scale());
            fiField.setOfcLnLength(annoFiColumn.length());
            fiField.setOfcBoUnique(annoFiColumn.isUnique());
            fiField.setOfcBoNullable(annoFiColumn.isNullable());
            //if (FiBoolean.isFalse(annoFiColumn.isNullable())) fiField.setNullable(false);
            fiField.setOfcTxDefValue(annoFiColumn.colCustomTypeDefinition());
            fiField.setOfcTxDefValue(annoFiColumn.defaultValue());
            fiField.setOfcTxColDefinition(annoFiColumn.colDefinitionExtra());
            fiField.setOfcBoUtfSupport(annoFiColumn.isUnicodeSupport());
            //fiField.setBoExcludeFromAutoColList(annoFiColumn.boExcludeFromAutoColList());

            if (!FiString.isEmpty(annoFiColumn.label())) fiField.setOfcTxHeader(annoFiColumn.label());
            //if (annoFiColumn.collation() != FiCollation.Default) fiField.setFiCollation(annoFiColumn.collation());
            if (!FiString.isEmpty(annoFiColumn.typeName())) fiField.setOfcTxTypeName(annoFiColumn.typeName());
            //if (annoFiColumn.defaultUpdateField()) fiField.setBoDefaultUpdateField(true);

            if (!FiString.isEmptyTrim(annoFiColumn.name())) {
                fiField.setOfcTxDbFieldName(annoFiColumn.name());
            } else {
                fiField.setOfcTxDbFieldName(field.getName());
            }

        }

        if (field.isAnnotationPresent(FiNotNull.class)) {
            fiField.setOfcBoNullable(false);
        }

//        if (field.isAnnotationPresent(FiWhere1.class)) {
//            fiField.setBoWhere1(true);
//        }
//
//        if (field.isAnnotationPresent(Temporal.class)) {
//            fiField.setTemporalType(field.getAnnotation(Temporal.class).value());
//        }
//
//        if (field.isAnnotationPresent(ColDefinitionExtra.class)) {
//            fiField.setColDefinitionExtra(field.getAnnotation(ColDefinitionExtra.class).value());
//        }

        // other related db

        if (field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(FiTransient.class)) {
            fiField.setOfcBoTransient(true);
        }

        // Id alanlar için ortak tanımlamalar
//        if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(FiId.class)) {
//            fiField.setBoKeyIdField(true);
//            fiField.setBoDtoField(true);
//        }
//
//        // FiId ye özel bilgiler set edilir
//        if (field.isAnnotationPresent(FiId.class)) {
//            FiId anno = field.getAnnotation(FiId.class);
//            fiField.setIdGenerationType(anno.idGenerationType());
//        }


//        if (field.isAnnotationPresent(FiCandId1.class)) {
//            fiField.setBoCandidateId1(true);
//            //FIXME nullable false olmayabilir , çoklu candid ise
//            fiField.setOfcBoNullable(false);
//            fiField.setBoDtoField(true);
//        }
//
//        if (field.isAnnotationPresent(FiCandId2.class)) {
//            fiField.setBoCandidateId2(true);
//            fiField.setBoDtoField(true);
//        }
//
//        if (field.isAnnotationPresent(FiSelect2.class)) {
//            fiField.setBoFiSelect(true);
//        }
//
//        if (field.isAnnotationPresent(FiSelect1.class)) {
//            fiField.setBoFiSelect1(true);
//        }
//
//        if (field.isAnnotationPresent(FiWhere1.class)) {
//            fiField.setBoFiWhere1(true);
//        }
//
//        if (field.isAnnotationPresent(FiOnlyFirstInsert.class)) {
//            fiField.setBoOnlyFirstInsert(true);
//        }
//
//        if (field.isAnnotationPresent(FiScopeIdField.class)) {
//            fiField.setBoScopeIdField(true);
//        }
//
//        if (field.isAnnotationPresent(FiDto.class)) {
//            fiField.setBoDtoField(true);
//        }

//		if (field.isAnnotationPresent(FiSeperatedField.class)) {
//			fiField.setBoSeperatedField(true);
//		}

//        if (field.isAnnotationPresent(FiFirmField.class)) {
//            fiField.setBoFirmField(true);
//        }
//
//        if (field.isAnnotationPresent(FiDefaultUpdate.class)) {
//            fiField.setBoDefaultUpdateField(true);
//
//            FiDefaultUpdate anno = field.getAnnotation(FiDefaultUpdate.class);
//
////			if (!FiString.isEmpty(anno.typeName())) {
////				fiField.setTypeName(anno.typeName());
////			}
//        }

//        if (field.isAnnotationPresent(FiCusFieldUserId.class)) {
//            fiField.setBoCusFieldUserId(true);
//            fiField.setBoDefaultUpdateField(true);
//        }
//
//        if (field.isAnnotationPresent(FiCusFieldDtChange.class)) {
//            fiField.setBoCusFieldDtChange(true);
//            fiField.setBoDefaultUpdateField(true);
//        }
//
//        if (field.isAnnotationPresent(FiCusFieldDtCreate.class)) {
//            fiField.setBoCusFieldDtCreate(true);
//            fiField.setBoOnlyFirstInsert(true);
//        }
//
//        if (field.isAnnotationPresent(FiCombo.class)) {
//            fiField.setBoComboField(true);
//        }
//
//        if (field.isAnnotationPresent(FiInsertMaxPlus.class)) {
//            fiField.setBoInsertMaxPlus(true);
//        }
//
//        if (field.isAnnotationPresent(FiGuid.class)) {
//            fiField.setBoGuidField(true);
//        }
//
//        if (field.isAnnotationPresent(FiDateSeperator.class)) {
//            fiField.setBoDateSeperatorField(true);
//        }
//
//        if (field.isAnnotationPresent(FiUniqGroup1.class)) {
//            fiField.setOfcBoUniqGro1(true);
//
//            FiUniqGroup1 anno = field.getAnnotation(FiUniqGroup1.class);
//
//            if (!FiString.isEmpty(anno.name())) {
//                fiField.setTxUnique1Name(anno.name());
//            }
//
//        }

    }
    /**
     * Field ile ilgili tüm alanlar (extra hariç) doldurulur.
     *
     * @param field
     * @param fiField
     * @return
     */
    public static FiField setupFiFieldAll(Field field, FiField fiField) {
        if (fiField == null) fiField = new FiField();
        assignFiFieldBasic(field, fiField);
        assignFiFieldDatabase(field, fiField);
        return fiField;
    }

    public static FiCol setupFiColAll(Field field, FiCol fiField) {
        if (field == null) fiField = new FiCol();
        assignFiFieldBasicForFiCol(field, fiField);
        assignFiFieldDatabaseForFiCol(field, fiField);
        return fiField;
    }

    public static FiField setupFiFieldBasic(Field field, FiField fiField) {
        if (fiField == null) fiField = new FiField();
        assignFiFieldBasic(field, fiField);
        return fiField;
    }

    public static List<FiField> getListFieldsNotNull(Class clazz, Object objectt) {

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
                listFields.add(setupFiFieldAll(field, fiField));
            }

        }

        return listFields;

    }

    public static List<FiField> getListFieldsNotNullWithId(Class clazz, Object objectt) {

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
                listFields.add(setupFiFieldAll(field, fiField));
            }

        }

        return listFields;

    }

    public static List<FiField> getListFieldsNotNullWithCandId1(Class clazz, Object objectt) {

        List<FiField> listFields = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {

            if (field.isAnnotationPresent(Transient.class)) continue;
            if (field.isAnnotationPresent(FiTransient.class)) continue;
            // Static alanlar alınmaz
            if (Modifier.isStatic(field.getModifiers())) continue;

            Object fieldValue = FiReflection.getProperty(objectt, field.getName());

            if (fieldValue != null || field.isAnnotationPresent(FiCandId1.class)) {
                listFields.add(setupFiFieldAll(field, null));
            }
        }

        return listFields;

    }

    /**
     * Id alanlarının herhangi biri null olup olmadığını kontrol eder.
     * <p>
     * True ise id alanlarında herhangi biri null
     * <p>
     * False tüm id alanları null degil
     *
     * @param entity
     * @param entityClass
     * @param <T>
     * @return
     */
    public static <T> Boolean checkIdFieldsAnyNull(T entity, Class<T> entityClass) {

        List<FiField> listFiFieldsShort = getListFieldsShortWithId(entityClass);

        Boolean isNull = null;

        //listIdFields.forEach(fiField -> {
        for (FiField field : listFiFieldsShort) {

            if (FiBool.isTrue(field.getBoKeyIdField())) {
                //Loghelperr.staticLogDebug("Id Fiedl"+field.getName());
                Object idValue = FiReflection.getPropertyNested(entity, field.getOfcTxFieldName());

                if (isNull == null && idValue != null) isNull = false;
                if (idValue == null) isNull = true;

            }
        }

        return isNull;
    }

    public static <T> Boolean checkDtCreatedFieldsNull(T entity, Class<T> entityClass) {

        List<FiField> listFiFieldsShort = getListFieldsShortWithId(entityClass);

        Boolean isNull = null;

        //listIdFields.forEach(fiField -> {
        for (FiField field : listFiFieldsShort) {

            if (FiBool.isTrue(field.getBoCusFieldDtCreate())) {
                //Loghelperr.staticLogDebug("Id Fiedl"+field.getName());
                Object idValue = FiReflection.getPropertyNested(entity, field.getOfcTxFieldName());

                if (isNull == null && idValue != null) isNull = false;
                if (idValue == null) isNull = true;

            }
        }

        return isNull;
    }

    public static <T> Boolean assignIdFields(T fromEntity, T toEntity, Class<T> entityClass) {

        List<FiField> listFiFieldsShort = getListFieldsShortWithId(entityClass);

        //Stream<FiField> listIdFields = listFiFieldsShort.stream().filter(fiField -> FiBoolean.isTrue(fiField.getBoIdField()));

        Boolean boResult = null;

        //listIdFields.forEach(fiField -> {
        for (FiField field : listFiFieldsShort) {

            if (FiBool.isTrue(field.getBoKeyIdField())) {
                Loghelper.get(FiReflectClass.class).debug("Id Field:" + field.getOfcTxFieldName());
                Object idValue = FiReflection.getPropertyNested(fromEntity, field.getOfcTxFieldName());
                boResult = FiReflection.setterNested(toEntity, field.getOfcTxFieldName(), idValue);
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

        for (FiField field : getListFieldsShortWithId(entityClazz)) {

            if (FiBool.isTrue(field.getBoKeyIdField())) {
                //Loghelperr.staticLogDebug("Id Field:"+field.getName());
                listIdFields.add(field.getOfcTxFieldName());
            }
        }

        return listIdFields;
    }

    public static List<String> getListDbFieldName(List<FiField> fieldListFilterAnno) {
        return fieldListFilterAnno.stream().map(fiField -> fiField.getOfcTxDbFieldName()).collect(toList());
    }

    public static List<String> getListFieldName(List<FiField> fieldListFilterAnno) {
        return fieldListFilterAnno.stream().map(fiField -> fiField.getOfcTxFieldName()).collect(toList());
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

            if (FiReflection.getProperty(entity, fiField.getOfcTxFieldName()) == null) {
                fiField.setNullCheck(true);
                found = true;
            }

        }

        return found;

    }

    public static List<FiCol> getListFiCol(Class entityClass) {
        return FiCol.convertListFiField(getListFieldsWoutStatic(entityClass));
    }

    public static List<FiCol> getListFiColWithFiColManual(Class entityClass) {
        return FiCol.convertListFiField(getListFieldsWithFiColManualWoutStaticMain(entityClass, false, null));
    }

    public static List<FiCol> getListFiColWithTransient(Class entityClass) {
        return FiCol.convertListFiField(getListFieldsWoutStatic(entityClass, true));
    }
}
