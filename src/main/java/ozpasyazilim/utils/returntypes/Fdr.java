package ozpasyazilim.utils.returntypes;

import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.core.FiNumber;
import ozpasyazilim.utils.core.FiString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Sorgu çalıştırılmışsa başarılı bir şekilde boResult True olur
 * <p>
 * Hata alırsa da false olur
 * <p>
 * İşlem yapılmamışsa null olur !!!
 * <p>
 * Cre : 22-02-2019 TO
 *
 * @param <EntClazz>> value property nin tipi
 */
public class Fdr<EntClazz> implements IFnResult<EntClazz> {

	/**
	 * True ise sorgu başarıyla çalıştırıldığını ifade eder (exception'a düşmemiş)
	 * <p>
	 * False ise sorguda hata olup,exception olmuştur
	 * <p>
	 * Null ise işlem yapılmadığını ifade eder  (last update:29-12-2019)
	 * <p>
	 * boQueryExecuted alternatif adı
	 */
	private Boolean boResult;
	private EntClazz value;
	private String message;
	private Integer rowsAffected;
	private Integer lnTotalCount;

	// Advanced Configuration
	private String txQueryType;
	private Integer lnInsertedRows;
	private Integer lnUpdatedRows;
	private Integer lnDeletedRows;

	// fdr id
	private String txId;
	private Exception exception;
	// Op : Operation
	private Integer lnSuccessOpCount;
	private Integer lnFailureOpCount;
	private Integer lnErrorCode;

	private Integer lnResponseCode;

	// ???
	Integer rowsAffectedExtraWorks;
	Integer rowsAffectedExtraByEntity;

	/**
	 * Operasyon sonucu nedir , true işlem sonucu pozitif, false işlem sonucu negatif olur.
	 * <p>
	 * boResult farkı : boResult , sorgunun başarılı çalıştırıldığını gösterir
	 * <p>
	 * örneğin checkExist yapılıyorsa varsa kayıt true, yoksa false olur.
	 */
	Boolean boOprResult;

	// Sorgu,işlem çalıştırılmadıysa false yapılır
	Boolean boQueryExecuted;

	// dbResult birleştirilmiş operasyon mu
	Boolean boCombinedOperation;

	// Combined Operasyon ise alt sonuçlar burada saklanabilir
	List<Fdr> fdrList;

	List<Exception> listException;

	public Fdr() {
	}

	public Fdr(Boolean boResult) {
		setBoResult(boResult);
		//setResult(Optional.ofNullable(boResult));
	}

	public Fdr(FnResultGen fnKayitSonuc) {
		setBoResult(fnKayitSonuc.getBResult());
		setMessage(fnKayitSonuc.getSMessage());
	}

	public Fdr(Integer rowCountUpdateWithUpBoResult) {
		setRowsAffectedWithUpBoResult(rowCountUpdateWithUpBoResult);
	}

	public Fdr(Boolean boResult, Integer rowCountUpdate) {
		setBoResult(boResult);
		setRowsAffectedWithUpBoResult(rowCountUpdate);
	}

	public Fdr(Integer rowCountUpdate, Exception ex) {
		setRowsAffectedWithUpBoResult(rowCountUpdate);
		setException(ex);
	}

	public Fdr(Integer rowCountUpdate, Boolean boResult) {
		setRowsAffectedWithUpBoResult(rowCountUpdate);
		setBoResult(boResult);
	}

	public Fdr(Boolean boResult, Exception ex) {
		setBoResult(boResult);
		setException(ex);
	}

	public Fdr(FiResponse fiResponse) {
		this.boResult = fiResponse.boResult;
		this.message = fiResponse.message;
	}

	public Fdr(Boolean boResult, String message) {
		this.boResult = boResult;
		this.message = message;
	}

