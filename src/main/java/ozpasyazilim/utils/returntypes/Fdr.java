package ozpasyazilim.utils.returntypes;

import javafx.util.Pair;
import ozpasyazilim.utils.annotations.FiReview;
import ozpasyazilim.utils.core.*;

import ozpasyazilim.utils.datatypes.Fkb;
import ozpasyazilim.utils.datatypes.FkbList;
import ozpasyazilim.utils.log.FieLog;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.log.MetaLogType;

import java.util.*;

/**
 * Ana Alanlar : boResult,message,value,
 * <p>
 * Sorgu başarılı bir şekilde çalıştırılmışsa boResult True olur, hata alırsa false olur.
 * <p>
 * İşlem yapılmamışsa boResult null olur !!!
 * <p>
 * Cre : 22-02-2019 torak
 * <p>
 * EntClazz value alanının tipi
 */
//@FiTable
public class Fdr<EntClazz> implements IFdr<EntClazz> {

  /**
   * Sql Sorguları için : True ise sorgu başarıyla çalıştırıldığını ifade eder (exception'a düşmemiş)
   * <p>
   * False ise sorguda hata olup,exception oluşmuştur
   * <p>
   * Null ise işlem yapılmadığını ifade eder (last update:29-12-2019)
   * <p>
   * boQueryExecuted alternatif adı
   * <p>
   * Farklı işlemlerde işlemin sonucu : başarılı - başarısız - işlem yapılmadı
   * <p>
   * gibi durumları ifade etmek için kullanılır
   */
  private Boolean fdBoResult;

  private String fdTxMessage;

  private String fdTxVal;

  private FkbList fdFkbListVal;

  private Fkb fdFkbVal;

  private Integer fdLnVal;

  /**
   * 1- Db'ye kayıt edilirken id atamasının değeri buraya yazılır
   */
  private EntClazz value;

  /**
   * Dönüş olarak kod veriliyorsa, bu alan kullanılır
   * <p>
   * Webservis Response'larden gelen dönüş kodu buraya yazılabilir
   * <p>
   * Error Kodu olarakda kullanılabilir
   */
  private Integer lnResponseCode;

  /**
   * İşleme ait id bilgisi (veya başlık olabilir)
   */
  private String txId;

  /**
   * Fdr için verilecek özel bir isim
   */
  String txName;

  /**
   * İşlemlerden alınan loglar
   */
  private List<FieLog> fdListLog;

  private List fdListVal;

  // *********************************** Ek Alanlar *****************************************

  private Integer rowsAffected;

  /**
   * Sayfalama Sorgularında Sorgunun toplam kayıt sayısını tutar
   */
  private Integer lnTotalCount;

  /**
   * Çoklu işlemlerde error (boResult=false) sonuç olduğunu gösterir (or birleştirmeleri için)
   */
  private Boolean fdBoFailExist;

  /**
   * listException olduğu için exception property çıkarılabilir
   */
  private Exception fdException;

  /**
   * Tekil ve Çoklu işlemlerde exception burada biriktirilir.
   */
  List<Exception> fdListException;

  /**
   * Sonuç Durumu : ilgili işlem hangi durum ile sonuçlandığı gösterir,
   * <p>
   * her metoda göre manası değişiklik gösterir
   */
  private Integer lnStatus;

  /**
   * Fdr türünü gösterir (email warn gibi)
   */
  private String fdTxFdrType;

  /**
   * Bazı fdr sonuçları haricen tutmak için
   * <p>
   * (sonuca dahil edilmeyip, bilgi olarak)
   */
  private List<Fdr> fdListFdrExcept;

  // Advanced Configurations

  /**
   * Başarılı operasyon (sorgu vs..) toplamı (true dönenler)
   * <p>
   * Op : Operation
   * <p>
   * Sorgular ayrı ayrı çalıştırıldığı kaç tane başarılı işlem var, onu gösterir.
   */
  private Integer lnSuccessOpCount;

  /**
   * Fail Operation Count (Başarısız işlem sayısı)
   */
  private Integer lnFailOprCount;

  private String txQueryType;

  // Sorgu sonuçları ile ilgili detay alanlar (bir nevi log)
  private Integer lnInsertedRows;
  private Integer lnUpdatedRows;
  private Integer lnDeletedRows;

  /**
   * Kafa karışıklığı oluşturabilir, kullanılmaması tavsiye
   * <p>
   * incelenecek, dikatli kullanılır
   * <p>
   * Operasyon sonucu nedir , true işlem sonucu pozitif, false işlem sonucu negatif olur.
   * <p>
   * boExec farkı : boExec, sorgunun veya yapılacak işlemin başarılı çalıştırıldığını gösterir
   * <p>
   * Örneğin checkExist yapılıyorsa, kayıt varsa opResutl true olur, yoksa false olur.
   * <p>
   * Daha sonra boResult olarak ismi değiştirilebilir
   */
  @FiReview
  Boolean boOpResult;

  // Sorgunun execute edildiğini göstermek için
  Boolean boQueryExecuted;

  /**
   * Birden fazla fdr birleştirilmiş ( combinedAnd veya or ile) mi ?
   */
  Boolean boMultiFdr;

  /**
   * Birden fazla fdr birleştirilmiş ( combinedAnd veya or ile) fdr ler burada tutulabilir
   */
  List<Fdr> listFdr;

  /**
   * True olunca Log eklemeyi engeller. Birleştirmeden sonra yapılır, tekrar eski Fdr ye log eklenirse , ana Fdr de o loglar görünmez. Loglamayı durdurmaz fakat , loglarda hatalı log eklendiğini göstermek için kullanıldı.
   */
  private Boolean boLockAddLog;


