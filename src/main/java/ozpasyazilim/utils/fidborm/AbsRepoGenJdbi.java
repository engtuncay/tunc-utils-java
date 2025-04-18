package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import org.reactfx.util.TriConsumer;
import ozpasyazilim.utils.core.FiException;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.annotations.FiDraft;
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.core.FiReflection;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static ozpasyazilim.utils.core.FiStFormat.*;

/**
 * Abs Repo Generic Jdbi (with EntClazz Generic)
 *
 * @param <EntClazz>
 */
public abstract class AbsRepoGenJdbi<EntClazz> extends AbsRepoGenMainJdbi<EntClazz> implements IRepoJdbi {

    /**
     * connProfile veya jdbi içeren constructor kullanılmalı
     */
    @Deprecated
    public AbsRepoGenJdbi() {
        setAutoClass();
    }

    public AbsRepoGenJdbi(String connProfile) {
        setConnProfile(connProfile);
        setAutoClass();
    }

    public AbsRepoGenJdbi(Jdbi jdbi, Class clazz) {
        this.jdbi = jdbi;
        this.entityClass = clazz;
    }

    public AbsRepoGenJdbi(Class clazz) {
        setEntityClass(clazz);
    }

    public AbsRepoGenJdbi(Jdbi jdbi) {
        setJdbi(jdbi);
        setAutoClass();
    }

    public AbsRepoGenJdbi(Handle handleRepo) {
        setHandleRepo(handleRepo);
    }

    // Sorgu Metodları

    /*
     * Not Null Return
     *
     */
    public Fdr<List<EntClazz>> jdSelectListBindEntity(String sqlQuery, EntClazz entClazz) {
        return jdSelectListBindObjectMain(sqlQuery, entClazz);
    }

