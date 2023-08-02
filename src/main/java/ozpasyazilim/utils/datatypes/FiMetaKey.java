package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.core.FiString;

/**
 * Meta Bilgileri Saklamak için yazıldı. Sadece Key ihtiyaç olduğu yerde kullanılır.
 * <p>
 * ToString olarak TxKey döner. (FiMeta toString'de alan dönmüyor)
 */
public class FiMetaKey {

    /**
     *
     */
    private String txKey;

//	private String txValue;
//	private Integer lnKey;
//	private String txLabel;
//	private String txType;

    public FiMetaKey() {

    }

//	public FiMeta2(Integer lnKey) {
//		this.lnKey = lnKey;
//	}

    public FiMetaKey(String txKey) {
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