  /**
   * Fdr ile tetiklemek istediğimiz işlemleri buraya kaydedilebilir ???
   * <p>
   * Fdr kullanım amacına ters ( fdr işlem sonucunda dönen obje )
   */
  List<Runnable> obsMethodFinished;

  /**
   * Or ile birleştirilmiş Fdr sonucu
   */
  Boolean fdBoOrCombined;

  // --------------- Methods

  public Fdr() {
  }

  public Fdr(Boolean fdBoResult) {
    setFdBoResult(fdBoResult);
    //setResult(Optional.ofNullable(boResult));
  }

  public Fdr(FnResultGen fnKayitSonuc) {
    setFdBoResult(fnKayitSonuc.getBResult());
    setFdrTxMessageWitAddLog(fnKayitSonuc.getSMessage());
  }

  public Fdr(Integer rowCountUpdateWithUpBoResult) {
    setRowsAffectedWithUpBoResult(rowCountUpdateWithUpBoResult);
  }

  public Fdr(Boolean fdBoResult, Integer rowCountUpdate) {
    setFdBoResult(fdBoResult);
    setRowsAffectedWithUpBoResult(rowCountUpdate);
  }

  public Fdr(Integer rowCountUpdate, Exception ex) {
    setRowsAffectedWithUpBoResult(rowCountUpdate);
    setFdException(ex);
  }

  public Fdr(Integer rowCountUpdate, Boolean fdBoResult) {
    setRowsAffectedWithUpBoResult(rowCountUpdate);
    setFdBoResult(fdBoResult);
  }

  public Fdr(Boolean fdBoResult, Exception ex) {
    setFdBoResult(fdBoResult);
    setFdException(ex);
  }

  public Fdr(FiResponse fiResponse) {
    this.fdBoResult = fiResponse.boResult;
    this.fdTxMessage = fiResponse.message;
  }

  public Fdr(Boolean fdBoResult, String fdTxMessage) {
    this.fdBoResult = fdBoResult;
    this.fdTxMessage = fdTxMessage;
  }

  public Fdr(Boolean fdBoResult, String fdTxMessage, MetaLogType metaLogType) {
    this.fdBoResult = fdBoResult;
    this.fdTxMessage = fdTxMessage;
    addLog(fdTxMessage, metaLogType);
  }

  public Fdr(Boolean fdBoResult, String txMessage, Boolean boAddException) {
    this.fdBoResult = fdBoResult;
    this.fdTxMessage = txMessage;

    if (FiBool.isTrue(boAddException)) {
      setFdException(new Exception(txMessage));
    }

  }

  public Fdr(String txMessage) {
    this.fdTxMessage = txMessage;
  }

  public static Fdr genInstance() {
    return new Fdr();
  }

  public static void cloneWithoutValue(Fdr fdrNew, Fdr fdrOld) {
    fdrNew.setFdBoResult(fdrOld.getFdBoResult());
    fdrNew.setRowsAffected(fdrOld.getRowsAffectedWithInit());
    fdrNew.setFdException(fdrOld.getFdException());
//		fdrNew.setBoPartialSuccces(fdrOld.getBoPartialSuccces());
//		fdrNew.setRowsAffectedExtraWorks(fdrOld.getRowsAffectedExtraWorks());
//		fdrNew.setRowsAffectedExtraByEntity(fdrOld.getRowsAffectedExtraByEntity());
    fdrNew.setLnSuccessOpCount(fdrOld.getLnSuccessOpCountInit());
    fdrNew.setLnFailOprCount(fdrOld.getLnFailOprCountInit());
  }

  public static Fdr creBoResult(Boolean boResult) {
    Fdr fdr = new Fdr();
    fdr.setFdBoResult(boResult);
    return fdr;
  }

  public static <PrmEnt> Fdr<PrmEnt> creByValue(PrmEnt value, Boolean boResult) {
    Fdr<PrmEnt> fdr = new Fdr<>(boResult);
    fdr.setValue(value);
    return fdr;
  }

  public static Fdr creEmptyAndResultFalse() {
    Fdr fdr = new Fdr();
    fdr.setValue(Optional.empty());
    fdr.setFdBoResult(false);
    return fdr;
  }

  public static Fdr creByBoResultAndErrorLog(Boolean boResult, String txErrorLog) {
    Fdr fdr = new Fdr();
    fdr.setFdBoResult(boResult);
    fdr.addLogError(txErrorLog);
    return fdr;
  }

  public static Fdr bui() {
    return new Fdr();
  }

  public static Fdr bui(Boolean boResult) {
    Fdr fdr = new Fdr();
    fdr.setFdBoResult(boResult);
    return fdr;
  }

  public Optional<Boolean> getOpResult() {
    return Optional.ofNullable(getFdBoResult());
  }

  /**
   * Sonuçların anlamları :
   * <p>
   * True --> Sorgu Hatasız Çalıştırıldı (Ama sonuç dönmeyebilir)
   * <p>
   * False --> Sorgu çalıştırılırken hata oluştu, try catch hata yakalanınca
   * <p>
   * Null --> Sorgu çalıştırılmadı.
   *
   * @return
   */
  public Boolean getFdBoResult() {
    return fdBoResult;
  }

  public void setFdBoResult(Boolean fdBoResult) {
    this.fdBoResult = fdBoResult;
  }

