package ozpasyazilim.utils.returntypes;

import java.util.Optional;

public class FnResult<T> implements IFnResult<T> {

	private Boolean boResult;
	private T value;
	private String message;
	private Integer rowsAffected;
	private Exception exception;

	//private Optional<Boolean> result = Optional.empty();

	public FnResult() {
	}

	public FnResult(boolean b) {
		//setResult(true);
	}

	public FnResult(FnResultGen fnKayitSonuc) {
		setBoResult(fnKayitSonuc.getBResult());
		setMessage(fnKayitSonuc.getSMessage());
	}

	public FnResult(Integer rowCountUpdate) {
		setRowsAffected(rowCountUpdate);

		if(rowCountUpdate!=null && rowCountUpdate>0) {
			setBoResult(true);
		}else{
			setBoResult(false);
		}
	}

	public FnResult(Boolean boResult, Integer rowCountUpdate) {
		setBoResult(true);
		setRowsAffected(rowCountUpdate);
	}

	public static FnResult genInstance() {
		return new FnResult();
	}

	public Optional<Boolean> getResult() {
		return Optional.ofNullable(boResult);
	}

	public Boolean getBoResult() {
		return boResult;
	}

	public Boolean getBoResultNotNull() {
		if(this.boResult==null) return false;
		return boResult;
	}

	public void setBoResult(Boolean boResult) {
		this.boResult = boResult;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getRowsAffected() {
		return rowsAffected;
	}

	public void setRowsAffected(Integer rowsAffected) {
		this.rowsAffected = rowsAffected;
		if(rowsAffected>0) {
			setBoResult(true);
		}
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public FnResult buildResult(boolean b) {
		this.setBoResult(true);
		return null;
	}

	public FnResult buildException(Exception e) {
		this.setException(e);
		return this;
	}


}
