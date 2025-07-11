package ozpasyazilim.utils.returntypes;

import javafx.util.Pair;
import ozpasyazilim.utils.annotations.FiReview;
import ozpasyazilim.utils.core.*;

import ozpasyazilim.utils.fidbanno.FiTable;
import ozpasyazilim.utils.log.OreLog;
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
@FiTable
public class Fdr<EntClazz> implements IFdr<EntClazz> {

    /**
     * True ise sorgu başarıyla çalıştırıldığını ifade eder (exception'a düşmemiş)
     * <p>
     * False ise sorguda hata olup,exception oluşmuştur
     * <p>
     * Null ise işlem yapılmadığını ifade eder (last update:29-12-2019)
     * <p>
     * boQueryExecuted alternatif adı
     */
    private Boolean fdrBoExec;

    private String message;

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
    List<OreLog> logList;

    // *********************************** Ek Alanlar *****************************************

    private Integer rowsAffected;

    /**
     * Sayfalama Sorgularında Sorgunun toplam kayıt sayısını tutar
     */
    private Integer lnTotalCount;

    /**
     * Çoklu işlemlerde false sonuç olduğunu gösterir (or birleştirmeleri için)
     */
    private Boolean boFalseExist;

    /**
     * listException olduğu için exception property çıkarılabilir
     */
    private Exception exception;

    /**
     * Tekil ve Çoklu işlemlerde exception burada biriktirilir.
     */
    List<Exception> listException;

    /**
     * Sonuç Durumu : ilgili işlem hangi durum ile sonuçlandığı gösterir,
     * <p>
     * her metoda göre manası değişiklik gösterir
     */
    private Integer lnStatus;


    // Advanced Configurations

    /**
     * Başarılı operasyon (sorgu vs..) toplamı (true dönenler)
     * <p>
     * Op : Operation
     * <p>
     * Sorgular ayrı ayrı çalıştırıldığı kaç tane başarılı işlem var, onu gösterir.
     */
    private Integer lnSuccessOpCount;

    private Integer lnFailureOpCount;

    private String txQueryType;

    // Sorgu sonuçları ile ilgili detay alanlar (bir nevi log)
    private Integer lnInsertedRows;
    private Integer lnUpdatedRows;
    private Integer lnDeletedRows;

    /**
     * Kafa karışıklığı oluşturabilir,incelenecek, dikatli kullanılır
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

    private String fdrTxValue;

    private Integer fdrLnValue;
    /**
     * Fdr ile tetiklemek istediğimiz işlemleri buraya kaydedilebilir
     */
    List<Runnable> obsMethodFinished;

// --------------- Methods

    public Fdr() {
    }

    public Fdr(Boolean fdrBoExec) {
        setFdrBoExec(fdrBoExec);
        //setResult(Optional.ofNullable(boResult));
    }

    public Fdr(FnResultGen fnKayitSonuc) {
        setFdrBoExec(fnKayitSonuc.getBResult());
        setMessage(fnKayitSonuc.getSMessage());
    }

    public Fdr(Integer rowCountUpdateWithUpBoResult) {
        setRowsAffectedWithUpBoResult(rowCountUpdateWithUpBoResult);
    }

    public Fdr(Boolean fdrBoExec, Integer rowCountUpdate) {
        setFdrBoExec(fdrBoExec);
        setRowsAffectedWithUpBoResult(rowCountUpdate);
    }

    public Fdr(Integer rowCountUpdate, Exception ex) {
        setRowsAffectedWithUpBoResult(rowCountUpdate);
        setException(ex);
    }

    public Fdr(Integer rowCountUpdate, Boolean fdrBoExec) {
        setRowsAffectedWithUpBoResult(rowCountUpdate);
        setFdrBoExec(fdrBoExec);
    }

    public Fdr(Boolean fdrBoExec, Exception ex) {
        setFdrBoExec(fdrBoExec);
        setException(ex);
    }

