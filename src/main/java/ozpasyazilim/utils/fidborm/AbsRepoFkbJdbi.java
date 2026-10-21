package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.core.FiCollection;
import ozpasyazilim.utils.core.FiException;
import ozpasyazilim.utils.datatypes.Fkb;
import ozpasyazilim.utils.datatypes.FkbList;
import ozpasyazilim.utils.datatypes.Fkf;
import ozpasyazilim.utils.jdbi.FiKeyBeanMapper;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

import java.util.*;
import java.util.function.Function;

public abstract class AbsRepoFkbJdbi extends AbsRepoJdbiCore { //implements IRepoJdbi

  private Handle handleRepo;

  /**
   * bağlantı kurulurken hata alınırsa set edilir
   */
  private Fdr fdrConnErr;

  @Deprecated
  private IFiTableMeta iFiTableMeta;

  @Deprecated
  private Fkf fkcDmFields;

  @Deprecated
  private FiCol ficIdAuto;

  private AbsFecTable absFecTable;

  public AbsRepoFkbJdbi(Jdbi jdbi) {
    setJdbi(jdbi);
  }

  public AbsRepoFkbJdbi(String connProfile) {
    this.connProfile = connProfile;
  }

  public void setAutoClass() {
  }

  /**
   * AbsFicTableV2().getFkfAll() metodunu döndürür
   *
   * @return
   */
  public Fkf getRepoFkfAll() {
    return getAbsFecTable().getFkfAll();
  }

  //public abstract Fkf getRepoFkfDto();

