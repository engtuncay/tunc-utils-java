package ozpasyazilim.utils.returntypes;

import ozpasyazilim.utils.core.FiType;

public class FiResponse<E> implements IFiResponse<E> {

	E fiValue;
	Boolean boResult;
	String txErrorMsgShort;
	String txErrorMsgDetail;
    Integer lnRowsAffected;
	Integer lnIdAffected;
	Integer lnTotalLength;
	String message;

	//Object spec1;
	// Deprecated yapılabilir
	// String resultCode;
	// Boolean isDone;
	// Boolean blBatchResult;

	public FiResponse(Boolean tfResult) {
		this.boResult = tfResult;
	}

	public FiResponse() {

	}

	public FiResponse(Integer rowsAffected) {
		if(rowsAffected!=null && rowsAffected>0) this.boResult =true;
		this.lnRowsAffected= rowsAffected;
	}

	public static FiResponse build() {
		return new FiResponse();
	}


	public FiResponse buildResult(Boolean blResult) {
		this.boResult = blResult;
		return this;
	}

	public FiResponse buildException(Exception e) {
		this.txErrorMsgShort = e.getMessage();
		return this;
	}

	public FiResponse buildMessage(String s) {
		this.message = s;
		return this;
	}

	public void append(FiResponse fiResponse) {

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

	public void setMessageAndBoResult(String message,Boolean boResult) {
		this.message = message;
		this.boResult = boResult;
	}

	public void appendMessagedetail(String messagedetailtoAppend) {
		if (message == null) { message = ""; }

		this.message += messagedetailtoAppend;
	}

	public E getResValue() {
		return fiValue;
	}

	public void setResValue(E resValue) {
		this.fiValue = resValue;
	}

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


	public FiResponse buildBoResult(Boolean b) {
		this.boResult = b;
		return this;
	}
}
