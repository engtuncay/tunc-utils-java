package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.annotations.FiDraft;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.datatypes.FiListString;
import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.entitysql.EntSqlColumn;
import ozpasyazilim.utils.gui.fxcomponents.FxEditorFactory;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.fidbanno.*;
import ozpasyazilim.utils.db.TableScheme;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.repoSql.RepoSqlColumn;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Not : Normal şartlarda transient alanlar eklenmez, özel olarak belirtmek lazım
 * <p>
 * Fiqugen -> FiQueryGenerator kısaltması
 */
public class FiQugen {

    public static String deleteById(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("DELETE " + tableName);

        //Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoIdField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = @" + fiField.getName());
                continue;
            }

        }

        query.append("\n WHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();
    }

    public static String deleteByCandId1(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("DELETE " + tableName);

        //Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = @" + fiField.getName());
                continue;
            }

        }

        query.append("\n WHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();
    }

    public static String deleteByCandId2(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("DELETE " + tableName);

        //Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoCandidateId2())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = @" + fiField.getName());
                continue;
            }

        }

        query.append("\n WHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();
    }

    public static String deleteByCandId2ByInFormat(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("DELETE " + tableName);

        //Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoCandidateId2())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " IN ( @" + fiField.getName() + " )");
                continue;
            }

        }

        query.append("\n WHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();
    }

    public static String selectQuery20ById_oldway(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        //String idField = getIdFieldSingle(fieldListFilterAnno);

        //if (idField == null) return null;

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoIdField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = :" + fiField.getName());
            }

            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getName());  // + " = :" + fiField.getName());

        }
        query.append("\nFROM " + tableName);
        query.append("\nWHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            //query = new StringBuilder();
        }

        //query.append("\nWHERE " + idField + "=:idvalue");

        return query.toString();
    }

    public static String selectQuery20ByIdNew(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        //String idField = getIdFieldSingle(fieldListFilterAnno);

        //if (idField == null) return null;

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoIdField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = @" + fiField.getName());
            }

            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getName());  // + " = :" + fiField.getName());

        }
        query.append("\nFROM " + tableName);
        query.append("\nWHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            //query = new StringBuilder();
        }

        //query.append("\nWHERE " + idField + "=:idvalue");

        return query.toString();
    }

    public static String selectQueryByCandIds(Class clazz) {
        return selectQueryByCandIds1Fi(clazz).getTxQuery();
    }

    public static FiQuery selectQueryByCandIds1Fi(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);
        FiQuery fiQuery = new FiQuery();

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = @" + fiField.getName());
                fiQuery.setTxCandIdFieldName(fiField.getDbFieldName());
            }

            index++;
            if (index != 1) query.append(", ");

            query.append(fiField.getDbFieldNameOrName());
        }
        query.append("\nFROM " + tableName);
        query.append("\nWHERE " + queryWhere);

        fiQuery.setTxQuery(query.toString());
        return fiQuery;
    }

    public static FiQuery selectQueryByCandIds2(Class clazz) {

        List<FiField> selectFieldList = FiEntity.getListFieldsShortWithId(clazz);
        List<FiField> fieldsWhere = new ArrayList<>();

        FiQuery fiQuery = new FiQuery();

        for (FiField fiField : selectFieldList) {
            if (FiBool.isTrue(fiField.getBoCandidateId2())) fieldsWhere.add(fiField);
        }

        fiQuery.setTxQuery(genSelectQuery(selectFieldList, fieldsWhere, clazz));

        return fiQuery;

//		List<FiField> fieldListFilterAnno = FiEntityHelper.getListFiFieldsShortWithId(clazz);
//		FiQuery fiQuery = new FiQuery();
//
//		String tableName = getTableName(clazz);
//
//		StringBuilder query = new StringBuilder();
//		StringBuilder queryWhere = new StringBuilder();
//
//		query.append("SELECT ");
//
//		Integer index = 0;
//		Integer indexWhere = 0;
//		for (FiField fiField : fieldListFilterAnno) {
//
//			if (FiBoolean.isTrue(fiField.getBoCandidateId2())) {
//				indexWhere++;
//				if (indexWhere != 1) queryWhere.append(" AND ");
//				queryWhere.append(fiField.getName() + " = @" + fiField.getName());
//				fiQuery.setTxCandIdFieldName(fiField.getDbFieldName());
//			}
//
//			index++;
//			if (index != 1) query.append(", ");
//
//			query.append(fiField.getDbFieldNameOrName());
//		}
//		query.append("\nFROM " + tableName);
//		query.append("\nWHERE " + queryWhere);
//
//		fiQuery.setTxQuery(query.toString());
//		return fiQuery;
    }


    private static String genSelectQuery(List<FiField> selectFieldList, List<FiField> fieldsWhere, Class clazz) {

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        Integer index = 0;
        for (FiField fiField : selectFieldList) {
            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getDbFieldNameOrName());
        }

        Integer indexWhere = 0;
        for (FiField fiField : fieldsWhere) {
            indexWhere++;
            if (indexWhere != 1) queryWhere.append(" AND ");
            queryWhere.append(fiField.getName() + " = @" + fiField.getName());
        }

        query.append("\nFROM " + tableName);
        query.append("\nWHERE " + queryWhere);

        //fiQuery.setTxQuery(query.toString());
        return query.toString();
    }

    public static String selectAllDtoByFiSelectFields(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        //String idField = getIdFieldSingle(fieldListFilterAnno);
        //if (idField == null) return null;

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoFiSelect())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = @" + fiField.getName());
            }

            if (FiBool.isTrue(fiField.getBoDtoField())) {
                index++;
                if (index != 1) query.append(", ");
                query.append(fiField.getName());  // + " = :" + fiField.getName());
            }

        }
        query.append("\nFROM " + tableName);
        query.append("\nWHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            //query = new StringBuilder();
        }

        //query.append("\nWHERE " + idField + "=:idvalue");

        return query.toString();
    }

    public static String selectAllDtoByFiWhere1(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        //String idField = getIdFieldSingle(fieldListFilterAnno);
        //if (idField == null) return null;

        StringBuilder sbSelectQuery = selectAllDto(clazz, fieldListFilterAnno);

        StringBuilder queryWhere = new StringBuilder();

        int indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {
            if (FiBool.isTrue(fiField.getBoFiWhere1())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName()).append(" = @").append(fiField.getName());
            }
        }

        sbSelectQuery.append("\nWHERE ").append(queryWhere);

        if (queryWhere.length() < 1) {
            sbSelectQuery = new StringBuilder();
        }

        return sbSelectQuery.toString();
    }

    public static StringBuilder selectAllDto(Class clazz, List<FiField> fieldListFilterAnno) {

        //String idField = getIdFieldSingle(fieldListFilterAnno);
        //if (idField == null) return null;

        String tableName = getTableName(clazz);
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");

        int index = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoDtoField())) {
                index++;
                if (index != 1) query.append(", ");
                query.append(fiField.getName());  // + " = :" + fiField.getName());
            }
        }

        query.append("\nFROM " + tableName);
        return query;
    }

    public static String selectAllByFiSelectFields(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        //String idField = getIdFieldSingle(fieldListFilterAnno);

        //if (idField == null) return null;

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoFiSelect())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = @" + fiField.getName());
            }
            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getName());  // + " = :" + fiField.getName());
        }
        query.append("\nFROM " + tableName);
        query.append("\nWHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            //query = new StringBuilder();
        }

        //query.append("\nWHERE " + idField + "=:idvalue");

        return query.toString();
    }

    public static String select1FieldsByCandId(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        int index = 0;
        int indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = :" + fiField.getName());
            }

            if (FiBool.isNotTrue(fiField.getBoFiSelect1())) continue;

            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getName());  // + " = :" + fiField.getName());

        }

        query.append("\nFROM " + tableName);
        query.append("\nWHERE " + queryWhere);

        return query.toString();
    }

    public static String selectQuery20ByIdFiSelect(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldList) {

            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = :" + fiField.getName());
                continue;
            }

            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getName());  // + " = :" + fiField.getName());

        }
        query.append("\nFROM " + tableName);
        query.append("\nWHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            //query = new StringBuilder();
        }

        return query.toString();
    }


    public static String selectQueryFiSelect(Class clazz, Integer rowCount) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        String txTop = "";

        if (rowCount != null) {
            txTop = "TOP " + rowCount + " ";
        }
        query.append("SELECT " + txTop);


        Integer index = 0;
        for (FiField fiField : fieldList) {

            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getName());  // + " = :" + fiField.getName());

        }

        query.append("\nFROM " + tableName);

        return query.toString();
    }

    /**
     * select1 alanına göre select sorgusu
     *
     * @param clazz
     * @param rowCount
     * @return
     */
    public static String selectQueryFiSelect1WithTop(Class clazz, Integer rowCount) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        String txTop = "";

        if (rowCount != null) {
            txTop = "TOP " + rowCount + " ";
        }

        query.append("SELECT " + txTop);

        int index = 0;
        for (FiField fiField : fieldList) {

            if (FiBool.isNotTrue(fiField.getBoFiSelect1())) continue;

            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getName());  // + " = :" + fiField.getName());

        }

        query.append("\nFROM " + tableName);

        return query.toString();
    }

    public static String selectTopQuery(Class clazz, Integer rowCount) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);
        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        String txTop = "";

        if (rowCount != null) {
            txTop = "TOP " + rowCount + " ";
        }
        query.append("SELECT " + txTop);

        Integer index = 0;

        for (FiField fiField : fieldList) {
            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getName());
        }
        query.append("\nFROM " + tableName);
        return query.toString();
    }

    public static String selectTopQueryLike(Class clazz, Integer rowCount, Object objectt) {

        List<FiField> fieldList = FiEntity.getListFieldsWoutStatic(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        String txTop = "";

        if (rowCount != null) {
            txTop = "TOP " + rowCount + " ";
        }
        query.append("SELECT " + txTop);

        Integer index = 0;


        for (FiField fiField : fieldList) {

            if (FiBool.isTrue(fiField.getTransient())) continue;

            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getName());

        }

        query.append("\nFROM " + tableName);

        Integer indexWhere = 0;
        if (objectt != null) {

            for (FiField fiField : fieldList) {

                if (FiBool.isTrue(fiField.getTransient())) continue;

                Object fieldValue = FiReflection.getProperty(objectt, fiField.getName());

                if (fieldValue != null) {
                    indexWhere++;
                    if (indexWhere != 1) queryWhere.append("AND ");
                    queryWhere.append(String.format("%s = @%s", fiField.getName(), fiField.getName()));
                }

            }
        }

        if (queryWhere.length() > 0) {
            query.append("\n WHERE " + queryWhere);
        }

        return query.toString();
    }

    public static String selectQueryCountWherAllFields(Class clazz, Object objectt) {

        List<FiField> fieldList = FiEntity.getListFieldsWoutStatic(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        //Integer index = 0;
        for (FiField fiField : fieldList) {

            if (FiBool.isTrue(fiField.getBoIdField())) {
                query.append(String.format(" count(%s) ", fiField.getName()));
                break;
            }

        }

        query.append("\nFROM " + tableName);

        Integer indexWhere = 0;
        if (objectt != null) {

            for (FiField fiField : fieldList) {

                Object fieldValue = FiReflection.getProperty(objectt, fiField.getName());

                if (fieldValue != null) {
                    indexWhere++;
                    if (indexWhere != 1) queryWhere.append(" AND ");
                    queryWhere.append(String.format("%s = @%s", fiField.getName(), fiField.getName()));
                }

            }
        }

        if (queryWhere.length() > 0) {
            query.append("\n WHERE " + queryWhere);
        }

        Loghelper.get(FiQugen.class).debug(" Query:" + query.toString());

        return query.toString();
    }

    public static String selectDtoFieldsByCandId1(Class clazz) {
        return selectDtoEntityByCandId1Fi(clazz).getTxQuery();
    }

    /**
     * ÖRnek
     * SELECT afrLnDurum, afrTxAktarimFirmaAdi, afrTxAktarimKlasoru, afrTxSorMerKod, afrTxEvrakSeri
     * FROM EntAktarimFirma
     * WHERE candId1Fields = @candId1Fields
     *
     * @param clazz
     * @return
     */
    public static FiQuery selectDtoEntityByCandId1Fi(Class clazz) {

        FiQuery fiQuery = new FiQuery();
        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldList) {

            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" and ");
                queryWhere.append(String.format("%s = @%s", fiField.getName(), fiField.getName()));
                fiQuery.setTxCandIdFieldName(fiField.getDbFieldName());
            }

            if (FiBool.isTrue(fiField.getBoDtoField()) || FiBool.isTrue(fiField.getBoIdField())
                    || FiBool.isTrue(fiField.getBoCandidateId1())) {
                index++;
                if (index != 1) query.append(", ");
                query.append(fiField.getName());
            }

        }

        query.append("\nFROM " + tableName);

        if (queryWhere.length() > 0) {
            query.append("\nWHERE " + queryWhere.toString());
        }
        fiQuery.setTxQuery(query.toString());
        return fiQuery;
    }

    public static String selectIdByGuid(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldList) {

            if (FiBool.isTrue(fiField.getBoGuidField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" and ");
                queryWhere.append(String.format("%s = @%s", fiField.getName(), fiField.getName()));
            }

            if (FiBool.isTrue(fiField.getBoIdField())) {
                index++;
                if (index != 1) query.append(", ");
                query.append(fiField.getName());
            }

        }

        query.append("\nFROM " + tableName);

        if (queryWhere.length() > 0) {
            query.append("\nWHERE " + queryWhere.toString());
        }

        return query.toString();
    }

    /**
     * ÖRnek
     * SELECT afrLnDurum, afrTxAktarimFirmaAdi, afrTxAktarimKlasoru, afrTxSorMerKod, afrTxEvrakSeri
     * FROM EntAktarimFirma
     * WHERE afrTxFirmaKod = @afrTxFirmaKod
     *
     * @param clazz
     * @return
     */
    public static String selectDtoFieldsBySeperatedField(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldList) {

            if (FiBool.isTrue(fiField.getBoFirmField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" and ");
                queryWhere.append(String.format("%s = @%s", fiField.getName(), fiField.getName()));
            }

            if (FiBool.isTrue(fiField.getBoDtoField()) || FiBool.isTrue(fiField.getBoIdField())
                    || FiBool.isTrue(fiField.getBoCandidateId1())) {
                index++;
                if (index != 1) query.append(", ");
                query.append(fiField.getName());
            }

        }

        query.append("\nFROM " + tableName);

        if (queryWhere.length() > 0) {
            query.append("\nWHERE " + queryWhere.toString());
        }

        return query.toString();
    }

    public static String selectDtoFieldsOrderByIdField(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        //StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        String idField = "";
        int index = 0;
        for (FiField fiField : fieldList) {

            if (FiBool.isTrue(fiField.getBoDtoField()) || FiBool.isTrue(fiField.getBoIdField())
                    || FiBool.isTrue(fiField.getBoCandidateId1())) {
                index++;
                if (index != 1) query.append(", ");
                query.append(fiField.getName());
            }

            if (FiBool.isTrue(fiField.getBoIdField())) {
                idField = fiField.getDbFieldNameOrName();
            }

        }

        query.append("\nFROM ").append(tableName);

        if (!FiString.isEmptyTrim(idField)) {
            query.append("\nORDER BY ").append(idField);
        }

        return query.toString();
    }

    public static String selectDtoFieldsWoutWhereOrderByCandIdField(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        //StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        String idField = "";
        int index = 0;
        for (FiField fiField : fieldList) {

            if (FiBool.isTrue(fiField.getBoDtoField()) || FiBool.isTrue(fiField.getBoIdField())
                    || FiBool.isTrue(fiField.getBoCandidateId1())) {
                index++;
                if (index != 1) query.append(", ");
                query.append(fiField.getName());
            }

            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                idField = fiField.getDbFieldNameOrName();
            }

        }

        query.append("\nFROM ").append(tableName);

        if (!FiString.isEmptyTrim(idField)) {
            query.append("\nORDER BY ").append(idField);
        }

        return query.toString();
    }

    public static String selectDtoFieldsWithWhere1(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldList) {

            if (FiBool.isTrue(fiField.getBoWhere1())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" and ");
                queryWhere.append(String.format("%s = @%s", fiField.getName(), fiField.getName()));
            }

            if (FiBool.isTrue(fiField.getBoDtoField()) || FiBool.isTrue(fiField.getBoIdField())
                    || FiBool.isTrue(fiField.getBoCandidateId1())) {
                index++;
                if (index != 1) query.append(", ");
                query.append(fiField.getName());
            }

        }

        query.append("\nFROM " + tableName);
        query.append("\nWHERE " + queryWhere.toString());

        return query.toString();
    }

    /**
     * select {dto_fields} where {firm_fields}
     *
     * @param clazz
     * @return
     */
    public static String selectDtoFieldsByFirmFields(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        Integer indexSelect = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldList) {

            if (FiBool.isTrue(fiField.getBoDtoField()) || FiBool.isTrue(fiField.getBoIdField())
                    || FiBool.isTrue(fiField.getBoCandidateId1())) {
                indexSelect++;
                if (indexSelect != 1) query.append(", ");
                query.append(fiField.getName());
            }

            if (FiBool.isTrue(fiField.getBoFirmField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" and ");
                queryWhere.append(String.format("%s = @%s", fiField.getName(), fiField.getName()));
            }

        }

        query.append("\nFROM " + tableName);
        query.append("\nWHERE " + queryWhere.toString());

        return query.toString();
    }

    public static String selectAllDtoWherFiCols(Class clazz, List<FiCol> fiColsWhere) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        int indexSelect = 0;
        for (FiField fiField : fieldList) {

            if (FiBool.isTrue(fiField.getBoDtoField()) || FiBool.isTrue(fiField.getBoIdField())
                    || FiBool.isTrue(fiField.getBoCandidateId1())) {
                indexSelect++;
                if (indexSelect != 1) query.append(", ");
                query.append(fiField.getName());
            }
        }

        query.append("\nFROM ").append(tableName);

        int indexWhere = 0;
        for (FiCol fiCol : fiColsWhere) {
            indexWhere++;
            if (indexWhere != 1) queryWhere.append(" AND ");
            queryWhere.append(fiCol.getTxDbFieldNameOrFieldName())
                    .append(" = @")
                    .append(fiCol.getFieldName());
        }

        query.append("\nWHERE ").append(queryWhere);

        return query.toString();
    }

    public static String selectComboFieldsBySeperatedField(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldList) {

            if (FiBool.isTrue(fiField.getBoFirmField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" and ");
                queryWhere.append(String.format("%s = @%s", fiField.getName(), fiField.getName()));
            }

            if (FiBool.isTrue(fiField.getBoComboField())) {
                index++;
                if (index != 1) query.append(", ");
                query.append(fiField.getName());
            }

        }

        query.append("\nFROM " + tableName);

        if (queryWhere.length() > 0) {
            query.append("\nWHERE " + queryWhere.toString());
        }

        return query.toString();
    }

    public static String selectQueryCountByCandIdFirst(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsWoutStatic(clazz);

        String candId1FirstField = getCandId1FirstField(fieldList);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");
        query.append(String.format(" count(%s) ", candId1FirstField));
        query.append("\nFROM " + tableName);
        queryWhere.append(String.format("%s = @%s", candId1FirstField, candId1FirstField));
        query.append("\n WHERE " + queryWhere);

        if (queryWhere.length() == 0) {
            queryWhere.append("where clause is not generated. Check Query Helper !!!");
        }

        //Loghelperr.getInstance(FiQueryHelper.class).debug(" Query:" + query.toString());

        return query.toString();
    }

    public static String selectQueryCountByCandId1s(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsWoutStatic(clazz);

        String candId1FirstField = getCandId1FirstField(fieldList);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");
        query.append(String.format(" count(%s) ", candId1FirstField));
        query.append("\nFROM " + tableName);

        int index = 0;

        for (FiField fiField : fieldList) {

            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                if (index > 0) {
                    queryWhere.append(" AND ");
                }
                queryWhere.append(fiField.getDbFieldName() + " = @" + fiField.getDbParamName());
                index++;
            }

        }
        //queryWhere.append(String.format("%s = @%s", candId1FirstField, candId1FirstField));
        query.append("\n WHERE " + queryWhere);

        if (queryWhere.length() == 0) {
            queryWhere.append("where clouse is not generated. Check Query Helper !!!");
        }

        //Loghelperr.getInstance(FiQueryHelper.class).debug(" Query:" + query.toString());

        return query.toString();
    }

    @FiDraft // candidfirst and candid lere çekilecek where şartı içi multiple olacak
    @Deprecated
    public static String selectQueryCountByCandId(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsWoutStatic(clazz);

        String candId1FirstField = getCandId1FirstField(fieldList);

        String tableName = getTableName(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("SELECT ");
        query.append(String.format(" count(%s) ", candId1FirstField));
        query.append("\nFROM " + tableName);
        queryWhere.append(String.format("%s = @%s", candId1FirstField, candId1FirstField));
        query.append("\n WHERE " + queryWhere);

        if (queryWhere.length() == 0) {
            queryWhere.append("where clouse is not generated. Check Query Helper !!!");
        }

        //Loghelperr.getInstance(FiQueryHelper.class).debug(" Query:" + query.toString());

        return query.toString();
    }


    /**
     * Generate insert query without id
     *
     * @param clazz
     * @return
     */
    @Deprecated
    public static String insertQueryJParamWoutId(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithNotID(clazz);

        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO " + getTableName(clazz) + " ( ");

        query.append(String.join(", ", FiEntity.getListDbFieldName(fieldListFilterAnno)));

        query.append(" ) VALUES ( ");

        query.append(String.join(", ", getListSqlParam(fieldListFilterAnno)));

        query.append(" )");

        return query.toString();

    }

    /**
     * Generate insert query without id
     *
     * @param clazz
     * @return
     */
    public static String insertQueryWoutId(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithNotID(clazz);

        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO " + getTableName(clazz) + " ( ");

        query.append(String.join(", ", FiEntity.getListDbFieldName(fieldListFilterAnno)));

        query.append(" ) VALUES ( ");

        query.append(String.join(", ", getListDbSqlParamWithAt(fieldListFilterAnno)));

        query.append(")");

        return query.toString();

    }

    public static String insertQueryWoutIdWithFieldName(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithNotID(clazz);

        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO " + getTableName(clazz) + " ( ");

        query.append(String.join(", ", FiEntity.getListDbFieldName(fieldListFilterAnno)));

        query.append(" ) VALUES ( ");

        query.append(String.join(", ", getListDbSqlParamWithAtAndFieldName(fieldListFilterAnno)));

        query.append(")");

        return query.toString();

    }

    public static String insertQueryWoutIdWithMaxFields(Class clazz) {

//		-- INSERT INTO Person
//		--     (no, name)
//		-- SELECT
//				--     (SELECT MAX(no)+1  FROM PERSON), @name

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithNotID(clazz);
        StringBuilder query = new StringBuilder();
        String tableName = getTableName(clazz);
        query.append("INSERT INTO " + tableName + " ( ");
        query.append(String.join(", ", FiEntity.getListDbFieldName(fieldListFilterAnno)));
        query.append(" ) SELECT ");

        Integer index = 0;

        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoIdField())) {
                continue;
            }

            index++;
            if (index != 1) query.append(", ");

            if (FiBool.isTrue(fiField.getBoInsertMaxPlus())) {
                //(SELECT MAX(no)+1  FROM PERSON)
                query.append(String.format("(SELECT MAX(%s)+1 FROM %s)", fiField.getName(), tableName));
                continue;
            }

            query.append("@" + fiField.getName());

        }

        //query.append(" )");

        return query.toString();

    }

    /**
     * Generate insert query without id , adds db name to the query
     *
     * @param clazz
     * @return
     */
    public static String insertQueryRtDbName(Class clazz, IRepoJdbi iRepoJdbi) {

        List<FiField> listField = FiEntity.getListFieldsShortWithNotID(clazz);

        StringBuilder query = new StringBuilder();

        query.append(String.format("INSERT INTO %s.dbo.%s ( ", iRepoJdbi.getDatabaseName(), getTableName(clazz)));

        query.append(String.join(", ", FiEntity.getListDbFieldName(listField)));

        query.append(" ) VALUES ( ");

        query.append(String.join(", ", getListSqlParam(listField)));

        query.append(" )");

        return query.toString();

    }

    /**
     * Generate insert query with id fields
     *
     * @param clazz
     * @return
     */
    public static String insertQueryWithId(Class clazz) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO " + getTableName(clazz) + " ( ");

        query.append(String.join(", ", FiEntity.getListDbFieldName(fieldListFilterAnno)));

        query.append(" ) VALUES ( ");

        query.append(String.join(", ", getListSqlParam(fieldListFilterAnno)));

        query.append(" )");

        return query.toString();

    }

    public static String updateQueryNotNullFieldsWoutIdFieldByCandId1(Class clazz, Object objectt) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsNotNullWithCandId1(clazz, objectt);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("UPDATE " + getTableName(clazz) + " SET ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = :" + fiField.getName());
                continue;
            }

            if (FiBool.isTrue(fiField.getBoIdField())) {
                continue;
            }

            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getName() + " = :" + fiField.getName());

        }

        query.append(" WHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            //query = new StringBuilder();
            return null;
        }

        return query.toString();
    }

    /**
     * @param clazz
     * @param includeOnlyFirstInsertFields First Insert de sadece kayıt edilecek alanlar (mesela dtCreated)
     * @return
     */
    public static String updateQueryByCandId1(Class clazz, Boolean includeOnlyFirstInsertFields) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("UPDATE " + getTableName(clazz) + " SET ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getDbFieldName() + " = :" + fiField.getDbParamName());
                continue;
            }

            if (!FiBool.isTrue(includeOnlyFirstInsertFields)) {
                if (FiBool.isTrue(fiField.getBoOnlyFirstInsert())) {
                    continue;
                }
            }

            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getName() + " = :" + fiField.getName());

        }

        query.append(" WHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();

    }

    public static String updateQuery20NotNullById(Class clazz, Object objectt) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsNotNullWithId(clazz, objectt);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("UPDATE " + getTableName(clazz) + " SET ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoIdField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = @" + fiField.getDbFieldName());
                continue;
            }

            if (FiBool.isTrue(fiField.getBoIdField())) continue;

            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getName() + " = @" + fiField.getDbParamName());

        }

        query.append(" WHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();

    }


    /**
     * Key alanlarını class alanlarındaki Id annotasyonuna göre alır
     *
     * @param clazz
     * @param listFields
     * @return
     */
    public static String updateQueryWithFiColsByIdFieldInClass(Class clazz, List<? extends IFiCol> listFields) {

        List<FiField> listClassFields = FiEntity.getListFieldsShortWithId(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("UPDATE " + getTableName(clazz) + " SET ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (IFiCol fiTableCol : listFields) {
            index++;
            if (index != 1) query.append(", ");
            query.append(fiTableCol.getFieldName() + " = @" + fiTableCol.getFieldName());
        }

        for (FiField fiField : listClassFields) {
            if (FiBool.isTrue(fiField.getBoIdField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = @" + fiField.getName());
                continue;
            }
        }

        query.append(" WHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();

    }

    /**
     * * FiTableCol a göre update sorgusu
     * * Id field ı update sorgusu içine yazmaz, where içine yazar
     * * DEfault update alanlarını sorgu içerisine ekler
     *
     * @param clazz
     * @param listFields
     * @return
     */
    public static String updateFiColListAndExtraWhereIdFiCols(Class clazz, List<? extends IFiCol> listFields) {

        Map<String, FiField> listClassFields = FiEntity.getMapFieldsShort(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("UPDATE " + getTableName(clazz) + " SET ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (IFiCol fiTableCol : listFields) {

            // id field dahil edilmez
            if (FiBool.isTrue(listClassFields.getOrDefault(fiTableCol.getFieldName(), new FiField()).getBoIdField())) {
                continue;
            }

            // non updatable alanlar dahil edilmez
            if (FiBool.isTrue(fiTableCol.getBoNonUpdatable())) {
                continue;
            }

            index++;
            if (index != 1) query.append(", ");
            query.append(fiTableCol.getFieldName() + " = @" + fiTableCol.getFieldName());
        }

        for (FiField fiField : listClassFields.values()) {

            if (FiBool.isTrue(fiField.getBoIdField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = @" + fiField.getName());
                continue;
            }

            if (FiBool.isTrue(fiField.getBoDefaultUpdateField())) {
                index++;
                if (index != 1) query.append(", ");
                query.append(fiField.getName() + " = @" + fiField.getName());
            }

        }

        query.append(" WHERE " + queryWhere);

        // where cümleciği yoksa sorguyu iptal edelim
        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();

    }

    /**
     * FiColList a göre update sorgusu
     * <p>
     * Id field ı update sorgusu içine yazmaz, where içine yazar
     *
     * @param clazz
     * @param listFields
     * @return
     */
    public static String updateQueryWithFiColListWhereIdFiCols(Class clazz, List<FiCol> listFields) {

        //Map<String, FiField> listClassFields = FiEntity.getMapFieldsShort(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("UPDATE ").append(getTableName(clazz)).append(" SET ");

        int index = 0;

        for (FiCol fiTableCol : listFields) {

            // non updatable alanlar dahil edilmez
            if (FiBool.isTrue(fiTableCol.getBoNonUpdatable())) {
                continue;
            }

            if (FiBool.isTrue(fiTableCol.getBoKeyField())) {
                continue;
            }

            index++;
            if (index != 1) query.append(", ");
            query.append(fiTableCol.getFieldName()).append(" = @").append(fiTableCol.getFieldName());

        }

        int indexWhere = 0;
        for (FiCol fiTableCol : listFields) {

            if (FiBool.isTrue(fiTableCol.getBoKeyField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiTableCol.getTxDbFieldNameOrFieldName()).append(" = @").append(fiTableCol.getFieldName());
                continue;
            }

        }


        query.append(" WHERE ").append(queryWhere);

        // where cümleciği yoksa sorguyu iptal edelim
        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();
    }

    /**
     * FiCol a göre update sorgusu
     * <p>
     * FiCol.Id Field olanları update sorgusu içine yazmaz, where içine yazar (IN operatörü kullanır !!!)
     * <p>
     * Diger ficol'ları update alanları olarak ekler (BoNonUpdatable true olmayacak)
     *
     * @param clazz
     * @param listFields
     * @return
     */
    public static String updateFiColsWhereINKeyFiCols(Class clazz, List<FiCol> listFields) {

        //Map<String, FiField> listClassFields = FiEntity.getMapFieldsShort(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("UPDATE ").append(getTableName(clazz)).append(" SET ");

        int index = 0;
        int indexWhere = 0;
        for (FiCol fiCol : listFields) {

            // id field dahil edilmez
            if (FiBool.isTrue(fiCol.getBoKeyField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiCol.getFieldName()).append(" IN ( @").append(fiCol.getFieldName()).append(" )");
                continue;
            }

            // non updatable alanlar dahil edilmez
            if (FiBool.isTrue(fiCol.getBoNonUpdatable())) {
                continue;
            }

            index++;
            if (index != 1) query.append(", ");
            query.append(fiCol.getFieldName()).append(" = @").append(fiCol.getFieldName());
        }

        query.append(" WHERE ").append(queryWhere);

        // where cümleciği yoksa sorguyu iptal edelim
        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();
    }

    /**
     * FiColList a göre update sorgusu
     * <p>
     * FiCol.KeyField olanları update sorgusu içine yazmaz, where içine yazar ( = operatörü kullanılır )
     * <p>
     * Diger ficol'ları update alanları olarak ekler (BoUpdatable false olmayacak)
     *
     * @param clazz
     * @param listFields
     * @return
     */
    public static String updateFiColsWhereKeyFiCols(Class clazz, List<FiCol> listFields) {

        //Map<String, FiField> listClassFields = FiEntity.getMapFieldsShort(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("UPDATE ").append(getTableName(clazz)).append(" SET ");

        int index = 0;
        int indexWhere = 0;
        for (FiCol fiCol : listFields) {

            // id field dahil edilmez
            if (FiBool.isTrue(fiCol.getBoKeyField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiCol.getFieldName()).append(" = @").append(fiCol.getFieldName());
                continue;
            }

            // non updatable alanlar dahil edilmez
            if (FiBool.isTrue(fiCol.getBoNonUpdatable())) {
                continue;
            }

            index++;
            if (index != 1) query.append(", ");
            query.append(fiCol.getFieldName()).append(" = @").append(fiCol.getFieldName());
        }

        query.append(" WHERE ").append(queryWhere);

        // where cümleciği yoksa sorguyu iptal edelim
        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();
    }

    /**
     * Örnek Sorgu çıktısı
     * <p>
     * SELECT cha_RECno, count( cha_RECno )
     * <p>
     * FROM EnmCariHareketEk
     * <p>
     * WHERE cha_RECno IN ( @cha_RECno )
     *
     * @param clazz
     * @param listFields
     * @return
     */
    public static String selectQueryCountIdByFiColListWhereIdList(Class clazz, List<FiCol> listFields) {

        //Map<String, FiField> listClassFields = FiEntity.getMapFieldsShort(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();
        StringBuilder queryGroupBy = new StringBuilder();

        query.append("SELECT ");

        FiCol fiColId = null;
        Integer index = 0;
        for (FiCol fiCol : listFields) {
            index++;
            if (index != 1) query.append(", ");
            query.append(fiCol.getFieldName());
            // idCol kayıt edilir
            if (FiBool.isTrue(fiCol.getBoKeyField())) fiColId = fiCol;
        }

        if (fiColId != null) {
            index++;
            if (index != 1) query.append(", ");
            query.append(String.format("count( %s ) lnCount", fiColId.getFieldName()));
            queryGroupBy.append(fiColId.getFieldName());
        }

        query.append("\nFROM " + getTableName(clazz));

        Integer indexWhere = 0;
        for (FiCol fiCol : listFields) {

            if (FiBool.isTrue(fiCol.getBoKeyField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiCol.getFieldName() + " IN ( @" + fiCol.getFieldName() + " )");
                continue;
            }
        }

        query.append("\nWHERE " + queryWhere);
        query.append("\nGROUP BY " + queryGroupBy);


        // where cümleciği yoksa sorguyu iptal edelim
        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();
    }

    /**
     * Örnek Sorgu
     * <p>
     * SELECT count( arcId ) fiLnCount
     * <p>
     * FROM EntAppRoleConfig
     * <p>
     * WHERE arcTxFirmaKod = @arcTxFirmaKod AND arcTxFirmaKod = @arcTxFirmaKod
     *
     * @param clazz
     * @param fiColId
     * @param listWhereFields
     * @return
     */
    public static String selectQueryCountByFicols(Class clazz, FiCol fiColId, List<FiCol> listWhereFields) {

        //Map<String, FiField> listClassFields = FiEntity.getMapFieldsShort(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();
        //StringBuilder queryGroupBy = new StringBuilder();

        query.append("SELECT ");

        // FiCol fiColId = null;

        // id field bulmak için
        // int index = 0;
        //        for (FiCol fiCol : listWhereFields) {
        //  index++;
        //  if (index != 1) query.append(", ");
        //  query.append(fiCol.getFieldName());
        //  // idCol kayıt edilir
        //  if (FiBool.isTrue(fiCol.getBoKeyField())) fiColId = fiCol;
        //  }

        if (fiColId != null) {
            //  index++;
            //  if (index != 1) query.append(", ");
            query.append(String.format("count( %s ) lnCount", fiColId.getFieldName()));
            //queryGroupBy.append(fiColId.getFieldName());
        }

        query.append("\nFROM ").append(getTableName(clazz));

        int indexWhere = 0;
        for (FiCol fiCol : listWhereFields) {

            //if (FiBool.isTrue(fiCol.getBoKeyField())) {
            indexWhere++;
            if (indexWhere != 1) queryWhere.append(" AND ");
            queryWhere.append(fiCol.getFieldName())
                    .append(" = @")
                    .append(fiCol.getFieldName());
        }

        query.append("\nWHERE ").append(queryWhere);
        //query.append("\nGROUP BY " + queryGroupBy);

        // where cümleciği yoksa sorguyu iptal edelim
        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();
    }


    public static String selectFiColsWitOrderBy(Class clazz, List<FiCol> ficolsSelect, List<FiCol> ficolsOrderBy) {

        //Map<String, FiField> listClassFields = FiEntity.getMapFieldsShort(clazz);

        StringBuilder query = new StringBuilder();
        //StringBuilder queryWhere = new StringBuilder();
        StringBuilder queryOrderBy = new StringBuilder();

        query.append("SELECT ");

        int indexSelect = 0;
        for (FiCol fiCol : ficolsSelect) {
            indexSelect++;
            if (indexSelect != 1) query.append(", ");
            query.append(fiCol.getTxDbFieldNameOrFieldName());
        }

        query.append("\nFROM ").append(getTableName(clazz));

//        int indexWhere = 0;
//        for (FiCol fiCol : ficolsSelect) {
//
//            //if (FiBool.isTrue(fiCol.getBoKeyField())) {
//            indexWhere++;
//            if (indexWhere != 1) queryWhere.append(" AND ");
//            queryWhere.append(fiCol.getFieldName())
//                    .append(" = @")
//                    .append(fiCol.getFieldName());
//        }
//
        //query.append("\nWHERE ").append(queryWhere);

        if (!FiCollection.isEmpty(ficolsOrderBy)) {
            int indexOrder = 0;
            for (FiCol fiCol : ficolsOrderBy) {
                indexOrder++;
                if (indexOrder != 1) queryOrderBy.append(", ");
                queryOrderBy.append(fiCol.getTxDbFieldNameOrFieldName());
            }
            query.append("\nORDER BY ").append(queryOrderBy);
        }

        // where cümleciği yoksa sorguyu iptal edelim
//        if (queryWhere.length() < 1) {
//            query = new StringBuilder();
//        }

        return query.toString();
    }

    /**
     * Key alanlarını class alanlarındaki Id annotasyonuna göre alır
     *
     * @param clazz
     * @param listFields
     * @return
     */
    public static String updateQueryWithFiTableColByCandId(Class clazz, List<? extends IFiCol> listFields) {

        List<FiField> listClassFields = FiEntity.getListFieldsShortWithId(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("UPDATE " + getTableName(clazz) + " SET ");

        Integer index = 0;
        Integer indexWhere = 0;
        for (IFiCol fiTableCol : listFields) {
            index++;
            if (index != 1) query.append(", ");
            query.append(fiTableCol.getFieldName() + " = @" + fiTableCol.getFieldName());
        }

        for (FiField fiField : listClassFields) {
            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = @" + fiField.getDbParamName());
                continue;
            }
        }

        query.append(" WHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();

    }

    /**
     * FiCol Listesi Update Field ve Key Field ile update sorgusu kurar
     *
     * @param clazz
     * @param fiCols
     * @return
     */
    public static String updateQueryWithFiColsByUpdateAndKeyField(Class clazz, List<FiCol> fiCols) {

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("UPDATE ").append(getTableName(clazz)).append(" SET ");

        int index = 0;
        int indexWhere = 0;

        for (FiCol fiCol : fiCols) {

            if (FiBool.isTrue(fiCol.getBoUpdateField())) {
                index++;
                if (index != 1) query.append(", ");
                query.append(fiCol.getFieldName()).append(" = @").append(fiCol.getFieldName());
            }

            if (FiBool.isTrue(fiCol.getBoKeyField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiCol.getFieldName()).append(" = @").append(fiCol.getFieldName()); // dbFieldName kullanımı ile geliştirilebilir
                continue;
            }

        }

        query.append(" WHERE ").append(queryWhere);

        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }
        return query.toString();
    }

    public static FiListString updateQueryWithFiColsMultiByFiColEntClass(List<FiCol> fiCols) {

        Map<Class, List<FiCol>> classListMap = FiCollection.listToMapMulti(fiCols, fiCol -> fiCol.getEntClass());

        FiListString fiListString = new FiListString();

        classListMap.forEach((clazz, fiCols1) -> {

            StringBuilder query = new StringBuilder();
            StringBuilder queryWhere = new StringBuilder();

            query.append("UPDATE " + getTableName(clazz) + " SET ");

            Integer index = 0;
            Integer indexWhere = 0;
            for (FiCol fiCol : fiCols1) {

                String fieldName = FiString.getIfNotEmptytOr(fiCol.getTxDbFieldName(), fiCol.getFieldName());
                String paramName = FiString.getIfNotEmptytOr(fiCol.getTxParamName(), fiCol.getFieldName());

                if (!FiBool.isTrue(fiCol.getBoKeyField()) && !FiBool.isTrue(fiCol.getBoNonUpdatable())) {
                    index++;
                    if (index != 1) query.append(", ");
                    query.append(fieldName + " = @" + paramName);
                }

                if (FiBool.isTrue(fiCol.getBoKeyField())) {
                    indexWhere++;
                    if (indexWhere != 1) queryWhere.append(" AND ");
                    queryWhere.append(fieldName + " = @" + paramName); // dbFieldName kullanımı ile geliştirilebilir
                    continue;
                }
            }
            query.append(" WHERE " + queryWhere);

            if (queryWhere.length() < 1) {
                query = new StringBuilder();
            }

            if (!FiString.isEmptyTrim(query.toString())) fiListString.add(query.toString());

        });


        return fiListString;
    }

    /**
     * Key alanlarını class alanlarındaki Id annotasyonuna göre alır
     *
     * @param clazz
     * @param fiKeyBean
     * @return
     */
    public static String updateQueryWithFiMapParamByCandId(Class clazz, FiKeyBean fiKeyBean) {

        List<FiField> listClassFields = FiEntity.getListFieldsShortWithId(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append("UPDATE " + getTableName(clazz) + " SET ");

        Integer index = 0;
        Integer indexWhere = 0;

        for (String key : fiKeyBean.keySet()) {
            index++;
            if (index != 1) query.append(", ");
            query.append(key + " = @" + key);
        }

        for (FiField fiField : listClassFields) {
            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = @" + fiField.getDbParamName());
                continue;
            }
        }

        query.append(" WHERE " + queryWhere);

        if (queryWhere.length() < 1) {
            query = new StringBuilder();
        }

        return query.toString();
    }

    private static List<String> getListSqlParam(List<FiField> fieldListFilterAnno) {
        return fieldListFilterAnno.stream().map(fiField -> ":" + fiField.getName()).collect(toList());
    }

    private static List<String> getListDbSqlParamWithAt(List<FiField> fieldListFilterAnno) {
        return fieldListFilterAnno.stream().map(fiField -> "@" + fiField.getDbParamName()).collect(toList());
    }

    private static List<String> getListDbSqlParamWithAtAndFieldName(List<FiField> fieldListFilterAnno) {
        return fieldListFilterAnno.stream().map(fiField -> "@" + fiField.getName()).collect(toList());
    }

    /**
     * Generate insert query without id and transient (non-persistent) fields
     *
     * @param clazz
     * @return
     */
    public static String insertQuery11Fi(Class clazz) {

        Field[] fields = clazz.getDeclaredFields(); // returns all members including private members but not inherited members.

        List<Field> fieldListFilterAnno = Arrays.asList(fields).stream().filter(field -> {
            if (field.isAnnotationPresent(FiTransient.class)) return false;
            if (field.isAnnotationPresent(FiId.class)) return false;
            return true;
        }).collect(toList());

        String tableName = getTableNameFi(clazz);

        if (tableName == null || tableName.equals("")) tableName = clazz.getSimpleName();

        //System.out.println("Table Name:"+ tableName);

        StringBuilder query = new StringBuilder();

        String txFields = fieldListFilterAnno.stream().map(ent -> ent.getName()).collect(Collectors.joining(","));
        String txFieldParams = fieldListFilterAnno.stream().map(ent -> ":" + ent.getName()).collect(Collectors.joining(","));

        query.append("INSERT INTO " + tableName + " ( " + txFields);
        query.append(" ) \n VALUES ( ");
        query.append(txFieldParams + "\n )");

        return query.toString();

    }

    public static String getTableName(Class clazz) {

        String tableName = clazz.getSimpleName();

        if (clazz.isAnnotationPresent(Table.class)) {
            Table annoTable = (Table) clazz.getAnnotation(Table.class);
            if (!FiString.isEmpty(annoTable.name())) return annoTable.name();
        }

        if (clazz.isAnnotationPresent(FiTable.class)) {
            FiTable annoTable = (FiTable) clazz.getAnnotation(FiTable.class);
            if (!FiString.isEmpty(annoTable.name())) return annoTable.name();
        }

        return tableName;

    }

    public static Boolean checkAnnoClassOfInsertSelect(Class clazz) {
        if (clazz.isAnnotationPresent(FiInsertSelect.class)) {
            return true;
        }
        return false;
    }

    public static String getTableNameFi(Class reflectClass) {

        if (reflectClass.isAnnotationPresent(FiTable.class)) {
            Annotation a = reflectClass.getAnnotation(FiTable.class);
            FiTable annotation = (FiTable) a;
            return annotation.name();
        }
        return null;
    }

//	listFormElements = new ArrayList<>();
//
//			listFormElements.add(OzTableCol.build("Id","cha_RECno").buildIsHidden(true).buildColType(OzColType.Integer));
//			listFormElements.add(OzTableCol.build("Seri","cha_evrakno_seri").buildEditorClass(FxTextField.class.getName()));
//			listFormElements.add(OzTableCol.build("Sıra","cha_evrakno_sira").buildEditorClass(FxLabel.class.getName()).buildColType(OzColType.Integer)  );
//			listFormElements.add(OzTableCol.build("Tarih","cha_tarihi").buildEditorClass(FxDatePicker.class.getName()).buildFiEditable(true).buildColType(OzColType.Date));

    public static String codeFormFields(Class clazz) {

        List<FiField> listFields = FiEntity.getListFieldsWoutStatic(clazz, true);

        StringBuilder query = new StringBuilder();

        //assignSqlTypeAndDef(listFields);

        query.append("List listFormElements = new ArrayList<>();\n\n");

        int index = 0;
        for (FiField field : listFields) {
            index++;

            String editorClass = new FxEditorFactory().convertSimpleTypeToCompClass(field.getClassNameSimple());

            String label = FiString.ifEmptyElseValue(field.getLabel(), field.getName());

            query.append(String.format("listFormElements.add(OzTableCol.build(\"%s\",\"%s\").buildEditorClass(%s.class.getName()));\n"
                    , label, field.getName(), editorClass));

            //if (index != 1) query.append("\n, ");
            //query.append(field.getName() + " " + field.assignSqlFieldType());

        }

        query.append("\n");

        return query.toString();

    }

//	List<FxTableCol> listTableCols = new ArrayList<>();
//
//		listTableCols.add(new FxTableColBuildHelper().build(Mkfields.his_kod));
//		listTableCols.add(FxTableCol.build("İsim", Mkfields.his_isim.toString()).buildPrefSize(200).buildPrintSize(20));
//	//listTableCols.add(new FxTableColBuildHelper().build(Mkfields.cari_per_adi).buildPrefSize(150).buildPrintSize(15));
//		listTableCols.add(new FxTableColBuildHelper().build(Mkfields.cha_meblagBakiye).buildColType(OzColType.Double).buildSumType(OzColSummaryType.SUM));

    public static String codeTableColsV1(Class clazz) {

        List<FiField> listFields = FiEntity.getListFieldsWoutStatic(clazz, true);

        StringBuilder query = new StringBuilder();

        query.append(String.format("public List<FxTableCol> cols%s(){\n\n", clazz.getSimpleName()));
        query.append("\tList<FxTableCol> listTableCols = new ArrayList<>();\n\n");

        int index = 0;
        for (FiField field : listFields) {
            index++;

            //String ozColType = new OzTableColHelper().convertSimpleTypeToOzColType(field.getTypeSimpleName());

            String label = FiString.ifEmptyElseValue(field.getLabel(), field.getName());

            query.append(String.format("\tlistTableCols.add(FxTableCol.build(\"%s\",\"%s\").buildColType(OzColType.%s));\n"
                    , label, field.getName(), field.getClassNameSimple()));

            //if (index != 1) query.append("\n, ");
            //query.append(field.getName() + " " + field.assignSqlFieldType());

        }

        query.append("\n\treturn listTableCols;\n");
        query.append("\n}\n");

        return query.toString();

    }

    public static String codeTableColsV2(Class clazz, Boolean includeTransients, String fieldEnumClass) {

        List<FiField> listFields = FiEntity.getListFieldsWoutStatic(clazz, includeTransients);

        StringBuilder query = new StringBuilder();

        query.append(String.format("public List<FiTableCol> cols%s(){\n\n", clazz.getSimpleName()));
        query.append("\tList<FiTableCol> listTableCols = new ArrayList<>();\n\n");

        int index = 0;
        for (FiField field : listFields) {
            index++;

            //String ozColType = new OzTableColHelper().convertSimpleTypeToOzColType(field.getTypeSimpleName());

            //String label = FiString.ifEmptyElseValue(field.getLabel(), field.getName());
            //FxTableColBuildHelper.build(EntegreField.id)
            query.append(String.format("\tlistTableCols.add(FiTableColBuildHelper.build(%s.%s).buildColType(OzColType.%s));\n"
                    , fieldEnumClass, field.getName(), field.getClassNameSimple()));

            // enum çıkarıldı
            // // enum %s("%s")
            // field.getName(), label

            //if (index != 1) query.append("\n, ");
            //query.append(field.getName() + " " + field.assignSqlFieldType());

        }

        query.append("\n\treturn listTableCols;\n");
        query.append("\n}\n");

        return query.toString();

    }

    public static String codeTableColsSimple(Class clazz, Boolean includeTransients) {

        List<FiField> listFields = FiEntity.getListFieldsWoutStatic(clazz, includeTransients);

        StringBuilder query = new StringBuilder();

        query.append(String.format("public List<FiTableCol> cols%s(){\n\n", clazz.getSimpleName()));
        query.append("\tList<FiTableCol> listTableCols = new ArrayList<>();\n\n");

        //int index = 0;
        for (FiField field : listFields) {
            //index++;

            //String ozColType = new OzTableColHelper().convertSimpleTypeToOzColType(field.getTypeSimpleName());

            //String label = FiString.ifEmptyElseValue(field.getLabel(), field.getName());
            //FxTableColBuildHelper.build(EntegreField.id)
            String fieldHeader = "\"" + field.getName() + "\"";
            String fieldName = "\"" + field.getName() + "\"";
            query.append(String.format("\tlistTableCols.add(FiTableColBuildHelper.buildfh(%s,%s).buildColType(OzColType.%s));\n"
                    , fieldName, fieldHeader, field.getClassNameSimple()));

            // enum çıkarıldı
            // // enum %s("%s")
            // field.getName(), label

        }

        query.append("\n\treturn listTableCols;\n");
        query.append("\n}\n");

        return query.toString();

    }

    public static String codeColsEnum(Class clazz, Boolean includeTransients) {

        List<FiField> listFields = FiEntity.getListFieldsWoutStatic(clazz, includeTransients);

        StringBuilder query = new StringBuilder();

        //query.append(String.format("public List<FiTableCol> cols%s(){\n\n", clazz.getSimpleName()));
        //query.append("\tList<FiTableCol> listTableCols = new ArrayList<>();\n\n");

        //int index = 0;
        for (FiField field : listFields) {
            //index++;

            //String ozColType = new OzTableColHelper().convertSimpleTypeToOzColType(field.getTypeSimpleName());

            //String label = FiString.ifEmptyElseValue(field.getLabel(), field.getName());
            //FxTableColBuildHelper.build(EntegreField.id)
            //String fieldHeader = "\""+ field.getName() +"\"";
            //String fieldName = "\""+ field.getName() +"\"";
            query.append(String.format("\t, %s(\"%s\")\n"
                    , field.getName(), field.getName())); // ,field.getClassNameSimple()

            // enum çıkarıldı
            // // enum %s("%s")
            // field.getName(), label

        }

        //query.append("\n\treturn listTableCols;\n");
        //query.append("\n}\n");

        return query.toString();

    }

    public static String codeFiTableColsFromHeader(List<String> listHeaders, String colsName) {

        StringBuilder query = new StringBuilder();

        query.append(String.format("public List<FiTableCol> genCols%s(){\n\n", colsName));

        query.append("\tList<FiTableCol> listTableCols = new ArrayList<>();\n\n");

        for (String field : listHeaders) {
            String fieldName = FiString.replaceTurkishCharacterstoLatin(field).replaceAll(" ", "");
            fieldName = FiString.firstLetterLowerOnly(fieldName);
            query.append(String.format("\tlistTableCols.add(FiTableColBuildHelper.buildo(\"%s\",\"%s\"));\n", fieldName, field));
        }

        query.append("\n\treturn listTableCols;\n");
        query.append("\n}\n");

        return query.toString();

    }

    /**
     * Field : lnRowNo
     * <p>
     * Named Parameters : lnBegin, lnEnd
     *
     * @param sql
     * @return
     */
    public static String convertPagableQuery(String sql) {

        String sqlPageble = String.format("SELECT mainQuery.* FROM (\n" +
                "%s\n" +
                ") AS mainQuery WHERE lnRowNo >= @lnBegin AND lnRowNo <= @lnEnd", sql);

        return sqlPageble;
    }

    public static String tableToEntityClass(String tableName, Jdbi jdbi) {

        List<TableScheme> result = getDbTableScheme(tableName, jdbi);

        if (result == null) return "Veritabanı Sorgusu Çalıştırılamadı...";

        StringBuilder entitycode = new StringBuilder();

        Map<String, String> mapTypeConvertorSqlserverToJava = getMapTypeConvertorSqlserverToJava();
        // https://docs.microsoft.com/en-us/sql/connect/jdbc/using-basic-data-types?view=sql-server-2017

        entitycode.append("\n\nimport java.util.Date;\n");
        entitycode.append("\n");
        entitycode.append("public class " + tableName + " { \n\n");

        result.forEach(tableScheme -> {

            String type = "";

            if (mapTypeConvertorSqlserverToJava.containsKey(tableScheme.getDATA_TYPE())) {
                type = mapTypeConvertorSqlserverToJava.get(tableScheme.getDATA_TYPE());
            } else {
                type = tableScheme.getDATA_TYPE();
            }

            entitycode.append("\t" + type + " " + tableScheme.getCOLUMN_NAME() + "; //");

            // descriptions related to a field
            if (tableScheme.getIS_NULLABLE().equals("NO")) entitycode.append(" IsNullable : NO");
            if (tableScheme.getCHARACTER_MAXIMUM_LENGTH() != null && !tableScheme.getCHARACTER_MAXIMUM_LENGTH().equals("0"))
                entitycode.append(" Max:" + tableScheme.getCHARACTER_MAXIMUM_LENGTH());
            if (tableScheme.getNUMERIC_SCALE() != null) {
                entitycode.append(" Scale:" + tableScheme.getNUMERIC_SCALE());
                entitycode.append(" Precision:" + tableScheme.getNUMERIC_PRECISION());
            }
            if (tableScheme.getIsPartOfPrimaryKey().equals("1")) entitycode.append(" Key Field");

            entitycode.append("\n");

        });

        entitycode.append("\n");
        entitycode.append("} \n");

        return entitycode.toString();

    }

    public static String codeEntityClassCsharp(String tableName, Jdbi jdbi) {

        List<TableScheme> result = getDbTableScheme(tableName, jdbi);

        if (result == null) return "Veritabanı Sorgusu Çalıştırılamadı...";

        StringBuilder entitycode = new StringBuilder();

        Map<String, String> mapTypeConvertorSqlserver = getMapTypeConvertorSqlserverToCsharp();
        // https://docs.microsoft.com/en-us/sql/connect/jdbc/using-basic-data-types?view=sql-server-2017


        entitycode.append("\n\n" +
                "using System.Collections.Generic;\n" +
                "using System.Linq;\n" +
                "using System.Text;\n" +
                "using System.Threading.Tasks;\n");
        entitycode.append("\n");
        entitycode.append("public class " + tableName + " { \n\n");

        result.forEach(tableScheme -> {

            String type = "";

            if (mapTypeConvertorSqlserver.containsKey(tableScheme.getDATA_TYPE())) {
                type = mapTypeConvertorSqlserver.get(tableScheme.getDATA_TYPE());
            } else {
                type = tableScheme.getDATA_TYPE();
            }

            entitycode.append("\t public " + type + " " + tableScheme.getCOLUMN_NAME() + " { get; set; } //");

            // descriptions related to a field
            if (tableScheme.getIS_NULLABLE().equals("NO")) entitycode.append(" IsNullable : NO");
            if (tableScheme.getCHARACTER_MAXIMUM_LENGTH() != null && !tableScheme.getCHARACTER_MAXIMUM_LENGTH().equals("0"))
                entitycode.append(" Max:" + tableScheme.getCHARACTER_MAXIMUM_LENGTH());
            if (tableScheme.getNUMERIC_SCALE() != null) {
                entitycode.append(" Scale:" + tableScheme.getNUMERIC_SCALE());
                entitycode.append(" Precision:" + tableScheme.getNUMERIC_PRECISION());
            }
            if (tableScheme.getIsPartOfPrimaryKey().equals("1")) entitycode.append(" Key Field");

            entitycode.append("\n");

        });

        entitycode.append("\n");
        entitycode.append("} \n");

        return entitycode.toString();

    }


    public static List<FiField> getFieldsAndDefinitionFromDb(String tableName, Jdbi jdbi) {

        List<TableScheme> result = getDbTableScheme(tableName, jdbi);

        if (result == null) return null; //"Veritabanı Sorgusu Çalıştırılamadı...";

        Map<String, String> mapTypeConvertorSqlserverToJava = getMapTypeConvertorSqlserverToJava();
        // https://docs.microsoft.com/en-us/sql/connect/jdbc/using-basic-data-types?view=sql-server-2017

        List<FiField> listFields = new ArrayList<>();

        result.forEach(tableScheme -> {

            FiField fiField = new FiField();

            fiField.setDbFieldName(tableScheme.getCOLUMN_NAME());

            StringBuilder fieldDefinition = new StringBuilder();

            String type = "";

            if (mapTypeConvertorSqlserverToJava.containsKey(tableScheme.getDATA_TYPE())) {
                type = mapTypeConvertorSqlserverToJava.get(tableScheme.getDATA_TYPE());
            } else {
                type = tableScheme.getDATA_TYPE();
            }

            fieldDefinition.append("\t" + type + " " + tableScheme.getCOLUMN_NAME() + "; //");

            // descriptions related to a field
            if (tableScheme.getIS_NULLABLE().equals("NO")) fieldDefinition.append(" IsNullable : NO");
            if (tableScheme.getCHARACTER_MAXIMUM_LENGTH() != null && !tableScheme.getCHARACTER_MAXIMUM_LENGTH().equals("0"))
                fieldDefinition.append(" Max:" + tableScheme.getCHARACTER_MAXIMUM_LENGTH());
            if (tableScheme.getNUMERIC_SCALE() != null) {
                fieldDefinition.append(" Scale:" + tableScheme.getNUMERIC_SCALE());
                fieldDefinition.append(" Precision:" + tableScheme.getNUMERIC_PRECISION());
            }
            if (tableScheme.getIsPartOfPrimaryKey().equals("1")) fieldDefinition.append(" Key Field");

            fiField.setSqlFieldDefinition(fieldDefinition.toString());
            listFields.add(fiField);

        });

        return listFields;

    }

    public static Map<String, FiField> getMapDbFields(Class clazz, Jdbi jdbi) {

        List<FiField> dbFields = getFieldsAndDefinitionFromDb(getTableName(clazz), jdbi);

        return FiCollection.listToMapSingle(dbFields, FiField::getDbFieldName);
    }


    public static List<TableScheme> getDbTableScheme(String tableName, Jdbi jdbi) {

        String sql = "Select C.DATA_TYPE, C.COLUMN_NAME, C.IS_NULLABLE\n" +
                ", Case When Z.CONSTRAINT_NAME Is Null Then 0 Else 1 End As IsPartOfPrimaryKey\n" +
                ",C.CHARACTER_MAXIMUM_LENGTH , C.NUMERIC_PRECISION, C.NUMERIC_SCALE\n" +
                "From INFORMATION_SCHEMA.COLUMNS As C\n" +
                "    Outer Apply (\n" +
                "                Select CCU.CONSTRAINT_NAME\n" +
                "                From INFORMATION_SCHEMA.TABLE_CONSTRAINTS As TC\n" +
                "                    Join INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE As CCU\n" +
                "                        On CCU.CONSTRAINT_NAME = TC.CONSTRAINT_NAME\n" +
                "                Where TC.TABLE_SCHEMA = C.TABLE_SCHEMA\n" +
                "                    And TC.TABLE_NAME = C.TABLE_NAME\n" +
                "                    And TC.CONSTRAINT_TYPE = 'PRIMARY KEY'\n" +
                "                    And CCU.COLUMN_NAME = C.COLUMN_NAME\n" +
                "                ) As Z              \n" +
                " Where C.TABLE_NAME = @TableName";

        List<TableScheme> result = null;

        try {
            result = jdbi.withHandle(handle -> {
                //handle.registerRowMapper(FieldMapper.factory(TblFiyatVade.class));
                return handle.select(FiStFormat.fif(sql).sqlFmtAt())
                        .bind("TableName", tableName)
                        .mapToBean(TableScheme.class)
                        .list();
                //.collect(Collectors.toList());
            });
        } catch (Exception ex) {
            //ex.printStackTrace();
            Loghelper.get(FiQugen.class).error(FiException.exToLog(ex));
        }
        return result;
    }


    public static <E> String codeEntityFieldsInitMethod(String tableName, Jdbi jdbi, Class<E> clazz, String sqlSelect, Boolean lastRecordPulling) {

        List<TableScheme> tableFields = getDbTableScheme(tableName, jdbi);

        List<String> listKeyField = new ArrayList<>();

        String keyField = null;

        for (TableScheme tableField : tableFields) {
            if (tableField.getIsPartOfPrimaryKey().equals("1")) {
                keyField = tableField.getCOLUMN_NAME();
                break;
            }
        }

        String sqlSelectEntity = "select top 1 * from " + tableName;

        if ((!FiString.isEmpty(keyField)) && FiBool.isTrue(lastRecordPulling)) {
            sqlSelectEntity += " order by " + keyField + " DESC";
        }


        if (sqlSelect != null) {
            sqlSelectEntity = sqlSelect;
        }

        String finalSqlSelectEntity = sqlSelectEntity;

        Optional<E> optEntity = jdbi.withHandle(handle -> {
            //handle.registerRowMapper(FieldMapper.factory(TblFiyatVade.class));
            return handle.select(FiStFormat.fif(finalSqlSelectEntity).sqlFmtAt())
                    .mapToBean(clazz)
                    .findFirst();
        });

        if (!optEntity.isPresent()) {
            Loghelper.get(getClassi()).debug("example entity null");
            return "";
        }

        StringBuilder entitycode = new StringBuilder();

        Map<String, String> mapTypeConvertorSqlserverToJava = getMapTypeConvertorSqlserverToJava();
        // https://docs.microsoft.com/en-us/sql/connect/jdbc/using-basic-data-types?view=sql-server-2017


        //entitycode.append("\n\nimport java.util.Date;\n");
        entitycode.append("\n");
        entitycode.append(String.format("public void fill%s (%s entity) {\n\n", tableName, tableName));

        E finalExEntity = optEntity.get();

        tableFields.forEach(tableScheme -> {

            String type = "";

            if (mapTypeConvertorSqlserverToJava.containsKey(tableScheme.getDATA_TYPE())) {
                type = mapTypeConvertorSqlserverToJava.get(tableScheme.getDATA_TYPE());
            } else {
                type = tableScheme.getDATA_TYPE();
            }
            String fieldValue = FiReflection.getProperty(finalExEntity, tableScheme.getCOLUMN_NAME()).toString();

            if (fieldValue == null) {
                fieldValue = "null";
            } else {

                if (type.equals("String")) {
                    fieldValue = String.format("\"%s\"", fieldValue);
                }

                if (type.equals("Date")) {
                    //fieldValue = "dtCreate";
                }
            }

            entitycode.append(String.format("\tif(entity.get%s() == null)", FiString.firstLetterUpperOnly(tableScheme.getCOLUMN_NAME())));
            entitycode.append(String.format(" entity.set%s(%s);\n", FiString.firstLetterUpperOnly(tableScheme.getCOLUMN_NAME()), fieldValue));
            //+"\t" + type + " " + tableScheme.getCOLUMN_NAME() + "; //");

            // descriptions related to a field
//			if (tableScheme.getIS_NULLABLE().equals("NO")) entitycode.append(" IsNullable : NO");
//			if (tableScheme.getCHARACTER_MAXIMUM_LENGTH() != null && !tableScheme.getCHARACTER_MAXIMUM_LENGTH().equals("0"))
//				entitycode.append(" Max:" + tableScheme.getCHARACTER_MAXIMUM_LENGTH());
//			if (tableScheme.getNUMERIC_SCALE() != null) {
//				entitycode.append(" Scale:" + tableScheme.getNUMERIC_SCALE());
//				entitycode.append(" Precision:" + tableScheme.getNUMERIC_PRECISION());
//			}
//			if (tableScheme.getIsPartOfPrimaryKey().equals("1")) entitycode.append(" Key Field");

        });

        entitycode.append("\n");
        entitycode.append("}\n");

        return entitycode.toString();

    }

    public static String codeEntityFieldsInitMethod(Jdbi jdbi, Class clazz, Optional optEntity2) {

        String tableName = getTableName(clazz);

        String sql = "Select C.DATA_TYPE, C.COLUMN_NAME, C.IS_NULLABLE\n" +
                ", Case When Z.CONSTRAINT_NAME Is Null Then 0 Else 1 End As IsPartOfPrimaryKey\n" +
                ",C.CHARACTER_MAXIMUM_LENGTH , C.NUMERIC_PRECISION, C.NUMERIC_SCALE\n" +
                "From INFORMATION_SCHEMA.COLUMNS As C\n" +
                "    Outer Apply (\n" +
                "                Select CCU.CONSTRAINT_NAME\n" +
                "                From INFORMATION_SCHEMA.TABLE_CONSTRAINTS As TC\n" +
                "                    Join INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE As CCU\n" +
                "                        On CCU.CONSTRAINT_NAME = TC.CONSTRAINT_NAME\n" +
                "                Where TC.TABLE_SCHEMA = C.TABLE_SCHEMA\n" +
                "                    And TC.TABLE_NAME = C.TABLE_NAME\n" +
                "                    And TC.CONSTRAINT_TYPE = 'PRIMARY KEY'\n" +
                "                    And CCU.COLUMN_NAME = C.COLUMN_NAME\n" +
                "                ) As Z              \n" +
                " Where C.TABLE_NAME = @TableName";

        List<TableScheme> tableFields = jdbi.withHandle(handle -> {
            //handle.registerRowMapper(FieldMapper.factory(TblFiyatVade.class));
            return handle.select(FiStFormat.fif(sql).sqlFmtAt())
                    .bind("TableName", tableName)
                    .mapToBean(TableScheme.class)
                    .list();
            //.collect(Collectors.toList());
        });

        List<String> listKeyField = new ArrayList<>();

        String keyField = null;

        for (TableScheme tableField : tableFields) {
            if (tableField.getIsPartOfPrimaryKey().equals("1")) {
                keyField = tableField.getCOLUMN_NAME();
                break;
            }
        }

        Optional optEntity = optEntity2;

        if (!optEntity.isPresent()) {
            System.out.println("example entity null");
            Loghelper.get(getClassi()).debug("example entity null");
            return "";
        }

        StringBuilder entitycode = new StringBuilder();

        Map<String, String> getTypeConvertorSqlserverToJavaMap = getMapTypeConvertorSqlserverToJava();

        //entitycode.append("\n\nimport java.util.Date;\n");
        entitycode.append("\n");
        entitycode.append(String.format("public void fill%s (%s entity) {\n\n", tableName, tableName));

        entitycode.append("\tDate dtTimestamp = new Date();\n\n");

        Object finalExEntity = optEntity.get();

//		if(finalExEntity==null){
//			Loghelperr.getInstance(getClassi()).debug("finalexentity null");
//		}

        tableFields.forEach(tableScheme -> {

            String type = "";

            if (getTypeConvertorSqlserverToJavaMap.containsKey(tableScheme.getDATA_TYPE())) {
                type = getTypeConvertorSqlserverToJavaMap.get(tableScheme.getDATA_TYPE());
            } else {
                type = tableScheme.getDATA_TYPE();
            }

            String fieldValue = null;
            if (FiReflection.hasProperty(finalExEntity, tableScheme.getCOLUMN_NAME())) {
                Object property = FiReflection.getProperty(finalExEntity, tableScheme.getCOLUMN_NAME());
                if (property != null) fieldValue = property.toString();
            } else {
                fieldValue = "Property Bulunamadı";
            }


            String fieldValForDesc = null;

            if (fieldValue == null) {
                fieldValue = "null";
            } else {

                if (type.equals("String")) {
                    fieldValue = String.format("\"%s\"", fieldValue);
                }

                if (type.equals("Date")) {
                    //fieldValue = "dtCreate";
                    fieldValForDesc = fieldValue;
                    fieldValue = "dtTimestamp";

                }
            }

            entitycode.append(String.format("\tif(entity.get%s() == null)", FiString.firstLetterUpperOnly(tableScheme.getCOLUMN_NAME())));
            entitycode.append(String.format(" entity.set%s(%s);", FiString.firstLetterUpperOnly(tableScheme.getCOLUMN_NAME()), fieldValue));

            StringBuilder entityDesc = new StringBuilder("");

            // descriptions related to a field
            if (true) {
                if (fieldValForDesc != null) entityDesc.append(" " + fieldValForDesc);
                if (tableScheme.getIS_NULLABLE().equals("NO")) entityDesc.append(" IsNullable : NO");
                if (tableScheme.getCHARACTER_MAXIMUM_LENGTH() != null && !tableScheme.getCHARACTER_MAXIMUM_LENGTH().equals("0"))
                    entityDesc.append(" Max:" + tableScheme.getCHARACTER_MAXIMUM_LENGTH());
                if (tableScheme.getNUMERIC_SCALE() != null) {
                    entityDesc.append(" Scale:" + tableScheme.getNUMERIC_SCALE());
                    entityDesc.append(" Precision:" + tableScheme.getNUMERIC_PRECISION());
                }
                if (tableScheme.getIsPartOfPrimaryKey().equals("1")) entityDesc.append(" Key Field");
                entityDesc.append(String.format(" Value(%s)", fieldValue));
            }

            if (!FiString.isEmpty(entityDesc.toString())) {
                entitycode.append(" //" + entityDesc.toString());
            }

            entitycode.append("\n");

            //+"\t" + type + " " + tableScheme.getCOLUMN_NAME() + "; //");

            // descriptions related to a field
//			if (tableScheme.getIS_NULLABLE().equals("NO")) entitycode.append(" IsNullable : NO");
//			if (tableScheme.getCHARACTER_MAXIMUM_LENGTH() != null && !tableScheme.getCHARACTER_MAXIMUM_LENGTH().equals("0"))
//				entitycode.append(" Max:" + tableScheme.getCHARACTER_MAXIMUM_LENGTH());
//			if (tableScheme.getNUMERIC_SCALE() != null) {
//				entitycode.append(" Scale:" + tableScheme.getNUMERIC_SCALE());
//				entitycode.append(" Precision:" + tableScheme.getNUMERIC_PRECISION());
//			}
//			if (tableScheme.getIsPartOfPrimaryKey().equals("1")) entitycode.append(" Key Field");

        });

        entitycode.append("\n");
        entitycode.append("}\n");

        return entitycode.toString();

    }

    private static Class<FiQugen> getClassi() {
        return FiQugen.class;
    }

    public static Boolean runCreateQuery(Class clazz, Jdbi jdbi, Boolean onlyPrintConsole) {

        if (jdbi == null) {
            Loghelper.get(getClassi()).debug("Jdbi Null");
            return false;
        }

        String queryCreate = createQuery20(clazz);
        Loghelper.get(getClassi()).debug("Query:\n" + queryCreate);

        if (FiBool.isTrue(onlyPrintConsole)) {
            Loghelper.get(getClassi()).debug("Only Console");
            return false;
        }

        AbsRepoGenJdbi repoGenericJdbi = new RepoJdbiCustom(jdbi, clazz);

        List<String> listQuery = Arrays.asList(queryCreate);

        return FiBool.convertDbResultGteZero(repoGenericJdbi.jdRunBatchUpdateQuery(listQuery));

    }

    public static String createQuery20(Class clazz) {

        List<FiField> listFields = FiEntity.getListFieldsAll(clazz);

        StringBuilder query = new StringBuilder();

        query.append("CREATE TABLE " + getTableName(clazz) + " ( \n");

        assignSqlTypeAndDef(listFields);

        int index = 0;
        for (FiField field : listFields) {

            // Sql Tipi Belirlenmeyenler için
            if (field.getSqlFieldDefinition() == null) {
                query.append("\n-- " + field.getName() + " " + field.getClassNameSimple()
                        + (field.getLength() != null ? " -- Length:" + field.getLength() : "")
                        + (field.getPrecision() != null ? " -- Prec.:" + field.getPrecision() : "")
                        + (field.getScale() != null ? "Scale :" + field.getScale() : ""));
                continue;
            }

            index++;
            if (index != 1) query.append("\n, ");
            query.append(field.getName() + " " + field.getSqlFieldDefinition());

        }

        query.append("\n)");

        return query.toString();

    }

    public static String uniqueQuery(Class clazz) {

        List<FiField> listFields = FiEntity.getListFieldsAll(clazz);

        StringBuilder sbFields = new StringBuilder();
        StringBuilder sbFieldsForName = new StringBuilder();
        String name = "";

        int index = 0;
        for (FiField field : listFields) {

            if (FiBool.isTrue(field.getBoUnique1())) {
                if (index != 0) sbFields.append("\n,");
                if (index != 0) sbFieldsForName.append("_");

                sbFields.append(field.getDbFieldName() + " ASC");
                sbFieldsForName.append(field.getDbFieldName());
                index++;

                if (!FiString.isEmpty(field.getTxUnique1Name())) {
                    name = field.getTxUnique1Name();
                }

            }

        }

        if (FiString.isEmpty(name)) {
            name = "ent_unique_" + sbFieldsForName;
//			}else{
//				name = "ent-unique-" + FiDate.toStringTimestamptPlain(new Date());
//			}
        }


        String sablon = "CREATE \n" +
                "UNIQUE NONCLUSTERED INDEX {{name}} ON [dbo].{{tableName}}\n" +
                "(\n" +
                "{{fields}}\n" +
                ")";

        FiKeyBean fiKeyBean = new FiKeyBean();
        fiKeyBean.put("name", name);
        fiKeyBean.put("tableName", getTableName(clazz));
        fiKeyBean.put("fields", sbFields.toString());

        return FiString.substitutor(sablon, fiKeyBean);
    }

    public static List<FiField> getFieldsWithDbDefinitionFromCode(Class clazz) {

        List<FiField> listFields = FiEntity.getListFieldsAll(clazz);

        assignSqlTypeAndDef(listFields);

        for (FiField field : listFields) {

            // Sql Tipi Belirlenmeyenler için
            if (field.getSqlFieldDefinition() == null) {

                StringBuilder fieldDefinition = new StringBuilder();
                fieldDefinition.append("\n-- " + field.getName() + " " + field.getClassNameSimple()
                        + (field.getLength() != null ? " -- Length:" + field.getLength() : "")
                        + (field.getPrecision() != null ? " -- Prec.:" + field.getPrecision() : "")
                        + (field.getScale() != null ? "Scale :" + field.getScale() : ""));

                field.setSqlFieldDefinition(fieldDefinition.toString());
            }

        }

        return listFields;

    }

    public static void assignSqlTypeAndDef(List<FiField> listFields) {

        //System.out.println("List Field Size:" + listFields.size());

        for (FiField field : listFields) {

            //System.out.println(" Field:" + field.getName() + " - Simple Name:" + field.getClassNameSimple());

            if (field.getPrecision() == null) field.setPrecision(0);
            if (field.getScale() == null) field.setScale(0);
            if (field.getLength() == null) field.setLength(0);
            if (field.getClassNameSimple() == null) field.setClassNameSimple("");

            // Sql Field Type Belirlenir
            String sqlFieldType = assignSqlFieldType(field);

            if (sqlFieldType == null) {
                continue;
            }

            String typeLength = "";

            if (sqlFieldType.equals("nvarchar") || sqlFieldType.equals("varchar")) {
                if (field.getLength().equals(255)) field.setLength(50); //length = 50; // default 50 ye çekildi.
                if (field.getLength().equals(0)) field.setLength(50); // length = 50; // default 50 yapıldı.
                typeLength = "(" + field.getLength() + ")";
            }


            if (sqlFieldType.equals("decimal")) {
                // default precision 18 ,scale 2
                if (field.getPrecision().equals(0)) field.setPrecision(18); //precision = 18;
                if (field.getScale().equals(0)) {

                    if (field.getClassNameSimple().equalsIgnoreCase("double")) {
                        field.setScale(5);
                    } else if (field.getClassNameSimple().equalsIgnoreCase("float")) {
                        field.setScale(4);
                    } else {
                        field.setScale(6);  // scale = 2;
                    }

                }
                typeLength = "(" + field.getPrecision() + "," + field.getScale() + ")";
            }

            if (!FiString.isEmpty(field.getColCustomType())) {
                sqlFieldType = field.getColCustomType();
                typeLength = "";
                //fieldAttributes = "";
            }

            String fieldAttributes = "";

            if (field.getFiCollation() != null && field.getFiCollation() != FiCollation.Default) {
                fieldAttributes += " COLLATE " + field.getFiCollation().toString();
            }

            // Field Alanın özellikleri eklenir

            if (FiBool.isTrue(field.getBoIdField())) {
                if (field.getIdGenerationType() == FiIdGenerationType.Identity) {
                    fieldAttributes += " IDENTITY(1,1)";
                }
                fieldAttributes += "  NOT NULL PRIMARY KEY ";
            }

            if (FiBool.isTrue(field.getUnique()) && !FiBool.isTrue(field.getBoIdField()))
                fieldAttributes += " UNIQUE";

            if (FiBool.isFalse(field.getNullable()) && !FiBool.isTrue(field.getBoIdField()))
                fieldAttributes += " NOT NULL";

            if (!FiString.isEmpty(field.getDefaultValue())) {
                fieldAttributes += " DEFAULT " + field.getDefaultValue();
            }


            if (!FiString.isEmpty(field.getColDefinitionExtra())) {
                fieldAttributes += " " + field.getColDefinitionExtra();
            }

            field.setSqlFieldDefinition(sqlFieldType + typeLength + fieldAttributes);

        }


    }

    private static String assignSqlFieldType(FiField field) {

        //if (getMapTypeConvertorJavaToSqlServer().containsKey(field.getClassNameSimple())) {

        String javaSimpleType = field.getClassNameSimple();

        String suffix = "";

        if (FiBool.isTrue(field.getBoUtfSupport())) suffix += "Utf";

        if (javaSimpleType.equals("Integer")) {
            if (field.getPrecision() != null && field.getPrecision() == 1) suffix = "P1";

            if (field.getPrecision() != null && field.getPrecision() >= 20) suffix = "P20";
        }

        String sqlFieldType = getMapTypeConvertorJavaToSqlServer().getOrDefault(javaSimpleType + suffix, null);

        if (sqlFieldType == null) {
            //Loghelperr.getInstance(getClass()).debug("sql field type null");
            sqlFieldType = getMapTypeConvertorJavaToSqlServer().getOrDefault(javaSimpleType, null);
        }

        if (sqlFieldType == null) {
            //Loghelperr.getInstance(getClass()).debug("sql field type null 2");
            return null;
        }

        if (sqlFieldType.equalsIgnoreCase("int")) {
            if (field.getPrecision().equals(1)) {
                sqlFieldType = "tinyint";
            }

            if (field.getPrecision() > 20) sqlFieldType = "bigint";
        }

        if (sqlFieldType.equalsIgnoreCase("double")) {
            if (field.getScale() > 5) sqlFieldType = "decimal";
        }

        if (sqlFieldType.equals("datetime") && field.getTemporalType() != null) {

            if (field.getTemporalType() == TemporalType.DATE) {
                sqlFieldType = "date";
            }

        }

        if (sqlFieldType.equals("float")) {

            if (field.getPrecision() > 0 && field.getScale() > 0) {
                sqlFieldType = "decimal"; // default precision 18 ,scale 2
            }
        }
        return sqlFieldType;
        //}

        //return null;
    }

    // https://docs.microsoft.com/en-us/sql/connect/jdbc/using-basic-data-types?view=sql-server-2017
    public static Map<String, String> getMapTypeConvertorSqlserverToJava() {
        //if (mapTypeConvertorSqlserverToJava == null) {

        Map<String, String> mapTypeConvertorSqlserverToJava = new HashMap();

        mapTypeConvertorSqlserverToJava.put("int", "Integer");
        mapTypeConvertorSqlserverToJava.put("varchar", "String");
        mapTypeConvertorSqlserverToJava.put("nvarchar", "String");
        mapTypeConvertorSqlserverToJava.put("tinyint", "Integer");
        mapTypeConvertorSqlserverToJava.put("decimal", "Double");
        mapTypeConvertorSqlserverToJava.put("float", "Double");
        mapTypeConvertorSqlserverToJava.put("image", "Byte[]");
        mapTypeConvertorSqlserverToJava.put("bit", "Boolean");
        mapTypeConvertorSqlserverToJava.put("datetime", "Date");
        mapTypeConvertorSqlserverToJava.put("varbinary", "Byte[]");
        mapTypeConvertorSqlserverToJava.put("smallint", "Integer"); // tinyint kullanılabilir


        //}
        return mapTypeConvertorSqlserverToJava;
    }

    public static Map<String, String> getMapTypeConvertorSqlserverToCsharp() {
        //if (mapTypeConvertorSqlserverToJava == null) {

        Map<String, String> mapTypeConvertorSqlserverToJava = new HashMap();

        mapTypeConvertorSqlserverToJava.put("int", "int");
        mapTypeConvertorSqlserverToJava.put("varchar", "string");
        mapTypeConvertorSqlserverToJava.put("nvarchar", "string");
        mapTypeConvertorSqlserverToJava.put("tinyint", "int");
        mapTypeConvertorSqlserverToJava.put("decimal", "double");
        mapTypeConvertorSqlserverToJava.put("float", "double");
        mapTypeConvertorSqlserverToJava.put("image", "Byte[]");
        mapTypeConvertorSqlserverToJava.put("bit", "bool");
        mapTypeConvertorSqlserverToJava.put("datetime", "DateTime");
        mapTypeConvertorSqlserverToJava.put("varbinary", "Byte[]");
        mapTypeConvertorSqlserverToJava.put("smallint", "int"); // tinyint kullanılabilir


        //}
        return mapTypeConvertorSqlserverToJava;
    }

    public static Map<String, String> getMapTypeConvertorJavaToSqlServer() {

        //if (mapTypeConvertorJavaToSqlServer == null) {

        Map<String, String> mapTypeConvertorJavaToSqlServer = new HashMap();

        // Integer
        mapTypeConvertorJavaToSqlServer.put("Integer", "int");
        // Integer Precision=20
        mapTypeConvertorJavaToSqlServer.put("IntegerP20", "bigint");
        // Integer Precision=1
        mapTypeConvertorJavaToSqlServer.put("IntegerP1", "tinyint");
        mapTypeConvertorJavaToSqlServer.put("Int16", "tinyint");

        mapTypeConvertorJavaToSqlServer.put("Short", "tinyint");
        mapTypeConvertorJavaToSqlServer.put("Long", "int");

        // Floating Point , hepsi decimal olarak yorumlandı
        mapTypeConvertorJavaToSqlServer.put("Float", "decimal");
        mapTypeConvertorJavaToSqlServer.put("Double", "decimal");
        mapTypeConvertorJavaToSqlServer.put("BigDecimal", "decimal");

        // String ( utf support olursa nvarchar a çevrilir)
        mapTypeConvertorJavaToSqlServer.put("String", "varchar");
        // string utf support lu ise , türü string utf olarak yapılır
        mapTypeConvertorJavaToSqlServer.put("StringUtf", "nvarchar");
        // Date Time
        mapTypeConvertorJavaToSqlServer.put("Date", "datetime");
        mapTypeConvertorJavaToSqlServer.put("LocalDate", "datetime");

        // Binary
        mapTypeConvertorJavaToSqlServer.put("ByteImage", "image");

        mapTypeConvertorJavaToSqlServer.put("Boolean", "bit");

        //mapTypeConvertorJavaToSqlServer.put("varbinary","Byte[]");
        //mapTypeConvertorJavaToSqlServer.put("smallint","Integer"); // tinyint kullanılabilir
//
//			if (sqlFieldType.equalsIgnoreCase("double")) {
//				if (field.getScale() > 5) sqlFieldType = "decimal";
//			}
//
//			if (sqlFieldType.equals("datetime") && field.getTemporalType() != null) {
//
//				if (field.getTemporalType() == TemporalType.DATE) {
//					sqlFieldType = "date";
//				}
//
//			}
//
//			if (sqlFieldType.equals("float")) {
//
//				if (field.getPrecision() > 0 && field.getScale() > 0) {
//					sqlFieldType = "decimal"; // default precision 18 ,scale 2
//				}
//			}


        //}

        return mapTypeConvertorJavaToSqlServer;
    }


    public static String updateScopeIdFieldWithScopeIdFnById(Class clazz, String fieldForScopeEntity) {

        //String sqlUpRecId = "\nUPDATE CARI_HESAPLAR SET cari_RECid_RECno = cari_RECno WHERE cari_RECno = SCOPE_IDENTITY();\nSELECT SCOPE_IDENTITY();";

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append(String.format("UPDATE %s SET %s = SCOPE_IDENTITY()", getTableName(clazz), fieldForScopeEntity));

        Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoIdField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = SCOPE_IDENTITY()");
                continue;
            }

            //index++;
            //if (index != 1) query.append(", ");
            //query.append(fiField.getName() + " = @" + fiField.getName());

        }

        query.append(" WHERE " + queryWhere);
        // Test amaçlı scope identity dönüyor mu diye bakıldı, dönmüyor
        //query.append(";\nSELECT SCOPE_IDENTITY();");

        if (queryWhere.length() < 1) {
            //query = new StringBuilder();
            return "error where";
        }

        return query.toString();
    }

    public static String updateScopeIdFieldWithSIdById(Class clazz, String fieldForScopeEntity) {

        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

        StringBuilder query = new StringBuilder();
        StringBuilder queryWhere = new StringBuilder();

        query.append(String.format("UPDATE %s SET %s = @scopeId", getTableName(clazz), fieldForScopeEntity));

        //Integer index = 0;
        Integer indexWhere = 0;
        for (FiField fiField : fieldListFilterAnno) {

            if (FiBool.isTrue(fiField.getBoIdField())) {
                indexWhere++;
                if (indexWhere != 1) queryWhere.append(" AND ");
                queryWhere.append(fiField.getName() + " = @scopeId");
                continue;
            }

            //index++;
            //if (index != 1) query.append(", ");
            //query.append(fiField.getName() + " = @" + fiField.getName());

        }

        query.append(" WHERE " + queryWhere);
        // Test amaçlı scope identity dönüyor mu diye bakıldı, dönmüyor
        //query.append(";\nSELECT SCOPE_IDENTITY();");

        if (queryWhere.length() < 1) {
            //query = new StringBuilder();
            return "error where";
        }

        return query.toString();
    }

    public static String getIdField(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        for (FiField fiField : fieldList) {
            if (FiBool.isTrue(fiField.getBoIdField())) {
                return fiField.getName();
            }
        }

        return null;
    }

    public static String getCandIdFieldFirst(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);

        for (FiField fiField : fieldList) {
            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                return fiField.getName();
            }
        }

        return null;
    }

    public static String getCandId1FirstField(Class clazz) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(clazz);
        return getCandId1FirstField(fieldList);

    }

    public static String getCandId1FirstField(List<FiField> fiFields) {

        for (FiField fiField : fiFields) {
            if (FiBool.isTrue(fiField.getBoCandidateId1())) {
                return fiField.getName();
            }
        }

        return null;
    }

    public static <EntClazz> String getScopeIdentityFirstField(Class<EntClazz> entityClass) {

        List<FiField> fieldList = FiEntity.getListFieldsShortWithId(entityClass);

        for (FiField fiField : fieldList) {
            if (FiBool.isTrue(fiField.getBoScopeIdField())) {
                return fiField.getName();
            }
        }

        return null;
    }


    public static Fdr<List<String>> getAlterAddFieldQueries(Class clazz, Jdbi jdbi) {

        Fdr<List<String>> fdrAlter = new Fdr<>();

        if (jdbi == null) {
            return fdrAlter.buiMessage("Veritabanı bağlantısı ayarlanmamış.");
        }

        Map<String, FiField> mapDbFields = getMapDbFields(clazz, jdbi);

        List<FiField> listFiFieldsSummary = getFieldsWithDbDefinitionFromCode(clazz);

        List<String> listAlterQueries = new ArrayList<>();

        for (FiField fiField : listFiFieldsSummary) {

            if (!mapDbFields.containsKey(fiField.getDbFieldName())) { // veritabanında ilgili alan yok
                Loghelper.debugLog(FiQugen.class, "Veritabanında ilgili alan yok:" + fiField.getDbFieldName());
                // ALTER TABLE [dbo].[EntAktarimFirma] ADD [afrTxFirmaHavaleBanka] varchar(25) COLLATE Turkish_CI_AS NULL
                String addQuery = String.format("ALTER TABLE %s ADD %s %s", getTableName(clazz), fiField.getDbFieldName(), fiField.getSqlFieldDefinition());
                listAlterQueries.add(addQuery);
            }

        }

        fdrAlter.setValue(listAlterQueries);
        fdrAlter.setBoResult(true);
        return fdrAlter;
    }

    public static List<FiField> getQueryFields(String sqlQuery) {

        //String regex = "(?isU).*select\\b(.*\\s*)\\bfrom";
        final String regex = ".*select\\b(.*\\s*)\\bfrom";

        //final String subst = "$1$2";

        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(sqlQuery);

        List<FiField> fieldList = new ArrayList<>();
        if (matcher.find()) {
            String text = matcher.group(1);
            System.out.println(text);

            String[] split = text.split(",");

            for (String colName : split) {

                if (colName.matches("\\040*\\w+\\040+as\\040(.*)")) {
                    colName = colName.replaceAll("\\040*\\w+\\040+as\\040(.*)", "$1");
                }

                FiField fiField = new FiField();
                String queryColumn = colName.trim();
                fiField.setDbFieldName(queryColumn);
                fiField.setName(queryColumn);
                fieldList.add(fiField);
            }

        }

        //System.out.println("Substitution result: " + result);
        return fieldList;
    }

    public static List<String> getQueryFieldsAsString(String sqlQuery) {

        //String regex = "(?isU).*select\\b(.*\\s*)\\bfrom";
        final String regex = ".*select\\b(.*\\s*)\\bfrom";

        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(sqlQuery);

        List<String> fieldList = new ArrayList<>();
        if (matcher.find()) {
            String text = matcher.group(1);
            System.out.println(text);

            String[] split = text.split(",");

            for (String colName : split) {

                colName = colName.trim();
                String regex1 = ".*\\s+as\\s(.*)"; // \040*\w+\040+as\040(.*)

                if (colName.matches(regex1)) {
                    colName = colName.replaceAll(regex1, "$1");
                }
//				else{
//					Loghelper.debug(FiQueryHelper.class, "colName:"+colName);
//				}

                fieldList.add(colName.trim());
            }

        }

        return fieldList;
    }

    /**
     * Db1 den Db2 ye kopyalanacak
     *
     * @param selectedTable
     * @param serverDb1
     * @param serverDb2
     * @return
     */
    public static String insertSelectQuery(String selectedTable, String serverDb1, String serverDb2) {

        // INSERT INTO table2
        // SELECT * FROM table1
        // WHERE condition;

        String sql = String.format("INSERT INTO %s.dbo.%s\n" +
                "SELECT * FROM %s.dbo.%s\n", serverDb2, selectedTable, serverDb1, selectedTable);
        return sql;
    }

    public static String insertSelectQueryWithDate(String selectedTable, String serverDb1, String serverDb2, String txDateFieldName, String txDateFieldValue) {

        String sql = String.format("INSERT INTO %s.dbo.%s\n" +
                "SELECT * FROM %s.dbo.%s\n" +
                "WHERE %s >= '%s'", serverDb2, selectedTable, serverDb1, selectedTable, txDateFieldName, txDateFieldValue);
        return sql;
    }

    public static String insertSelectQueryWithColsAndDate(String selectedTable, String serverDb1, String serverDb2, String txDateFieldName, String txDateFieldValue, Jdbi jdbi) {
        //String txFieldsCommaSeperated = getFieldsCommaSeperated(clazz);
        String txFieldsCommaSeperated = getFieldsCommaSeperatedFromDb(selectedTable, jdbi);
        if (txDateFieldValue == null) txDateFieldValue = "";
        if (!txDateFieldValue.contains("@")) {
            txDateFieldValue = "'" + txDateFieldValue + "'";
        }

        String sql = String.format("SET IDENTITY_INSERT %s.dbo.%s ON\n\n" +
                "INSERT INTO %s.dbo.%s (%s)\n" +
                "SELECT %s FROM %s.dbo.%s\n" +
                "WHERE %s >= %s\n\n" +
                "SET IDENTITY_INSERT %s.dbo.%s OFF\n", serverDb2, selectedTable, serverDb2, selectedTable, txFieldsCommaSeperated, txFieldsCommaSeperated, serverDb1, selectedTable, txDateFieldName, txDateFieldValue, serverDb2, selectedTable);
        return sql;
    }

    public static String insertSelectQueryWithCols(String selectedTable, String serverDb1, String serverDb2, Jdbi jdbi) {
        //String txFieldsCommaSeperated = getFieldsCommaSeperated(clazz);
        String txFieldsCommaSeperated = getFieldsCommaSeperatedFromDb(selectedTable, jdbi);
        String sql = String.format("SET IDENTITY_INSERT %s.dbo.%s ON\n\n" +
                "INSERT INTO %s.dbo.%s (%s)\n" +
                "SELECT %s FROM %s.dbo.%s\n\n" +
                "SET IDENTITY_INSERT %s.dbo.%s OFF\n", serverDb2, selectedTable, serverDb2, selectedTable, txFieldsCommaSeperated, txFieldsCommaSeperated, serverDb1, selectedTable, serverDb2, selectedTable);
        return sql;
    }

    private static String getFieldsCommaSeperated(Class clazz) {
        List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);
        StringBuilder query = new StringBuilder();

        Integer index = 0;
        for (FiField fiField : fieldListFilterAnno) {
            index++;
            if (index != 1) query.append(", ");
            query.append(fiField.getName());
        }
        return query.toString();
    }

    private static String getFieldsCommaSeperatedFromDb(String txTableName, Jdbi jdbi) {
        Fdr<List<EntSqlColumn>> fieldListFilterAnno = new RepoSqlColumn(jdbi).selectColumnsAll(txTableName);
        StringBuilder query = new StringBuilder();

        if (fieldListFilterAnno.isTrueBoResult()) {
            Integer index = 0;
            for (EntSqlColumn entSqlColumn : fieldListFilterAnno.getValue()) {
                index++;
                if (index != 1) query.append(", ");
                query.append(entSqlColumn.getCOLUMN_NAME());
            }
            return query.toString();
        } else {
            return "";
        }

    }

    public static String selectAllSimple(String txTableName) {
        if (FiString.isEmptyTrim(txTableName)) return "";
        return String.format("SELECT * FROM %s", txTableName);
    }


}

