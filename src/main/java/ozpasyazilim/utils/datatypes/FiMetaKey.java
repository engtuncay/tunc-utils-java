package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.core.FiString;

/**
 * String Key ve TxLabel tutan Meta Bilgileri Saklamak için yazıldı.
 * <p>
 * ToString olarak TxKey döner. (FiMeta toString'de alan dönmüyor)
 */
public class FiMetaKey {

    /**
     *
     */
    private String txKey;

    private String txLabel;

//	private String txValue;
//	private Integer lnKey;
//	private String txLabel;
//	private String txType;

    public FiMetaKey() {

    }

    public FiMetaKey(String txKey) {
        this.txKey = txKey;
    }

    public FiMetaKey(String txKey,String txLabel) {
        this.txKey = txKey;
        this.txLabel = txLabel;
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

    public String getTxLabel() {
        return txLabel;
    }

    public void setTxLabel(String txLabel) {
        this.txLabel = txLabel;
    }
}