    public Fdr(FiResponse fiResponse) {
        this.fdrBoExec = fiResponse.boResult;
        this.message = fiResponse.message;
    }

    public Fdr(Boolean fdrBoExec, String message) {
        this.fdrBoExec = fdrBoExec;
        this.message = message;
    }

    public Fdr(Boolean fdrBoExec, String message, MetaLogType metaLogType) {
        this.fdrBoExec = fdrBoExec;
        this.message = message;
        addLog(message, metaLogType);
    }

    public Fdr(Boolean fdrBoExec, String txMessage, Boolean boAddException) {
        this.fdrBoExec = fdrBoExec;
        this.message = txMessage;

        if (FiBool.isTrue(boAddException)) {
            setException(new Exception(txMessage));
        }

    }

    public Fdr(String txMessage) {
        this.message = txMessage;
    }

    public static Fdr genInstance() {
        return new Fdr();
    }

    public static void cloneWithoutValue(Fdr fdrNew, Fdr fdrOld) {
        fdrNew.setFdrBoExec(fdrOld.getFdrBoExec());
        fdrNew.setRowsAffected(fdrOld.getRowsAffectedWithInit());
        fdrNew.setException(fdrOld.getException());
//		fdrNew.setBoPartialSuccces(fdrOld.getBoPartialSuccces());
//		fdrNew.setRowsAffectedExtraWorks(fdrOld.getRowsAffectedExtraWorks());
//		fdrNew.setRowsAffectedExtraByEntity(fdrOld.getRowsAffectedExtraByEntity());
        fdrNew.setLnSuccessOpCount(fdrOld.getLnSuccessOpCountInit());
        fdrNew.setLnFailureOpCount(fdrOld.getLnFailureOpCountInit());
    }

    public static Fdr creBoResult(Boolean boResult) {
        Fdr fdr = new Fdr();
        fdr.setFdrBoExec(boResult);
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
        fdr.setFdrBoExec(false);
        return fdr;
    }

    public static Fdr creByBoResultAndErrorLog(Boolean boResult, String txErrorLog) {
        Fdr fdr = new Fdr();
        fdr.setFdrBoExec(boResult);
        fdr.addLogError(txErrorLog);
        return fdr;
    }

    public static Fdr bui() {
        return new Fdr();
    }

    public static Fdr bui(Boolean boResult) {
        Fdr fdr = new Fdr();
        fdr.setFdrBoExec(boResult);
        return fdr;
    }

