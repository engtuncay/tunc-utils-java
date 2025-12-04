package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.annotations.FiExpiremental;
import ozpasyazilim.utils.core.FiString;

/**
 *
 *
 */
@FiExpiremental
public class FintMeta {

  /**
   * LnCode (LnKodu)
   * <p>
   * Key olarak Integer kullanmak istenirse
   */
  private Integer fimLnKey;

  /**
   * TxCode (TxKodu)
   */
//  private String fimTxKey;

  private String fimTxValue;


  /**
   * Açıklama (Description) gibi düşünebiliriz
   */
  private String fimTxLabel;

  private String txType;

  public FintMeta() {

  }

//	public FiMeta(Integer lnKey) {
//		this.lnKey = lnKey;
//	}

//  public FintMeta(String fimTxKey) {
//    this.fimTxKey = fimTxKey;
//  }

  public FintMeta(int fimLnKey, String fimTxLabel) {
    this.fimLnKey = fimLnKey;
    this.fimTxLabel = fimTxLabel;
  }

//  public FintMeta(String fimTxKey, String fimTxLabel) {
//    this.fimTxKey = fimTxKey;
//    this.fimTxLabel = fimTxLabel;
//  }

//  public static FintMeta bui(String txKey) {
//    FintMeta fiMeta = new FintMeta();
//    fiMeta.setFimTxKey(txKey);
//    return fiMeta;
//  }

  // Getter and Setter

  public String getFimTxValue() {
    return fimTxValue;
  }

  public void setFimTxValue(String fimTxValue) {
    this.fimTxValue = fimTxValue;
  }

  public Integer getFimLnKey() {
    return fimLnKey;
  }

  public void setFimLnKey(Integer fimLnKey) {
    this.fimLnKey = fimLnKey;
  }

  public String getFimTxLabel() {
    return fimTxLabel;
  }

  public void setFimTxLabel(String fimTxLabel) {
    this.fimTxLabel = fimTxLabel;
  }

  /**
   * gk: getTxKey
   *
   * @return
   */
//  public String getKey() {
//    return fimTxKey;
//  }

//  public String getFimTxKey() {
//    return fimTxKey;
//  }

//  public void setFimTxKey(String fimTxKey) {
//    this.fimTxKey = fimTxKey;
//  }
  public String getTxType() {
    return txType;
  }

  public void setTxType(String txType) {
    this.txType = txType;
  }

  /**
   *
   *
   * @return
   */
  @Override
  public String toString() {
    return FiString.orEmpty(getFimLnKey());
  }

  /**
   * txKey'e göre karşılaştırma yapar.
   * <p>
   * txKey null ise false döner
   *
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof FintMeta) {
      FintMeta fiMeta2 = (FintMeta) obj;
      if (fiMeta2.getFimLnKey() == null || getFimLnKey() == null) return false;
      ;
      return getFimLnKey().equals(fiMeta2.getFimLnKey());
    }
    return super.equals(obj);
  }

}
