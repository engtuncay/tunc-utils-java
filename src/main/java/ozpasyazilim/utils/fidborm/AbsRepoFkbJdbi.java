package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.core.FiException;
import ozpasyazilim.utils.datatypes.Fkb;
import ozpasyazilim.utils.datatypes.FkbList;
import ozpasyazilim.utils.datatypes.Fkfic;
import ozpasyazilim.utils.jdbi.FiKeyBeanMapper;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.returntypes.FdrFkbList;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

import java.util.*;
import java.util.function.Function;

public abstract class AbsRepoFkbJdbi extends AbsRepoJdbiCore { //implements IRepoJdbi

  private Handle handleRepo;

  private IFiTableMeta iFiTableMeta;

  private Fkfic fkcDmFields;
  private Fkfic fkcFieldsAll;
  private FicList fclTable;
  private FiCol ficIdAuto;

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
  public Fdr<List<Fkb>> jdSelectListFkb2BindMapMain(String sqlQuery, Map<String, Object> mapBind) {

    Fdr<List<Fkb>> fdr = new Fdr<>();
    fdr.setValue(new ArrayList<>());

    try {
      List<Fkb> result = getJdbi().withHandle(handle -> {
        return handle.createQuery(FiQueTools.stoj(sqlQuery))
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

  public FdrFkbList jdSelectFkbList(FiQuery fiQuery) {
    FdrFkbList fdr = new FdrFkbList();
    fdr.setValue(new FkbList());

    try {
      List<Fkb> result = getJdbi().withHandle(handle -> {
        return handle.createQuery(FiQueTools.stoj(fiQuery.getTxQuery()))
            .bindMap(fiQuery.getMapParams())
            .map(new FiKeyBeanMapper(false))
            .list();
      });
      FkbList fkbList = new FkbList(result);
      fdr.setBoResultAndValue(true, fkbList, 1);
      fdr.setFdFkbListVal(fkbList);
    } catch (Exception ex) {
      Loghelper.get(getClass()).error("Query Problem. Hata (Exception):\n" + FiException.exTosMain(ex));
      fdr.setBoResult(false, ex);
    }

    return fdr;
  }

  public Fdr jdSelectAsFkbList(FiQuery fiQuery) {

    Fdr fdr = new Fdr();
    fdr.setValue(new FkbList());

    try {
      List<Fkb> result = getJdbi().withHandle(handle -> {
        return handle.createQuery(FiQueTools.stoj(fiQuery.getTxQuery()))
            .bindMap(fiQuery.getMapParams())
            .map(new FiKeyBeanMapper(false))
            .list();
      });
      FkbList fkbList = new FkbList(result);
      fdr.setFdBoResult(true);
      fdr.setFdFkbListVal(fkbList);
      //fdr.setRowsAffected(1);
    } catch (Exception ex) {
      Loghelper.get(getClass()).error("Query Problem. Hata (Exception):\n" + FiException.exTosMain(ex));
      fdr.setBoResult(false, ex);
    }

    return fdr;
  }

  public Fdr jdSelFirstAsFkb(FiQuery fiQuery) {

    Fdr fdr = new Fdr();
    fdr.setValue(new FkbList());

    try {
      List<Fkb> result = getJdbi().withHandle(handle -> {
        return handle.createQuery(FiQueTools.stoj(fiQuery.getTxQuery()))
            .bindMap(fiQuery.getMapParams())
            .map(new FiKeyBeanMapper(false))
            .list();
      });

      //FkbList fkbList = new FkbList(result);
      fdr.setFdBoResult(true);

      if (!result.isEmpty()) {
        //Loghelper.get(getClass()).debug("fkb val set edildi");
        fdr.setFdFkbVal(result.get(0));
      }
      //fdr.setRowsAffected(1);
    } catch (Exception ex) {
      Loghelper.get(getClass()).error("Query Problem. Hata (Exception):\n" + FiException.exTosMain(ex));
      fdr.setBoResult(false, ex);
    }

    return fdr;
  }

  public Fdr<FkbList> jdSelectListFkb1BindMapMain(String sqlQuery, Map<String, Object> mapBind) {

    Fdr<FkbList> fdr = new Fdr<>();
    fdr.setValue(new FkbList());

    try {
      List<Fkb> result = getJdbi().withHandle(handle -> {
        return handle.createQuery(FiQueTools.stoj(sqlQuery))
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

  public FdrFkbList jdSelectFkbList(String sqlQuery, Map<String, Object> mapBind) {
    Fkb fkbParams = new Fkb(mapBind);
    FiQuery fiQuery = new FiQuery(sqlQuery, fkbParams);
    return jdSelectFkbList(fiQuery);
  }

  /**
   * Value null dönerse, -1 olarak yorumlar
   *
   * @param sql
   * @param fiMapParams
   * @return
   */
  public Fdr<Integer> jdSelectSingleIntOrMinus1(String sql, Fkb fiMapParams) {
    Fdr<Integer> fdrSql = jdSelectSingleEntityBindMap(sql, fiMapParams, Integer.class);
    if (fdrSql.getValue() == null) {
      fdrSql.setValue(-1);
    }
    return fdrSql;
  }

  public Fdr<Fkb> jdSelectFkbSingleBindMapMain(String sqlQuery, Map<String, Object> mapBind) {

    Fdr<Fkb> fdr = new Fdr<>();
    fdr.setValue(new Fkb());

    try {
      Optional<Fkb> result = getJdbi().withHandle(handle -> {
        return handle.createQuery(FiQueTools.stoj(sqlQuery))
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
  public Fdr<Integer> jdSelectSingleInt(String sql, Fkb fiKeyBean) {
    return jdSelectSingleEntityBindMap(sql, fiKeyBean, Integer.class);
  }

  public <PrmEnt> Fdr<PrmEnt> jdSelectSingleEntityBindMap(String sql, Map<String, Object> mapParam, Class<PrmEnt> resultClazz) {

    Fdr<PrmEnt> fdr = new Fdr<>();
    fdr.setValue(null);

    try {
      Optional<PrmEnt> result = getJdbi().withHandle(handle -> {
        return handle.select(FiQueTools.stoj(sql))
            .bindMap(mapParam)
            .mapTo(resultClazz)
            .findFirst();
      });

      result.ifPresent(fdr::setValue);
      fdr.setFdBoResult(true);
    } catch (Exception ex) {
      Loghelper.get(getClass()).error(FiException.exTosMain(ex));
      fdr.setFdBoResult(false);
      fdr.setValue(null);
    }
    return fdr;
  }

  public Fdr jdUpdateBindMapMain(FiQuery fiQuery) {
    return jdUpdateBindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
  }

  /**
   * fkbParams bind eder
   *
   * @param fiQuery
   * @return
   */
  public Fdr jdExecute(FiQuery fiQuery) {
    return jdUpdateBindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
  }

  public Fdr jdUpdateBindMapMain(String updateQuery, Map<String, Object> fiMapParams) {

    Fdr fdrMain = new Fdr();
    try {
      Integer rowCountUpdate = getJdbi().withHandle(handle -> {
        return handle.createUpdate(FiQueTools.stojExcludable1(updateQuery))
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
        return handle.createUpdate(FiQueTools.stoj(insertQuery))
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
        return handle.createUpdate(FiQueTools.stoj(insertQuery))
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
        return handle.createQuery(FiQueTools.stoj(sqlQuery))
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

  public Fdr jdInsFkb(Fkb formAsFkb, Boolean boInserFieldsOnly, IFiTableMeta iFiTableMeta) {

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
  public Fdr jdUpFkbByIdFields(Fkb formAsFkb, Boolean boUpdateFieldsOnly) {
    String sql = FiQugen.updateFiColsArb(getiFiTableMeta(), formAsFkb.getListFiColInit(), boUpdateFieldsOnly);

    FiQuery fiQuery = new FiQuery(sql, formAsFkb);
    //fiQuery.logQueryAndParams();

    return jdInsertFiQuery(fiQuery);
  }

  public Fdr jdDeleteFkbByIdCols(Fkb fiKeyBean, IFiTableMeta iFiTableMeta) {

    String sql = FiQugen.deleteWhereIdFiColsV2(iFiTableMeta, fiKeyBean.getListFiColInit());

    FiQuery fiQuery = new FiQuery(sql, fiKeyBean);
    //fiQuery.logQueryAndParams();

    return jdDeleteFiQuery(fiQuery);
  }

  public Fdr jdDeleteFkbByIdColsV2(Fkb fiKeyBean, IFiTableMeta iFiTableMeta, List<FiCol> fiCols) {

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
  public Fdr jdDeleteListById(List<Fkb> fkbList) {
    Fdr fdrMain = new Fdr();
    for (Fkb fiKeyBean : fkbList) {
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

    for (Fkb fiKeyBean : fkbList) {
      Fdr fdrDelete = jdDeleteFkbByIdColsV2(fiKeyBean, getiFiTableMeta(), fkbList.getFicColList());
      fdrMain.combineAnd(fdrDelete);
    }
    return fdrMain;
  }

  public Fkfic getFkcDmFields() {
    return fkcDmFields;
  }

  public void setFkcDmFields(Fkfic fkcDmFields) {
    this.fkcDmFields = fkcDmFields;
  }

  public Fkfic getFkcFieldsAll() {
    return fkcFieldsAll;
  }

  public void setFkcFieldsAll(Fkfic fkcFieldsAll) {
    this.fkcFieldsAll = fkcFieldsAll;
  }

  public FicList getFclTable() {
    return fclTable;
  }

  public void setFclTable(FicList fclTable) {
    this.fclTable = fclTable;
  }

  public FiCol getFicIdAuto() {
    return ficIdAuto;
  }

  public void setFicIdAuto(FiCol ficIdAuto) {
    this.ficIdAuto = ficIdAuto;
  }

  /**
   * Transactionlar AbsRepoJdbi ve Handle ile yönetilir.
   * <p>
   * Transaction Catch'i dışarıda yakalanıyor.
   *
   * @param fnTransaction (handle)->(fdr)
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

  public Fdr<Optional<Integer>> jdhSelectSingleIntOptBindMap(Handle handle, String sqlQuery, Fkb map) {

    Fdr<Optional<Integer>> fdr = new Fdr<>();

    try {
      Optional<Integer> result = handle.select(FiQueTools.stoj(sqlQuery))
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

  /**
   * Insert Fkb Entity Without Id Fields
   *
   * @param handle
   * @param fkbEntity
   * @param fqueconf
   * @return
   */
  public Fdr jdhInsertFkb(Handle handle, Fkb fkbEntity, Fqueconf fqueconf) {
    return jdhInsertFkbMain(handle, fkbEntity, false, fqueconf);
  }

  public Fdr jdhInsertFkbMain(Handle handle, Fkb fkbEntity, Boolean boIncludeIdFields, Fqueconf fqueconf) {

    Fdr fdrMain = new Fdr();
    //fkbEntity.logParams();
    try {
      String sql;

      if (FiBool.isTrue(boIncludeIdFields)) {
        // includeId Fields
        // fqueconf

        Fdr fdrQueryIns = FiQueryGenMs.insertV2(fqueconf);
        sql = FiQueTools.stoj(fdrQueryIns.getFdTxValue());
      } else { // fdrQueryIns query without id fields
        Fdr fdrQueryIns = FiQueryGenMs.insertV2(fqueconf);
        //fdrQueryIns.logFdr();
        sql = FiQueTools.stoj(fdrQueryIns.getFdTxValue());
      }

      Integer rowCountUpdate = handle.createUpdate(sql)
          .bindMap(fkbEntity)
          .execute(); // returns row count updated
      fdrMain.setFdBoResult(true);
      fdrMain.setRowsAffected(rowCountUpdate);
    } catch (Exception ex) {
      Loghelper.get(getClass()).error(FiException.exTosMain(ex));
      fdrMain.setBoResult(false, ex);
    }

    return fdrMain;
  }


}