  // Tek tek kullanılmalı
  @Deprecated
  public void setBoResultWithCheckRowsAffected(Boolean boResult) {
    this.fdBoResult = boResult;
    // rows affected 0 dan büyük olmalı true olması için
    checkRowsAffectedAndBoResult(boResult);
  }

  // Tek tek kullanılmalı
  @Deprecated
  public void setBoResultWithCheckRowsAffected(Boolean boResult, Integer rowsAffected) {
    appendRowsAffected(rowsAffected);
    this.fdBoResult = boResult;
    checkRowsAffectedAndBoResult(boResult);
  }

  public void checkRowsAffectedAndBoResult(Boolean boResult) {
    // rows affected 0 dan büyük olmalı true olması için
    if (FiBool.isTrue(boResult) && getRowsAffectedNotNull() < 1) {
      this.fdBoResult = false;
      this.boQueryExecuted = true;
    }

    if (FiBool.isFalse(boResult)) {
      setRowsAffectedWithUpBoResult(-1);
    }
  }

  public EntClazz getValue() {
    return value;
  }

  public EntClazz getValueInit() {
    if (value == null) {
      if (value instanceof List) {
        value = (EntClazz) new ArrayList<>();
      } else {
        Loghelper.get(getClass()).debug("getValueInit List Türü Degil");
      }
    }
    return value;
  }

  public String getValueAsString() {
    if (value == null) return null;
    return (String) value;
  }

  public Integer getValueAsInteger() {
    if (value == null) return null;

    if(value instanceof Integer){
      return (Integer) value;
    }

    return null;
  }

  public EntClazz getValueOr(EntClazz entClazz) {
    if (value == null) return entClazz;
    return value;
  }

  public void setValue(EntClazz value) {
    this.value = value;
  }

  public String getFdTxMessage() {
    //String yeniSatir = (!FiString.isEmpty(getResMessage().toString()) ? "\n" : "");
    return fdTxMessage; //getResMessage().toString() + yeniSatir + FiString.ifNullStringThenEmpty(message);
  }

  public String getMessageNotNull() {
    if (fdTxMessage == null) return "";
    return fdTxMessage;
  }

  public void setFdrTxMessageWitAddLog(String fdrTxMessage) {
    this.fdTxMessage = fdrTxMessage;
    addLogInfo(fdrTxMessage);
  }

  public void setFdTxMessage(String fdTxMessage) {
    this.fdTxMessage = fdTxMessage;
  }

  public void setMessageForAppend(String message) {
    this.fdTxMessage = message;
  }

  private Integer getRowsAffected() {
    return rowsAffected;
  }

  public void setRowsAffected(Integer rowsAffected) {
    this.rowsAffected = rowsAffected;
  }

  /**
   * Null -1 olarak yorumlayarak dönüş yapar
   *
   * @return
   */
  public Integer getRowsAffectedNotNull() {
    if (rowsAffected == null) return -1;
    return rowsAffected;
  }

  public Integer getRowsAffectedOrEmpty() {
    if (rowsAffected == null) return 0;
    return rowsAffected;
  }

  /**
   * Null ise burada 0 döner
   *
   * @return
   */
  public Integer getRowsAffectedWithInit() {
    if (rowsAffected == null) {
      this.rowsAffected = 0;
    }
    return rowsAffected;
  }

  public void setRowsAffectedWithUpBoResult(Integer rowsAffected) {
    this.rowsAffected = rowsAffected;
    if (rowsAffected != null && rowsAffected > 0) {
      setFdBoResult(true);
    }
  }

  public void appendLnTrueResult(Integer lnSuccessCount) {
    if (lnSuccessCount == null) return;
    setLnSuccessOpCount(getLnSuccessOpCountInit() + lnSuccessCount);
  }

  public void appendLnFalseResult(Integer lnFailureCount) {
    if (lnFailureCount == null) return;
    setLnFailOprCount(getLnFailOprCountInit() + lnFailureCount);
  }

  public void appendLnInserted(Integer lnInsertedRows) {
    if (FiNumber.orZero(lnInsertedRows) == 0) return;
    setLnInsertedRows(FiNumber.orZero(getLnInsertedRows()) + FiNumber.orZero(lnInsertedRows));
  }

  public void appendLnDeleted(Integer lnDeletedRows) {
    if (FiNumber.orZero(lnDeletedRows) == 0) return;
    setLnDeletedRows(FiNumber.orZero(getLnDeletedRows()) + FiNumber.orZero(lnDeletedRows));
  }

  public void appendLnUpdated(Integer lnUpdatedRows) {
    if (FiNumber.orZero(lnUpdatedRows) == 0) return;
    setLnUpdatedRows(FiNumber.orZero(getLnUpdatedRows()) + FiNumber.orZero(lnUpdatedRows));
  }

  public Exception getFdException() {
    return fdException;
  }

  public Exception getExceptionNtn() {
    if (fdException == null) {
      return new Exception("exception boş,atanmamış.(ntn)");
    }
    return fdException;
  }

  public Boolean hasException() {
    return getFdException() != null;
  }

  public void setFdException(Exception fdException) {
    this.fdException = fdException;
  }

  public Fdr buildException(Exception e) {
    this.setFdException(e);
    return this;
  }

  public Fdr<EntClazz> buildValue(EntClazz entity) {
    this.setValue(entity);
    return this;
  }

  public Fdr<EntClazz> buiMessageWitLogV1(String message) {
    this.setFdrTxMessageWitAddLog(message);
    return this;
  }

  public Fdr<EntClazz> buiMessOnly(String message) {
    this.setFdTxMessage(message);
    return this;
  }

