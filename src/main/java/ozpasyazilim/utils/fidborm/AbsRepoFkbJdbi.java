package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.core.FiException;
import ozpasyazilim.utils.datatypes.FiKeybean;
import ozpasyazilim.utils.datatypes.FkbList;
import ozpasyazilim.utils.jdbi.FiKeyBeanMapper;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.returntypes.FdrFkbList;
import ozpasyazilim.utils.table.FiCol;

import java.util.*;

public abstract class AbsRepoFkbJdbi extends AbsRepoJdbiCore { //implements IRepoJdbi

    protected Handle handleRepo;

    public IFiTableMeta iFiTableMeta;

    // connProfile veya jdbi ile constructor kullanılmalı
    //    public AbsRepoFkbJdbi() {
    //
    //    }

    public AbsRepoFkbJdbi(Jdbi jdbi) {
        setJdbi(jdbi);
    }

    public AbsRepoFkbJdbi(String connProfile) {
        this.connProfile = connProfile;
    }

    public void setAutoClass() {
    }

    public Handle getHandleRepo() {
        return handleRepo;
    }

    public void setHandleRepo(Handle handleRepo) {
        this.handleRepo = handleRepo;
    }

    public AbsRepoFkbJdbi(Handle handleRepo) {
        setHandleRepo(handleRepo);
    }

