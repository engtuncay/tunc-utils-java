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
  private Integer ftLnKey;

  //  /**
  //   * TxCode (TxKodu)
  //   */
  //  private String fimTxKey;

  private String ftTxValue;


  /**
   * Açıklama (Description) gibi düşünebiliriz
   */
  private String ftTxLabel;

  private String txType;

  public FintMeta() {

  }

  public FintMeta(int ftLnKey, String ftTxLabel) {
    this.ftLnKey = ftLnKey;
    this.ftTxLabel = ftTxLabel;
  }

  // Getter and Setter

  public String getFtTxValue() {
    return ftTxValue;
  }

  public void setFtTxValue(String ftTxValue) {
    this.ftTxValue = ftTxValue;
  }

  public Integer getFtLnKey() {
    return ftLnKey;
  }

  public void setFtLnKey(Integer ftLnKey) {
    this.ftLnKey = ftLnKey;
  }

  public String getFtTxLabel() {
    return ftTxLabel;
  }

  public void setFtTxLabel(String ftTxLabel) {
    this.ftTxLabel = ftTxLabel;
  }

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
    return FiString.orEmpty(getFtLnKey());
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
      if (fiMeta2.getFtLnKey() == null || getFtLnKey() == null) return false;
      ;
      return getFtLnKey().equals(fiMeta2.getFtLnKey());
    }
    return super.equals(obj);
  }

}