  public Fdr<EntClazz> buiMessageWitLog(String message) {
    this.setFdrTxMessageWitAddLog(message);
    addLogInfo(message);
    return this;
  }

  /**
   * İşlem sonuçlarını And ile birleştirme
   * <p>
   * hepsi true olursa sonuç true olur, bir tane false varsa sonuç false olur.
   * <p>
   * Transaction mantığında işlemler için: ya hepsi ya hiç
   * <p>
   * Tüm İşlemlerde Birleştirilen Alanlar (!!!) : Log, Message, Exception
   * <p>
   * Değerlerde birleştirme vs yapmaz.
   *
   * @param fdrSubWork Birleştirilecek Fdr İşi (alt fdr işi)
   */
  public void combineAnd(Fdr fdrSubWork) {

    // And işlemi olduğu false sonuç, boResult false yapar
    if (FiBool.isFalse(fdrSubWork.getFdBoResult())) {
      setFdBoResult(false);
      setLnFailOprCount(getLnFailOprCountInit() + 1);
    }

    if (FiBool.isTrue(fdrSubWork.getFdBoResult())) {
      setLnSuccessOpCount(getLnSuccessOpCountInit() + 1);
      if (getFdBoResult() == null) setFdBoResult(true);
    }

    // Çoklu fdr Aktif edilmişse listeye eklenir
    if (FiBool.isTrue(getBoMultiFdr())) {
      getFdrListInit().add(fdrSubWork);
    }

    // Tümü için yapılacaklar
    if (fdrSubWork.getFdException() != null) {
      setFdException(fdrSubWork.getFdException());
      // exception birden fazla olma ihtimali var.
      getFdListExceptionInit().add(fdrSubWork.getFdException());
    }

    if (fdrSubWork.getFdListFdrExcept() != null) {
      getFdListFdrExceptInit().addAll(fdrSubWork.getFdListFdrExceptInit());
    }

    // Tüm işlemlerde mesaj birleştirilir.
    if (!FiString.isEmptyTrim(fdrSubWork.getFdTxMessage())) appendMessageLnOnly(fdrSubWork.getFdTxMessage());

    // Loglar birleştirilir.
    if (!FiCollection.isEmpty(fdrSubWork.getFdListLog())) getFdLogListInit().addAll(fdrSubWork.getFdListLog());

    appendRowsAffected(fdrSubWork.getRowsAffectedOrEmpty());
    appendLnUpdated(fdrSubWork.getLnUpdatedRows());
    appendLnInserted(fdrSubWork.getLnInsertedRows());
    appendLnDeleted(fdrSubWork.getLnDeletedRows());

    // Birleştirme yapıldığı için eski Fdr'ye log eklenmesi engellenir
    fdrSubWork.setBoLockAddLog(true);

    // subwork'deki ResponseCode da alınır
    if (fdrSubWork.getLnResponseCode() != null) setLnResponseCode(fdrSubWork.getLnResponseCode());

  }

  public void appendMessageLn(String message) {
    if (message == null) return;
    addLogInfo(message);
    setMessageForAppend(FiString.orEmpty(getFdTxMessage()) + FiString.addNewLineToBeginIfNotEmpty(message));
  }

  public void appendMessageLnOnly(String message) {
    if (message == null) return;
    setMessageForAppend(FiString.orEmpty(getFdTxMessage()) + FiString.addNewLineToBeginIfNotEmpty(message));
  }

  /**
   * Eklenecek fdr'yi Or baglacı ile baglar
   * <p>
   * İşlemlerden bir tanesi true ise, genel sonuç true olur.
   * <p>
   * Tüm İşlemlerde Birleştirilen Alanlar : Log, Message, Exception
   * <p>
   * İşlemlerde başarısız olan varsa fdBoErrorExist true yapılır. sonuç true olsa, başarısız işlemlerde var demektir.
   *
   * @param fdrSubWork
   */
  public void combineOr(Fdr fdrSubWork) {

    // false sonuç gelirse, boResult null ise false çevirir, yoksa değiştirmez
    // Başarısız işlemin olduğunu belirtmek için - fdBoFailExist true yapılır
    if (FiBool.isFalse(fdrSubWork.getFdBoResult())) {
      appendLnFalseResult(1);
      if (getFdBoResult() == null) setFdBoResult(false);
      setFdBoFailExist(true);
    }


    if (FiBool.isTrue(fdrSubWork.getFdBoResult())) {
      setFdBoResult(true);
      appendLnTrueResult(1);
      //getResMessage().append(fiDbResult.getResMessage().toString());
    }

    if (fdrSubWork.getFdException() != null) {
      setFdException(fdrSubWork.getFdException());
      // exception birden fazla olma ihtimali var.
      getFdListExceptionInit().add(fdrSubWork.getFdException());
    }

    if (fdrSubWork.getFdListFdrExcept() != null) {
      getFdListFdrExceptInit().addAll(fdrSubWork.getFdListFdrExceptInit());
    }

    appendRowsAffected(fdrSubWork.getRowsAffectedOrEmpty());
    // Tüm işlemlerde mesaj birleştirilir.
    appendMessageLn(fdrSubWork.getFdTxMessage());
    // Loglar birleştirilir
    if (!FiCollection.isEmpty(fdrSubWork.getFdListLog())) getFdLogListInit().addAll(fdrSubWork.getFdListLog());
    // Parametre Fdr'nin logları aktarıldığı tekrar üstüne log eklenmemeli
    fdrSubWork.setBoLockAddLog(true);

    if (FiBool.isTrue(getBoMultiFdr())) {
      getFdrListInit().add(fdrSubWork);
    }

  }

