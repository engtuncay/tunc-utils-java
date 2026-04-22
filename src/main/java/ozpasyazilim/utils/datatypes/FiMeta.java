package ozpasyazilim.utils.datatypes;

/**
 * txKey, txValue propertylerine sahip (keyed/named object her property'sine karşılık gelir (key ve değer string)
 * <p>
 * txLabel,lnCode gibi ekstra özellikler eklenmiştir. Integer-String value şeklinde de kullanılabilir.
 * <p>
 * toString de txKey'i döner (!!!)
 * <p>
 * equals ise txKey'e göre karşılaştırma yapar.
 *
 * fimTxKey -> ftTxKey yapıldı
 */
public class FiMeta {

  /**
   * TxCode (TxKodu)
   */
  private String ftTxKey;

  private String ftTxValue;

  /**
   * LnCode (LnKodu)
   * <p>
   * Key olarak Integer kullanmak istenirse
   */
  private Integer ftLnKey;

  /**
   * Açıklama (Description) gibi düşünebiliriz
   */
  private String ftTxLabel;

  private String txType;

  public FiMeta() {

  }

//	public FiMeta(Integer lnKey) {
//		this.lnKey = lnKey;
//	}

  public FiMeta(String ftTxKey) {
    this.ftTxKey = ftTxKey;
  }

  public FiMeta(int ftLnKey, String ftTxLabel) {
    this.ftLnKey = ftLnKey;
    this.ftTxLabel = ftTxLabel;
  }

  public FiMeta(String ftTxKey, String ftTxLabel) {
    this.ftTxKey = ftTxKey;
    this.ftTxLabel = ftTxLabel;
  }

  public static FiMeta bui(String txKey) {
    FiMeta fiMeta = new FiMeta();
    fiMeta.setFtTxKey(txKey);
    return fiMeta;
  }

  // Getter and Setter

  public String getFtTxValue() {
    return ftTxValue;
  }

  public String getValue() {
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

  /**
   * gk: getTxKey
   *
   * @return
   */
  public String getTxKey() {
    return ftTxKey;
  }

  public String getFtTxKey() {
    return ftTxKey;
  }

  public String getKey() {
    return ftTxKey;
  }

  public void setFtTxKey(String ftTxKey) {
    this.ftTxKey = ftTxKey;
  }

  public void setTxKey(String ftTxKey) {
    this.ftTxKey = ftTxKey;
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
    return getTxKey();
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
      if (fiMeta2.getTxKey() == null || getTxKey() == null) return false;
      return getTxKey().equals(fiMeta2.getTxKey());
    }
    return super.equals(obj);
  }
}