    public Optional<Boolean> getOpResult() {
        return Optional.ofNullable(getFdrBoExec());
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
    public Boolean getFdrBoExec() {
        return fdrBoExec;
    }

    public void setFdrBoExec(Boolean fdrBoExec) {
        this.fdrBoExec = fdrBoExec;
    }

    // Tek tek kullanılmalı
    @Deprecated
    public void setBoResultWithCheckRowsAffected(Boolean boResult) {
        this.fdrBoExec = boResult;
        // rows affected 0 dan büyük olmalı true olması için
        checkRowsAffectedAndBoResult(boResult);
    }

    // Tek tek kullanılmalı
    @Deprecated
    public void setBoResultWithCheckRowsAffected(Boolean boResult, Integer rowsAffected) {
        appendRowsAffected(rowsAffected);
        this.fdrBoExec = boResult;
        checkRowsAffectedAndBoResult(boResult);
    }

    public void checkRowsAffectedAndBoResult(Boolean boResult) {
        // rows affected 0 dan büyük olmalı true olması için
        if (FiBool.isTrue(boResult) && getRowsAffectedNotNull() < 1) {
            this.fdrBoExec = false;
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
        return (Integer) value;
    }

    public EntClazz getValueOr(EntClazz entClazz) {
        if (value == null) return entClazz;
        return value;
    }

    public void setValue(EntClazz value) {
        this.value = value;
    }

    public String getMessage() {
        //String yeniSatir = (!FiString.isEmpty(getResMessage().toString()) ? "\n" : "");
        return message; //getResMessage().toString() + yeniSatir + FiString.ifNullStringThenEmpty(message);
    }

    public String getMessageNotNull() {
        if (message == null) return "";
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        addLogInfo(message);
    }

    public void setMessageForAppend(String message) {
        this.message = message;
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
            setFdrBoExec(true);
        }
    }

    public void appendLnTrueResult(Integer lnSuccessCount) {
        if (lnSuccessCount == null) return;
        setLnSuccessOpCount(getLnSuccessOpCountInit() + lnSuccessCount);
    }

    public void appendLnFalseResult(Integer lnFailureCount) {
        if (lnFailureCount == null) return;
        setLnFailureOpCount(getLnFailureOpCountInit() + lnFailureCount);
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

    public Exception getException() {
        return exception;
    }

    public Exception getExceptionNtn() {
        if (exception == null) {
            return new Exception("exception boş,atanmamış.(ntn)");
        }
        return exception;
    }

    public Boolean hasException() {
        return getException() != null;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Fdr buildException(Exception e) {
        this.setException(e);
        return this;
    }

    public Fdr<EntClazz> buildValue(EntClazz entity) {
        this.setValue(entity);
        return this;
    }

    public Fdr<EntClazz> buiMessage(String message) {
        this.setMessage(message);
        return this;
    }

    public Fdr<EntClazz> buiMessageWitLog(String message) {
        this.setMessage(message);
        addLogInfo(message);
        return this;
    }

    /**
     * İşlem sonuçlarının hepsi true olursa sonuç true olur, bir tane false varsa sonuç false olur.
     * <p>
     * Tüm İşlemlerde Birleştirilen Alanlar : Log, Message, Exception
     * <p>
     *
     * <p>
     * Value ile birleştirme vs yapmaz.
     *
     * @param fdrSubWork Birleştirilecek Fdr (alt fdr işi)
     */
    public void combineAnd(Fdr fdrSubWork) {

        // And işlemi olduğu false sonuç, boResult false yapar
        if (FiBool.isFalse(fdrSubWork.getFdrBoExec())) {
            setFdrBoExec(false);
            setLnFailureOpCount(getLnFailureOpCountInit() + 1);
        }

        if (FiBool.isTrue(fdrSubWork.getFdrBoExec())) {
            setLnSuccessOpCount(getLnSuccessOpCountInit() + 1);
            if (getFdrBoExec() == null) setFdrBoExec(true);
        }

        // null sonuçlara özel combine işlemi
//        if (fdrSubWork.getBoResult() == null) {
//
//        }
        if (FiBool.isTrue(getBoMultiFdr())) {
            getFdrListInit().add(fdrSubWork);
        }

        // Tümü için yapılacaklar
        if (fdrSubWork.getException() != null) {
            setException(fdrSubWork.getException());
            // exception birden fazla olma ihtimali var.
            getListExceptionInit().add(fdrSubWork.getException());
        }

        // Tüm işlemlerde mesaj birleştirilir.
        if (!FiString.isEmptyTrim(fdrSubWork.getMessage())) appendMessageLn(fdrSubWork.getMessage());

        // Loglar birleştirilir.
        if (!FiCollection.isEmpty(fdrSubWork.getLogList())) getLogListInit().addAll(fdrSubWork.getLogList());

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
        setMessageForAppend(FiString.orEmpty(getMessage()) + FiString.addNewLineToBeginIfNotEmpty(message));
    }

    /**
     * Eklenecek fdr'yi Or baglacı ile baglar
     * <p>
     * İşlemlerden bir tanesi true ise, genel sonuç true olur.
     * <p>
     * Tüm İşlemlerde Birleştirilen Alanlar : Log, Message, Exception
     *
     * @param fdrSubWork
     */
    public void combineOr(Fdr fdrSubWork) {

        // false sonuç gelirse, boResult null ise false çevirir, yoksa değiştirmez
        if (FiBool.isFalse(fdrSubWork.getFdrBoExec())) {
            appendLnFalseResult(1);
            if (getFdrBoExec() == null) setFdrBoExec(false);
            setBoFalseExist(true);
        }

        if (FiBool.isTrue(fdrSubWork.getFdrBoExec())) {
            setFdrBoExec(true);
            appendLnTrueResult(1);
            //getResMessage().append(fiDbResult.getResMessage().toString());
        }

        if (fdrSubWork.getException() != null) {
            setException(fdrSubWork.getException());
            // exception birden fazla olma ihtimali var.
            getListExceptionInit().add(fdrSubWork.getException());
        }

        appendRowsAffected(fdrSubWork.getRowsAffectedOrEmpty());
        // Tüm işlemlerde mesaj birleştirilir.
        appendMessageLn(fdrSubWork.getMessage());
        // Loglar birleştirilir
        if (!FiCollection.isEmpty(fdrSubWork.getLogList())) getLogListInit().addAll(fdrSubWork.getLogList());
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
        if (fdrAppend.getException() != null) getListExceptionInit().add(fdrAppend.getException());

        // Tüm işlemlerde mesaj birleştirilir.
        appendMessageLn(fdrAppend.getMessage());
        if (!FiCollection.isEmpty(fdrAppend.getLogList())) getLogListInit().addAll(fdrAppend.getLogList());
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

    public Integer getLnFailureOpCountInit() {
        if (lnFailureOpCount == null) {
            lnFailureOpCount = 0;
        }
        return lnFailureOpCount;
    }

    public void setLnFailureOpCount(Integer lnFailureOpCount) {
        this.lnFailureOpCount = lnFailureOpCount;
    }

    public Boolean getBoResultInit() {
        if (getFdrBoExec() == null) return false;
        return getFdrBoExec();
    }

    /**
     * Sorgu çalıştırılmış ve rowsAffected > 0 ise true döner
     *
     * @return
     */
    public Boolean getResultByRowsAffected() {
        if (getFdrBoExec() == null) return false;
        if (getBoResultInit() && getRowsAffectedWithInit() > 0) return true;
        return false;
    }

    public Boolean getBoResultNtn() {
        if (getFdrBoExec() == null) return false;
        return getFdrBoExec();
    }

    public Boolean getBoResultNotNullWithChecked() {
        // Rows affected 1 altında ise false olarak yorumlanır
        if (getRowsAffectedNotNull() < 1) {
            return false;
        }
        if (getFdrBoExec() == null) return false;
        return getFdrBoExec();
    }

    public Boolean getBoPartialSuccces2() {
        if (getLnSuccessOpCountInit() > 0 && getLnFailureOpCountInit() > 0) {
            return true;
        }
        return false;
    }

    public void setBoExecAndMsg(Boolean boResult, String message) {
        setFdrBoExec(boResult);
        setMessage(message);
    }

    public Fdr buiBoExec(Boolean boExec, Exception ex) {
        setFdrBoExec(boExec);
        setException(ex);
        if (FiString.isEmpty(getMessage())) {
            setMessage(FiException.TosSummary(ex));
        }
        return this;
    }

    public Fdr buiBoExec(Boolean b) {
        setFdrBoExec(b);
        return this;
    }

    public void appendRowsAffected(Integer rowsAffected) {
        if (rowsAffected == null) return;

        if (rowsAffected > 0) {
            setRowsAffectedWithUpBoResult(getRowsAffectedWithInit() + rowsAffected);
        }
    }

    public void setBoResult(Boolean boExec, Exception exError) {
        setFdrBoExec(boExec);
        setException(exError);
    }

    public void setBoResultAndValue(Boolean boExec, EntClazz resValue, Integer rowsAffected) {
        setFdrBoExec(boExec);
        setRowsAffected(rowsAffected);
        setValue(resValue);
    }

    public void setBoResultAndValue(Boolean boResult, EntClazz resValue) {
        setFdrBoExec(boResult);
        setRowsAffected(rowsAffected);
        setValue(resValue);
    }

    public void setBoResultAndRowsAff(Boolean boResult, Integer rowsAffected) {
        setFdrBoExec(boResult);
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
        return FiBool.isTrue(getFdrBoExec());
    }

    public boolean isTrueResultAndValueNtn() {
        if (FiBool.isTrue(getFdrBoExec()) && getValue() != null) return true;
        return false;
    }

    public boolean isTrueBoResultAndValueExists() {
        return FiBool.isTrue(getFdrBoExec()) && getValue() != null;
    }

    public boolean isFalseBoResult() {
        return FiBool.isFalse(getFdrBoExec());
    }

    public boolean isFalseOrNullBoResult() {
        if (getFdrBoExec() == null) return true;
        return FiBool.isFalse(getFdrBoExec());
    }

    public boolean isNullBoResult() {
        return getFdrBoExec() == null;
    }

    public boolean isEmptyMessage() {
        return FiString.isEmptyTrim(getMessage());
    }

    public Integer getLnTotalCount() {
        return lnTotalCount;
    }

    public void setLnTotalCount(Integer lnTotalCount) {
        this.lnTotalCount = lnTotalCount;
    }

    public void copyValues(Fdr fdr) {
        setException(fdr.getException());
        setMessage(fdr.getMessage());
        setFdrBoExec(fdr.getFdrBoExec());
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
        if (entiy == null) setFdrBoExec(false);
        setFdrBoExec(true);
    }

    public Fdr<EntClazz> appendMessageToUp(String txMessage) {
        addLogInfo(txMessage);
        setMessageForAppend(txMessage + "\n" + getMessage());
        return this;
    }

    public List<Exception> getListException() {
        return listException;
    }

    public List<Exception> getListExceptionInit() {
        if (listException == null) {
            listException = new ArrayList<>();
        }
        return listException;
    }

    public List<Exception> getListExceptionNtn() {
        if (listException == null) {
            return new ArrayList<>();
        }
        return listException;
    }

    public void setListException(List<Exception> listException) {
        this.listException = listException;
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

    public List<OreLog> getLogList() {
        return logList;
    }

    public List<OreLog> getLogListInit() {
        if (logList == null) {
            logList = new ArrayList<>();
        }
        return logList;
    }

    public void setLogList(List<OreLog> logList) {
        this.logList = logList;
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

    public void addLog(OreLog log) {
        if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
        getLogListInit().add(log);
    }

    public void addLog(String txMessage, MetaLogType metaLogType) {
        if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
        getLogListInit().add(new OreLog(txMessage, metaLogType));
    }

    public Fdr addLogInfo(String txMessage) {
        if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
        //if(getBoLockAddLogNtn()) throw new RuntimeException("Error: Added Log to Blocked Fdr !!!!!!!!");
        getLogListInit().add(new OreLog(txMessage, MetaLogType.INFO));
        return this;
    }

    public Fdr addLogStep(String txMessage) {
        if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
        //if(getBoLockAddLogNtn()) throw new RuntimeException("Error: Added Log to Blocked Fdr !!!!!!!!");
        getLogListInit().add(new OreLog(txMessage, MetaLogType.STEP));
        return this;
    }

    public Fdr<EntClazz> addLogError(String txMessage) {
        if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
        getLogListInit().add(new OreLog(txMessage, MetaLogType.ERROR));
        return this;
    }

    public void addLogWarn(String txMessage) {
        if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
        getLogListInit().add(new OreLog(txMessage, MetaLogType.WARN));
    }

    public void addLogTypeLog(String txMessage) {
        getLogListInit().add(new OreLog(txMessage, MetaLogType.LOG));
    }

    public String getLogAndMessageWitErrorInfo() {
        return FiString.addNewLineToEndIfNotEmpty(getMessage()) + getLogPlain();
    }

    public String getLogPlainWitErrorInfo() {

        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (OreLog oreLog : getLogListInit()) {
            if (index > 0) sb.append("\n");
            if (oreLog.getTxLogTypeNtn().equals(MetaLogType.ERROR.toString())) {
                sb.append("HATA !!! : ");
            }
            sb.append(oreLog.getOrlTxMessage());
            index++;
        }
        return sb.toString();

    }

    public String getLogErrorPlain() {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (OreLog oreLog : getLogListInit()) {
            if (index > 0) sb.append("\n");
            if (oreLog.getTxLogTypeNtn().equals(MetaLogType.ERROR.toString())) {
                sb.append(oreLog.getOrlTxMessage());
            }
            index++;
        }
        return sb.toString();
    }

    public String getLogPlain() {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (OreLog oreLog : getLogListInit()) {
            if (index > 0) sb.append("\n");
            sb.append(oreLog.getOrlTxMessage());
            index++;
        }
        return sb.toString();
    }

    public String getLogsAllTosWithMessage() {
        StringBuilder sb = new StringBuilder();

        int index = 0;

        if (!FiString.isEmpty(getMessage())) {
            sb.append(getMessage());
            index++;
        }

        for (OreLog oreLog : getLogListInit()) {
            if (index > 0) sb.append("\n");
            sb.append(oreLog.getOrlTxMessage());
            index++;
        }

        return sb.toString();
    }

    public Pair<String, Boolean> getLogsAllWitErrorWarnPairErrorExist() {
        StringBuilder sb = new StringBuilder("");
        int index = 0;
        boolean boErrorExist = false;
        for (OreLog oreLog : getLogListInit()) {
            if (index > 0) sb.append("\n");
            if (oreLog.getOrlTxMessage().equals(MetaLogType.ERROR.toString())) {
                boErrorExist = true;
                sb.append("HATA !!! : ");
            }
            sb.append(oreLog.getOrlTxMessage());
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
            getLogListInit().addAll(fdr.getLogListInit());
        }
    }

    public void convertResultIfNull(boolean boResult) {
        if (getFdrBoExec() == null) setFdrBoExec(boResult);
    }

    public Boolean getBoFalseExist() {
        return boFalseExist;
    }

    public void setBoFalseExist(Boolean boFalseExist) {
        this.boFalseExist = boFalseExist;
    }

    public String convertBoExecToTxResult() {
        if (getFdrBoExec() == null) return "Sonuçsuz (!!!)";
        if (getFdrBoExec()) {
            if (FiBool.isTrue(getBoFalseExist())) {
                return "Kısmı Başarılı";
            }
            return "Başarılı";
        } else {
            return "Başarısız";
        }
    }

    public boolean getIsFalseExist() {
        return FiBool.isTrue(getBoFalseExist());
    }

    public void addLogErrorException(List<Exception> listExceptionInit) {
        if (getBoLockAddLogNtn()) Loghelper.get(getClass()).debug("Error: Added Log to Blocked Fdr !!!!!!!!");
        if (!listExceptionInit.isEmpty()) {
            for (Exception exception1 : listExceptionInit) {
                getLogListInit().add(new OreLog(FiException.TosSummary(exception1), MetaLogType.ERROR));
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

    public Integer getLnFailureOpCount() {
        return lnFailureOpCount;
    }

    public Boolean getBoOpResult() {
        return boOpResult;
    }

//    public Fdr getRefFdrMethod() {
//        return refFdrMethod.get();
//    }
//
//    public ObjectProperty<Fdr<EntClazz>> refFdrMethodProperty() {
//        if (refFdrMethod == null) {
//            refFdrMethod = new SimpleObjectProperty<>();
//        }
//        return refFdrMethod;
//    }
//
//    public void setRefFdrMethod(Fdr refFdrMethod) {
//        this.refFdrMethod.set(refFdrMethod);
//    }

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

    public String getFdrTxValue() {
        return fdrTxValue;
    }

    public void setFdrTxValue(String fdrTxValue) {
        this.fdrTxValue = fdrTxValue;
    }

    public Integer getFdrLnValue() {
        return fdrLnValue;
    }

    public void setFdrLnValue(Integer fdrLnValue) {
        this.fdrLnValue = fdrLnValue;
    }
}