  /**
   * Sonuçla ilgili değişiklik yapmaz, Log,Exception,Message ları alır.
   *
   * @param fdrAppend
   */
  public void combineLogs(Fdr fdrAppend) {

    //setException(fdrAppend.getException());
    if (fdrAppend.getFdException() != null) getFdListExceptionInit().add(fdrAppend.getFdException());

    // Tüm işlemlerde mesaj birleştirilir.
    appendMessageLn(fdrAppend.getFdTxMessage());
    if (!FiCollection.isEmpty(fdrAppend.getFdListLog())) getFdLogListInit().addAll(fdrAppend.getFdListLog());
    fdrAppend.setBoLockAddLog(true);
  }

  public void combineListData(Fdr fdr2) {

    if (fdr2.getValue() != null && getValue() instanceof List && fdr2.getValue() instanceof List) {

      List listData = (List) fdr2.getValue();

      if (getValue() == null) {
        setValue((EntClazz) listData);
      } else {
        List list = (List) getValue();
        list.addAll((Collection) fdr2.getValue());
      }

    }
  }

  public Integer getLnSuccessOpCountInit() {
    if (lnSuccessOpCount == null) {
      lnSuccessOpCount = 0;
    }
    return lnSuccessOpCount;
  }

  public Integer getLnSuccessOpCount() {
    return lnSuccessOpCount;
  }

  public void setLnSuccessOpCount(Integer lnSuccessOpCount) {
    this.lnSuccessOpCount = lnSuccessOpCount;
  }

  public Integer getLnFailOprCountInit() {
    if (lnFailOprCount == null) {
      lnFailOprCount = 0;
    }
    return lnFailOprCount;
  }

  public void setLnFailOprCount(Integer lnFailOprCount) {
    this.lnFailOprCount = lnFailOprCount;
  }

  public Boolean getBoResultInit() {
    if (getFdBoResult() == null) return false;
    return getFdBoResult();
  }

  /**
   * Sorgu çalıştırılmış ve rowsAffected > 0 ise true döner
   *
   * @return
   */
  public Boolean getResultByRowsAffected() {
    if (getFdBoResult() == null) return false;
    if (getBoResultInit() && getRowsAffectedWithInit() > 0) return true;
    return false;
  }

  public Boolean getBoResultNtn() {
    if (getFdBoResult() == null) return false;
    return getFdBoResult();
  }

  public Boolean getBoResultNotNullWithChecked() {
    // Rows affected 1 altında ise false olarak yorumlanır
    if (getRowsAffectedNotNull() < 1) {
      return false;
    }
    if (getFdBoResult() == null) return false;
    return getFdBoResult();
  }

  public Boolean getBoPartialSuccces2() {
    if (getLnSuccessOpCountInit() > 0 && getLnFailOprCountInit() > 0) {
      return true;
    }
    return false;
  }

  public void setBoResultAndMsg(Boolean boResult, String message) {
    setFdBoResult(boResult);
    setFdrTxMessageWitAddLog(message);
  }

  public Fdr buiBoResult(Boolean boExec, Exception ex) {
    setFdBoResult(boExec);
    setFdException(ex);
    if (FiString.isEmpty(getFdTxMessage())) {
      setFdrTxMessageWitAddLog(FiException.TosSummary(ex));
    }
    return this;
  }

  public Fdr buiBoResult(Boolean b) {
    setFdBoResult(b);
    return this;
  }

  public void appendRowsAffected(Integer rowsAffected) {
    if (rowsAffected == null) return;

    if (rowsAffected > 0) {
      setRowsAffectedWithUpBoResult(getRowsAffectedWithInit() + rowsAffected);
    }
  }

  public void setBoResult(Boolean boExec, Exception exError) {
    setFdBoResult(boExec);
    setFdException(exError);
  }

  public void setBoResult(Boolean fdrBoResult) {
    setFdBoResult(fdrBoResult);
  }

  public void setBoResultAndValue(Boolean boExec, EntClazz resValue, Integer rowsAffected) {
    setFdBoResult(boExec);
    setRowsAffected(rowsAffected);
    setValue(resValue);
  }

  public void setBoResultAndValue(Boolean boResult, EntClazz resValue) {
    setFdBoResult(boResult);
    setRowsAffected(rowsAffected);
    setValue(resValue);
  }

  public void setBoResultAndRowsAff(Boolean boResult, Integer rowsAffected) {
    setFdBoResult(boResult);
    setRowsAffected(rowsAffected);
  }

  public Fdr buildOperated(Boolean boExecuted) {
    setBoQueryExecuted(boExecuted);
    return this;
  }

  public Boolean getBoNotExecutedNotNull() {
    // executed null ise, çalıştırılmadığına ifade eder
    if (boQueryExecuted == null) return true;
    return !boQueryExecuted;
  }

  public Boolean getBoExecutedNotNull() {
    // executed null ise çalıştırılmadığına ifade eder
    if (boQueryExecuted == null) return false;
    return boQueryExecuted;
  }

  public Boolean getBoQueryExecuted() {
    return boQueryExecuted;
  }

  public void setBoQueryExecuted(Boolean boQueryExecuted) {
    this.boQueryExecuted = boQueryExecuted;
  }

  public void appendRowsAffected(int[] arrQueryResult) {
    for (int rowsaff : arrQueryResult) {
      appendRowsAffected(rowsaff);
    }
  }


