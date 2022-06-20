package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.annotations.FiExpiremental;

// Genel Bilgi Saklamak Amacıyla kullanılabilir mi ???
@FiExpiremental
public class FiMeta {

	private String txKey;

	private String txValue;
	// Key Meta Karşılık Gelen Integer Kod varsa
	private Integer lnValue;

	private String txLabel;

	public FiMeta() {

	}

	public FiMeta(Integer lnValue) {
		this.lnValue = lnValue;
	}

	// Getter and Setter

	public String getTxValue() {
		return txValue;
	}

	public void setTxValue(String txValue) {
		this.txValue = txValue;
	}

	public Integer getLnValue() {
		return lnValue;
	}

	public void setLnValue(Integer lnValue) {
		this.lnValue = lnValue;
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
