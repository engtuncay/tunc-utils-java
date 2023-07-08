package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.core.FiString;

/**
 * Meta Bilgileri Saklamak için yazıldı.
 *
 * ToString olarak TxKey döner. (FiMeta toString'de alan dönmüyor)
 */
public class FiMeta2 {

	/**
	 *
	 */
	private String txKey;

//	private String txValue;

//	private Integer lnKey;

//	private String txLabel;

//	private String txType;

	public FiMeta2() {

	}

//	public FiMeta2(Integer lnKey) {
//		this.lnKey = lnKey;
//	}

	public FiMeta2(String txKey) {
		this.txKey = txKey;
	}

	// Getter and Setter

	public String getTxKey() {
		return txKey;
	}

	public void setTxKey(String txKey) {
		this.txKey = txKey;
	}

	@Override
	public String toString() {
		return FiString.orEmpty(txKey);
	}

}