  @Deprecated
  public void setBoOpResult(Boolean boOpResult) {
    this.boOpResult = boOpResult;
  }

  public void setResValue(EntClazz value) {
    setValue(value);
  }

  public EntClazz getResValue() {
    return value;
  }

  /**
   * List'in size kontrolü yapılırken kullanılır, geriye null obje dönmez.
   *
   * @return
   */
  public List getValueAsListNtn() {
    if (getValue() != null) return (List) getValue();
    return new ArrayList();
  }

  public String getTxId() {
    return txId;
  }

  public void setTxId(String txId) {
    this.txId = txId;
  }

  public List<Fdr> getFdrResultList() {
    if (listFdr == null) {
      listFdr = new ArrayList<>();
    }
    return listFdr;
  }

  public void setFdrResultList(List<Fdr> fdrList) {
    this.listFdr = fdrList;
  }

  public void appendMsg(String message) {
    appendMessageLn(message);
  }

  public boolean isTrueBoResult() {
    return FiBool.isTrue(getFdBoResult());
  }

  public boolean isTrueResultAndValueNtn() {
    if (FiBool.isTrue(getFdBoResult()) && getValue() != null) return true;
    return false;
  }

  public boolean isTrueBoResultAndValueExists() {
    return FiBool.isTrue(getFdBoResult()) && getValue() != null;
  }

  public boolean isFalseBoResult() {
    return FiBool.isFalse(getFdBoResult());
  }

  public boolean isFalseOrNullBoResult() {
    if (getFdBoResult() == null) return true;
    return FiBool.isFalse(getFdBoResult());
  }

  public boolean isNullBoResult() {
    return getFdBoResult() == null;
  }

  public boolean isEmptyMessage() {
    return FiString.isEmptyTrim(getFdTxMessage());
  }

  public Integer getLnTotalCount() {
    return lnTotalCount;
  }

  public void setLnTotalCount(Integer lnTotalCount) {
    this.lnTotalCount = lnTotalCount;
  }

  public void copyValues(Fdr fdr) {
    setFdException(fdr.getFdException());
    setFdrTxMessageWitAddLog(fdr.getFdTxMessage());
    setFdBoResult(fdr.getFdBoResult());
  }

  public Integer getLnInsertedRows() {
    return lnInsertedRows;
  }

  public void setLnInsertedRows(Integer lnInsertedRows) {
    this.lnInsertedRows = lnInsertedRows;
  }

  public Integer getLnUpdatedRows() {
    return lnUpdatedRows;
  }

  public void setLnUpdatedRows(Integer lnUpdatedRows) {
    this.lnUpdatedRows = lnUpdatedRows;
  }

  public String getTxQueryType() {
    if (txQueryType == null) {
      return "";
    }
    return txQueryType;
  }

  public void setTxQueryType(String txQueryType) {
    this.txQueryType = txQueryType;
  }

  /**
   * bir tane başarılı işlem varsa partial success olur
   *
   * @return boolean
   */
  public boolean isPartialSuccess() {
    if (getLnSuccessOpCountInit() > 0) {
      return true;
    }
    return false;
  }

  public Integer getLnDeletedRows() {
    return lnDeletedRows;
  }

  public void setLnDeletedRows(Integer lnDeletedRows) {
    this.lnDeletedRows = lnDeletedRows;
  }

  public void setBoResultByNull(Object entiy) {
    if (entiy == null) setFdBoResult(false);
    setFdBoResult(true);
  }

  public Fdr<EntClazz> appendMessageToUp(String txMessage) {
    addLogInfo(txMessage);
    setMessageForAppend(txMessage + "\n" + getFdTxMessage());
    return this;
  }

  public List<Exception> getFdListException() {
    return fdListException;
  }

  public List<Exception> getFdListExceptionInit() {
    if (fdListException == null) {
      fdListException = new ArrayList<>();
    }
    return fdListException;
  }

  public List<Exception> getFdListExceptionNtn() {
    if (fdListException == null) {
      return new ArrayList<>();
    }
    return fdListException;
  }

  public void setFdListException(List<Exception> fdListException) {
    this.fdListException = fdListException;
  }

  public Integer getLnResponseCode() {
    return lnResponseCode;
  }

  public Integer getLnResponseCodeNtn() {
    if (lnResponseCode == null) {
      return -1;
    }
    return lnResponseCode;
  }

  public void setLnResponseCode(Integer lnResponseCode) {
    this.lnResponseCode = lnResponseCode;
  }

  public List<Fdr> getListFdr() {
    return listFdr;
  }

  public List<Fdr> getFdrListInit() {
    if (listFdr == null) {
      listFdr = new ArrayList<>();
    }
    return listFdr;
  }

  public void setListFdr(List<Fdr> listFdr) {
    this.listFdr = listFdr;
  }

  public List<FieLog> getFdListLog() {
    return fdListLog;
  }

  public List<FieLog> getFdLogListInit() {
    if (fdListLog == null) {
      fdListLog = new ArrayList<>();
    }
    return fdListLog;
  }

  public void setFdListLog(List<FieLog> fdListLog) {
    this.fdListLog = fdListLog;
  }

  public Boolean getBoMultiFdr() {
    return boMultiFdr;
  }

  public Boolean getBoMultiFdrNtn() {
    if (boMultiFdr == null) {
      return false;
    }
    return boMultiFdr;
  }

  public void setBoMultiFdr(Boolean boMultiFdr) {
    this.boMultiFdr = boMultiFdr;
  }