	public Fdr(Boolean boResult, String txMessage, Boolean boAddException) {
		this.boResult = boResult;
		this.message = txMessage;

		if (FiBoolean.isTrue(boAddException)) {
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
		fdrNew.setBoResult(fdrOld.getBoResult());
		fdrNew.setRowsAffected(fdrOld.getRowsAffectedWithInit());
		fdrNew.setException(fdrOld.getException());
//		fdrNew.setBoPartialSuccces(fdrOld.getBoPartialSuccces());
		fdrNew.setRowsAffectedExtraWorks(fdrOld.getRowsAffectedExtraWorks());
		fdrNew.setRowsAffectedExtraByEntity(fdrOld.getRowsAffectedExtraByEntity());
		fdrNew.setLnSuccessOpCount(fdrOld.getLnSuccessOpCount());
		fdrNew.setLnFailureOpCount(fdrOld.getLnFailureOpCount());
	}

	public static Fdr creBoResult(Boolean boResult) {
		Fdr fdr = new Fdr();
		fdr.setBoResult(boResult);
		return fdr;
	}

	public static Fdr creOperaResult(Boolean boOperaResult) {
		Fdr fdr = new Fdr();
		fdr.setBoOprResult(boOperaResult);
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
		fdr.setBoResult(false);
		return fdr;
	}

	public Optional<Boolean> getOpResult() {
		return Optional.ofNullable(getBoResult());
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
	public Boolean getBoResult() {
		return boResult;
	}

	public void setBoResult(Boolean boResult) {
		this.boResult = boResult;
	}

	// Tek tek kullanılmalı
	@Deprecated
	public void setBoResultWithCheckUpWithRowsAff(Boolean boResult) {
		this.boResult = boResult;
		// rows affected 0 dan büyük olmalı true olması için
		checkAndRefreshResult(boResult);
	}

	// Tek tek kullanılmalı
	@Deprecated
	public void setBoResultWithCheckUpWithRowsAff(Boolean boResult, Integer rowsAffected) {
		appendRowsAffected(rowsAffected);
		this.boResult = boResult;
		checkAndRefreshResult(boResult);
	}

	public void checkAndRefreshResult(Boolean boResult) {
		// rows affected 0 dan büyük olmalı true olması için
		if (FiBoolean.isTrue(boResult) && getRowsAffectedNotNull() < 1) {
			this.boResult = false;
			this.boQueryExecuted = true;
		}

		if (FiBoolean.isFalse(boResult)) {
			setRowsAffectedWithUpBoResult(-1);
		}
	}

	public EntClazz getValue() {
		return value;
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

	public String getAllResMessPlusMessage() {
//		String lastMess = getResMessage().toString();
//		if (!FiString.isEmpty(message)) {
//			lastMess += message;
//		}
		return message;
	}

	public void setMessage(String message) {
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
			setBoResult(true);
		}
	}

	public void appendLnSuccess(Integer lnSuccessCount) {
		if (lnSuccessCount == null) return;
		setLnSuccessOpCount(getLnSuccessOpCount() + lnSuccessCount);
	}

	public void appendLnFailure(Integer lnFailureCount) {
		if (lnFailureCount == null) return;
		setLnFailureOpCount(getLnFailureOpCount() + lnFailureCount);
	}

	public void appendLnInserted(Integer lnInsertedRows) {
		setLnInsertedRows(FiNumber.orZero(getLnInsertedRows()) + FiNumber.orZero(lnInsertedRows));
	}

	public void appendLnDeleted(Integer lnDeletedRows) {
		setLnDeletedRows(FiNumber.orZero(getLnDeletedRows()) + FiNumber.orZero(lnDeletedRows));
	}

	public void appendLnUpdated(Integer lnUpdatedRows) {
		setLnUpdatedRows(FiNumber.orZero(getLnUpdatedRows()) + FiNumber.orZero(lnUpdatedRows));
	}


	public Exception getException() {
		return exception;
	}

	public Boolean hasException() {
		return getException() != null;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Fdr buildResult(boolean b) {
		this.setBoResult(true);
		//this.setOpResult(true);
		return null;
	}

	public Fdr buildException(Exception e) {
		this.setException(e);
		return this;
	}

	public Fdr<EntClazz> buildValue(EntClazz entity) {
		this.setValue(entity);
		return this;
	}

	public Fdr<EntClazz> buildMessage(String message) {
		this.setMessage(message);
		return this;
	}

	/**
	 * İşlem sonuçlarının hepsi true olursa sonuç true olur, bir tane false varsa sonuç false olur.
	 * <p>
	 * null için birleştirme yapılmamış, mesaj birleştirilebilir.
	 *
	 * @param fdr
	 */
	public void combineAnd(Fdr fdr) {

		if (FiBoolean.isFalse(fdr.getBoResult())) {
			setBoResult(false);
			setLnFailureOpCount(getLnFailureOpCount() + 1);
			setException(fdr.getException()); // exception listesine eklenebilir - birden fazla olma ihtimali var.
			getListExceptionInit().add(fdr.getException());
		}

		if (FiBoolean.isTrue(fdr.getBoResult())) {
			setLnSuccessOpCount(getLnSuccessOpCount() + 1);
			if (getBoResult() == null) setBoResult(true);
		}

		// null sonuçlar için combine işlemi
		if (fdr.getBoResult() == null) {

		}

		// Tümü için yapılacaklar


		appendRowsAffected(fdr.getRowsAffectedOrEmpty());
		appendLnUpdated(fdr.getLnUpdatedRows());
		appendLnInserted(fdr.getLnInsertedRows());
		appendLnDeleted(fdr.getLnDeletedRows());

		// Tüm işlemlerde mesaj birleştirilir.
		appendMessageLn(fdr.getMessage());
	}

	public void appendMessageLn(String message) {
		if (message == null) return;
		setMessage(FiString.orEmpty(getMessage()) + FiString.addNewLineToBeginIfNotEmpty(message));
	}

	/**
	 * İşlemlerden bir tane true varsa, genel sonuç true olur.
	 * <p>
	 * Or baglacı ile baglar
	 *
	 * @param fdr
	 */
	public void combineOr(Fdr fdr) {

		if (FiBoolean.isFalse(fdr.getBoResult())) {
			appendLnFailure(1);
			setException(fdr.getException());
			if (getBoResult() == null) setBoResult(false);
			//getResMessage().append(fiDbResult.getResMessage().toString());
		}

		if (FiBoolean.isTrue(fdr.getBoResult())) {
			setBoResult(true);
			appendLnSuccess(1);
			//getResMessage().append(fiDbResult.getResMessage().toString());
		}

		appendRowsAffected(fdr.getRowsAffectedOrEmpty());
		// Tüm işlemlerde mesaj birleştirilir.
		appendMessageLn(fdr.getMessage());

	}

	public Integer getLnSuccessOpCount() {
		if (lnSuccessOpCount == null) {
			lnSuccessOpCount = 0;
		}
		return lnSuccessOpCount;
	}

	public void setLnSuccessOpCount(Integer lnSuccessOpCount) {
		this.lnSuccessOpCount = lnSuccessOpCount;
	}

	public Integer getLnFailureOpCount() {
		if (lnFailureOpCount == null) {
			lnFailureOpCount = 0;
		}
		return lnFailureOpCount;
	}

	public void setLnFailureOpCount(Integer lnFailureOpCount) {
		this.lnFailureOpCount = lnFailureOpCount;
	}

	public Boolean getBoResultInit() {
		if (getBoResult() == null) return false;
		return getBoResult();
	}

	/**
	 * Sorgu çalıştırılmış ve rowsAffected > 0 ise true döner
	 *
	 * @return
	 */
	public Boolean getResultByRowsAffected() {
		if (getBoResult() == null) return false;
		if (getBoResultInit() && getRowsAffectedWithInit() > 0) return true;
		return false;
	}

	public Boolean getBoOperaResultNotNull() {
		if (getBoResult() == null) return false;
		return getBoResult();
	}

	public Boolean getBoResultNotNullWithChecked() {
		// Rows affected 1 altında ise false olarak yorumlanır
		if (getRowsAffectedNotNull() < 1) {
			return false;
		}
		if (getBoResult() == null) return false;
		return getBoResult();
	}

	public Boolean getBoPartialSuccces2() {
		if (getLnSuccessOpCount() > 0 && getLnFailureOpCount() > 0) {
			return true;
		}
		return false;
	}

	public void setBoResultAndMess(Boolean boResult, String message) {
		setBoResult(boResult);
		setMessage(message);
	}

	public Fdr buildBoResult(Boolean b) {
		setBoResult(b);
		return this;
	}

	public Integer getLnErrorCode() {
		return lnErrorCode;
	}

	public void setLnErrorCode(Integer lnErrorCode) {
		this.lnErrorCode = lnErrorCode;
	}

	public void appendRowsAffected(Integer rowsAffected) {
		if (rowsAffected == null) return;

		if (rowsAffected > 0) {
			setRowsAffectedWithUpBoResult(getRowsAffectedWithInit() + rowsAffected);
		}
	}

	public void setBoResult(Boolean boResult, Exception exError) {
		setBoResult(boResult);
		setException(exError);
	}

	public Integer getRowsAffectedExtraWorks() {
		return rowsAffectedExtraWorks;
	}

	public void setRowsAffectedExtraWorks(Integer rowsAffectedExtraWorks) {
		this.rowsAffectedExtraWorks = rowsAffectedExtraWorks;
	}

	public Integer getRowsAffectedExtraByEntity() {
		return rowsAffectedExtraByEntity;
	}

	public void setRowsAffectedExtraByEntity(Integer rowsAffectedExtraByEntity) {
		this.rowsAffectedExtraByEntity = rowsAffectedExtraByEntity;
	}

	public void setBoResultAndValue(Boolean boResult, EntClazz resValue, Integer rowsAffected) {
		setBoResult(boResult);
		setRowsAffected(rowsAffected);
		setValue(resValue);
	}

	public void setBoResultAndValue(Boolean boResult, EntClazz resValue) {
		setBoResult(boResult);
		setRowsAffected(rowsAffected);
		setValue(resValue);
	}

	public void setBoResultAndRowsAff(Boolean boResult, Integer rowsAffected) {
		setBoResult(boResult);
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

	public Boolean getBoOprResult() {
		return boOprResult;
	}

	public void setBoOprResult(Boolean boOprResult) {
		this.boOprResult = boOprResult;
	}

	public Boolean getBoCombinedOperation() {
		return boCombinedOperation;
	}

	public void setBoCombinedOperation(Boolean boCombinedOperation) {
		this.boCombinedOperation = boCombinedOperation;
	}

	public void setResValue(EntClazz value) {
		setValue(value);
	}

	public EntClazz getResValue() {
		return value;
	}

	public List getValueCollection() {
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
		if (fdrList == null) {
			fdrList = new ArrayList<>();
		}
		return fdrList;
	}

	public void setFdrResultList(List<Fdr> fdrList) {
		this.fdrList = fdrList;
	}

	public void append(String message) {
		appendMessageLn(message);
	}

	public boolean isTrueBoResult() {
		return FiBoolean.isTrue(getBoResult());
	}

	public boolean isTrueResultAndValueNtn() {
		if (FiBoolean.isTrue(getBoResult()) && getValue() != null) return true;
		return false;
	}

	public boolean isTrueBoResultAndValueExists() {
		return FiBoolean.isTrue(getBoResult()) && getValue() != null;
	}

	public boolean isFalseBoResult() {
		return FiBoolean.isFalse(getBoResult());
	}

	public boolean isNullBoResult() {
		return getBoResult() == null;
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
		setBoResult(fdr.getBoResult());
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

	public boolean isPartialSuccess() {
		if (getLnSuccessOpCount() > 0) {
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
		if (entiy == null) setBoResult(false);
		setBoResult(true);
	}

	public Fdr<EntClazz> appendMessageToUp(String s) {
		setMessage(s + "\n" + getMessage());
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

	public List<Exception> getListExceptionNotNull() {
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
}