    // Sorgu Metodları
    public Fdr<List<FiKeybean>> jdSelectListFkb2BindMapMain(String sqlQuery, Map<String, Object> mapBind) {

        Fdr<List<FiKeybean>> fdr = new Fdr<>();
        fdr.setValue(new ArrayList<>());

        try {
            List<FiKeybean> result = getJdbi().withHandle(handle -> {
                return handle.createQuery(Fiqt.stoj(sqlQuery))
                        .bindMap(mapBind)
                        .map(new FiKeyBeanMapper(false))
                        .list();
            });
            fdr.setBoResultAndValue(true, result, 1);

        } catch (Exception ex) {
            Loghelper.get(getClass()).error("Query Problem");
            Loghelper.get(getClass()).error("Hata (Exception):\n" + FiException.exTosMain(ex));
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    public FdrFkbList jdSelectListFkb3BindMapMain(FiQuery fiQuery) {
        return jdSelectListFkb3BindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    public Fdr<FkbList> jdSelectListFkb1BindMapMain(String sqlQuery, Map<String, Object> mapBind) {

        Fdr<FkbList> fdr = new Fdr<>();
        fdr.setValue(new FkbList());

        try {
            List<FiKeybean> result = getJdbi().withHandle(handle -> {
                return handle.createQuery(Fiqt.stoj(sqlQuery))
                        .bindMap(mapBind)
                        .map(new FiKeyBeanMapper(false))
                        .list();
            });
            FkbList fkbList = new FkbList(result);
            fdr.setBoResultAndValue(true, fkbList, 1);
        } catch (Exception ex) {
            Loghelper.get(getClass()).error("Query Problem");
            Loghelper.get(getClass()).error("Hata (Exception):\n" + FiException.exTosMain(ex));
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    public FdrFkbList jdSelectListFkb3BindMapMain(String sqlQuery, Map<String, Object> mapBind) {

        FdrFkbList fdr = new FdrFkbList();
        fdr.setValue(new FkbList());

        try {
            List<FiKeybean> result = getJdbi().withHandle(handle -> {
                return handle.createQuery(Fiqt.stoj(sqlQuery))
                        .bindMap(mapBind)
                        .map(new FiKeyBeanMapper(false))
                        .list();
            });
            FkbList fkbList = new FkbList(result);
            fdr.setBoResultAndValue(true, fkbList, 1);
        } catch (Exception ex) {
            Loghelper.get(getClass()).error("Query Problem");
            Loghelper.get(getClass()).error("Hata (Exception):\n" + FiException.exTosMain(ex));
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    /**
     * Value null dönerse, -1 olarak yorumlar
     *
     * @param sql
     * @param fiMapParams
     * @return
     */
    public Fdr<Integer> jdSelectSingleIntOrMinus1(String sql, FiKeybean fiMapParams) {
        Fdr<Integer> fdrSql = jdSelectSingleEntityBindMap(sql, fiMapParams, Integer.class);
        if (fdrSql.getValue() == null) {
            fdrSql.setValue(-1);
        }
        return fdrSql;
    }

    public Fdr<FiKeybean> jdSelectFkbSingleBindMapMain(String sqlQuery, Map<String, Object> mapBind) {

        Fdr<FiKeybean> fdr = new Fdr<>();
        fdr.setValue(new FiKeybean());

        try {
            Optional<FiKeybean> result = getJdbi().withHandle(handle -> {
                return handle.createQuery(Fiqt.stoj(sqlQuery))
                        .bindMap(mapBind)
                        .map(new FiKeyBeanMapper(false))
                        .findOne();
            });

            result.ifPresent(fdr::setValue); //if (result.isPresent()) fdr.setValue(result.get());

            fdr.setBoResultAndRowsAff(true, 1);

        } catch (Exception ex) {
            Loghelper.get(getClass()).error("Query Problem");
            Loghelper.get(getClass()).error("Hata (Exception):\n" + FiException.exTosMain(ex));
            fdr.setBoResult(false, ex);
        }

        return fdr;
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
    public Fdr<Integer> jdSelectSingleInt(String sql, FiKeybean fiKeyBean) {
        return jdSelectSingleEntityBindMap(sql, fiKeyBean, Integer.class);
    }

    public <PrmEnt> Fdr<PrmEnt> jdSelectSingleEntityBindMap(String sql, Map<String, Object> mapParam, Class<PrmEnt> resultClazz) {

        Fdr<PrmEnt> fdr = new Fdr<>();
        fdr.setValue(null);

        try {
            Optional<PrmEnt> result = getJdbi().withHandle(handle -> {
                return handle.select(Fiqt.stoj(sql))
                        .bindMap(mapParam)
                        .mapTo(resultClazz)
                        .findFirst();
            });

            result.ifPresent(fdr::setValue);
            fdr.setFdrBoResult(true);
        } catch (Exception ex) {
            Loghelper.get(getClass()).error(FiException.exTosMain(ex));
            fdr.setFdrBoResult(false);
            fdr.setValue(null);
        }
        return fdr;
    }

    public Fdr jdUpdateBindMapMain(FiQuery fiQuery) {
        return jdUpdateBindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    public Fdr jdUpdateBindMapMain(String updateQuery, Map<String, Object> fiMapParams) {
        Fdr fdrMain = new Fdr();
        try {
            Integer rowCountUpdate = getJdbi().withHandle(handle -> {
                return handle.createUpdate(Fiqt.stoj(updateQuery))
                        .bindMap(fiMapParams)
                        .execute(); // returns row count updated
            });
            //Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
            //fiDbResult.setLnSuccessWithUpBoResult(1, rowCountUpdate);
            fdrMain.setBoResultAndRowsAff(true, rowCountUpdate);
        } catch (Exception ex) {
            fdrMain.setBoResult(false, ex);
            Loghelper.get(getClass()).error(FiException.exToErrorLog(ex));
        }
        return fdrMain;
    }

    public Fdr jdInsertFiQuery(FiQuery fiQuery) {
        return jdInsertBindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    /**
     * Update ile aynı metoddan kopyalandı
     *
     * @param insertQuery
     * @param fiMapParams
     * @return
     */
    public Fdr jdInsertBindMapMain(String insertQuery, Map<String, Object> fiMapParams) {

        Fdr fdrMain = new Fdr();
        try {
            Integer rowCountUpdate = getJdbi().withHandle(handle -> {
                return handle.createUpdate(Fiqt.stoj(insertQuery))
                        .bindMap(fiMapParams)
                        .execute(); // returns row count updated
            });
            //Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
            //fiDbResult.setLnSuccessWithUpBoResult(1, rowCountUpdate);
            fdrMain.setBoResultAndRowsAff(true, rowCountUpdate);
        } catch (Exception ex) {
            fdrMain.setBoResult(false, ex);
            Loghelper.get(getClass()).error(FiException.exToErrorLog(ex));
        }
        return fdrMain;
    }

    public Fdr jdDeleteFiQuery(FiQuery fiQuery) {
        return jdDeleteBindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
    }

    public Fdr jdDeleteBindMapMain(String insertQuery, Map<String, Object> fiMapParams) {

        Fdr fdrMain = new Fdr();
        try {
            Integer rowCountUpdate = getJdbi().withHandle(handle -> {
                return handle.createUpdate(Fiqt.stoj(insertQuery))
                        .bindMap(fiMapParams)
                        .execute(); // returns row count updated
            });
            //Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
            //fiDbResult.setLnSuccessWithUpBoResult(1, rowCountUpdate);
            fdrMain.setBoResultAndRowsAff(true, rowCountUpdate);
        } catch (Exception ex) {
            fdrMain.setBoResult(false, ex);
            Loghelper.get(getClass()).error(FiException.exToErrorLog(ex));
        }
        return fdrMain;
    }


    public <EntMethodClazz> Fdr<List<EntMethodClazz>> jdSelectListBindMapMain(String sqlQuery, Map<String, Object> mapBind, Class<EntMethodClazz> clazz) {

        Fdr<List<EntMethodClazz>> fdr = new Fdr<>();
        fdr.setValue(new ArrayList<>());

        try {
            List<EntMethodClazz> result = getJdbi().withHandle(handle -> {
                return handle.createQuery(Fiqt.stoj(sqlQuery))
                        .bindMap(mapBind)
                        .mapToBean(clazz)
                        .list();
            });
            fdr.setBoResultAndValue(true, result, 1);
        } catch (Exception ex) {
            Loghelper.get(getClass()).error("Query Problem");
            Loghelper.get(getClass()).error("Hata (Exception):\n" + FiException.exTosMain(ex));
            fdr.setBoResult(false, ex);
        }
        return fdr;
    }

    public IFiTableMeta getiFiTableMeta() {
        return iFiTableMeta;
    }

    public void setiFiTableMeta(IFiTableMeta iFiTableMeta) {
        this.iFiTableMeta = iFiTableMeta;
    }

    public Fdr jdInsFkb(FiKeybean formAsFkb, Boolean boInserFieldsOnly, IFiTableMeta iFiTableMeta) {

        if (iFiTableMeta == null) iFiTableMeta = getiFiTableMeta();

        String sql = FiQugen.insertFiCols2(iFiTableMeta, formAsFkb.getListFiColInit(), boInserFieldsOnly);

        FiQuery fiQuery = new FiQuery(sql, formAsFkb);
        //fiQuery.logQueryAndParams();

        return jdInsertFiQuery(fiQuery);
    }


    /**
     * @param formAsFkb
     * @param boUpdateFieldsOnly : updateField true olanlar sorguda güncellenir
     * @return
     */
    public Fdr jdUpFkbByIdFields(FiKeybean formAsFkb, Boolean boUpdateFieldsOnly) {
        String sql = FiQugen.updateFiColsArb(getiFiTableMeta(), formAsFkb.getListFiColInit(), boUpdateFieldsOnly);

        FiQuery fiQuery = new FiQuery(sql, formAsFkb);
        //fiQuery.logQueryAndParams();

        return jdInsertFiQuery(fiQuery);
    }

    public Fdr jdDeleteFkbByIdCols(FiKeybean fiKeyBean, IFiTableMeta iFiTableMeta) {

        String sql = FiQugen.deleteWhereIdFiColsV2(iFiTableMeta, fiKeyBean.getListFiColInit());

        FiQuery fiQuery = new FiQuery(sql, fiKeyBean);
        //fiQuery.logQueryAndParams();

        return jdDeleteFiQuery(fiQuery);
    }

    public Fdr jdDeleteFkbByIdColsV2(FiKeybean fiKeyBean, IFiTableMeta iFiTableMeta, List<FiCol> fiCols) {

        String sql = FiQugen.deleteWhereIdFiColsV2(iFiTableMeta, fiCols);

        FiQuery fiQuery = new FiQuery(sql, fiKeyBean);
        //fiQuery.logQueryAndParams();

        return jdDeleteFiQuery(fiQuery);
    }

    /**
     * Transaction olmalı
     *
     * @param fkbList
     * @return
     */
    public Fdr jdDeleteListById(List<FiKeybean> fkbList) {
        Fdr fdrMain = new Fdr();
        for (FiKeybean fiKeyBean : fkbList) {
            Fdr fdrDelete = jdDeleteFkbByIdCols(fiKeyBean, getiFiTableMeta());
            fdrMain.combineAnd(fdrDelete);
        }
        return fdrMain;
    }

    /**
     * FkbList'e FiColList tanımlaması yapılmalı
     *
     * @param fkbList
     * @return
     */
    public Fdr jdDeleteListByIdV2(FkbList fkbList) {
        Fdr fdrMain = new Fdr();

        //Loghelper.get(getClass()).debug("FkbList.getFiColList():" + FiConsole.textListObjectsNotNullFields(fkbList.getFiColList()));

        for (FiKeybean fiKeyBean : fkbList) {
            Fdr fdrDelete = jdDeleteFkbByIdColsV2(fiKeyBean, getiFiTableMeta(), fkbList.getFiColList());
            fdrMain.combineAnd(fdrDelete);
        }
        return fdrMain;
    }
}