    /**
     * FiDbResult içi default olarak new Arraylist ile doldurulmadığı için kullanan metodlar kontrol edilecek
     * <p>
     * daha sonra silinecek
     *
     * @param sqlQuery
     * @param mapBind
     * @return
     */
    @Deprecated
    public Fdr<List<EntClazz>> jdSelectList2(String sqlQuery, Map<String, Object> mapBind) {

        if (entityClass == null) setAutoClass();

        Jdbi jdbi = getJdbi();

        Fdr<List<EntClazz>> fdr = new Fdr<>();

        List<EntClazz> result = null;
        try {
            result = jdbi.withHandle(handle -> {
                return handle.createQuery(Fiqt.stoj(sqlQuery))
                        .bindMap(mapBind)
                        .mapToBean(getEntityClass())
                        .list();
            });

            fdr.setFdrBoExec(true);
            fdr.setValue(result);

        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    public List<EntClazz> jdSelectListMultiRaw(String sqlQuery, Map<String, Object> mapBind) {

//        if (entityClass == null) setAutoClass();

        sqlQuery = Fiqt.fhrFixAndDeActivateOptParams(sqlQuery);

        String sqlNew = convertSqlAndMapToMultiParam(sqlQuery, mapBind);

        return jdSelectListBindMapRaw(sqlNew, mapBind);

    }

    /**
     * map için list olan parametreleri sql sorgusunun için multi param çevirir
     *
     * @param sqlQuery
     * @param mapBind
     * @return
     */
    public Fdr<List<EntClazz>> jdSelectListMultiMain(String sqlQuery, Map<String, Object> mapBind) {

        if (entityClass == null) setAutoClass();

        sqlQuery = Fiqt.fhrFixAndDeActivateOptParams(sqlQuery);

        String sqlNew = convertSqlAndMapToMultiParam(sqlQuery, mapBind);

        return jdSelectListBindMapMainNtn(sqlNew, mapBind);

    }

    public Fdr<List<EntClazz>> jdUpdateMultiMain(String sqlQuery, Map<String, Object> mapBind) {

        if (entityClass == null) setAutoClass();

        sqlQuery = Fiqt.fhrFixAndDeActivateOptParams(sqlQuery);

        String sqlNew = convertSqlAndMapToMultiParam(sqlQuery, mapBind);

        return jdUpdateBindMapMain(sqlNew, mapBind);

    }

    private String convertSqlAndMapToMultiParam(String sqlQuery, Map<String, Object> mapBind) {

        if (mapBind == null) return sqlQuery;

        Map<String, List> mapParamMulti = new HashMap<>();

        Map<String, Object> mapParamNew = new HashMap<>();

        mapBind.forEach((param, value) -> {

            if (value instanceof List) {

                List listData = (List) value;
                //Loghelperr.getInstance(getClass()).debug("Field List:"+param.toString());
                mapParamMulti.put(param, listData);

                Integer index = -1;
                for (Object listDatum : listData) {
                    index++;
                    String sablon = param + "_" + index.toString();
                    mapParamNew.put(sablon, listData.get(index));
                }

            } else {
                //Loghelperr.getInstance(getClass()).debug("Field Prim:"+param.toString());
                //mapParamNew.put(param,value);
            }


        });

        for (Map.Entry<String, List> entry : mapParamMulti.entrySet()) {
            //System.out.println(entry.getKey() + "/" + entry.getValue());
            sqlQuery = Fiqt.convertSqlForMultiParamByTemplate2(sqlQuery, entry.getKey(), entry.getValue().size());
            mapBind.remove(entry.getKey());
        }

        mapBind.putAll(mapParamNew);
        return sqlQuery;
    }

    public List<EntClazz> jdSelectListBindEntityRaw(String sqlQuery, EntClazz bindEntity) {

        if (entityClass == null) setAutoClass();

        Jdbi jdbi = getJdbi();

        List<EntClazz> result = null;
        try {
            result = jdbi.withHandle(handle -> {
                return handle.select(Fiqt.stoj(sqlQuery))
                        .bindBean(bindEntity)
                        .mapToBean(getEntityClass())
                        .list();
            });

        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
        }

        return result;
    }

    public List<EntClazz> jdSelectListNested(String sqlQuery, Map<String, Object> mapBind) {

        if (entityClass == null) setAutoClass();

        Jdbi jdbi = getJdbi();

        List<EntClazz> result = null;
        try {
            result = jdbi.withHandle(handle -> {
                return handle.select(fif(sqlQuery).sqlFmtAt())
                        .bindMap(mapBind)
                        .map(new FiBeanNestedRowMapper<>(getEntityClass()))
                        //.mapToBean(getEntityClass())
                        .list();
            });

        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
        }

        return result;
    }


    public List<EntClazz> jdSelectAllRaw(Integer rowCount) {
        if (entityClass == null) setAutoClass();

        Jdbi jdbi = getJdbi();
        List<EntClazz> result = null;

        try {
            result = jdbi.withHandle(handle -> {
                return handle.select(FiQugen.selectTopQuery(getEntityClass(), rowCount))
                        .mapToBean(getEntityClass())
                        .list();
            });
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
        }

        return result;
    }


    /**
     * @return Not Null
     */
    public Fdr<List<EntClazz>> jdSelectAllDtoOrderByCandId() {

        Fdr<List<EntClazz>> fdr = new Fdr<>();
        List<EntClazz> result = new ArrayList<>();
        fdr.setValue(result);

        try {
            result = getJdbi().withHandle(handle -> {
                return handle.select(FiQugen.selectDtoFieldsWoutWhereOrderByCandIdField(getEntityClass()))
                        .mapToBean(getEntityClass())
                        .list();
            });
            fdr.setFdrBoExec(true);
            fdr.setValue(result);
        } catch (Exception ex) {
            Loghelper.get(getClass()).error(FiException.exTosMain(ex));
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    /**
     * not null return
     * <p>
     * after false operation, return empty set
     *
     * @return
     */
    public Fdr<List<EntClazz>> jdSelectAll() {

        Fdr<List<EntClazz>> fdr = new Fdr<>();

        List<EntClazz> result = new ArrayList<>();
        fdr.setValue(result);

        try {
            result = getJdbi().withHandle(handle -> {
                return handle.select(FiQugen.selectTopQuery(getEntityClass(), null))
                        .mapToBean(getEntityClass())
                        .list();
            });
            fdr.setFdrBoExec(true);
            fdr.setValue(result);
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    public Fdr<List<EntClazz>> jdSelectAllDtoByWhere1(EntClazz entity) {

        Fdr<List<EntClazz>> fdr = new Fdr<>();
        List<EntClazz> result = new ArrayList<>();
        fdr.setValue(result);

        try {
            result = getJdbi().withHandle(handle -> {
                return handle.select(Fiqt.stoj(FiQugen.selectDtoFieldsWithWhere1(getEntityClass())))
                        .bindBean(entity)
                        .mapToBean(getEntityClass())
                        .list();
            });
            fdr.setFdrBoExec(true);
            fdr.setValue(result);
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    public Fdr<List<EntClazz>> jdSelectAllDtoByFirmFieldsBindMap(FiKeyBean fiKeyBean) {
        //String sqlQuery = FiQugen.selectDtoFieldsByFirmFields(getEntityClass());
        FiQuery fiQuery = new FiQuery(FiQugen.selectDtoFieldsByFirmFields(getEntityClass()), fiKeyBean);
        //fiQuery.logQuery();
        //fiQuery.logParams();
        return jdSelectListBindMapMainNtn(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    public Fdr<List<FiKeyBean>> jdfSelectAllDtoByFirmFieldsBindMap(FiKeyBean fiKeyBean) {
        //String sqlQuery = FiQugen.selectDtoFieldsByFirmFields(getEntityClass());
        FiQuery fiQuery = new FiQuery(FiQugen.selectDtoFieldsByFirmFields(getEntityClass()), fiKeyBean);
        //fiQuery.logQuery();
        //fiQuery.logParams();
        return jdSelectFkbListBindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    public Fdr<List<FiKeyBean>> jdfSelectAllDtoOrderByIdField() {
        FiQuery fiQuery = new FiQuery(FiQugen.selectDtoFieldsOrderByIdField(getEntityClass()));
        //fiQuery.logQuery();
        return jdSelectFkbListBindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    public Fdr<List<EntClazz>> jdSelectAllDtoWhereFicols(FiKeyBean fkbSorgu, List<FiCol> fiColsWhere) {
        FiQuery fiQuery = new FiQuery(FiQugen.selectAllDtoWherFiCols(getEntityClass(), fiColsWhere), fkbSorgu);
        //fiQuery.logQuery();
        //fiQuery.logParams();
        return jdSelectListBindMapMainNtn(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    public List<EntClazz> jdSelectLike(Integer rowCount, EntClazz entity) {
        if (entityClass == null) setAutoClass();

        Jdbi jdbi = getJdbi();
        List<EntClazz> result = null;
        try {
            result = jdbi.withHandle(handle -> {
                return handle.select(fif(FiQugen.selectTopQueryLike(getEntityClass(), 100, entity)).sqlFmtAt())
                        .bindBean(entity)
                        .mapToBean(getEntityClass())
                        .list();
            });

        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
        }

        return result;
    }

    public Optional<Integer> jdSelectCountByAllFields(EntClazz entity) {

        if (entityClass == null) setAutoClass();

        Jdbi jdbi = getJdbi();
        Optional<Integer> result = null;
        try {
            result = jdbi.withHandle(handle -> {
                return handle.select(Fiqt.stoj(FiQugen.selectQueryCountWherAllFields(getEntityClass(), entity)))
                        .bindBean(entity)
                        .mapTo(Integer.class)
                        .findFirst();
            });

        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
        }

        return result;
    }

    public Fdr<Optional<Integer>> jdSelectCountByCandId1First(Object candId1Value) {

        if (entityClass == null) setAutoClass();

        if (candId1Value == null) {
            return Fdr.creEmptyAndResultFalse();
        }

        String candId1First = FiQugen.getCandId1FirstField(getEntityClass());
        String sqlQuery = FiQugen.selectQueryCountByCandIdFirst(getEntityClass());
        FiKeyBean map = FiKeyBean.bui().buiPut(candId1First, candId1Value);

        return jdSelectSingleOpCustomEntityBindMapNtn(sqlQuery, map, Integer.class);
    }

    public Fdr<Optional<Integer>> jdSelectCountByCandId1(EntClazz entity) {

        if (entityClass == null) setAutoClass();

        //String candId1First = FiQueryHelper.getCandId1FirstField(getEntityClass());
        String sqlQuery = FiQugen.selectQueryCountByCandId1s(getEntityClass());

        return jdSelectSingleIntBindEntity(sqlQuery, entity);

    }

    public Fdr<Optional<Integer>> jdCountByCandIds(EntClazz entClazz) {

        if (entityClass == null) setAutoClass();

//		String candId1First = FiQueryGenerator.getCandId1FirstField(getEntityClass());
        String sqlQuery = FiQugen.selectQueryCountByCandId1s(getEntityClass());

        Fdr<Integer> result = new Fdr<>();

        Fdr<Optional<Integer>> fdr = jdSelectSingleOptIntegerValBindBean(sqlQuery, entClazz);

        return fdr;
    }

    public Fdr<Optional<Integer>> jdSelectSingleIntOptBindMapWithDeAct(String sqlQuery, FiKeyBean map) {

        Fdr<Optional<Integer>> fdr = new Fdr<>();

        try {
            Optional<Integer> result = getJdbi().withHandle(handle -> {
                return handle.select(Fiqt.fimSqlQueryWithDeActType1(sqlQuery))
                        .bindMap(map)
                        .mapTo(Integer.class)
                        .findFirst();
            });
            fdr.setBoResultAndValue(true, result, 1);
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
            fdr.setValue(Optional.empty());
        }

        return fdr;

    }

    public Fdr<Optional<Integer>> jdSelectSingleIntOptBindEntity(String sqlQuery, EntClazz entClazz) {

        Fdr<Optional<Integer>> fdr = new Fdr<>();

        try {
            Optional<Integer> result = getJdbi().withHandle(handle -> {
                return handle.select(Fiqt.fimSqlQueryWithDeActType1(sqlQuery))
                        .bindBean(entClazz)
                        .mapTo(Integer.class)
                        .findFirst();
            });
            fdr.setBoResultAndValue(true, result, 1);
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
            fdr.setValue(Optional.empty());
        }

        return fdr;

    }

    public Fdr<Optional<Integer>> jdhSelectSingleIntOptBindMap(Handle handle, String sqlQuery, FiKeyBean map) {

        Fdr<Optional<Integer>> fdr = new Fdr<>();

        try {
            Optional<Integer> result = handle.select(Fiqt.stoj(sqlQuery))
                    .bindMap(map)
                    .mapTo(Integer.class)
                    .findFirst();
            fdr.setBoResultAndValue(true, result, 1);
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
            fdr.setValue(Optional.empty());
        }

        return fdr;
    }

    public List<EntClazz> jdSelectAllFiSelect(Integer rowCount) {
        if (entityClass == null) setAutoClass();

        Jdbi jdbi = getJdbi();
        List<EntClazz> result = null;
        try {
            result = jdbi.withHandle(handle -> {
                return handle.select(FiQugen.selectQueryFiSelect(getEntityClass(), 100))
                        .mapToBean(getEntityClass())
                        .list();
            });

        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
        }

        return result;
    }

    public Fdr jdDeleteById(EntClazz entity) {
        String sqlDelete = FiQugen.deleteById(getEntityClass());
        return jdUpdateBindEntityMain(sqlDelete, entity);
    }

    public Fdr jdhDeleteById(EntClazz entity, Handle handle) {
        String sqlDelete = FiQugen.deleteById(getEntityClass());
        return jdhUpdateBindEntityMain(handle, sqlDelete, entity);
    }

    public Fdr jdDeleteByCandId1(EntClazz entity) {
        String sqlDelete = FiQugen.deleteByCandId1(getEntityClass());
        return jdUpdateBindEntityMain(sqlDelete, entity);
    }

    public Fdr jdhDeleteByCandId1(EntClazz entity, Handle handle) {
        String sqlDelete = FiQugen.deleteByCandId1(getEntityClass());
        return jdhUpdateBindEntityMain(handle, sqlDelete, entity);
    }

    public Fdr jdDeleteByCandId2(EntClazz entity) {
        String sqlDelete = FiQugen.deleteByCandId2(getEntityClass());
        return jdUpdateBindEntityMain(sqlDelete, entity);
    }

    public Fdr jdhDeleteByCandId2(EntClazz entity, Handle handle) {
        String sqlDelete = FiQugen.deleteByCandId2(getEntityClass());
        return jdhUpdateBindEntityMain(handle, sqlDelete, entity);
    }

    public Fdr jdDeleteByCandId2IntList(List<Integer> listData) {

        String sql = FiQugen.deleteByCandId2ByInFormat(getEntityClass());

        String dbFieldName = FiReflectClass.getListFieldsCandId2(getEntityClass()).get(0).getOfcTxDbField();

        FiKeyBean fiKeyBean = FiKeyBean.bui().buiPut(dbFieldName, listData);

        FiQuery fiQuery = new FiQuery(sql);
        fiQuery.convertListParamsToMultiParams(fiKeyBean);

        return jdUpdateBindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    public Fdr jdhDeleteByCandId2IntList(List<Integer> listData, Handle handle) {

        String sql = FiQugen.deleteByCandId2ByInFormat(getEntityClass());

//		Loghelper.get(getClass()).debug("Delete query:" + sql);

        String dbFieldName = FiReflectClass.getListFieldsCandId2(getEntityClass()).get(0).getOfcTxDbField();

        FiKeyBean fiKeyBean = FiKeyBean.bui().buiPut(dbFieldName, listData);

        FiQuery fiQuery = new FiQuery(sql);
        fiQuery.convertListParamsToMultiParams(fiKeyBean);

//		Loghelper.get(getClass()).debug("Delete query(multi):" + fiQuery.getTxQuery());
//		Loghelper.get(getClass()).debug(FiConsole.logMain(fiMapParams));

        return jdhUpdateBindMap(fiQuery.getTxQuery(), fiQuery.getMapParams(), handle);
    }

    public Fdr jdDeleteByCandId2MapStringList(List<String> listData) {

        String sql = FiQugen.deleteByCandId2ByInFormat(getEntityClass());

        String dbFieldName = FiReflectClass.getListFieldsCandId2(getEntityClass()).get(0).getOfcTxDbField();

        FiKeyBean fiKeyBean = FiKeyBean.bui().buiPut(dbFieldName, listData);

        FiQuery fiQuery = new FiQuery(sql);
        fiQuery.convertListParamsToMultiParams(fiKeyBean);

        return jdUpdateBindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    public Fdr jdDeleteListByCandId(List<EntClazz> entityList) {
        // FIXME Tek Transaction çevirmek lazım
        Fdr fdr = new Fdr();

        for (EntClazz entClazz : entityList) {
            fdr.combineAnd(jdDeleteByCandId1(entClazz));
        }

        return fdr;
    }

    public Fdr jdDeleteListById(List<EntClazz> entityList) {
        // FIXME Tek Transaction çevirmek lazım
        Fdr fdr = new Fdr();

        for (EntClazz entClazz : entityList) {
            fdr.combineAnd(jdDeleteById(entClazz));
        }

        return fdr;
    }

    public Integer jdRunBatchUpdateQuery(List<String> queryList) {
        if (queryList == null) return null;

        Integer rowCountUpdate = null;

        try {
            rowCountUpdate = getJdbi().withHandle(handle -> {

                Integer rowAffectedLast = null;

                for (String query : queryList) {
                    rowAffectedLast = handle.createUpdate(query).execute();
                }
                //Loghelperr.getInstance(getClass()).debug("Affected:"+ rowAffectedLast);
                return rowAffectedLast;
            });
            return rowCountUpdate;
        } catch (Exception ex) {
            Loghelper.errorException(getClass(), ex);
            return null;
        }

    }

    public Fdr jdRunBatchUpdateQueryFi(List<String> queryList) {

        if (queryList == null || queryList.size() == 0) {
            return new Fdr(false, "Boş sorgu");
        }

        Fdr fdr = new Fdr();

        try {
            getJdbi().useHandle(handle -> {
                for (String query : queryList) {
                    if (FiString.isEmpty(query.trim())) continue;
                    Integer rowAffected = handle.createUpdate(query).execute();
                    //System.out.println("Rows Affe:"+ rowAffected.toString());//0 dönüyor , onun  için aşağıda bir eklendi.
                    fdr.appendRowsAffected(1);
                }
                //Loghelperr.getInstance(getClass()).debug("Affected:"+ rowAffectedLast);
            });
            fdr.setBoResultWithCheckRowsAffected(true);
        } catch (Exception ex) {
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    // XNOTE transaction için güzel bir örnek - jdbi
    public Fdr jdUpdateBatchWitTrans(List<String> queryList, FiKeyBean mapParams) {

        if (queryList == null || queryList.size() == 0) {
            return new Fdr(false, "Boş sorgu");
        }

        Fdr fdrMain = new Fdr();

        try {
            getJdbi().useTransaction(handle -> {

                for (String query : queryList) {
                    if (FiString.isEmpty(query.trim())) continue;
                    Integer rowAffected = handle.createUpdate(Fiqt.stoj(query))
                            .bindMap(mapParams).execute();
                    fdrMain.appendRowsAffected(rowAffected);
                }

            });
            fdrMain.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.get(getClass()).error(FiException.exTosMain(ex));
            fdrMain.setBoResult(false, ex);
        }

        return fdrMain;
    }

    public Fdr jdUpdateQueryListBindEntityWithTrans(List<String> queryList, Object bean) {

        if (queryList == null || queryList.size() == 0) {
            return new Fdr(null, "Çalıştırılacak sorgu yok.");
        }

        Fdr fdrAll = new Fdr();

        try {
            getJdbi().useTransaction(handle -> {

                for (String query : queryList) {
                    if (FiString.isEmpty(query.trim())) continue;
                    Fdr fdrQuery = jdhUpdateBindObjectMain(handle, query, bean);
                    fdrAll.combineAnd(fdrQuery);
                    //fiDbResultAll.appendRowsAffected(rowAffected);
                }
                //Loghelperr.getInstance(getClass()).debug("Affected:"+ rowAffectedLast);
            });
            fdrAll.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.errorException(getClass(), ex);
            fdrAll.setBoResult(false, ex);
        }

        return fdrAll;
    }

    @Deprecated
    @FiDraft
    public Fdr jdUpdateBatchAsScriptWitTrans(String query, FiKeyBean fiKeyBean) {

        if (FiString.isEmpty(query)) {
            return new Fdr(false, "Boş sorgu");
        }

        Loghelper.get(getClass()).debug("Query:" + query);

        Fdr fdr = new Fdr();

        try {
            getJdbi().useTransaction(handle -> {
                int[] rowAffected = handle.createScript(Fiqt.stoj(query)).bindMap(fiKeyBean).execute();
                fdr.appendRowsAffected(rowAffected);
                //Loghelperr.getInstance(getClass()).debug("Affected:"+ rowAffectedLast);
            });
            fdr.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    // Alt Sorgu Metodları

    public Fdr<Optional<EntClazz>> jdSelectEntityOptById(Integer id) {
        String sql = FiQugen.selectQuery20ById_oldway(getEntityClass());
        String idField = FiQugen.getIdField(getEntityClass());
        FiKeyBean fiKeyBean = FiKeyBean.bui().buiPut(idField, id);
        return jdSelectEntityOptBindMap(sql, fiKeyBean);
    }

    public Fdr<EntClazz> jdSelectEntityById(Integer id) {
        String sql = FiQugen.selectQuery20ByIdNew(getEntityClass());
        //Loghelper.get(getClass()).debug("jdSelectEntityById sql:" + sql);
        String idField = FiQugen.getIdField(getEntityClass());
        FiKeyBean fiKeyBean = FiKeyBean.bui().buiPut(idField, id);
        return jdSelectEntityBindMap(sql, fiKeyBean);
    }

    public Fdr<List<Map<String, Object>>> jdSelectListMapById(Integer id) {
        String sql = FiQugen.selectQuery20ById_oldway(getEntityClass());
        String idField = FiQugen.getIdField(getEntityClass());
        FiKeyBean fiKeyBean = FiKeyBean.bui().buiPut(idField, id);
        return jdSelectListMapBindMap(sql, fiKeyBean);
    }

    public Fdr<List<EntClazz>> jdSelectListById(Integer id) {
        String sql = FiQugen.selectQuery20ById_oldway(getEntityClass());
        String idField = FiQugen.getIdField(getEntityClass());
        FiKeyBean fiKeyBean = FiKeyBean.bui().buiPut(idField, id);
        return jdSelectListBindMapMainNtn(sql, fiKeyBean);
    }

    public Fdr<List<EntClazz>> jdSelectListByCandIds(EntClazz entClazz) {
        String sql = FiQugen.selectQueryByCandIds(getEntityClass());
        return jdSelectListBindEntity(sql, entClazz);
    }

    public Fdr<List<EntClazz>> jdSelectListByCandId2(EntClazz entClazz) {
        String sql = FiQugen.selectQueryByCandIds2(getEntityClass()).getTxQuery();
        return jdSelectListBindEntity(sql, entClazz);
    }

    public Fdr<List<EntClazz>> jdSelectListAllDtoByFiSelectFields(EntClazz entClazz) {
        String sql = FiQugen.selectAllDtoByFiSelectFields(getEntityClass());
        return jdSelectListBindEntity(sql, entClazz);
    }

    /**
     * Select  [Dto Alanlar] Where [FiWhere1 Alanlar]
     *
     * @param entClazz
     * @return
     */
    public Fdr<List<EntClazz>> jdSelectListAllDtoByFiWhere1(EntClazz entClazz) {
        String sql = FiQugen.selectAllDtoByFiWhere1(getEntityClass());
        //Loghelper.get(getClass()).debug("Query\n" + sql);
        return jdSelectListBindEntity(sql, entClazz);
    }

    public Fdr<List<EntClazz>> jdSelectEntityTop1() {
        String sql = FiQugen.selectTopQuery(getEntityClass(), 1);
        return jdSelectListBindMapMainNtn(sql, null);
    }

    public Fdr<Optional<EntClazz>> jdSelectEntityOptByStringCandId1(String txKod) {
        String sql = FiQugen.selectQueryByCandIds(getEntityClass());
        String txFieldName = FiQugen.getCandIdFieldFirst(getEntityClass());
        FiKeyBean fiKeyBean = FiKeyBean.bui().buiPut(txFieldName, txKod);
        return jdSelectEntityOptBindMap(sql, fiKeyBean);
    }

    public Fdr<EntClazz> jdSelectEntityByStringCandId1(String txKod) {
        //Loghelper.get(getClass()).debug("jdSelectEntityByStringCandId1 start");
        String sql = FiQugen.selectQueryByCandIds(getEntityClass());

        String txFieldName = FiQugen.getCandIdFieldFirst(getEntityClass());
        FiKeyBean fiKeyBean = FiKeyBean.bui().buiPut(txFieldName, txKod);

        Fdr<Optional<EntClazz>> optionalFdr = jdSelectEntityOptBindMap(sql, fiKeyBean);
        //Loghelper.get(getClass()).debug("jdSelect Sorgu Bitti");

        Fdr<EntClazz> fdr = new Fdr<>();
        fdr.combineAnd(optionalFdr);

        if (optionalFdr.getValue() != null && optionalFdr.getValue().isPresent()) {
            fdr.setValue(optionalFdr.getValue().get());
        }

        //Loghelper.get(getClass()).debug("jdSelectEntityByStringCandId1 end");
        return fdr;
    }

    public Fdr<Optional<EntClazz>> jdSelectEntityByCandIdOpt1(EntClazz entity) {
        String sql = FiQugen.selectQueryByCandIds(getEntityClass());
        return jdSelectEntityBindEntityOptMain(sql, entity);
    }

    public Fdr<EntClazz> jdSelectEntityByCandId1(EntClazz entity) {
        String sql = FiQugen.selectQueryByCandIds(getEntityClass());
        return jdSelectEntityBindEntityMain(sql, entity);
    }

    public Fdr<Optional<EntClazz>> jdSelectDtoEntityByStrCandId1(String txValue) {
        FiQuery fiQuery = FiQugen.selectDtoEntityByCandId1Fi(getEntityClass());
        FiKeyBean fiKeyBean = FiKeyBean.bui().buiPut(fiQuery.getTxCandIdFieldName(), txValue);
        return jdSelectEntityOptBindMap(fiQuery.getTxQuery(), fiKeyBean);
    }

    public Fdr<Optional<EntClazz>> jdSelectDtoEntityByCandId1(EntClazz entity) {
        FiQuery sql = FiQugen.selectDtoEntityByCandId1Fi(getEntityClass());
        return jdSelectEntityBindEntityOptMain(sql.getTxQuery(), entity);
    }

    public Fdr<EntClazz> jdSelectDtoEntity2ByCandId1(EntClazz entity) {
        FiQuery sql = FiQugen.selectDtoEntityByCandId1Fi(getEntityClass());
        Fdr<Optional<EntClazz>> optionalFdr = jdSelectEntityBindEntityOptMain(sql.getTxQuery(), entity);

        Fdr<EntClazz> fdrResulut = new Fdr<>();

        if (optionalFdr.getValue().isPresent()) {
            fdrResulut.setValue(optionalFdr.getValue().get());
        }

        return fdrResulut;
    }

    @FiDraft
    @Deprecated
    public List<EntClazz> jdSelectByTxField(String txField, String txKod) {
        if (entityClass == null) setAutoClass();

        return null;
    }

    /**
     * FiSelect1 annonatationa göre select sorgususu hazırlar
     *
     * @param topCount
     * @return
     */
    public Fdr<List<EntClazz>> jdSelectListFiSel1(Integer topCount) {
        String query = FiQugen.selectQueryFiSelect1WithTop(getEntityClass(), topCount);
        return jdSelectListBindMapMainNtn(query, null);
    }

    public List<EntClazz> jdSelectListDtoFieldsBySeperatedFieldRaw(FiKeyBean fiKeyBean) {
        String sql = FiQugen.selectDtoFieldsBySeperatedField(getEntityClass());
        return jdSelectListBindMapRaw(sql, fiKeyBean);
    }

    public Fdr<List<EntClazz>> jdSelectListComboFieldsBySeperatedFields(EntClazz entClazz) {
        String sql = FiQugen.selectDtoFieldsBySeperatedField(getEntityClass());
        return jdSelectListBindEntity(sql, entClazz);
    }

    public Fdr<Optional<EntClazz>> jdSelectEntityDtoFieldsByCandId1(EntClazz entity) {
        String sql = FiQugen.selectDtoFieldsByCandId1(getEntityClass());
        return jdSelectEntityBindEntityOptMain(sql, entity);
    }

    public Fdr<List<EntClazz>> jdSelectListDtoFieldsByCandId1(EntClazz entity) {
        String sql = FiQugen.selectDtoFieldsByCandId1(getEntityClass());
        return jdSelectListBindEntity(sql, entity);
    }

    public Fdr<List<EntClazz>> jdSelect1ListByCandId(FiKeyBean fiKeyBean) {
        String query = FiQugen.select1FieldsByCandId(getEntityClass());
        return jdSelectListBindMapMainNtn(query, fiKeyBean);
    }


    public Fdr<Optional<Integer>> jdSelectSingleOptIntegerValBindBean(String sqlQuery, EntClazz entClazz) {
        return jdSelectSingleOptBindEntityMain(sqlQuery, entClazz, Integer.class);
    }

    public Integer jdRunBatchUpdateQuery(String... queryArr) {
        if (queryArr.length == 0) return null;

        List<String> queryList = Arrays.asList(queryArr);
        return jdRunBatchUpdateQuery(queryList);
    }

    public Fdr jdRunBatchUpdateQueryFi(String... queryArr) {
        if (queryArr.length == 0) return null;

        List<String> queryList = Arrays.asList(queryArr);
        return jdRunBatchUpdateQueryFi(queryList);
    }

    public Fdr jdInsertEntity(EntClazz entity) {
        return jdInsertEntityMain(entity, false);
    }

    public Fdr jdInsertEntityWitId(EntClazz entity) {
        return jdInsertEntityMain(entity, true);
    }

    /**
     * Entity'nin id alanları hariç, insert sorgusunu hazırlar.
     *
     * @param entity
     * @param boIncludeIdFields
     * @return
     */
    public Fdr jdInsertEntityMain(EntClazz entity, Boolean boIncludeIdFields) {

        Jdbi jdbi = getJdbi();

        Integer rowCountUpdate = null;

        Fdr fdr = new Fdr();

        try {
            rowCountUpdate = jdbi.withHandle(handle -> {

                String sql = null;
                if (FiBool.isTrue(boIncludeIdFields)) {
                    sql = Fiqt.stoj(FiQugen.insertQueryWithId(getEntityClass()));
                } else {
                    sql = Fiqt.stoj(FiQugen.insertQueryWoutId(getEntityClass()));
                }

                return handle.createUpdate(sql)
                        .bindBean(entity)
                        .execute(); // returns row count updated
            });
            fdr.setFdrBoExec(true);
            fdr.setRowsAffected(rowCountUpdate);
            fdr.setLnInsertedRows(rowCountUpdate);
            fdr.setTxQueryType(MetaQueryTypes.bui().insert);

        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            // URFIXME kısa hata açıklaması alanabilir
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    /**
     * Insert Entity Wout Id Fields
     *
     * @param handle
     * @param entity
     * @return
     */
    public Fdr jdhInsertEntity(Handle handle, EntClazz entity) {
        return jdhInsertEntityMain(handle, entity, false);
    }

    public Fdr jdhInsertEntityMain(Handle handle, EntClazz entity, Boolean boIncludeIdFields) {

        Fdr fdrMain = new Fdr();

        try {

            String sql; //= FiQueryTools.stoj(FiQueryGenerator.insertQueryWoutId(getEntityClass()));

            if (FiBool.isTrue(boIncludeIdFields)) {
                sql = Fiqt.stoj(FiQugen.insertQueryWithId(getEntityClass()));
            } else {
                sql = Fiqt.stoj(FiQugen.insertQueryWoutId(getEntityClass()));
            }

            Integer rowCountUpdate = handle.createUpdate(sql)
                    .bindBean(entity)
                    .execute(); // returns row count updated
            fdrMain.setFdrBoExec(true);
            fdrMain.setRowsAffected(rowCountUpdate);
        } catch (Exception ex) {
            Loghelper.get(getClass()).error(FiException.exTosMain(ex));
            fdrMain.setBoResult(false, ex);
        }

        return fdrMain;
    }

    public Fdr jdInsertQueryBindEntityMain(String sqlInsert, EntClazz entity) {

        Fdr fdr = new Fdr();

        try {
            Integer rowCountUpdate = getJdbi().withHandle(handle -> {
                return handle.createUpdate(Fiqt.stoj(sqlInsert))
                        .bindBean(entity)
                        .execute(); // returns row count updated
            });
            fdr.setFdrBoExec(true);
            fdr.setRowsAffected(rowCountUpdate);
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    public Fdr jdhInsertQueryBindEntity(String sqlInsert, EntClazz entity, Handle handle) {

        Fdr fdr = new Fdr();

        try {
            Integer rowCountUpdate = handle.createUpdate(Fiqt.stoj(sqlInsert))
                    .bindBean(entity)
                    .execute(); // returns row count updated

            fdr.setFdrBoExec(true);
            fdr.setRowsAffected(rowCountUpdate);
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    public Fdr jdhInsertEntityDbNameAdded(Handle handle, EntClazz entity) {

        Integer rowCountUpdate = null;

        try {
            rowCountUpdate = handle.createUpdate(FiQugen.insertQueryRtDbName(getEntityClass(), this))
                    .bindBean(entity)
                    .execute();

        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            return new Fdr(rowCountUpdate, ex);
        }

        return new Fdr(rowCountUpdate);
    }


    public Fdr jdInsertListWithTransMain(List<EntClazz> listEntity) {

        Jdbi jdbi = getJdbi();

        Boolean boResult = null;

        try {
            boResult = jdbi.inTransaction(handle -> {

                handle.begin();
                try {
                    // transactions
                    listEntity.forEach(ent -> {
                        handle.createUpdate(Fiqt.stoj(FiQugen.insertQueryWoutId(getEntityClass())))
                                .bindBean(ent)
                                .execute(); // returns row count updated

                    });
                    handle.commit();
                    return true;
                } catch (Exception e) {
                    Loghelper.debugException(getClass(), e);
                    handle.rollback();
                    return false;
                }
            });
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            return new Fdr(false, ex);
        }

        return new Fdr(boResult);

    }

    /**
     * Tabloya tüm alanlarını yazar ,id alanda içinde <br>
     * id alan auto generated id olmayan tablolarda uygulanır.
     *
     * @param listEntity
     * @return
     */
    public Fdr jdInsertListWithIdMain(List<EntClazz> listEntity) {

        Jdbi jdbi = getJdbi();

        //Boolean boResult = null;
        Fdr fdr = new Fdr();

        try {
            jdbi.useTransaction(handle -> {

                handle.begin();
                try {
                    // transactions
                    listEntity.forEach(ent -> {

                        int execute = handle.createUpdate(FiQugen.insertQueryWithId(getEntityClass()))
                                .bindBean(ent)
                                .execute();// returns row count updated
                        fdr.appendRowsAffected(execute);
                    });
                    handle.commit();
                    fdr.setFdrBoExec(true);
                    //return true;
                } catch (Exception e) {
                    Loghelper.debugException(getClass(), e);
                    handle.rollback();
                    fdr.setBoResult(false, e);
                    //return false;
                }
            });
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;

    }

    public Fdr jdInsertListWithTransMain(List<EntClazz> listEntity, TriConsumer<Handle, EntClazz, Fdr> extraWorksForEntity, BiConsumer<Handle, Fdr> extraWorksGeneral) {

        Jdbi jdbi = getJdbi();

        Boolean boResult = null;

        Fdr fdr = new Fdr();

        try {
            boResult = jdbi.inTransaction(handle -> {

                handle.begin();
                try {
                    // transactions
                    listEntity.forEach(ent -> {
                        handle.createUpdate(FiQugen.insertQueryJParamWoutId(getEntityClass()))
                                .bindBean(ent)
                                .execute(); // returns row count updated

                        if (extraWorksForEntity != null) {
                            extraWorksForEntity.accept(handle, ent, fdr);
                        }

                    });

                    if (extraWorksGeneral != null) {
                        extraWorksGeneral.accept(handle, fdr);
                    }

                    handle.commit();
                    return true;
                } catch (Exception e) {
                    Loghelper.debugException(getClass(), e);
                    handle.rollback();
                    return false;
                }
            });
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            return new Fdr(false, ex);
        }

        return new Fdr(boResult);

    }

    /**
     * Null olmayan alanları update eder
     *
     * @param entity
     * @return
     */
    public Fdr jdUpdateEntityNotNullFields(EntClazz entity) {

        String str = FiQugen.updateQueryNotNullFieldsWoutIdFieldByCandId1(getEntityClass(), entity);
        return jdUpdateBindEntityMain(str, entity);

    }

    public Fdr jdUpdateBindEntityMain(String updateQuery, EntClazz bindEntity) {
        return jdUpdateBindObjectMain(updateQuery, bindEntity);
    }

    public Fdr jdUpdateBindMapAndEntity(String updateQuery, EntClazz bindEntity, FiKeyBean fiKeyBean) {
        return jdUpdateBindMapAndObjectMain(updateQuery, bindEntity, fiKeyBean);
    }

    /**
     * jdO - jd Operation
     *
     * @param updateQuery
     * @param bindEntity
     * @return
     */
    public Fdr jdUpdateBindObjectMain(String updateQuery, Object bindEntity) {

        Jdbi jdbi = getJdbi();
        Fdr fdrMain = new Fdr();

        try {
            Integer rowCountUpdate = jdbi.withHandle(handle -> {
                return handle.createUpdate(Fiqt.stoj(updateQuery))
                        .bindBean(bindEntity)
                        .execute(); // returns row count updated
            });
            //fiDbResult.setRowsAffectedWithUpBoResult(rowCountUpdate);
            fdrMain.setBoResultAndRowsAff(true, rowCountUpdate); // 16-01-20 çevrildi.
            //fdrMain.setTxQueryType(MetaQueryTypes.bui().update);
            if (rowCountUpdate > 0) {
                fdrMain.setLnUpdatedRows(rowCountUpdate);
            }
            //Loghelper.get(getClass()).debug("Row Affected:"+rowCountUpdate);

        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            fdrMain.setBoResult(false, ex);
            fdrMain.appendMessageLn(FiException.TosSummary(ex));
        }

        return fdrMain;
    }

    /**
     * önce Map bind eder,sonra entity (map de varsa,entity dekini almaz)
     *
     * @param updateQuery
     * @param bindEntity
     * @return
     */
    public Fdr jdUpdateBindMapAndObjectMain(String updateQuery, Object bindEntity, FiKeyBean fiKeyBean) {

        Jdbi jdbi = getJdbi();

        Fdr fdr = new Fdr();

        try {
            Integer rowCountUpdate = jdbi.withHandle(handle -> {
                return handle.createUpdate(Fiqt.stoj(updateQuery))
                        .bindMap(fiKeyBean)
                        .bindBean(bindEntity)
                        .execute(); // returns row count updated
            });
            //fiDbResult.setRowsAffectedWithUpBoResult(rowCountUpdate);
            fdr.setBoResultAndRowsAff(true, rowCountUpdate); // 16-01-20 çevrildi.
            //fdr.setTxQueryType(MetaQueryTypes.bui().update);
            if (rowCountUpdate > 0) {
                fdr.setLnUpdatedRows(rowCountUpdate);
            }
            //Loghelper.get(getClass()).debug("Row Affected:"+rowCountUpdate);

        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    /**
     * To Execute Delete Query With binding entity
     * <p>
     * It uses Jdbi CreateUpdate Method
     *
     * @param deleteQuery
     * @param bindEntity
     * @return
     */
    public Fdr jdDeleteBindObjectMain(String deleteQuery, Object bindEntity) {

        Jdbi jdbi = getJdbi();

        Fdr fdr = new Fdr();

        try {
            Integer rowsAffected = jdbi.withHandle(handle -> {
                return handle.createUpdate(Fiqt.stoj(deleteQuery))
                        .bindBean(bindEntity)
                        .execute(); // returns row count updated
            });
            //fiDbResult.setRowsAffectedWithUpBoResult(rowsAffected);
            fdr.setBoResultAndRowsAff(true, rowsAffected); // 16-01-20 çevrildi.
            //fdr.setTxQueryType(MetaQueryTypes.bui().update);
            if (rowsAffected > 0) {
                fdr.setLnDeletedRows(rowsAffected);
            }
            //Loghelper.get(getClass()).debug("Row Affected:"+rowsAffected);

        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    public Fdr jdUpdateBindMapMain(FiQuery fiQuery) {
        return jdUpdateBindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    public Fdr jdDeleteBindMapMain(FiQuery fiQuery) {
        return jdDeleteBindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    public Fdr jdDeleteBindMapMain(String updateQuery, Map<String, Object> fiMapParams) {
        return jdUpdateBindMapMain(updateQuery, fiMapParams);
    }

    public Fdr jdUpdateBindMapMain(String updateQuery, Map<String, Object> fiMapParams) {

        Jdbi jdbi = getJdbi();

        Fdr fdr = new Fdr();

        try {
            Integer rowCountUpdate = jdbi.withHandle(handle -> {
                return handle.createUpdate(Fiqt.stojExcludable1(updateQuery))
                        .bindMap(fiMapParams)
                        .execute(); // returns row count updated
            });
            //Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
            fdr.setBoResultAndRowsAff(true, rowCountUpdate);
        } catch (Exception ex) {
            Loghelper.get(getClass()).error(FiException.exTosMain(ex));
            fdr.setBoResult(false, ex);
            //fdr.setLnResult(0);
        }
        return fdr;
    }

    public Fdr jdInsertBindMapMain(String insertQuery, Map<String, Object> mapParams) {

        Jdbi jdbi = getJdbi();

        Fdr fdr = new Fdr();

        try {
            Integer rowCountUpdate = jdbi.withHandle(handle -> {
                return handle.createUpdate(Fiqt.stojExcludable1(insertQuery))
                        .bindMap(mapParams)
                        .execute(); // returns row count updated
            });
            //Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
            //fiDbResult.setLnSuccessWithUpBoResult(1, rowCountUpdate);
            fdr.setBoResultAndRowsAff(true, rowCountUpdate);
            //fdr.setLnResult(1);
        } catch (Exception ex) {
            Loghelper.get(getClass()).error(FiException.exTosMain(ex));
            fdr.setBoResult(false, ex);
            //fdr.setLnResult(0);
        }
        return fdr;
    }

    public Fdr jdhUpdateBindMap(String updateQuery, Map<String, Object> fiMapParams, Handle handle) {

        Fdr fdr = new Fdr();

        try {
            Integer rowCountUpdate = handle.createUpdate(Fiqt.stoj(updateQuery))
                    .bindMap(fiMapParams)
                    .execute(); // returns row count updated
            //Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
            //fiDbResult.setLnSuccessWithUpBoResult(1, rowCountUpdate);
            fdr.setBoResultAndRowsAff(true, rowCountUpdate);
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }
        return fdr;
    }

    public Fdr jdhUpdateBindEntityMain(Handle handle, String updateQuery, EntClazz bindEntity) {

        Fdr fdr = new Fdr();

        try {
            int rowsAffected = handle.createUpdate(Fiqt.stojExcludable1(updateQuery))
                    .bindBean(bindEntity)
                    .execute();
            fdr.setBoResultAndRowsAff(true, rowsAffected); // 16-01-20 added.
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    public Fdr jdhUpdateBindObjectMain(Handle handle, String updateQuery, Object bindEntity) {

        Fdr fdr = new Fdr();

        try {
            int rowsAffected = handle.createUpdate(Fiqt.stoj(updateQuery))
                    .bindBean(bindEntity)
                    .execute();
            fdr.setBoResultAndRowsAff(true, rowsAffected);
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    /**
     * FiTableCol a göre update sorgusu
     * <p>
     * Id field ı update sorgusu içine yazmaz, where içine yazar (annotation'dan kontrol eder)
     * <p>
     * Non updatable alanlar dahil edilmez ( FiTableCol da belirtilen )
     * <p>
     * Default update alanlarını sorgu içerisine ekler (dtCreate,dtLastUp gibi)
     *
     * @param listFields
     * @param entity
     * @return
     */
    public Fdr jdUpdateFiColsBindEntityByIdFieldInFiCols(List<? extends IFiCol> listFields, EntClazz entity) {

        String sqlQuery = FiQugen.updateFiColListAndExtraWhereIdFiCols(getEntityClass(), listFields);
        //Loghelper.get(getClass()).debug(sqlQuery);
        return jdUpdateBindEntityMain(sqlQuery, entity);

    }

    /**
     * FiCols içerisinde id fieldlara göre güncelleme yapar
     *
     * @param handle
     * @param listFiCols
     * @param entity
     * @return
     */
    public Fdr jdhUpdateFiColsBindEntityByIdFiCols(Handle handle, List<? extends IFiCol> listFiCols, EntClazz entity) {

        String sqlQuery = FiQugen.updateFiColListAndExtraWhereIdFiCols(getEntityClass(), listFiCols);

        Loghelper.get(getClass()).debug("Update Query: " + sqlQuery);

        return jdhUpdateBindEntityMain(handle, sqlQuery, entity);
    }

    public Fdr jdUpdateFiColsBindMapByIdFieldInFiCols(List<? extends IFiCol> listFields, FiKeyBean fiKeyBean) {

        String sqlQuery = FiQugen.updateFiColListAndExtraWhereIdFiCols(getEntityClass(), listFields);
        //Loghelper.get(getClass()).debug(sqlQuery);
        return jdUpdateBindMapMain(sqlQuery, fiKeyBean);
    }

    public Fdr jdhUpdateFiColsWhereKeyFiColsBindFkb(Handle handle, FiKeyBean fkbParams) {

        String sqlQuery = FiQugen.updateFiColsWhereKeyFiCols(getEntityClass(), fkbParams.getListFiColInit());
        //Loghelper.get(getClass()).debug("jdhUpdateFiColsWhereIdFiColsBindFkb : " + sqlQuery);
        return jdhUpdateBindMap(sqlQuery, fkbParams, handle);

    }

    public Fdr jdUpdateFiColsWhereKeyFiColsBindFkb(FiKeyBean fkbParams) {

        String sqlQuery = FiQugen.updateFiColsWhereKeyFiCols(getEntityClass(), fkbParams.getListFiColInit());
        FiQuery fiQuery = new FiQuery(sqlQuery, fkbParams);

        //fkbParams.logFiCols();
        //fiQuery.logQueryAndParams();

        return jdUpdateBindMapMain(fiQuery);
    }

    /**
     * Table ismini generic entity'den alır
     *
     * @param fkbParams
     * @return
     */
    public Fdr jdInsertFkb(FiKeyBean fkbParams) {
        return jdInsertFiColsBindFkb(fkbParams.getListFiColInit(), fkbParams);
    }

    public Fdr jdInsertFiColsBindFkb(List<FiCol> fiColList, FiKeyBean fkbParams) {

        String sql = FiQugen.insertFiCols(getEntityClass(), fiColList);
        Loghelper.get(getClass()).debug("jdInsertFiColsBindFkb " + sql);

        return jdUpdateBindMapMain(sql, fkbParams);

    }

    /**
     * Id Field in FiColList
     *
     * @param fiColList
     * @return
     */
    public Fdr jdUpdateFiColsBindColValueByIdField(List<FiCol> fiColList) {

        String sqlQuery = FiQugen.updateFiColListAndExtraWhereIdFiCols(getEntityClass(), fiColList);

        FiKeyBean fkbParams = new FiKeyBean();
        for (IFiCol fiCol : fiColList) {
            fkbParams.put(fiCol.getOfcTxFieldName(), fiCol.getColValue());
        }

        Loghelper.get(getClass()).debug(sqlQuery);
        return jdUpdateBindMapMain(sqlQuery, fkbParams);
    }

    public Fdr jdUpdateFiColsBindColValueByIdFieldList(List<FiCol> fiColList) {

        String sqlQuery = FiQugen.updateFiColsWhereINKeyFiCols(getEntityClass(), fiColList);

        FiKeyBean fkbParams = new FiKeyBean();
        for (IFiCol fiCol : fiColList) {
            fkbParams.put(fiCol.getOfcTxFieldName(), fiCol.getColValue());
        }

        FiQuery fiQuery = new FiQuery(sqlQuery, fkbParams);
        fiQuery.convertListParamsToMultiParamsWithKeep();

        Loghelper.get(getClass()).debug(sqlQuery);
        return jdUpdateBindMapMain(fiQuery);
    }

    /**
     * Bind FiColValue
     *
     * @param fiColList
     * @return
     */
    public Fdr<List<EntClazz>> jdSelectCountIdByFiColListWhereIdList(List<FiCol> fiColList) {

        String sqlQuery = FiQugen.selectQueryCountIdByFiColListWhereIdList(getEntityClass(), fiColList);

        FiKeyBean fkbParams = new FiKeyBean();
        for (IFiCol fiCol : fiColList) {
            fkbParams.put(fiCol.getOfcTxFieldName(), fiCol.getColValue());
        }

        FiQuery fiQuery = new FiQuery(sqlQuery, fkbParams);
        fiQuery.convertListParamsToMultiParamsWithKeep();

        Loghelper.get(getClass()).debug(sqlQuery);
        return jdSelectListBindMap(fiQuery);
    }

    /**
     * @param fiColsSelect
     * @param fiColsOrder
     * @return
     */
    public Fdr<List<EntClazz>> jdSelectFiColsWitOrder(List<FiCol> fiColsSelect, List<FiCol> fiColsOrder) {
        String sql = FiQugen.selectFiColsWitOrderBy(getEntityClass(), fiColsSelect, fiColsOrder);
        //Loghelper.get(getClass()).debug(sql);
        return jdSelectListBindMapMainNtn(sql, null);
    }

    public Fdr<List<EntClazz>> jdSelectFiColListAndOrder(List<FiCol> fiColsSelect, List<FiCol> fiColsOrder) {

        String sqlQuery = FiQugen.selectFiColsWitOrderBy(getEntityClass(), fiColsSelect, null);

        FiKeyBean fkbParams = new FiKeyBean();
        for (IFiCol fiCol : fiColsSelect) {
            fkbParams.put(fiCol.getOfcTxFieldName(), fiCol.getColValue());
        }

        FiQuery fiQuery = new FiQuery(sqlQuery, fkbParams);
        fiQuery.convertListParamsToMultiParamsWithKeep();

        Loghelper.get(getClass()).debug(sqlQuery);
        return jdSelectListBindMap(fiQuery);
    }


    public Fdr jdUpdateFiColsBindEntityByIdFieldInClass(List<? extends IFiCol> listFields, EntClazz entity) {

        String sqlQuery = FiQugen.updateQueryWithFiColsByIdFieldInClass(getEntityClass(), listFields);

        return jdUpdateBindEntityMain(sqlQuery, entity);
    }

    public Fdr jdUpdateFiTableColsBindEntityByCandId1(List<? extends IFiCol> listFields, EntClazz entity) {

        String sqlQuery = FiQugen.updateQueryWithFiTableColByCandId(getEntityClass(), listFields);

        return jdUpdateBindEntityMain(sqlQuery, entity);

    }

    public Fdr jdUpdateQueryBindEntityList(List<EntClazz> listEntity, String sqlUpdate) {
        return jdUpdateQueryBindEntityListMain(listEntity, sqlUpdate, null, null);
    }

    /**
     * BindBean yöntemiyle değişkenleri sorguya bağlar
     *
     * @param listEntity
     * @param sqlUpdate
     * @return
     */
    public Fdr jdUpdateQueryBindEntityListMain(List<EntClazz> listEntity, String sqlUpdate
            , BiFunction<Handle, EntClazz, Fdr> extraWorksForEntity
            , Function<Handle, Fdr> extraWorksGeneral) {

        Jdbi jdbi = getJdbi();

        Boolean boResultTransaction = null;

        Fdr fdr = new Fdr();

        try {
            boResultTransaction = jdbi.inTransaction(handle -> {

                handle.begin();
                try {
                    // transactions
                    listEntity.forEach(ent -> {

                        Integer rowsAffectedUpdate = handle.createUpdate(Fiqt.stoj(sqlUpdate))
                                .bindBean(ent)
                                .execute(); // returns row count updated

                        fdr.appendRowsAffected(rowsAffectedUpdate);

                        if (extraWorksForEntity != null) {
                            Fdr fdrExtraForEntity = extraWorksForEntity.apply(handle, ent);
                            fdr.combineAnd(fdrExtraForEntity);
                        }

                    });

                    if (extraWorksGeneral != null) {
                        Fdr fdrExtraForGeneral = extraWorksGeneral.apply(handle);
                        fdr.combineAnd(fdrExtraForGeneral);
                    }

                    handle.commit();
                    return true;
                } catch (Exception e) {
                    Loghelper.debugException(getClass(), e);
                    handle.rollback();
                    return false;
                }
            });
            fdr.setFdrBoExec(boResultTransaction);
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            fdr.setBoResult(false, ex);
            return fdr;
        }

        return fdr;

    }

    public Fdr jdhUpdateQueryBindEntityListMain(List<EntClazz> listEntity, String sqlUpdate, Handle handle) {

        Fdr fdrBatch = new Fdr();

        // transactions
        for (EntClazz entity : listEntity) {
            Fdr fdr = jdhUpdateBindEntityMain(handle, sqlUpdate, entity);

            if (fdr.getException() != null) {
                break;
            }

            fdrBatch.combineAnd(fdr);
        }

        return fdrBatch;
    }

    @Deprecated
    protected Fdr jdInsertOrUpdateList(List<EntClazz> listEntity, String sqlUpdate, Boolean boBindGeneratedKey) {
        return jdInsertOrUpdateListMain(listEntity, sqlUpdate, null, null, boBindGeneratedKey);
    }

    /**
     * Entity'nin herhangi bir id alanının null olup olmamadığına göre insert veya update işlemi yapar.
     *
     * @param entity
     * @param fiTableColList
     * @return
     */
    public Fdr jdInsertEntityOrUpdateFiTableColsBindEntityByIds(EntClazz entity, List<FiCol> fiTableColList) {

        Boolean boIdNull = FiReflectClass.checkIdFieldsAnyNull(entity, getEntityClass());

        if (FiBool.isTrue(boIdNull)) { // insert
            return jdInsertEntity(entity);
        } else { // update
            return jdUpdateFiColsBindEntityByIdFieldInFiCols(fiTableColList, entity);
        }

    }

    /**
     * Entity'nin herhangi bir id alanının null olup olmamadığına göre insert veya update işlemi yapar.
     *
     * @param entity
     * @param fiTableColList
     * @return
     */
    public Fdr jdInsertEntityOrUpdateFiTableColsBindEntityByDtCreated(EntClazz entity, List<FiCol> fiTableColList) {

        Boolean boIdNull = FiReflectClass.checkDtCreatedFieldsNull(entity, getEntityClass());

        if (FiBool.isTrue(boIdNull)) { // insert
            return jdInsertEntity(entity);
        } else { // update
            return jdUpdateFiColsBindEntityByIdFieldInFiCols(fiTableColList, entity);
        }

    }

    /**
     * Consumer yerine , Function kullanılacak ve fidbresult dönmeli <br>
     * entity lerin id field null degilse update query çalıştırır, yoksa otomatik insert sorgusu çalışır
     *
     * @param listEntity
     * @param sqlUpdate
     * @return
     */
    @Deprecated
    protected Fdr jdInsertOrUpdateListMain(List<EntClazz> listEntity, String sqlUpdate
            , TriConsumer<Handle, EntClazz, Fdr> extraWorksForEntity
            , BiConsumer<Handle, Fdr> extraWorksGeneral, Boolean boBindGeneratedKey) {

        Jdbi jdbi = getJdbi();

        Boolean boResult = null;

        Fdr fdr = new Fdr();

        try {
            boResult = jdbi.inTransaction(handle -> {

                handle.begin();
                try {
                    // transactions
                    Boolean boExistError = false;
                    listEntity.forEach(ent -> {

                        // check id field is null or not
                        Boolean boIdNull = FiReflectClass.checkIdFieldsAnyNull(ent, getEntityClass());
                        //Loghelperr.getInstance(getClass()).debug("Is Null:"+boIdNull.toString());

                        // id null ise insert yap
                        if (FiBool.isTrue(boIdNull)) {
                            // Loghelperr.getInstance(getClass()).debug("Insert AddOrUpdate");
                            // generated key elde etmek için opsiyonel olması lazım

                            if (FiBool.isTrue(boBindGeneratedKey)) {

                                String idField = FiReflectClass.getListIdFields(getEntityClass()).get(0);
                                Class idClazz = FiReflection.getFieldClassType(getEntityClass(), idField);

                                Optional opId = handle.createUpdate(Fiqt.stoj(FiQugen.insertQueryJParamWoutId(getEntityClass())))
                                        .bindBean(ent)
                                        .executeAndReturnGeneratedKeys(idField)
                                        //.map(new FiBeanNestedRowMapper<>(getEntityClass())) // GENERATED_KEYS adında bir alana atama yapmaya çalışıyor
                                        .mapTo(idClazz)
                                        .findFirst(); // returns row count updated

                                opId.ifPresent(insertedId -> {
                                    FiReflection.setterNested(ent, idField, insertedId);
                                });

                            } else { // generated keyler alınmasına gerek yoksa
                                handle.createUpdate(Fiqt.stoj(FiQugen.insertQueryJParamWoutId(getEntityClass())))
                                        .bindBean(ent).execute();
                            }


                        }

                        // id Null degilse, update yap
                        if (FiBool.isFalse(boIdNull)) {
                            Loghelper.get(getClass()).debug("Update AddOrUpdate");
                            handle.createUpdate(Fiqt.stoj(sqlUpdate))
                                    .bindBean(ent)
                                    .execute(); // returns row count updated
                        }

                        //if (boIdNull == true);

                        if (extraWorksForEntity != null) {
                            extraWorksForEntity.accept(handle, ent, fdr);
                        }

                    });

                    if (extraWorksGeneral != null) {
                        extraWorksGeneral.accept(handle, fdr);
                    }

                    handle.commit();
                    return true;
                } catch (Exception e) {
                    Loghelper.debugException(getClass(), e);
                    handle.rollback();
                    return false;
                }
            });
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            return new Fdr(false, ex);
        }

        return new Fdr(boResult);

    }

    /**
     * entity'lere özel update sorgusu fnSqlUpdatePerEntity ile verilebilir
     *
     * @param listEntity
     * @param fnSqlUpdatePerEntity Her bir Entity'de update sorgusunu üretecek fonksiyon (Entity'e özel update sorgusu)
     * @return
     */
    public Fdr jdUpdateListWithCustomSql(List<EntClazz> listEntity
            , Function<EntClazz, String> fnSqlUpdatePerEntity
            , BiFunction<Handle, EntClazz, Fdr> afterWorksByEntity
            , Function<Handle, Fdr> afterWorksGeneral) {

        Jdbi jdbi = getJdbi();

        Boolean boResult = null;

        Fdr fdr = new Fdr();

        try {
            boResult = jdbi.inTransaction(handle -> {

                handle.begin();
                try {
                    // transactions
                    Boolean boExistError = false;
                    listEntity.forEach(ent -> {

                        // id Null degilse, update yap
                        if (true) {
                            //Loghelperr.getInstance(getClass()).debug("Update AddOrUpdate");
                            String sql = Fiqt.stoj(fnSqlUpdatePerEntity.apply(ent));
                            //FiConsole.debug("sql:" + sql);
                            if (!FiString.isEmpty(sql)) {
                                Integer rowsAffected1 = handle.createUpdate(sql)
                                        .bindBean(ent)
                                        .execute(); // returns row count updated
                                fdr.appendRowsAffected(rowsAffected1);
                            }
                        }

                        if (afterWorksByEntity != null) {
                            afterWorksByEntity.apply(handle, ent);
                        }

                    });

                    if (afterWorksGeneral != null) {
                        afterWorksGeneral.apply(handle);
                    }

                    handle.commit();
                    return true;
                } catch (Exception e) {
                    Loghelper.debugException(getClass(), e);
                    handle.rollback();
                    return false;
                }
            });
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            return new Fdr(false, ex);
        }

        fdr.setFdrBoExec(boResult);
        return fdr;

    }

    public <CustomEnt> Fdr jdUpdateListWitCustomEntity(String sqlUpdate, List<CustomEnt> listEntity, Map<String, Object> mapBind) {

//          , Function<T, String> fnSqlUpdatePerEntity
//			, BiFunction<Handle, T, FiDbResult> extraWorksForEntity
//			, Function<Handle, FiDbResult> extraWorksGeneral

        Jdbi jdbi = getJdbi();

        Boolean boResult = null;

        Fdr fdr = new Fdr();

        try {
            boResult = jdbi.inTransaction(handle -> {

                handle.begin();
                try {
                    // transactions
                    Boolean boExistError = false;
                    listEntity.forEach(ent -> {

                        // id Null degilse, update yap
                        if (true) {
                            //Loghelperr.getInstance(getClass()).debug("Update AddOrUpdate");
                            int execute = handle.createUpdate(Fiqt.stoj(sqlUpdate))
                                    .bindMap(mapBind)
                                    .bindBean(ent)
                                    .execute();// returns row count updated
                            fdr.appendRowsAffected(execute);
                        }

//						if (extraWorksForEntity != null) {
//							extraWorksForEntity.apply(handle, ent);
//						}

                    });

//					if (extraWorksGeneral != null) {
//						extraWorksGeneral.apply(handle);
//					}

                    handle.commit();
                    return true;
                } catch (Exception e) {
                    Loghelper.debugException(getClass(), e);
                    handle.rollback();
                    return false;
                }
            });
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            return new Fdr(false, ex);
        }

        fdr.setFdrBoExec(boResult);
        return fdr;

    }


    /**
     * Consumer ile degil , Function ile yapılacak, fidbresult döndürmeli <br>
     * entity lerin id field null degilse update query çalıştırır, yoksa otomatik insert sorgusu çalışır <br>
     * entity lere özel update sorgusu fnSqlUpdatePerEntity ile verilebilir
     *
     * @param listEntity
     * @param fnSqlUpdatePerEntity Update sorgusunu üretir (Entity'e özel update sorgusu)
     * @return
     */
    @Deprecated
    public Fdr jdInsertOrUpdateList(List<EntClazz> listEntity
            , Function<EntClazz, String> fnSqlUpdatePerEntity
            , TriConsumer<Handle, EntClazz, Fdr> extraWorksForEntity
            , BiConsumer<Handle, Fdr> extraWorksGeneral) {

        Jdbi jdbi = getJdbi();

        Boolean boResult = null;

        Fdr fdr = new Fdr();

        try {
            boResult = jdbi.inTransaction(handle -> {

                handle.begin();
                try {
                    // transactions
                    Boolean boExistError = false;
                    listEntity.forEach(ent -> {

                        // check id field is null or not
                        Boolean boIdNull = FiReflectClass.checkIdFieldsAnyNull(ent, getEntityClass());
                        //Loghelperr.getInstance(getClass()).debug("Is Null:"+boIdNull.toString());

                        // id null ise insert yap
                        if (FiBool.isTrue(boIdNull)) {
                            //Loghelperr.getInstance(getClass()).debug("Insert AddOrUpdate");
                            handle.createUpdate(Fiqt.stoj(new FiQugen().insertQueryJParamWoutId(getEntityClass())))
                                    .bindBean(ent)
                                    .execute(); // returns row count updated
                        }

                        // id Null degilse, update yap
                        if (FiBool.isFalse(boIdNull)) {
                            //Loghelperr.getInstance(getClass()).debug("Update AddOrUpdate");
                            handle.createUpdate(Fiqt.stoj(fnSqlUpdatePerEntity.apply(ent)))
                                    .bindBean(ent)
                                    .execute(); // returns row count updated
                        }

                        if (boIdNull == true) ;


                        if (extraWorksForEntity != null) {
                            extraWorksForEntity.accept(handle, ent, fdr);
                        }

                    });

                    if (extraWorksGeneral != null) {
                        extraWorksGeneral.accept(handle, fdr);
                    }

                    handle.commit();
                    return true;
                } catch (Exception e) {
                    Loghelper.debugException(getClass(), e);
                    handle.rollback();
                    return false;
                }
            });
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            return new Fdr(false, ex);
        }

        return new Fdr(boResult);

    }

    /**
     * entity lerin id field null degilse update query çalıştırır
     * listUpdateCols 'a göre güncelle sorgusunu oluşturur
     *
     * @param listEntity
     * @param listUpdateCols Update edilecek alanlar (id alan hariç)
     * @return
     */
    public Fdr jdUpdateFiTableColsBindEntityListByIdField(List<EntClazz> listEntity
            , List<? extends IFiCol> listUpdateCols
            , BiFunction<Handle, EntClazz, Fdr> extraWorksForEntity
            , Function<Handle, Fdr> extraWorksGeneral) {

        String sqlUpdate = FiQugen.updateQueryWithFiColsByIdFieldInClass(getEntityClass(), listUpdateCols);

        // list içinde id null kontrolü yapmak istenirse (iter ile çıkarmak gerekir) // birleştirmeden önce yapılmıştı
        // Boolean boIdNull = FiEntityHelper.checkIdFieldsNullOrFull(ent, getEntityClass());

        return jdUpdateQueryBindEntityListMain(listEntity, sqlUpdate, extraWorksForEntity, extraWorksGeneral);

    }

    /**
     * Cand Id ye göre fitable col listesindeki alanları update eder.
     *
     * @param listEntity
     * @param listUpdateCols
     * @param extraWorksForEntity
     * @param extraWorksGeneral
     * @return
     */
    public Fdr jdUpdateFiColsBindEntityListByCandId(List<EntClazz> listEntity
            , List<? extends IFiCol> listUpdateCols
            , BiFunction<Handle, EntClazz, Fdr> extraWorksForEntity
            , Function<Handle, Fdr> extraWorksGeneral) {

        String sqlUpdate = FiQugen.updateQueryWithFiTableColByCandId(getEntityClass(), listUpdateCols);

        // list içinde id null kontrolü yapmak istenirse (iter ile çıkarmak gerekir) // birleştirmeden önce yapılmıştı
        // Boolean boIdNull = FiEntityHelper.checkIdFieldsNullOrFull(ent, getEntityClass());

        return jdUpdateQueryBindEntityListMain(listEntity, sqlUpdate, extraWorksForEntity, extraWorksGeneral);

    }


    public Fdr<Optional<String>> jdSelectStringOpValue(String sql, Map<String, Object> mapParam) {

        Jdbi jdbi = getJdbi();
        Optional<String> result = null;

        Fdr<Optional<String>> fdr = new Fdr<>();

        try {
            result = jdbi.withHandle(handle -> {
                return handle.select(Fiqt.stoj(sql))
                        .bindMap(mapParam)
                        .mapTo(String.class)
                        .findFirst();
            });

            fdr.setValue(result);
            fdr.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
        }

        return fdr;

    }

    /**
     * Not Null Return ( null return etmez )
     *
     * @param sql
     * @param mapParam
     * @param resultClazz
     * @param <PrmEnt>
     * @return
     */
    public <PrmEnt> Fdr<Optional<PrmEnt>> jdSelectSingleOpCustomEntityBindMapNtn(String sql, Map<String, Object> mapParam, Class<PrmEnt> resultClazz) {

        Fdr<Optional<PrmEnt>> fdr = new Fdr<>();
        //Loghelper.get(getClass()).debug("Jdbi isnull:" + getJdbi() == null);

        try {
            Optional<PrmEnt> result = getJdbi().withHandle(handle -> {
                return handle.select(Fiqt.stoj(sql))
                        .bindMap(mapParam)
                        .mapTo(resultClazz)
                        .findFirst();
            });

            fdr.setValue(result);
            fdr.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setFdrBoExec(false);
        }

        if(fdr.getValue()==null) fdr.setValue(Optional.empty());

        return fdr;
    }

    /**
     * Sorgu çalışır ve deger cekemezse degeri null olur
     * <p>
     * Sorguda hata olursa result false olur, deger yine null olur
     *
     * @param sql
     * @param mapParam
     * @param resultClazz
     * @param <PrmEnt>
     * @return
     */
    public <PrmEnt> Fdr<PrmEnt> jdSelectSingleCustomTypeBindMap(String sql, Map<String, Object> mapParam, Class<PrmEnt> resultClazz) {

        Fdr<PrmEnt> fdr = new Fdr<>();

        try {
            Optional<PrmEnt> result = getJdbi().withHandle(handle -> {
                return handle.select(Fiqt.stoj(sql))
                        .bindMap(mapParam)
                        .mapTo(resultClazz)
                        .findFirst(); // if returns null or zero rows, then return Optional.empty()
            });

            result.ifPresent(fdr::setValue);
            fdr.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.get(getClass()).debug(FiException.exToErrorLog(ex));
            fdr.setFdrBoExec(false);
            fdr.setValue(null);
        }

        return fdr;
    }

    /**
     * Not Null Return
     *
     * @param sql
     * @param fiKeyBean
     * @return
     */
    public Fdr<Optional<Integer>> jdSelectSingleOpInt(String sql, FiKeyBean fiKeyBean) {
        return jdSelectSingleOpCustomEntityBindMapNtn(sql, fiKeyBean, Integer.class);
    }

    /**
     * Sorgu çalışır ve deger cekemezse degeri null olur
     * <p>
     * Sorguda hata olursa result false olur, deger yine null olur
     *
     * @param sql
     * @param fiKeyBean
     * @return
     */
    public Fdr<Integer> jdSelectSingleInt(String sql, FiKeyBean fiKeyBean) {
        return jdSelectSingleCustomTypeBindMap(sql, fiKeyBean, Integer.class);
    }

    /**
     * M1 means minus 1
     *
     * @param sql
     * @param fiMapParams
     * @return
     */
    public Fdr<Integer> jdSelectSingleIntOrMinus1(String sql, FiKeyBean fiMapParams) {
        FiQuery fiQuery = new FiQuery(sql, fiMapParams);
        return jdSelectSingleIntOrMinus1(fiQuery);
    }

    public Fdr<Integer> jdSelectSingleIntOrMinus1(FiQuery fiQuery) {
        Fdr<Integer> fdrSql = jdSelectSingleCustomTypeBindMap(fiQuery.getTxQuery(), fiQuery.getMapParams(), Integer.class);
        if (fdrSql.getValue() == null) {
            fdrSql.setValue(-1);
        }
        return fdrSql;
    }

    public Fdr<String> jdSelectSingleString(String sql, FiKeyBean fiKeyBean) {
        return jdSelectSingleCustomTypeBindMap(sql, fiKeyBean, String.class);
    }

    /**
     * Not Null Return
     * <p>
     * Virgülle ayrılmış olarak String olarak kaydedilen değeri, List'e çevirerek dönüş yapar
     *
     * @param sql
     * @param fiMapParams
     * @return
     */
    public Fdr<List<String>> jdSelectCsvStringAsList(String sql, FiKeyBean fiMapParams) {

        Fdr<String> stringFdr = jdSelectSingleCustomTypeBindMap(sql, fiMapParams, String.class);

        Fdr<List<String>> fdr = new Fdr<>();
        fdr.copyValues(stringFdr);

        if (stringFdr.getValue() != null) {
            String value = stringFdr.getValue();
            List<String> listVal = Arrays.asList(value.split(","));
            fdr.setValue(listVal);
        } else {
            fdr.setValue(new ArrayList<>());
        }

        return fdr;
    }

    public Fdr<Optional<Integer>> jdSelectSingleIntBindEntity(String sql, EntClazz entClazz) {
        return jdSelectSingleOptBindEntityMain(sql, entClazz, Integer.class);
    }

    public Fdr<Integer> jdSelectSingleInt2BindEntity(String sql, EntClazz entClazz) {
        return jdSelectSingleBindEntityMain(sql, entClazz, Integer.class);
    }

    public <PrmEnt> Fdr<Optional<PrmEnt>> jdSelectSingleOptBindEntityMain(String sql, EntClazz entClazz, Class<PrmEnt> resultClazz) {

        Jdbi jdbi = getJdbi();

        Fdr<Optional<PrmEnt>> fdr = new Fdr<>();
        fdr.setValue(Optional.empty());

        try {
            Optional<PrmEnt> result = jdbi.withHandle(handle -> {
                return handle.select(Fiqt.stoj(sql))
                        .bindBean(entClazz)
                        .mapTo(resultClazz)
                        .findFirst();
            });

            fdr.setValue(result);
            fdr.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }


    public <PrmEnt> Fdr<PrmEnt> jdSelectSingleBindEntityMain(String sql, EntClazz entClazz, Class<PrmEnt> resultClazz) {

        Fdr<PrmEnt> fdr = new Fdr<>();
//		fdr.setValue(0);

        try {
            Optional<PrmEnt> result = getJdbi().withHandle(handle -> {
                return handle.select(Fiqt.stoj(sql))
                        .bindBean(entClazz)
                        .mapTo(resultClazz)
                        .findFirst();
            });

            result.ifPresent(fdr::setValue);
            fdr.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    /**
     * Hata Olursa -1 döner
     *
     * @param sql
     * @param entClazz
     * @return
     */
    public Fdr<Integer> jdSelectIntBindEntityMainNtn(String sql, EntClazz entClazz) {

        Fdr<Integer> fdr = new Fdr<>();

        try {
            Optional<Integer> result = getJdbi().withHandle(handle -> {
                return handle.select(Fiqt.stoj(sql))
                        .bindBean(entClazz)
                        .mapTo(Integer.class)
                        .findFirst();
            });

            result.ifPresent(fdr::setValue);
            fdr.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.get(getClass()).debug("Query Problem:" + FiException.exToErrorLog(ex));
            fdr.setValue(-1);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    /**
     * Not null return , null yerine Optional.Empty döner
     *
     * @param sql
     * @param mapParam
     * @return
     */
    public Fdr<Optional<EntClazz>> jdSelectEntityOptBindMap(String sql, Map<String, Object> mapParam) {

        Fdr<Optional<EntClazz>> fdr = new Fdr<>();
        fdr.setValue(Optional.empty());

        //if(checkJdbi(fdr).isFalseBoResult()) return fdr;

        try {
            Optional<EntClazz> result = getJdbi().withHandle(handle -> {
                return handle.select(Fiqt.stoj(sql))
                        .bindMap(mapParam)
                        .mapToBean(getEntityClass())
                        .findFirst();
            });

            fdr.setValue(result);
            fdr.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.get(getClass()).error(FiException.exTosMain(ex));
            fdr.setBoResult(false, ex);
        }
        return fdr;
    }

    private Fdr checkJdbi(Fdr<Optional<EntClazz>> fdr) {
        if (getJdbi() == null) {
            Loghelper.get(getClass()).error("Null jdbi:" + getDatabaseName());
            fdr.setFdrBoExec(false);
            fdr.setMessage("Jdbi Tanımlı Değil :" + getDatabaseName());
            return fdr;
        }
        return fdr;
    }

    private Fdr checkJdbi() {
        if (getJdbi() == null) {
            return Fdr.creBoResult(false).buiMessage("Jdbi Tanımlı Değil :" + getDatabaseName());
        }
        return Fdr.creBoResult(true);
    }

    /**
     * Tek Entity dönmesi için Jdbi'ın findFirst metodu kullanıldı.
     *
     * @param fiQuery
     * @return
     */
    public Fdr<EntClazz> jdSelectEntityBindMap(FiQuery fiQuery) {
        return jdSelectEntityBindMap(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    /**
     * Tek Entity dönmesi için Jdbi'ın findFirst metodu kullanıldı.
     *
     * @param sql
     * @param mapParam
     * @return
     */
    public Fdr<EntClazz> jdSelectEntityBindMap(String sql, Map<String, Object> mapParam) {

        Fdr<EntClazz> fdr = new Fdr<>();
        fdr.setValue(null);

        try {
            Optional<EntClazz> result = getJdbi().withHandle(handle -> {
                return handle.select(Fiqt.stoj(sql))
                        .bindMap(mapParam)
                        .mapToBean(getEntityClass())
                        .findFirst();
            });
            result.ifPresent(fdr::setValue);
            fdr.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.get(getClass()).error("Query Problem");
            Loghelper.get(getClass()).debug(FiException.exTosMain(ex));
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    public Fdr<List<Map<String, Object>>> jdSelectListMapBindMap(String sql, Map<String, Object> mapParam) {

        Jdbi jdbi = getJdbi();

        Fdr<List<Map<String, Object>>> fdr = new Fdr<>();
        fdr.setValue(new ArrayList<>());

//		List<Map<String, Object>> users =
//				handle.createQuery("SELECT id, name FROM user ORDER BY id ASC")
//						.mapToMap()
//						.list();

        try {
            List<Map<String, Object>> result = jdbi.withHandle(handle -> {
                return handle.select(Fiqt.stoj(sql))
                        .bindMap(mapParam)
                        .mapToMap()
                        .list();
            });

            fdr.setValue(result);
            fdr.setFdrBoExec(true);

        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    public Fdr<Optional<EntClazz>> jdSelectEntityBindEntityOptMain(String sql, EntClazz entity) {

        Fdr<Optional<EntClazz>> fdr = new Fdr<>();
        fdr.setValue(Optional.empty());

        try {
            Optional<EntClazz> result = getJdbi().withHandle(handle -> {
                return handle.select(Fiqt.stoj(sql))
                        .bindBean(entity)
                        .mapToBean(getEntityClass())
                        .findFirst();
            });

            fdr.setValue(result);
            fdr.setFdrBoExec(true);

        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setException(ex);
            fdr.setFdrBoExec(false);
        }

        return fdr;

    }

    public Fdr<EntClazz> jdSelectEntityBindEntityMain(String sql, EntClazz entity) {

        Fdr<EntClazz> fdrMain = new Fdr<>();
        //fdrMain.setValue(Optional.empty());

        try {
            Optional<EntClazz> result = getJdbi().withHandle(handle -> {
                return handle.select(Fiqt.stoj(sql))
                        .bindBean(entity)
                        .mapToBean(getEntityClass())
                        .findFirst();
            });

            if (result.isPresent()) fdrMain.setValue(result.get());
            fdrMain.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.get(getClass()).debug("Query Problem");
            Loghelper.get(getClass()).debug(FiException.exTosMain(ex));
            fdrMain.setException(ex);
            fdrMain.setFdrBoExec(false);
        }
        return fdrMain;
    }

    @Deprecated
    public Fdr jdInsertEntityWithScopeIdUseTransHp3(EntClazz listEntity, Function<Handle, Fdr> afterMainGeneral) {
        return jdInsertListWithScopeIdUseTransHp2(Arrays.asList(listEntity), null, null, afterMainGeneral);
    }

    @Deprecated
    public Fdr jdInsertEntityWithScopeIdUseTransHp3(EntClazz entity, BiFunction<Handle, EntClazz, Fdr> beforeMainByEntity, Function<Handle, Fdr> afterMainGeneral) {
        return jdInsertListWithScopeIdUseTransHp2(Arrays.asList(entity), null, beforeMainByEntity, afterMainGeneral);
    }

    public Fdr jdInsertListWithScopeIdUseTransHp2(List<EntClazz> listEntity, String fieldForScopeEntity, BiFunction<Handle, EntClazz, Fdr> beforeWorksByEntity, Function<Handle, Fdr> afterMainGeneral) {
        return jdInsertListWithScopeIdUseTransHp1(listEntity, fieldForScopeEntity, null, beforeWorksByEntity, afterMainGeneral, null);
    }

    public Fdr jdInsertListWithScopeIdUseTransHp1(List<EntClazz> listEntity, String fieldForScopeEntity
            , Function<Handle, Fdr> beforeMainGeneral
            , BiFunction<Handle, EntClazz, Fdr> beforeMainByEntity
            , Function<Handle, Fdr> afterMainGeneral
            , Boolean boBindGeneratedKey) {
        return jdInsertListWithScopeIdMainUseTrans(listEntity, fieldForScopeEntity, beforeMainGeneral, beforeMainByEntity, afterMainGeneral, boBindGeneratedKey, null);
    }

    public Fdr jdInsertListWithScopeIdMainUseTrans(List<EntClazz> listEntity, String fieldForScopeEntity
            , Function<Handle, Fdr> beforeMainGeneral
            , BiFunction<Handle, EntClazz, Fdr> beforeMainByEntity
            , Function<Handle, Fdr> afterMainGeneral
            , Boolean boBindGeneratedKey
            , BiFunction<Handle, EntClazz, Fdr> afterMainByEntity) {

        if (fieldForScopeEntity == null) {
            fieldForScopeEntity = FiQugen.getScopeIdentityFirstField(getEntityClass());

            if (fieldForScopeEntity == null) {
                return new Fdr(false, "Scope Identity Alanı Bulunamadı");
            }
        }

        Jdbi jdbi = getJdbi();
        Fdr fdrBatch = new Fdr();
        String sql1 = FiQugen.insertQueryWoutId(getEntityClass());
        String sql2 = FiQugen.updateScopeIdFieldWithScopeIdFnById(getEntityClass(), fieldForScopeEntity);

        try {
            jdbi.useTransaction(handle -> {
                //try {
                handle.begin();
                if (beforeMainGeneral != null) {
                    Fdr fdrBeforeMainGeneral = beforeMainGeneral.apply(handle);
                    if (fdrBeforeMainGeneral != null && fdrBeforeMainGeneral.getException() != null) {
                        throw fdrBeforeMainGeneral.getException();
                    }
                    fdrBatch.combineAnd(fdrBeforeMainGeneral);
                }
                // transactions
                for (EntClazz ent : listEntity) {
                    //Loghelper.get(getClass()).debug(FiConsole.textObjectFieldsSimple(ent, false));
                    jdhrEntityTransactionsForInsertWithScopeId(beforeMainByEntity, boBindGeneratedKey, afterMainByEntity, fdrBatch, sql1, sql2, handle, ent);
                    //Loghelperr.getInstance(getClass()).debug("Rows Affected: " + execute);
                }

                if (afterMainGeneral != null) {
                    Fdr fdrExtraWorks = afterMainGeneral.apply(handle);

                    if (fdrExtraWorks != null && fdrExtraWorks.getException() != null) {
                        throw fdrExtraWorks.getException();
                    }

                    //fiDbResultBatch.combine(fdrExtraWorks);
                }
                handle.commit();
                //return true;
//				} catch (Exception e) {
//					Loghelper.debugException(getClass(), e);
//					handle.rollback();
//					fiDbResultBatch.setBoResultAndException(false, e);
//					return false;
//				}
            });
            //fiDbResultBatch.setBoResult(boResult);
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            Loghelper.get(getClass()).debug("Genel Catch de Yakalandı");
            fdrBatch.setFdrBoExec(false);
            fdrBatch.setException(ex);
            return fdrBatch;
        }
        //fiDbResultBatch.setBoResult(boResult);
        return fdrBatch;
    }

    public Fdr jdQueryTransactions(List<EntClazz> listEntity
            , Function<Handle, Fdr> beforeMainGeneral
            , BiFunction<Handle, EntClazz, Fdr> entityMain
            , Function<Handle, Fdr> afterMainGeneral) {

        Fdr fdrBatch = new Fdr();

        try {

            getJdbi().useTransaction(handle -> {

                //try {
                handle.begin();
                if (beforeMainGeneral != null) {
                    Fdr fdrBeforeMainGeneral = beforeMainGeneral.apply(handle);

                    if (fdrBeforeMainGeneral != null && fdrBeforeMainGeneral.getException() != null) {
                        throw fdrBeforeMainGeneral.getException();
                    }

                    fdrBatch.combineAnd(fdrBeforeMainGeneral);
                }

                if (entityMain != null) {
                    // transactions
                    for (EntClazz ent : listEntity) {

                        Fdr fdrEntityWorks = entityMain.apply(handle, ent);

                        // entity lerden birinde exception fırlatınca işlem kesilir
                        if (fdrEntityWorks != null && fdrEntityWorks.getException() != null) {
                            throw fdrEntityWorks.getException();
                        }

                        fdrBatch.combineAnd(fdrEntityWorks);

                    }
                }

                if (afterMainGeneral != null) {
                    Fdr fdrAfterMain = afterMainGeneral.apply(handle);

                    if (fdrAfterMain != null && fdrAfterMain.getException() != null) {
                        throw fdrAfterMain.getException();
                    }

                    fdrBatch.combineAnd(fdrAfterMain);
                }

                handle.commit();
                //return true;
//				} catch (Exception e) {
//					Loghelper.debugException(getClass(), e);
//					handle.rollback();
//					fiDbResultBatch.setBoResultAndException(false, e);
//					return false;
//				}

            });

            //fiDbResultBatch.setBoResult(boResult);

        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            Loghelper.get(getClass()).debug("Genel Catch de Yakalandı");
            fdrBatch.setFdrBoExec(false);
            fdrBatch.setException(ex);
        }

        return fdrBatch;
    }

    @FiDraft
    @Deprecated
    public Fdr jdQueryTransactionsDetailed(List<EntClazz> listEntity
            , Function<Handle, Fdr> beforeMainGeneral
            , BiFunction<Handle, EntClazz, Fdr> beforeEntityMain
            , BiFunction<Handle, EntClazz, Fdr> entityMain
            , BiFunction<Handle, EntClazz, Fdr> afterEntityMain
            , Function<Handle, Fdr> afterMainGeneral) {

        Fdr fdrBatch = new Fdr();

        try {

            getJdbi().useTransaction(handle -> {

                //try {
                handle.begin();
                if (beforeMainGeneral != null) {
                    Fdr fdrBeforeMainGeneral = beforeMainGeneral.apply(handle);

                    if (fdrBeforeMainGeneral != null && fdrBeforeMainGeneral.getException() != null) {
                        throw fdrBeforeMainGeneral.getException();
                    }

                    fdrBatch.combineAnd(fdrBeforeMainGeneral);
                }

                if (entityMain != null) {
                    // transactions
                    for (EntClazz ent : listEntity) {

                        Fdr fdrEntityWorks = entityMain.apply(handle, ent);

                        // entity lerden birinde exception fırlatınca işlem kesilir
                        if (fdrEntityWorks != null && fdrEntityWorks.getException() != null) {
                            throw fdrEntityWorks.getException();
                        }

                        fdrBatch.combineAnd(fdrEntityWorks);

                    }
                }

                if (afterMainGeneral != null) {
                    Fdr fdrAfterMain = afterMainGeneral.apply(handle);

                    if (fdrAfterMain != null && fdrAfterMain.getException() != null) {
                        throw fdrAfterMain.getException();
                    }

                    fdrBatch.combineAnd(fdrAfterMain);
                }

                handle.commit();
                //return true;
//				} catch (Exception e) {
//					Loghelper.debugException(getClass(), e);
//					handle.rollback();
//					fiDbResultBatch.setBoResultAndException(false, e);
//					return false;
//				}

            });

            //fiDbResultBatch.setBoResult(boResult);

        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            Loghelper.get(getClass()).debug("Genel Catch de Yakalandı");
            fdrBatch.setFdrBoExec(false);
            fdrBatch.setException(ex);
        }

        return fdrBatch;
    }

    public void jdhrEntityTransactionsForInsertWithScopeIdSh(BiFunction<Handle
            , EntClazz, Fdr> beforeMainByEntity, Boolean boBindGeneratedKey
            , Fdr fdrBatch
            , String sql1, String sql2
            , Handle handle, EntClazz ent) throws Exception {
        jdhrEntityTransactionsForInsertWithScopeId(beforeMainByEntity, boBindGeneratedKey, null
                , fdrBatch, sql1, sql2, handle, ent);
    }

    public void jdhrEntityTransactionsForInsertWithScopeId(
            BiFunction<Handle, EntClazz, Fdr> beforeMainByEntity
            , Boolean boBindGeneratedKey
            , BiFunction<Handle, EntClazz, Fdr> afterMainByEntity
            , Fdr fdrBatch, String sql1, String sql2
            , Handle handle, EntClazz ent) throws Exception {

        if (beforeMainByEntity != null) {
            Fdr fdrBeforeMainByEntity = beforeMainByEntity.apply(handle, ent);

            if (fdrBeforeMainByEntity != null && fdrBeforeMainByEntity.getException() != null) {
                throw fdrBeforeMainByEntity.getException();
            }

            fdrBatch.combineAnd(fdrBeforeMainByEntity);
        }

        if (FiBool.isTrue(boBindGeneratedKey)) { // generated key'de alınacak

            String idField = FiReflectClass.getListIdFields(getEntityClass()).get(0);
            Class idClazz = FiReflection.getFieldClassType(getEntityClass(), idField);

            Optional opId = handle.createUpdate(Fiqt.stoj(sql1 + ";SET NOCOUNT ON;" + sql2))
                    .bindBean(ent)
                    .executeAndReturnGeneratedKeys(idField)
                    .mapTo(idClazz)
                    .findFirst(); // returns row count updated

            opId.ifPresent(insertedId -> {
                FiReflection.setterNested(ent, idField, insertedId);
            });

            fdrBatch.appendRowsAffected(1);

        } else { // generated keyler alınmasına gerek yoksa
            int execute = handle.createUpdate(Fiqt.stoj(sql1 + "; " + sql2))
                    .bindBean(ent)
                    .execute();// returns row count updated
            fdrBatch.appendRowsAffected(execute);
        }

        if (afterMainByEntity != null) {
            Fdr fdrAfterMainByEntity = afterMainByEntity.apply(handle, ent);

            if (fdrAfterMainByEntity != null && fdrAfterMainByEntity.getException() != null) {
                throw fdrAfterMainByEntity.getException();
            }

            fdrBatch.combineAnd(fdrAfterMainByEntity);
        }

    }

    public Fdr jdhInsertWithScopeId(EntClazz ent, Boolean boBindGeneratedKey, String sql1, String sql2, Handle handle) {

        Fdr fdrMain = new Fdr();

        if (FiBool.isTrue(boBindGeneratedKey)) { // generated key'de alınacak

            String idField = FiReflectClass.getListIdFields(getEntityClass()).get(0);
            Class idClazz = FiReflection.getFieldClassType(getEntityClass(), idField);

            try {
                Optional opId = handle.createUpdate(Fiqt.stoj(sql1 + ";SET NOCOUNT ON;" + sql2))
                        .bindBean(ent)
                        .executeAndReturnGeneratedKeys(idField)
                        .mapTo(idClazz)
                        .findFirst(); // returns row count updated

                opId.ifPresent(insertedId -> {
                    FiReflection.setterNested(ent, idField, insertedId);
                });
                fdrMain.appendRowsAffected(1);
                fdrMain.setFdrBoExec(true);
            } catch (Exception ex) {
                fdrMain.setFdrBoExec(false);
                fdrMain.setException(ex);
            }
            return fdrMain;
        } else { // generated keyler alınmasına gerek yoksa
            try {
                int execute = handle.createUpdate(Fiqt.stoj(sql1 + "; " + sql2))
                        .bindBean(ent)
                        .execute();// returns row count updated
                fdrMain.appendRowsAffected(execute);
                fdrMain.setFdrBoExec(true);
            } catch (Exception ex) {
                fdrMain.setFdrBoExec(false);
                fdrMain.setException(ex);
            }
            return fdrMain;
        }

    }

    /**
     * Tablodan sadece tek keyin dönüşünü alır. Bunu entity'nin id alanını kayıt eder.
     * <p>
     * Id Değerini ayrıca Fdr'ye kayıt eder.
     *
     * @param handle
     * @param entity
     * @return
     */
    public Fdr jdhInsertEntityBindKey(Handle handle, EntClazz entity) {

        String idField = FiReflectClass.getListIdFields(getEntityClass()).get(0);
        Class idClazz = FiReflection.getFieldClassType(getEntityClass(), idField);

        return jdhInsertEntityBindKey(handle, entity, idField, idClazz);
    }

    /**
     * Tablodan sadece tek keyin dönüşünü alır. Bunu entity'nin id alanını kayıt eder.
     * <p>
     * Id Değerini ayrıca Fdr'ye kayıt eder.
     *
     * @param handle
     * @param entity
     * @return
     */
    public Fdr jdhInsertEntityBindKey(Handle handle, EntClazz entity, String idField, Class idClazz) {

        Fdr fdrMain = new Fdr();

        String insertQuery = Fiqt.stoj(FiQugen.insertQueryWoutId(getEntityClass()));

        //String idField = FiEntity.getListIdFields(getEntityClass()).get(0);
        //Class idClazz = FiReflection.getFieldClassType(getEntityClass(), idField);

        try {
            ResultBearing resultBearing = handle.createUpdate(Fiqt.stoj(insertQuery)) //+ ";SET NOCOUNT ON;"
                    .bindBean(entity)
                    .executeAndReturnGeneratedKeys(idField);//

            Optional opId = resultBearing.mapTo(idClazz).findFirst();

            opId.ifPresent(insertedId -> {
                //Loghelper.get(getClass()).debug("insertedId:" + insertedId);
                FiReflection.setterNested(entity, idField, insertedId);
                fdrMain.setValue(insertedId);
            });

            fdrMain.appendRowsAffected(1);
            fdrMain.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.get(getClass()).debug(FiException.exTosMain(ex));
            fdrMain.setFdrBoExec(false);
            fdrMain.setException(ex);
        }

        return fdrMain;
    }


    /**
     * id değeri , Fdr'ye kayıt edilir.
     *
     * @param handle
     * @param entity
     * @return
     */
    public Fdr jdhInsertEntityBindKeyV2(Handle handle, EntClazz entity) {

        Fdr fdrMain = new Fdr();

        String insertQuery = Fiqt.stoj(FiQugen.insertQueryWoutId(getEntityClass()));

        String idField = FiReflectClass.getListIdFields(getEntityClass()).get(0);

        try {
            Optional<Map<String, Object>> opId = handle.createUpdate(Fiqt.stoj(insertQuery + ";SET NOCOUNT ON;"))
                    .bindBean(entity)
                    .executeAndReturnGeneratedKeys(idField)
                    .mapToMap()
                    .findFirst();

            opId.ifPresent(stringObjectMap -> fdrMain.setValue(stringObjectMap.get("generated_keys")));
            fdrMain.appendRowsAffected(1);
            fdrMain.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.get(getClass()).debug(FiException.exTosMain(ex));
            fdrMain.setFdrBoExec(false);
            fdrMain.setException(ex);
        }
        return fdrMain;

    }

    public Fdr jdInsertEntityWithScopeId(EntClazz ent) {
        return jdInsertEntityTransactionsWithScopeIdMain(ent, null, null, null);
    }

    public Fdr jdInsertEntityWithScopeId(EntClazz ent, Boolean boBindGeneratedKey) {
        return jdInsertEntityTransactionsWithScopeIdMain(ent, null, null, boBindGeneratedKey);
    }

    /**
     * BeforeWorks ve AfterWorks transaction içerisinde yapılır
     *
     * @param ent
     * @param beforeWorks
     * @param afterWorks
     * @param boBindGeneratedKey
     * @return
     */
    public Fdr jdInsertEntityTransactionsWithScopeIdMain(EntClazz ent, Function<Handle, Fdr> beforeWorks
            , Function<Handle, Fdr> afterWorks, Boolean boBindGeneratedKey) {

        String fieldForScopeEntity = FiQugen.getScopeIdentityFirstField(getEntityClass());

        if (fieldForScopeEntity == null) {
            Loghelper.debugLog(getClass(), "Scope Identity Alanı Bulunamadı");
            return new Fdr(false, "Scope Identity Alanı Bulunamadı");
        }

        //FiConsole.debug(ent);

        //Jdbi jdbi = getJdbi();
        Fdr fdrBatch = new Fdr();

        String sql1 = FiQugen.insertQueryWoutId(getEntityClass());

        try {

            getJdbi().useTransaction(handle -> {

                //try {

                handle.begin();

                if (beforeWorks != null) {
                    Fdr fdrBeforeWorksGeneral = beforeWorks.apply(handle);

                    if (fdrBeforeWorksGeneral != null && fdrBeforeWorksGeneral.getException() != null) {
                        throw fdrBeforeWorksGeneral.getException();
                    }
                    fdrBatch.combineAnd(fdrBeforeWorksGeneral);
                }

                // transactions
                if (FiBool.isTrue(boBindGeneratedKey)) {

                    //String sql2 = FiQueryHelper.updateScopeIdFieldWithSIdById(getEntityClass(), fieldForScopeEntity);
                    String sql2 = FiQugen.updateScopeIdFieldWithScopeIdFnById(getEntityClass(), fieldForScopeEntity);

                    String idField = FiReflectClass.getListIdFields(getEntityClass()).get(0);
                    Class idClazz = FiReflection.getFieldClassType(getEntityClass(), idField);

                    //Loghelper.debug(getClass(), "idField:" + idField);
                    //Loghelper.debug(getClass(), "idClazz:" + idClazz);
                    //Loghelper.debug(getClass(), "sql1:" + fimSqlAt(sql1));

                    Optional optId = handle.createUpdate(Fiqt.stoj(sql1) + ";SET NOCOUNT ON;" + sql2)
                            .bindBean(ent)
                            .executeAndReturnGeneratedKeys(idField)
                            .mapTo(idClazz)
                            .findFirst();

                    optId.ifPresent(insertedId -> {
                        FiReflection.setterNested(ent, idField, insertedId);
                        //int rowsAffScopeFieldUpdate = handle.createUpdate(fimSqlAt(sql2)).bind("scopeId", insertedId).execute();
                    });

                    //throw new Exception("Kasıtlı Son");

                    //Loghelper.debug(getClass(), "sql2"+sql2);
                    //Loghelper.debug(getClass(), "sql2 rows affected"+execute);
                    fdrBatch.appendRowsAffected(1);

                } else { // generated keyler alınmasına gerek yoksa

                    String sqlUpScope = FiQugen.updateScopeIdFieldWithScopeIdFnById(getEntityClass(), fieldForScopeEntity);

                    int execute = handle.createUpdate(Fiqt.stoj(sql1 + "; " + sqlUpScope))
                            .bindBean(ent)
                            .execute();// returns row count updated
                    fdrBatch.appendRowsAffected(execute);

                }

                //Loghelperr.getInstance(getClass()).debug("Rows Affected: " + execute);

                if (afterWorks != null) {
                    Fdr fdrExtraWorks = afterWorks.apply(handle);

                    if (fdrExtraWorks != null && fdrExtraWorks.getException() != null) {
                        throw fdrExtraWorks.getException();
                    }
                    fdrBatch.combineAnd(fdrExtraWorks);
                }

                handle.commit();
                //return true;
//				} catch (Exception e) {
//					Loghelper.debugException(getClass(), e);
//					handle.rollback();
//					fiDbResultBatch.setBoResultAndException(false, e);
//					fiDbResultBatch.setRowsAffected(-1);
//					//return false;
//				}
            });
            fdrBatch.setFdrBoExec(true);
        } catch (Exception ex) {
            Loghelper.debugException(getClass(), ex);
            Loghelper.get(getClass()).debug("Genel Catch de Yakalandı");
            fdrBatch.setBoResult(false, ex);
//			fdrResultBatch.setRowsAffected(-1);
            return fdrBatch;
        }

        //fiDbResultBatch.setBoResult(boResult);
        return fdrBatch;
    }

    public Fdr jdhInsertEntityWithScopeIdMain(Handle handle, EntClazz ent, Boolean boBindGeneratedKey) {

        String fieldForScopeEntity = FiQugen.getScopeIdentityFirstField(getEntityClass());

        if (fieldForScopeEntity == null) {
            Loghelper.debugLog(getClass(), "Scope Identity Alanı Bulunamadı");
            return new Fdr(false, "Scope Identity Alanı Bulunamadı", true);
        }

        Fdr fdrMain = new Fdr();

        String sql1 = FiQugen.insertQueryWoutId(getEntityClass());

        if (FiBool.isTrue(boBindGeneratedKey)) {

            //String sql2 = FiQueryGenerator.updateScopeIdFieldWithSIdById(getEntityClass(), fieldForScopeEntity);
            String sql2 = FiQugen.updateScopeIdFieldWithScopeIdFnById(getEntityClass(), fieldForScopeEntity);

            String idField = FiReflectClass.getListIdFields(getEntityClass()).get(0);
            Class idClazz = FiReflection.getFieldClassType(getEntityClass(), idField);

            //Loghelper.debug(getClass(), "idField:" + idField);
            //Loghelper.debug(getClass(), "idClazz:" + idClazz);
            //Loghelper.debug(getClass(), "sql1:" + fimSqlAt(sql1));

            try {
                Optional optId = handle.createUpdate(Fiqt.stoj(sql1) + ";SET NOCOUNT ON;" + sql2)
                        .bindBean(ent)
                        .executeAndReturnGeneratedKeys(idField)
                        .mapTo(idClazz)
                        .findFirst();

                optId.ifPresent(insertedId -> {
                    FiReflection.setterNested(ent, idField, insertedId);
                    //int rowsAffScopeFieldUpdate = handle.createUpdate(fimSqlAt(sql2)).bind("scopeId", insertedId).execute();
                });
                fdrMain.appendRowsAffected(1);
                fdrMain.setFdrBoExec(true);

            } catch (Exception ex) {
                fdrMain.setException(ex);
                fdrMain.setFdrBoExec(false);
                return fdrMain;
            }

            //throw new Exception("Kasıtlı Son");

            //Loghelper.debug(getClass(), "sql2"+sql2);
            //Loghelper.debug(getClass(), "sql2 rows affected"+execute);


        } else { // generated keyler alınmasına gerek yoksa

            String sqlUpScope = FiQugen.updateScopeIdFieldWithScopeIdFnById(getEntityClass(), fieldForScopeEntity);

            try {
                int execute = handle.createUpdate(Fiqt.stoj(sql1 + "; " + sqlUpScope))
                        .bindBean(ent)
                        .execute();// returns row count updated
                fdrMain.appendRowsAffected(execute);
                fdrMain.setFdrBoExec(true);
            } catch (Exception ex) {
                fdrMain.setException(ex);
                fdrMain.setFdrBoExec(false);
                return fdrMain;
            }

        }

        return fdrMain;
    }

    public Fdr jdhInsertEntityWithScopeIdAlt1(EntClazz ent, Boolean boBindGeneratedKey, Handle handle) {

        String fieldForScopeEntity = FiQugen.getScopeIdentityFirstField(getEntityClass());

        if (fieldForScopeEntity == null) {
            Loghelper.debugLog(getClass(), "Scope Identity Alanı Bulunamadı");
            Exception exception = new Exception("Scope Identity bulunamadı.");
            return new Fdr(false, exception);
        }

        Fdr fdrMain = new Fdr();

        String sql1 = FiQugen.insertQueryWoutId(getEntityClass());
        String sql2 = FiQugen.updateScopeIdFieldWithScopeIdFnById(getEntityClass(), fieldForScopeEntity);

        try {

            // transactions
            if (FiBool.isTrue(boBindGeneratedKey)) {

                String idField = FiReflectClass.getListIdFields(getEntityClass()).get(0);
                Class idClazz = FiReflection.getFieldClassType(getEntityClass(), idField);

                Optional opId = handle.createUpdate(Fiqt.stoj(sql1 + ";SET NOCOUNT ON;" + sql2))
                        .bindBean(ent)
                        .executeAndReturnGeneratedKeys(idField)
                        .mapTo(idClazz)
                        .findFirst(); // returns row count updated

                opId.ifPresent(insertedId -> {
                    FiReflection.setterNested(ent, idField, insertedId);
                });
                fdrMain.appendRowsAffected(1);

            } else { // generated keyler alınmasına gerek yoksa
                int execute = handle.createUpdate(Fiqt.stoj(sql1 + "; " + sql2))
                        .bindBean(ent)
                        .execute();// returns row count updated
                fdrMain.appendRowsAffected(execute);
            }

            //Loghelperr.getInstance(getClass()).debug("Rows Affected: " + execute);
            fdrMain.setFdrBoExec(true);
            return fdrMain;
        } catch (Exception exception) {
            Loghelper.debugException(getClass(), exception);
            fdrMain.setBoResult(false, exception);
            return fdrMain;
        }

    }

    public Fdr jdhInsertEntityBindKey0(EntClazz ent, Boolean boBindGeneratedKey, Handle handle) {

        Fdr fdrMain = new Fdr();

        String sql1 = FiQugen.insertQueryWoutId(getEntityClass());

        try {
            if (FiBool.isTrue(boBindGeneratedKey)) {

                String idField = FiReflectClass.getListIdFields(getEntityClass()).get(0);
                Class idClazz = FiReflection.getFieldClassType(getEntityClass(), idField);

                Optional opId = handle.createUpdate(Fiqt.stoj(sql1))
                        .bindBean(ent)
                        .executeAndReturnGeneratedKeys() //idField 18072023
                        .mapTo(idClazz)
                        .findFirst(); // returns generated keys

                opId.ifPresent(insertedId -> {
                    FiReflection.setterNested(ent, idField, insertedId);
                });
                fdrMain.appendRowsAffected(1);

            } else { // generated keyler alınmasına gerek yoksa
                int execute = handle.createUpdate(Fiqt.stoj(sql1))
                        .bindBean(ent)
                        .execute();// returns row count updated
                fdrMain.appendRowsAffected(execute);
            }
            //Loghelperr.getInstance(getClass()).debug("Rows Affected: " + execute);
            fdrMain.setFdrBoExec(true);
            return fdrMain;
        } catch (Exception exception) {
            Loghelper.debugException(getClass(), exception);
            fdrMain.setBoResult(false, exception);
            return fdrMain;
        }

    }

    @Deprecated
    @FiDraft
    public Fdr jdInsertEntityWithMaxFields(EntClazz entity) {
        String sql = FiQugen.insertQueryWoutIdWithMaxFields(getEntityClass());
//		Loghelper.get(getClass()).debug("Sql:" + sql);
        return jdInsertQueryBindEntityMain(sql, entity);
    }

    @Deprecated
    @FiDraft
    public Fdr jdhInsertEntityWithMaxFields(EntClazz entity, Handle handle) {
        String sql = FiQugen.insertQueryWoutIdWithMaxFields(getEntityClass());
//		Loghelper.get(getClass()).debug("Sql:" + sql);
        return jdhInsertQueryBindEntity(sql, entity, handle);
    }


    public Fdr jdExecuteTrans(Function<Handle, Fdr> fnTransactions) {

        Fdr fdrMain = new Fdr();

        try {

            getJdbi().useTransaction(handle -> {

                handle.getConnection().setAutoCommit(false);
                //handle.begin();
                Fdr fdr = fnTransactions.apply(handle);
                fdrMain.combineAnd(fdr);

                if (!fdr.getListExceptionNtn().isEmpty()) {
                    List<Exception> listException = fdr.getListException();
                    //handle.rollback();
                    throw listException.get(0);
                }

                if (fdr.getException() != null) {
                    //handle.rollback();
                    throw fdr.getException();
                }

                handle.commit();
                //boResult yukarıdaki transactiondan alır
                //fdrMain.setBoResult(true);
                //handle.close(); // close edilirse : failed to commit transaction hatası veriyor
            });

        } catch (Exception ex) {
            Loghelper.get(getClass()).error(FiException.exTosMain(ex));
            fdrMain.setBoResult(false, ex);
        }

        return fdrMain;
    }

    /**
     * Transactionlar AbsRepoJdbi ve Handle ile yönetilir.
     * <p>
     * Transaction Catch'i dışarıda yakalanıyor.
     *
     * @param fnTransaction
     * @return
     */
    public Fdr jdExecuteTransByUse(Function<Handle, Fdr> fnTransaction) {
        // BiFunction<AbsRepoJdbi<EntClazz>, Handle, Fdr>

        Fdr fdrMain = new Fdr();

        try {
            // Use Transaction kullanılıyor ****
            getJdbi().useTransaction(handle -> {
                //handle.begin();
                handle.getConnection().setAutoCommit(false);

                Fdr fdr = fnTransaction.apply(handle);
                fdrMain.combineAnd(fdr);

                if (fdr.getException() != null) {
                    throw fdr.getException();
                }

                handle.commit();
                //Loghelper.get(getClass()).debug("handle commit edildi");
            });

        } catch (Exception ex) {
            Loghelper.get(getClass()).debug(FiException.exTosMain(ex));
            //Loghelper.get(getClass()).debug("Genel Catch de Yakalandı");
            fdrMain.setBoResult(false, ex);
        }

        return fdrMain;
    }

    /**
     * inTransaction ile kullanımı (fdr callback function içerisinden return edilir)
     * <p>
     * Using start 13-07-22
     *
     * @param fnTransactions
     * @return
     */
    public Fdr jdExecuteTransByIn(BiFunction<AbsRepoGenJdbi<EntClazz>, Handle, Fdr> fnTransactions) {

        // InTransaction Kulanılıyor ****
        Fdr fdrJdbi = getJdbi().inTransaction(handle -> {

            try {
                handle.begin();
                Fdr fdrTrans = fnTransactions.apply(this, handle);
                // fdrMain.combineAnd(fdrTrans);

                if (fdrTrans.getException() != null) {
                    throw fdrTrans.getException();
                }
                handle.commit();
                return fdrTrans;
            } catch (Exception ex) {
                Fdr fdrMain = new Fdr();
                Loghelper.debugException(getClass(), ex);
                handle.rollback();
                fdrMain.setBoResult(false, ex);
                return fdrMain;
            }
        });

        return fdrJdbi;
    }

    public Fdr jdDeleteBindEntityMain(String sql, EntClazz entClazz) {
        return jdDeleteBindObjectMain(sql, entClazz);
    }

    public Fdr<List<EntClazz>> jdSelectListBindMap(FiQuery fiQuery) {
        return jdSelectListBindMapMainNtn(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    public Fdr<List<EntClazz>> jdSelectList(FiQuery fiQuery) {
        return jdSelectListBindMapMainNtn(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    /**
     * FiQuery oluşturarak prepSqlParamFull metodunu çalıştırır.
     *
     * @param sql
     * @param fiKeyBean
     * @return
     */
    public Fdr<List<EntClazz>> jdSelectListWitPrep(String sql, FiKeyBean fiKeyBean) {
        FiQuery fiQuery = new FiQuery(sql, fiKeyBean);
        fiQuery.processParamsC1();
//		Loghelper.get(getClass()).debug("Sql:" + fiQuery.getTxQuery());
        return jdSelectListBindMapMainNtn(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    /**
     *
     *  fiQuery.convertListParamsToMultiParams() çalıştırılır, ekstra olarak.
     *
     * @param sql
     * @param fiKeyBean
     * @return
     */
    public Fdr<List<EntClazz>> jdSelectListWitConvertMulti(String sql, FiKeyBean fiKeyBean) {
        FiQuery fiQuery = new FiQuery(sql, fiKeyBean);
        fiQuery.convertListParamsToMultiParams();
        //Loghelper.get(getClass()).debug("sql:" + fiQuery.getTxQuery());
        return jdSelectListBindMapMainNtn(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    public Fdr updateByFiColUpdateFieldsWhereId(FiKeyBean formAsKeyBean, List<FiCol> listFormElements) {
        String sql = FiQugen.updateQueryWithFiColsByUpdateAndKeyField(getEntityClass(), listFormElements);
        Loghelper.get(getClass()).debug("sql:" + sql);
        return jdUpdateBindMapMain(sql, formAsKeyBean);
    }

}
