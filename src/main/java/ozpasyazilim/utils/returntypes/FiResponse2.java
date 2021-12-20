package ozpasyazilim.utils.returntypes;

import ozpasyazilim.utils.core.FiType;

public class FiResponse2<E> implements IFiResponse<E> {

	E resValue;
	Boolean boResult;
	String txErrorMsgShort;
	String txErrorMsgDetail;
    Integer lnRowsAffected;
	Integer lnIdAffected;
	Integer lnTotalLength;
	String message;
	//Object spec1;

	// Deprecated yapılabilir
	String resultCode;
	Boolean isDone;
	Boolean blBatchResult;

	public FiResponse2(boolean b) {
		this.isDone = b;
	}

	public FiResponse2() {

	}

	public FiResponse2(Integer rowsAffected) {
		if(rowsAffected!=null && rowsAffected>0) this.boResult =true;
		this.lnRowsAffected= rowsAffected;
	}

	public static FiResponse2 build() {
		return new FiResponse2();
	}


	public FiResponse2 buildResult(Boolean blResult) {
		this.boResult = blResult;
		return this;
	}

	public FiResponse2 buildException(Exception e) {
		this.txErrorMsgShort = e.getMessage();
		return this;
	}

	public FiResponse2 buildMessage(String s) {
		this.message = s;
		return this;
	}

	public void append(FiResponse2 fiResponse) {

		if(this.boResult ==null && FiType.isTrue(fiResponse.boResult)) this.boResult = fiResponse.boResult;

		if(!fiResponse.boResult) this.boResult =false;


		if(FiType.isEmptyWithTrim(fiResponse.getTxErrorMsgShort())) {
			this.setTxErrorMsgShort(this.txErrorMsgShort + "\n \n" + fiResponse.txErrorMsgShort);
		}

	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void appendMessagedetail(String messagedetailtoAppend) {
		if (message == null) { message = ""; }

		this.message += messagedetailtoAppend;
	}


	public Boolean getIsDone() {
		return isDone;
	}

	public void setIsDone(Boolean isDone) {
		this.isDone = isDone;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public E getResValue() {
		return resValue;
	}

	public void setResValue(E resValue) {
		this.resValue = resValue;
	}

	public Boolean getBlBatchResult() {
		return blBatchResult;
	}

	public void setBlBatchResult(Boolean blBatchResult) {
		this.blBatchResult = blBatchResult;
	}

	public Boolean getDone() {return isDone;}

	public void setDone(Boolean done) {isDone = done;}

	public Boolean getBoResult() {return boResult;}

	public void setBoResult(Boolean boResult) {this.boResult = boResult;}

	public String getTxErrorMsgShort() {
		return txErrorMsgShort;
	}

	public void setTxErrorMsgShort(String txErrorMsgShort) {this.txErrorMsgShort = txErrorMsgShort;}

	public String getTxErrorMsgDetail() {
		return txErrorMsgDetail;
	}

	public void setTxErrorMsgDetail(String txErrorMsgDetail) {
		this.txErrorMsgDetail = txErrorMsgDetail;
	}

	public Integer getLnRowsAffected() {
		return lnRowsAffected;
	}

	public void setLnRowsAffected(Integer lnRowsAffected) {
		this.lnRowsAffected = lnRowsAffected;
	}

	public Integer getLnIdAffected() {
		return lnIdAffected;
	}

	public void setLnIdAffected(Integer lnIdAffected) {
		this.lnIdAffected = lnIdAffected;
	}

	public Integer getLnTotalLength() {
		return lnTotalLength;
	}

	public void setLnTotalLength(Integer lnTotalLength) {
		this.lnTotalLength = lnTotalLength;
	}


}