  public FicList getRepoFclDto() {
    return null;
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

  public Fdr jdSelectAsFkbList(FiQuery fiQuery) {

    Fdr fdr = new Fdr();
    fdr.setValue(new FkbList());

    try {
      List<Fkb> result = getJdbi().withHandle(handle -> {
        return handle.createQuery(FiQueryUtils.stoj(fiQuery.getTxQuery()))
            .bindMap(fiQuery.getFkbParams())
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
        return handle.createQuery(FiQueryUtils.stoj(fiQuery.getTxQuery()))
            .bindMap(fiQuery.getFkbParams())
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
        return handle.createQuery(FiQueryUtils.stoj(sqlQuery))
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

  public Fdr jdSelectFkbSingleBindMapMainV2(String sqlQuery, Map<String, Object> mapBind) {

    Fdr fdr = new Fdr<>();
    fdr.setValue(new Fkb());

    try {
      Optional<Fkb> result = getJdbi().withHandle(handle -> {
        return handle.createQuery(FiQueryUtils.stoj(sqlQuery))
            .bindMap(mapBind)
            .map(new FiKeyBeanMapper(false))
            .findOne();
      });

      result.ifPresent(fdr::setFdFkbVal);
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
        return handle.select(FiQueryUtils.stoj(sql))
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

  /**
   * Sorgudan gelen sonuç FdLnVal alanına değeri girer
   *
   * @param sql
   * @param mapParam
   * @return
   */
  public Fdr jdSelectLnValBindMap(String sql, Map<String, Object> mapParam) {

    Fdr fdr = new Fdr<>();

    try {
      Optional<Integer> result = getJdbi().withHandle(handle -> {
        return handle.select(FiQueryUtils.stoj(sql))
            .bindMap(mapParam)
            .mapTo(Integer.class)
            .findFirst();
      });

      result.ifPresent(fdr::setFdLnVal);
      fdr.setFdBoResult(true);
    } catch (Exception ex) {
      Loghelper.get(getClass()).error(FiException.exTosMain(ex));
      fdr.setFdBoResult(false);
    }

    return fdr;
  }

  /**
   * Sorgudan gelen sonuç FdTxVal alanına değeri girer
   *
   * @param fiQuery
   * @return
   */
  public Fdr jdSelectTxValBindMap(FiQuery fiQuery) {

    String sql = fiQuery.getTxQuery();
    Fkb fkbParams = fiQuery.getFkbParams();

    Fdr fdr = new Fdr<>();

    try {
      Optional<String> result = getJdbi().withHandle(handle -> {
        return handle.select(FiQueryUtils.stoj(sql))
            .bindMap(fkbParams)
            .mapTo(String.class)
            .findFirst();
      });

      result.ifPresent(fdr::setFdTxVal);
      fdr.setFdBoResult(true);
    } catch (Exception ex) {
      Loghelper.get(getClass()).error(FiException.exTosMain(ex));
      fdr.setFdBoResult(false);
    }

    return fdr;
  }


//  public Fdr jdUpdateBindMapMain(FiQuery fiQuery) {
//    return jdUpdateBindMapMain(fiQuery.getTxQuery(), fiQuery.getFkbParams());
//  }

  /**
   * fkbParams bind eder
   *
   * @param fiQuery
   * @return
   */
  public Fdr jdExecute(FiQuery fiQuery) {
    return jdUpdateBindMapMain(fiQuery);
  }

  public Fdr jdUpdateBindMap(String txUpdateQuery, Map<String, Object> fiMapParams) {
    Fkb fkbParams = new Fkb(fiMapParams);
    FiQuery fiQuery = new FiQuery(txUpdateQuery, fkbParams);
    return jdUpdateBindMapMain(fiQuery);
  }

  /**
   *
   * Başarılı olursa Fdr objesine boResult ve rowsAffected (Update sorgusunda önemli olabilir) kaydediyor
   * <p>
   * Başarısız olursa boResult ve exception'ı kaydediyor, error log'u yazdırıyor
   *
   * @param fiQuery
   * @return
   */
  public Fdr jdUpdateBindMapMain(FiQuery fiQuery) {

    String txUpdateQuery = fiQuery.getTxQuery();
    Fkb fkbParams = fiQuery.getFkbParams();

    Fdr fdrMain = new Fdr();
    try {
      Integer rowCountUpdate = getJdbi().withHandle(handle -> {
        return handle.createUpdate(FiQueryUtils.stojExcludable1(txUpdateQuery))
            .bindMap(fkbParams)
            .execute(); // returns row count updated
      });
      //Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
      fdrMain.setBoResultAndRowsAff(true, rowCountUpdate);
    } catch (Exception ex) {
      fdrMain.setBoResult(false, ex);
      Loghelper.get(getClass()).error(FiException.exToErrorLog(ex));
    }
    return fdrMain;
  }

  public Fdr jdInsertFiQuery(FiQuery fiQuery) {
    return jdInsertBindMapMain(fiQuery.getTxQuery(), fiQuery.getFkbParams());
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
        return handle.createUpdate(FiQueryUtils.stoj(insertQuery))
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
    return jdDeleteBindMapMain(fiQuery.getTxQuery(), fiQuery.getFkbParams());
  }

  public Fdr jdDeleteBindMapMain(String insertQuery, Map<String, Object> fiMapParams) {

    Fdr fdrMain = new Fdr();
    try {
      Integer rowCountUpdate = getJdbi().withHandle(handle -> {
        return handle.createUpdate(FiQueryUtils.stoj(insertQuery))
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


  /**
   * default impl : bind map
   *
   * @param fiQuery
   * @param handle
   * @return
   */
  public Fdr jdhDeleteMain(FiQuery fiQuery, Handle handle) {

    String txQuery = fiQuery.getTxQuery();
    Map<String, Object> fiMapParams = fiQuery.getFkbParams();

    Fdr fdrMain = new Fdr();
    try {
      Integer rowCountUpdate = handle.createUpdate(FiQueryUtils.stoj(txQuery))
          .bindMap(fiMapParams)
          .execute(); // returns row count updated;
      //Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
      //fiDbResult.setLnSuccessWithUpBoResult(1, rowCountUpdate);
      fdrMain.setBoResultAndRowsAff(true, rowCountUpdate);
    } catch (Exception ex) {
      fdrMain.setBoResult(false, ex);
      Loghelper.get(getClass()).error(FiException.exToErrorLog(ex));
    }
    return fdrMain;
  }

  /**
   * default impl : bind map
   *
   * @param fiQuery
   * @param handle
   * @return
   */
  public Fdr jdhUpdateMain(FiQuery fiQuery, Handle handle) {

    String txQuery = fiQuery.getTxQuery();
    Map<String, Object> fiMapParams = fiQuery.getFkbParams();

    Fdr fdrMain = new Fdr();
    try {
      Integer rowCountUpdate = handle.createUpdate(FiQueryUtils.stoj(txQuery))
          .bindMap(fiMapParams)
          .execute(); // returns row count updated;
      //Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
      //fiDbResult.setLnSuccessWithUpBoResult(1, rowCountUpdate);
      fdrMain.setBoResultAndRowsAff(true, rowCountUpdate);
    } catch (Exception ex) {
      fdrMain.setBoResult(false, ex);
      Loghelper.get(getClass()).error(FiException.exToErrorLog(ex));
    }
    return fdrMain;
  }

  /**
   * default impl : bind map
   *
   * @param fiQuery
   * @param handle
   * @return
   */
  public Fdr jdhInsertMain(FiQuery fiQuery, Handle handle) {

    String insertQuery = fiQuery.getTxQuery();
    Map<String, Object> fiMapParams = fiQuery.getFkbParams();

    Fdr fdrMain = new Fdr();
    try {
      Integer rowCountUpdate = handle.createUpdate(FiQueryUtils.stoj(insertQuery))
          .bindMap(fiMapParams)
          .execute(); // returns row count updated;
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
        return handle.createQuery(FiQueryUtils.stoj(sqlQuery))
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

    String sql = FiQugenCls.insertFiColsV2(iFiTableMeta, formAsFkb.getListFiColInit(), boInserFieldsOnly);

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
    String sql = FiQugenCls.updateFiColsArb(getiFiTableMeta(), formAsFkb.getListFiColInit(), boUpdateFieldsOnly);

    FiQuery fiQuery = new FiQuery(sql, formAsFkb);
    //fiQuery.logQueryAndParams();

    return jdInsertFiQuery(fiQuery);
  }

  public Fdr jdDeleteFkbByIdCols(Fkb fiKeyBean, IFiTableMeta iFiTableMeta) {

    String sql = FiQugenCls.deleteWhereIdFiColsV2(iFiTableMeta, fiKeyBean.getListFiColInit());

    FiQuery fiQuery = new FiQuery(sql, fiKeyBean);
    //fiQuery.logQueryAndParams();

    return jdDeleteFiQuery(fiQuery);
  }

  public Fdr jdDeleteFkbByIdColsV2(Fkb fiKeyBean, IFiTableMeta iFiTableMeta, List<FiCol> fiCols) {

    String sql = FiQugenCls.deleteWhereIdFiColsV2(iFiTableMeta, fiCols);

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

  public Fkf getFkcDmFields() {
    return fkcDmFields;
  }

  public void setFkcDmFields(Fkf fkcDmFields) {
    this.fkcDmFields = fkcDmFields;
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
   * fnTransaction içerisinde exception kayıt edilirse thrown edilip catch edilir. commit başarısız olur.
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

        if (fdr.getFdException() != null) {
          throw fdr.getFdException();
        }

        List<Exception> fdListExceptionInit = fdr.getFdListExceptionInit();
        if (!fdListExceptionInit.isEmpty()) {
          throw fdListExceptionInit.get(0);
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
      Optional<Integer> result = handle.select(FiQueryUtils.stoj(sqlQuery))
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
   * @param fiQuconf
   * @return
   */
  public Fdr jdhInsertFkb(Handle handle, Fkb fkbEntity, FiQuconf fiQuconf) {
    return jdhInsertFkbMain(handle, fkbEntity, false, fiQuconf);
  }

  public Fdr jdhInsertFkbMain(Handle handle, Fkb fkbEntity, Boolean boIncludeIdFields, FiQuconf fiQuconf) {

    Fdr fdrMain = new Fdr();
    //fkbEntity.logParams();
    try {
      String sql;

      if (FiBool.isTrue(boIncludeIdFields)) {
        // includeId Fields
        // fqueconf

        Fdr fdrQueryIns = FiQugenMsOld.insQueryV2(fiQuconf);
        sql = FiQueryUtils.stoj(fdrQueryIns.getFdTxVal());
      } else { // fdrQueryIns query without id fields
        Fdr fdrQueryIns = FiQugenMsOld.insQueryV2(fiQuconf);
        //fdrQueryIns.logFdr();
        sql = FiQueryUtils.stoj(fdrQueryIns.getFdTxVal());
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


  public Fdr fkInsert(Fkb fkbEntity) {
    Fdr fdrMain = new Fdr();

    FiQuery fiQuery = new FiQuery(fkbEntity);
    fiQuery.setFkfAll(getRepoFkfAll());

    Fdr fdrSorgu = FiQugenMs.insQueryByFkfAll(fiQuery);
    fdrMain.combineAnd(fdrSorgu);

    if (fdrMain.isFalseBoResult()) return fdrMain;

    //fiQuery.logQueryAndParams();

    return jdInsertFiQuery(fiQuery);
  }

  /**
   * Sadece FicList'deki alanlara göre insert yapar (id dahil etmez)
   *
   * @param fkbEntity
   * @param fclInsert
   * @return
   */
  public Fdr fkInsert(Fkb fkbEntity, FicList fclInsert) {
    Fdr fdrMain = new Fdr();

    FiQuconf fiQuconf = new FiQuconf();
    fiQuconf.setFkfAll(getRepoFkfAll());

    Fdr fdrSorgu = FiQugenMsOld.insQueryV3Custom(fiQuconf, fclInsert);
    fdrMain.combineAnd(fdrSorgu);

    if (fdrMain.isFalseBoResult()) return fdrMain;

    FiQuery fiQuery = new FiQuery(fdrSorgu.getFdTxVal(), fkbEntity);
    //fiQuery.logQueryAndParams();

    return jdInsertFiQuery(fiQuery);
  }

  /**
   * getRepoFkfAll kullanarak insert yapar
   *
   * @param fkbEntity
   * @return
   */
  public Fdr fkInserByFkfAll(Fkb fkbEntity) {
    Fdr fdrMain = new Fdr();

    FiQuery fiQuery = new FiQuery(fkbEntity);
    fiQuery.setFkfAll(getRepoFkfAll());

    Fdr fdrSorgu = FiQugenMs.insQueryByFkfAll(fiQuery);
    fdrMain.combineAnd(fdrSorgu);

    if (fdrMain.isFalseBoResult()) return fdrMain;

    //fiQuery.logQueryAndParams();

    return jdInsertFiQuery(fiQuery);
  }

  /**
   * Sadece FicList'deki alanlara göre insert yapar (id dahil etmez)
   *
   * @param fkbEntity
   * @param fclInsert
   * @return
   */
  public Fdr fkInsertV2(Fkb fkbEntity, FicList fclInsert) {
    Fdr fdrMain = new Fdr();

    FiQuery fiQuery = new FiQuery(fkbEntity);
    fiQuery.setFicListQuery(fclInsert);
    fiQuery.setAbsFecTable(getAbsFecTable());

    Fdr fdrSorgu = FiQugenMs.insQueryV4ByFicList(fiQuery);
    fdrMain.combineAnd(fdrSorgu);

    if (fdrMain.isFalseBoResult()) return fdrMain;

    //fiQuery.logQueryAndParams();

    return jdInsertFiQuery(fiQuery);
  }

  /**
   * Delete Entities By Where Params
   *
   * @param fkbEntity
   * @param fclWhere
   * @return
   */
  public Fdr fkDeleteV1(Fkb fkbEntity, FicList fclWhere) {
    Fdr fdrMain = new Fdr();

    FiQuery fiQuery1 = new FiQuery(fkbEntity);
    //fiQuery1.setFkfAll(getRepoFkfAll());
    fiQuery1.setFicListWhere(fclWhere);
    fiQuery1.setAbsFecTable(getAbsFecTable());

    Fdr fdrSorgu = FiQugenMs.delQueryV1(fiQuery1);
    fdrMain.combineAnd(fdrSorgu);

    if (fdrMain.isFalseBoResult()) return fdrMain;

    fiQuery1.logQueryAndParams();

    return jdInsertFiQuery(fiQuery1);
  }

  /**
   * Select DTO List
   *
   * @param fkbParams
   * @param ficListExtra
   * @return
   */
  public Fdr osSelDtoList(Fkb fkbParams, FicList ficListExtra) {
    FiQuery fiQuery = new FiQuery(fkbParams);
    FicList fclDto = getRepoFclDto();

    if (!FiCollection.isEmpty(ficListExtra)) {
      fclDto.addAll(ficListExtra);
    }

    fiQuery.setFicListQuery(fclDto);

    Fdr fdrSorgu = FiQugenMs.selQueryByFicList(fiQuery);
    if (fdrSorgu.isFalseBoResult()) return fdrSorgu;

    return jdSelectAsFkbList(fiQuery);
  }

  /**
   * FkbList döndüren Select Sorgusunu çalıştırır
   *
   * @return fkbList
   */
  public Fdr fkSelect(FiQuery fiQuery) {
    return jdSelectAsFkbList(fiQuery);
  }

  public Fdr getFdrConnErr() {
    return fdrConnErr;
  }

  public void setFdrConnErr(Fdr fdrConnErr) {
    this.fdrConnErr = fdrConnErr;
  }

  public Fdr fkSelectByFicList(FiQuery fiQuery) {
    Fdr fdrSql = FiQugenMs.selQueryByFicList(fiQuery);

    //fiQuery.logQueryAndParams();

    if (fdrSql.isNotTrueBoResult()) return fdrSql;

    return jdSelectAsFkbList(fiQuery);
  }

  public AbsFecTable getAbsFecTable() {
    return absFecTable;
  }

  public AbsFecTable getAbsFecTableNtn() {
    if (absFecTable == null) {
      return new VirFecTable();
    }
    return absFecTable;
  }

  public void setAbsFecTable(AbsFecTable absFecTable) {
    this.absFecTable = absFecTable;
  }

  public Fdr osSelStarByFt(Fkb fkbParams, FicList ficListWhere) {
    FiQuery fiQuery = new FiQuery(fkbParams);
    fiQuery.setFicListWhere(ficListWhere);
    fiQuery.setAbsFecTable(getAbsFecTable());

    FiQugenMs.selStarByFecAndFclWhere(fiQuery);

    fiQuery.logQueryAndParams();

    return jdSelectAsFkbList(fiQuery);
  }


}