  public void addLog(FieLog log) {
    if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
    getFdLogListInit().add(log);
  }

  public void addLog(String txMessage, MetaLogType metaLogType) {
    if (getBoLockAddLogNtn()) {
      Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
    }
    getFdLogListInit().add(new FieLog(txMessage, metaLogType));
  }

  public Fdr addLogInfo(String txMessage) {

    if (getBoLockAddLogNtn()) {
      Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
    }
    //if(getBoLockAddLogNtn()) throw new RuntimeException("Error: Added Log to Blocked Fdr !!!!!!!!");
    getFdLogListInit().add(new FieLog(txMessage, MetaLogType.INFO));
    return this;
  }

  public Fdr addLogInfoBack(String txMessage) {
    if (getBoLockAddLogNtn()) {
      Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
    }
    //if(getBoLockAddLogNtn()) throw new RuntimeException("Error: Added Log to Blocked Fdr !!!!!!!!");
    getFdLogListInit().add(new FieLog(txMessage, MetaLogType.INFOBACK));
    return this;
  }

  public Fdr addLogStep(String txMessage) {
    if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
    //if(getBoLockAddLogNtn()) throw new RuntimeException("Error: Added Log to Blocked Fdr !!!!!!!!");
    getFdLogListInit().add(new FieLog(txMessage, MetaLogType.STEP));
    return this;
  }

  public Fdr<EntClazz> addLogError(String txMessage) {
    if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
    getFdLogListInit().add(new FieLog(txMessage, MetaLogType.ERROR));
    return this;
  }

  public Fdr<EntClazz> addLogErrorBack(String txMessage) {
    if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
    getFdLogListInit().add(new FieLog(txMessage, MetaLogType.ERRBACK));
    return this;
  }

  public void addLogWarn(String txMessage) {
    if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
    getFdLogListInit().add(new FieLog(txMessage, MetaLogType.WARN));
  }

  public void addLogWarnFe(String txMessage) {
    if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
    getFdLogListInit().add(new FieLog(txMessage, MetaLogType.WARNFE));
  }

  public void addLogWarnBack(String txMessage) {
    if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
    getFdLogListInit().add(new FieLog(txMessage, MetaLogType.WARNBACK));
  }

  public void addLogAlert(String txMessage) {
    if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
    getFdLogListInit().add(new FieLog(txMessage, MetaLogType.ALERT));
  }

  public void addLogTypeLog(String txMessage) {
    getFdLogListInit().add(new FieLog(txMessage, MetaLogType.LOG));
  }

  public String getLogAndMessageWitErrorInfo() {
    return FiString.addNewLineToEndIfNotEmpty(getFdTxMessage()) + getLogPlain();
  }

  public String getLogPlainWitErrorInfo() {

    StringBuilder sb = new StringBuilder();
    int index = 0;
    for (FieLog fieLog : getFdLogListInit()) {
      if (index > 0) sb.append("\n");
      if (fieLog.getTxLogTypeNtn().equals(MetaLogType.ERROR.toString())) {
        sb.append("HATA !!! : ");
      }
      sb.append(fieLog.getFieTxMessage());
      index++;
    }
    return sb.toString();

  }

  public String getLogErrorPlain() {
    StringBuilder sb = new StringBuilder();
    int index = 0;
    for (FieLog fieLog : getFdLogListInit()) {
      if (index > 0) sb.append("\n");
      if (fieLog.getTxLogTypeNtn().equals(MetaLogType.ERROR.toString())) {
        sb.append(fieLog.getFieTxMessage());
      }
      index++;
    }
    return sb.toString();
  }

  public String getLogPlain() {
    StringBuilder sb = new StringBuilder();
    int index = 0;
    for (FieLog fieLog : getFdLogListInit()) {
      if (index > 0) sb.append("\n");
      sb.append(fieLog.getFieTxMessage());
      index++;
    }
    return sb.toString();
  }

  public String getLogsAllTosWithMessage() {
    StringBuilder sb = new StringBuilder();

    int index = 0;

    if (!FiString.isEmpty(getFdTxMessage())) {
      sb.append(getFdTxMessage());
      index++;
    }

    for (FieLog fieLog : getFdLogListInit()) {
      if (index > 0) sb.append("\n");
      sb.append(fieLog.getFieTxMessage());
      index++;
    }

    return sb.toString();
  }

  public String getLogsAllTos() {
    StringBuilder sb = new StringBuilder();

    for (FieLog fieLog : getFdLogListInit()) {
      sb.append(fieLog.getFieTxMessage()).append("\n");
    }
    FiString.rtrimSb(sb, "\n");

    return sb.toString();
  }

  public Pair<String, Boolean> getLogsAllWitErrorWarnPairErrorExist() {
    StringBuilder sb = new StringBuilder("");
    int index = 0;
    boolean boErrorExist = false;
    for (FieLog fieLog : getFdLogListInit()) {
      if (index > 0) sb.append("\n");
      if (fieLog.getFieTxMessage().equals(MetaLogType.ERROR.toString())) {
        boErrorExist = true;
        sb.append("HATA !!! : ");
      }
      sb.append(fieLog.getFieTxMessage());
      index++;
    }
    Pair<String, Boolean> pair = new Pair<>(sb.toString(), boErrorExist);
    return pair;
  }

  public String getTxName() {
    return txName;
  }

  public void setTxName(String txName) {
    this.txName = txName;
  }

  public Boolean getBoLockAddLog() {
    return boLockAddLog;
  }

  public Boolean getBoLockAddLogNtn() {
    if (boLockAddLog == null) {
      return false;
    }
    return boLockAddLog;
  }