/*

USE [OZPASWEB];
		GO
		SET ANSI_NULLS ON;
		GO
		SET QUOTED_IDENTIFIER ON;
		GO     -- LNG int, BYT tinyint , TXT varchar , TRH tarih , UID guid ,
		CREATE TABLE [dbo].[TBLMSDTAHSILAT2] (
		[LNGDISTKOD] int NOT NULL,
		[LNGYIL] int NOT NULL,
		[LNGKOD] int IDENTITY(1, 1) NOT NULL,
		[BYTTIP] tinyint NOT NULL,    // short
		[BYTACIKKAPALI] tinyint NOT NULL,
		[LNGMUSTERIKOD] int NOT NULL,
		[LNGSTKOD] int NOT NULL,
		[LNGTAHSILATSTKOD] int NOT NULL,
		[TXTMAKBUZNO] varchar(10) COLLATE Turkish_CI_AS NULL,
		[TXTBELGENO] varchar(30) COLLATE Turkish_CI_AS NULL,
		[TXTACIKLAMA] nvarchar(100) COLLATE Turkish_CI_AS NULL,
		[DBLTUTAR] decimal(28, 8) NOT NULL,
		[TRHISLEMTARIHI] datetime NOT NULL,
		[TRHVADETARIHI] datetime NULL,
		[TRHODEMETARIHI] datetime NULL,
		[BYTBAKIYEKOD] tinyint NOT NULL,
		[BYTGIRISTIP] tinyint NOT NULL,
		[BYTBASIMDURUM] tinyint NOT NULL,
		[BYTDURUM] tinyint NOT NULL,
		[BYTODENDI] tinyint NOT NULL DEFAULT ((1)),
		[TXTBANKA] varchar(50) COLLATE Turkish_CI_AS NULL,
		[TXTBANKASUBE] varchar(50) COLLATE Turkish_CI_AS NULL,
		[TXTESASBORCLU] varchar(100) COLLATE Turkish_CI_AS NULL,
		[TXTKESIDEYERI] varchar(30) COLLATE Turkish_CI_AS NULL,
		[TXTCEKNO] varchar(30) COLLATE Turkish_CI_AS NULL,
		[TXTKARTNO] varchar(30) COLLATE Turkish_CI_AS NULL,
		[TRHSONKULLANMATARIHI] datetime NULL,
		[TRHSONISLEMTARIHI] datetime NOT NULL DEFAULT (getdate()),
		[LNGSONKULLANICIKOD] int NOT NULL,
		[TRHILKISLEMTARIHI] datetime NOT NULL DEFAULT (getdate()),
		[LNGILKKULLANICIKOD] int NOT NULL,
		[TXTREFERANS] varchar(100) COLLATE Turkish_CI_AS NULL,
		[LNGFATURAKOD] bigint NULL,
		[BYTAKTARILDI] tinyint NOT NULL DEFAULT ((0)),
		[LNGISLEMTIP] int NOT NULL,
		[LNGSIPARISKOD] bigint NULL,
		[UIDGUIDKOD] uniqueidentifier NULL,
		[LNGBANKAKOD] int NULL,
		[LNGIRSALIYEKOD] bigint NULL,
		[LNGERPKOD] int NULL,
		[DBLKOMISYONORAN] decimal(28, 8) NULL,
		[DBLKOMISYONTUTAR] decimal(28, 8) NULL,
		[BYTTAKSIT] tinyint NULL,
		[BYTKAPATMADURUM] tinyint NULL DEFAULT ((0)),
		[TXTHESAPNO] nvarchar(50) COLLATE Turkish_CI_AS NULL,
		[BYTONAY] tinyint NULL,
		[TRHONAYTARIHI] date NULL,
		[BYTKESIDEYERI] tinyint NULL DEFAULT ((0)),
		[TXTVERGINO] nvarchar(11) COLLATE Turkish_CI_AS NULL,
		[LNGVERGIDAIRESI] int NULL,
		[LNGKKBANKAKOD] int NULL,
		[LNGTAHSILATIPTALNEDEN] int NULL,
		[LNGPROFORMAFATURAKOD] int NULL,
		[LNGDOVIZKOD] int NULL,
		[LNGPARENTKOD] int NULL,
		[LNGVIRMANDETAYKOD] int NULL,
		[LNGODEMEPLANI] int NULL,
		[LNGRUTGRUPKOD] int NULL,
		PRIMARY KEY CLUSTERED ([LNGYIL] ASC, [LNGKOD] ASC)
		WITH ( PAD_INDEX = OFF,
		FILLFACTOR = 100,
		IGNORE_DUP_KEY = OFF,
		STATISTICS_NORECOMPUTE = OFF,
		ALLOW_ROW_LOCKS = ON,
		ALLOW_PAGE_LOCKS = ON,
		DATA_COMPRESSION = NONE )
		ON [PRIMARY],
		FOREIGN KEY ([LNGDISTKOD])
		REFERENCES [dbo].[TBLDIST] ( [LNGKOD] ),
		FOREIGN KEY ([LNGMUSTERIKOD])
		REFERENCES [dbo].[TBLMUSTERI] ( [LNGKOD] ),
		FOREIGN KEY ([LNGSTKOD])
		REFERENCES [dbo].[TBLSATISTEMSILCISI] ( [LNGSTKOD] ),
		FOREIGN KEY ([LNGTAHSILATSTKOD])
		REFERENCES [dbo].[TBLSATISTEMSILCISI] ( [LNGSTKOD] ),
		FOREIGN KEY ([LNGISLEMTIP])
		REFERENCES [dbo].[TBLISLEMTIP] ( [LNGKOD] )
		)
		ON [PRIMARY];
		GO
		ALTER TABLE [dbo].[TBLMSDTAHSILAT2] SET (LOCK_ESCALATION = TABLE);
		GO


*/
