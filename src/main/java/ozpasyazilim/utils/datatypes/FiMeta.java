package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.annotations.FiExpiremental;

// Genel Bilgi Saklamak Amacıyla kullanılabilir mi ???
@FiExpiremental
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
}