  public void setBoLockAddLog(Boolean boLockAddLog) {
    this.boLockAddLog = boLockAddLog;
  }

  public void combineAllLog() {
    for (Fdr fdr : getFdrListInit()) {
      addLogInfo(String.format("--- %s ---", fdr.getTxName()));
      getFdLogListInit().addAll(fdr.getFdLogListInit());
    }
  }

  public void convertResultIfNull(boolean boResult) {
    if (getFdBoResult() == null) setFdBoResult(boResult);
  }

  public Boolean getFdBoFailExist() {
    return fdBoFailExist;
  }

  public void setFdBoFailExist(Boolean fdBoFailExist) {
    this.fdBoFailExist = fdBoFailExist;
  }

  public String convertBoResultToMessage() {
    if (getFdBoResult() == null) return "Sonuçsuz (!!!)";
    if (getFdBoResult()) {
      if (FiBool.isTrue(getFdBoFailExist())) {
        return "Kısmı Başarılı";
      }
      return "Başarılı";
    } else {
      return "Başarısız";
    }
  }

  public boolean getIsFalseExist() {
    return FiBool.isTrue(getFdBoFailExist());
  }

  public void addLogErrorException(List<Exception> listExceptionInit) {
    if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
    if (!listExceptionInit.isEmpty()) {
      for (Exception exception1 : listExceptionInit) {
        getFdLogListInit().add(new FieLog(FiException.TosSummary(exception1), MetaLogType.ERROR));
      }
    }
  }

  public void addLogErrorException(Exception exception) {
    addLogErrorException(Arrays.asList(exception));
  }

  public Integer getLnStatus() {
    return lnStatus;
  }

  public Integer getLnStatusNtn() {
    if (lnStatus == null) {
      return -1;
    }
    return lnStatus;
  }

  public void setLnStatus(Integer lnStatus) {
    this.lnStatus = lnStatus;
  }

  public Integer getLnFailOprCount() {
    return lnFailOprCount;
  }

  public List<Runnable> getObsMethodFinished() {
    if (obsMethodFinished == null) {
      obsMethodFinished = new ArrayList<>();
    }
    return obsMethodFinished;
  }

  public void setObsMethodFinished(List<Runnable> obsMethodFinished) {
    this.obsMethodFinished = obsMethodFinished;
  }

  public void trigBoMethodFinished() {
    for (Runnable runnable : getObsMethodFinished()) {
      runnable.run();
    }
  }

  public String getFdTxVal() {
    return fdTxVal;
  }

  public void setFdTxVal(String fdTxVal) {
    this.fdTxVal = fdTxVal;
  }

  public Integer getFdLnVal() {
    return fdLnVal;
  }

  public void setFdLnVal(Integer fdLnVal) {
    this.fdLnVal = fdLnVal;
  }

  public FkbList getFdFkbListVal() {
    return fdFkbListVal;
  }

  public FkbList getFdFkbListValNtn() {
    if (fdFkbListVal == null) {
      fdFkbListVal = new FkbList();
    }
    return fdFkbListVal;
  }

  public void setFdFkbListVal(FkbList fdFkbListVal) {
    this.fdFkbListVal = fdFkbListVal;
  }

  public Fkb getFdFkbVal() {
    return fdFkbVal;
  }

  public Fkb getFdFkbValNtn() {
    if (fdFkbVal == null) {
      return new Fkb();
    }
    return fdFkbVal;
  }

  public void setFdFkbVal(Fkb fdFkbVal) {
    this.fdFkbVal = fdFkbVal;
  }

  public List getFdListVal() {
    return fdListVal;
  }

  public void setFdListVal(List fdListVal) {
    this.fdListVal = fdListVal;
  }

  public void logFdr() {

    if (getFdTxVal() != null) {
      Loghelper.get(getClass()).debug("FdTxValue:" + getFdTxVal());
    }

    if (getFdFkbVal() != null) {
      Loghelper.get(getClass()).debug("FdFkbVal:" + getFdFkbVal());
    }


  }

  public boolean hasLogType(MetaLogType metaLogType) {
    for (FieLog fieLog : getFdLogListInit()) {
      if (metaLogType.toString().equals(fieLog.getFieTxLogType())) {
        return true;
      }
    }
    return false;
  }

  public Boolean getFdBoOrCombined() {
    return fdBoOrCombined;
  }

  /**
   * Fdr sonucu Or ile birleştirilmiş
   *
   * @return
   */
  public Boolean getFdBoOrCombinedNtn() {
    if (fdBoResult == null) {
      return false;
    }
    return fdBoOrCombined;
  }

  public void setFdBoOrCombined(Boolean fdBoOrCombined) {
    this.fdBoOrCombined = fdBoOrCombined;
  }


  public void logAllLogs() {
    Loghelper.get(getClass()).debug(getLogsAllTos());
  }

  public List<Fdr> getFdListFdrExcept() {
    return fdListFdrExcept;
  }

  public List<Fdr> getFdListFdrExceptInit() {
    if (fdListFdrExcept == null) {
      fdListFdrExcept = new ArrayList<>();
    }
    return fdListFdrExcept;
  }

  public void setFdListFdrExcept(List<Fdr> fdListFdrExcept) {
    this.fdListFdrExcept = fdListFdrExcept;
  }

  public String getFdTxFdrType() {
    return fdTxFdrType;
  }

  public void setFdTxFdrType(String fdTxFdrType) {
    this.fdTxFdrType = fdTxFdrType;
  }
}
