package ozpasyazilim.utils.datatypes;

/**
 * txKey, txValue propertylerine sahip (keyed/named object her property'sine karşılık gelir (key ve değer string)
 * <p>
 * txLabel,lnCode gibi ekstra özellikler eklenmiştir
 */
public class FiMeta {

	/**
	 * TxCode (TxKodu)
	 */
	private String txKey;

	private String txValue;

	/**
	 * LnCode (LnKodu)
	 * <p>
	 * Key Meta Karşılık Gelen Integer Kod varsa
	 */
	private Integer lnKey;

	private String txLabel;

	private String txType;

	public FiMeta() {

	}

	public FiMeta(Integer lnKey) {
		this.lnKey = lnKey;
	}

	public FiMeta(String txKey) {
		this.txKey = txKey;
	}

	public FiMeta(int lnKey, String txLabel) {
		this.lnKey = lnKey;
		this.txLabel = txLabel;
	}

	// Getter and Setter

	public String getTxValue() {
		return txValue;
	}

	public void setTxValue(String txValue) {
		this.txValue = txValue;
	}

	public Integer getLnKey() {
		return lnKey;
	}

	public void setLnKey(Integer lnKey) {
		this.lnKey = lnKey;
	}

	public String getTxLabel() {
		return txLabel;
	}

	public void setTxLabel(String txLabel) {
		this.txLabel = txLabel;
	}

	public String getTxKey() {
		return txKey;
	}

	public void setTxKey(String txKey) {
		this.txKey = txKey;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}
}
