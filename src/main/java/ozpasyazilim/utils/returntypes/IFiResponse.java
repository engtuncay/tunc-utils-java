package ozpasyazilim.utils.returntypes;

public interface IFiResponse<E> {

	public IFiResponse buildResult(Boolean blResult);

	public IFiResponse buildException(Exception e);

	public IFiResponse buildMessage(String s);

	//public void append(FiResponseGen fiResponse);

	public String getMessage();

	public void setMessage(String message);

	public void appendMessagedetail(String messagedetailtoAppend);

	public E getResValue();

	public void setResValue(E resValue);

	public Boolean getBoResult();

	public void setBoResult(Boolean boResult);

	public String getTxErrorMsgShort();

	public void setTxErrorMsgShort(String txErrorMsgShort);

	public String getTxErrorMsgDetail();

	public void setTxErrorMsgDetail(String txErrorMsgDetail);

	public Integer getLnRowsAffected();

	public void setLnRowsAffected(Integer lnRowsAffected);

	public Integer getLnIdAffected();

	public void setLnIdAffected(Integer lnIdAffected);

	public Integer getLnTotalLength();

	public void setLnTotalLength(Integer lnTotalLength);


}
