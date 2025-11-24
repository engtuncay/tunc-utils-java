package ozpasyazilim.utils.datatypes;

/**
 * txKey, txValue propertylerine sahip (keyed/named object her property'sine karşılık gelir (key ve değer string)
 * <p>
 * txLabel,lnCode gibi ekstra özellikler eklenmiştir. Integer-String value şeklinde de kullanılabilir.
 * <p>
 * toString de txKey'i döner (!!!)
 * <p>
 * equals ise txKey'e göre karşılaştırma yapar.
 */
public class FiMeta {

  /**
   * TxCode (TxKodu)
   */
  private String fimTxKey;

  private String fimTxValue;

  /**
   * LnCode (LnKodu)
   * <p>
   * Key Meta Karşılık Gelen Integer Kod varsa
   */
  private Integer fimLnKey;

  /**
   * Açıklama (Description) gibi düşünebiliriz
   */
  private String fimTxLabel;

  private String txType;

  public FiMeta() {

  }

//	public FiMeta(Integer lnKey) {
//		this.lnKey = lnKey;
//	}

  public FiMeta(String fimTxKey) {
    this.fimTxKey = fimTxKey;
  }

  public FiMeta(int fimLnKey, String fimTxLabel) {
    this.fimLnKey = fimLnKey;
    this.fimTxLabel = fimTxLabel;
  }

  public FiMeta(String fimTxKey, String fimTxLabel) {
    this.fimTxKey = fimTxKey;
    this.fimTxLabel = fimTxLabel;
  }

  public static FiMeta bui(String txKey) {
    FiMeta fiMeta = new FiMeta();
    fiMeta.setFimTxKey(txKey);
    return fiMeta;
  }

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
  public String getKey() {
    return fimTxKey;
  }

  public String getFimTxKey() {
    return fimTxKey;
  }

  public void setFimTxKey(String fimTxKey) {
    this.fimTxKey = fimTxKey;
  }

  public String getTxType() {
    return txType;
  }

  public void setTxType(String txType) {
    this.txType = txType;
  }

  /**
   * txKey boş ise lnkey dönsün diye de mekanizma kurulabilir.
   *
   * @return
   */
  @Override
  public String toString() {
    return getKey();
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
    if (obj instanceof FiMeta) {
      FiMeta fiMeta2 = (FiMeta) obj;
      if (fiMeta2.getKey() == null || getKey() == null) return false;
      return getKey().equals(fiMeta2.getKey());
    }
    return super.equals(obj);
  }
}
