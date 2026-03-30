package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.ficols.FimFiCol;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.table.FiCol;

import java.math.BigDecimal;
import java.util.*;

/**
 * Map [String,String] türünde özel tip
 */
public class FiKeytext extends HashMap<String, String> {

  public FiKeytext() {
  }

  public static FiKeytext build() {
    return new FiKeytext();
  }

  public void clearEmptyKeys() {
    List<String> listToDelete = new ArrayList<>();
    this.forEach((key, value) -> {
      if (FiString.isEmpty(value)) {
        listToDelete.add(key);
      }
    });
    for (String key : listToDelete) {
      this.remove(key);
    }
  }

  public Boolean isEmptyKey(String txKey) {
    if (this.containsKey(txKey)) {
      if (FiString.isEmpty(this.get(txKey))) {
        return true;
      }
    }
    return false;
  }

//	public String getTos(Object txKey) {
//		if (txKey == null) return null;
//		return get(txKey.toString());
//	}

  public String getTosOrEmpty(Object txKey) {
    if (txKey == null) return "";
    if (!containsKey(txKey.toString())) return "";
    return FiString.orEmpty(get(txKey.toString()));
  }

  public FiKeytext buiPut(Object fieldName, String value) {
    if (fieldName == null) return this;

    this.put(fieldName.toString(), value);
    return this;
  }

  public FiKeytext buiPutIfNotNull(Object fieldName, String value) {
    if (fieldName == null) return this;
    if (value == null) return this;

    this.put(fieldName.toString(), value);
    return this;
  }

  public FiKeytext bind(Object key, String value) {
    if (key == null) return this;
    put(key.toString(), value);
    return this;
  }

  public FiKeytext putIfNotEmpty(Object fieldName, String value, Boolean addPercentage) {
    if (fieldName == null) return this;
    if (FiType.isEmptyObj(value)) return this;

    if (FiBool.isTrue(addPercentage)) {
      this.put(fieldName.toString(), "%" + value + "%");
    } else {
      this.put(fieldName.toString(), value);
    }
    return this;
  }

  /**
   * Key alanının Tos metodu ile ekleme yapar
   *
   * @param key
   * @param value
   * @return
   */
  public FiKeytext putKeyTos(Object key, String value) {
    this.put(key.toString(), value);
    return this;
  }

//  public FiKeybean addFiCol(FiCol fiCol, Object value) {
//    this.put(fiCol.toString(), value);
//    getListFiColInit().add(fiCol);
//    getMapFiColInit().put(fiCol.getFcTxFieldName(), fiCol);
//    return this;
//  }

  public FiKeytext putField(FiCol fiCol, String value) {
    this.put(fiCol.getFcTxFieldName(), value);
    return this;
  }

  public Integer getFullParamCount() {
    Collection<String> values = this.values();
    Integer lnCount = 0;
    for (Object value : values) {
      if (value != null) lnCount++;
    }
    return lnCount;
  }

  public void add(Object field, String value) {
    this.put(field.toString(), value);
  }

  public Object getAsObj(String txKey) {
    if (txKey == null) return null;
    return get(txKey.toString());
  }

  /**
   * Get KeyValue As String
   *
   * @param fiCol
   * @return
   */
  public String getAsString(FiCol fiCol) {
    if (fiCol == null || FiString.isEmpty(fiCol.getFcTxFieldName())) return null;

    if (containsKey(fiCol.getFcTxFieldName())) {
      Object objValue = get(fiCol.getFcTxFieldName());

      if (objValue == null) return null;

      return (String) objValue;
    }

    return null;
  }

  public String getValueAsString(String txKey) {
    if (FiString.isEmpty(txKey)) return null;

    if (containsKey(txKey)) {
      Object objValue = get(txKey);

      if (objValue == null) return null;

      return (String) objValue;
    }

    return null;
  }

  public String getValueAsStringNtn(String txKey) {
    String txVal = getValueAsString(txKey);
    if (txVal == null) {
      return "";
    }
    return txVal;
  }

  public Integer getFicValAsIntOrMinusOne(FiCol fiCol) {
    return FiNumber.orMinusOne(getFicAsInt(fiCol));
  }

  public Integer getFicAsInt(FiCol fiCol) {

    if (fiCol == null) return null;

    return getAsInt(fiCol.getFcTxFieldName());
  }

  public int getFicAsIntNtnMinusOne(FiCol fiCol) {

    if (fiCol == null) return -1;

    // Returns an Optional describing the specified value, if non-null, otherwise returns an empty Optional.
    return Optional.ofNullable(getAsInt(fiCol.getFcTxFieldName())).orElse(-1);
  }

  public Integer getAsInt(String txKey) {

    if (FiString.isEmpty(txKey)) return null;

    if (containsKey(txKey)) {
      Object objValue = get(txKey);

      if (objValue == null) return null;

      //Loghelper.get(getClass()).debug(FiReflection.getSimpleTypeName(objValue.getClass()));

      if (objValue instanceof Integer) {
        return (Integer) objValue;
      }

      if (objValue instanceof Short) {
        Short shortValue = (Short) objValue;
        return shortValue.intValue();
      }

    }

    return null;
  }

  public Double getFicAsDouble(FiCol fiCol) {
    if (fiCol == null) return null;
    return getAsDouble(fiCol.getFcTxFieldName());
  }

  public Double getFicAsDouble(IFiCol fiCol) {
    if (fiCol == null) return null;
    return getAsDouble(fiCol.getFcTxFieldName());
  }

  public Double getIFicAsDoubleOrZero(IFiCol fiCol) {
    return FiNumber.orZero(getFicAsDouble(fiCol));
  }

  public Double getAsDouble(String txKey) {

    if (FiString.isEmpty(txKey)) return null;

    if (containsKey(txKey)) {
      Object objValue = get(txKey);

      if (objValue == null) return null;

      //Loghelper.get(getClass()).debug("Class:"+objValue.getClass().getSimpleName());

      if (objValue instanceof Double) {
        return (Double) objValue;
      }

      if (objValue instanceof BigDecimal) {
        return ((BigDecimal) objValue).doubleValue();
      }

    }

    return null;
  }

  public boolean checkEmpty(FiCol fiCol) {
    if (!containsKey(fiCol.toString())) return true;

    Object objValue = get(fiCol.toString());

    if (objValue == null) return true;

    if (objValue instanceof String) {
      return FiString.isEmptyTrim((String) objValue);
    }

    return false;
  }

  /**
   * key-value setlerinden value'sı empty olan var mı kontrolü.
   *
   * @param fiColList
   * @return True : Boş var , False: Yok
   */
  public boolean checkEmptyValueInFkb(List<FiCol> fiColList) {
    for (FiCol fiCol : fiColList) {
      if (checkEmpty(fiCol)) return true;
    }
    return false;
  }

  public boolean containsKeyValueFi(FiCol fiCol, Object value) {
    if (containsKey(fiCol.toString())) {
      Object objValue = get(fiCol.toString());

      return objValue.equals(value);
    }
    return false;
  }

  public boolean containsKeyNotEmpty(FiCol fiCol) {
    if (containsKey(fiCol.toString())) {

      Object objValue = get(fiCol.toString());
      //Loghelper.get(getClass()).debug("Değer:"+objValue);

      if (objValue instanceof String) {
        //Loghelper.get(getClass()).debug("String");
        return !FiString.isEmpty((String) objValue);
      }


      if (objValue != null) {
        return true;
      }

      return false;
    }
    return false;
  }

  public boolean containsFiColFn(FiCol fiCol) {
    return containsKey(fiCol.getFcTxFieldName());
  }

  public void logParams() {
    Loghelper.get(getClass()).debug("FiKeyBean.logParams called");
    Loghelper.get(getClass()).debug(FiConsole.textFiKeytext(this));
  }

  public Object getByFiCol(FiCol fiCol) {
    return get(fiCol.getFcTxFieldName());
  }

  public Object getFicValue(FiCol fiCol) {
    return getByFiCol(fiCol);
  }

  public Object getFimValue(FiMeta fiCol) {
    return getFieldValue(fiCol.getFtTxKey());
  }

  public String getFimValueAsString(FiMeta fiCol) {
    return getValueAsString(fiCol.getFtTxKey());
  }

  public String getFkcValueAsString(FiKeybean fkbCol) {
    return getValueAsString(fkbCol.getFnm());
  }

  public String getFimValueAsStringNtn(FiMeta fiCol) {
    return getValueAsStringNtn(fiCol.getFtTxKey());
  }

  private Object getFieldValue(String txKey) {
    return get(txKey);
  }

  public String getTos(Object fiCol) {
    if (fiCol == null) return null;

    return get(fiCol.toString());
  }

  public Object getFiCol(FiCol fiCol) {
    return get(fiCol.getFcTxFieldName());
  }

  public FiListString getFullKeys() {
    FiListString fiListString = new FiListString();
    for (Map.Entry<String, String> entry : entrySet()) {
      //System.out.println(entry.getKey() + "/" + entry.getValue());
      if (!FiObjects.isEmpty(entry.getValue())) {
        fiListString.add(entry.getKey());
      }
    }
    return fiListString;
  }

  public void addFieldDb(FiCol fiCol, String value) {
    addField(fiCol.getTxDbFieldNameOrFieldName(), value);
  }

  public void addFieldBy(FiCol fiCol, String value) {
    addField(fiCol.getFcTxFieldName(), value);
  }

  public void addFieldBy(FiMeta fiMeta, String value) {
    addField(fiMeta.getFtTxKey(), value);
  }

  public void addFieldFic(FiCol fiCol, String value) {
    addField(fiCol.getFcTxFieldName(), value);
  }

  public void addFic(FiCol fiCol, String value) {
    addField(fiCol.getFcTxFieldName(), value);
  }

  public void addField(String txKey, String value) {
    add(txKey, value);
  }

  public void addFicIfNotExist(FiCol fiCol, String value) {
    if (fiCol == null || FiString.isEmpty(fiCol.getFcTxFieldName())) return;

    addFieldIfNotExist(fiCol.getFcTxFieldName(), value);
  }

  public void addFieldIfNotExist(String txKey, String value) {
    if (!containsKey(txKey)) {
      add(txKey, value);
    }
  }

//  public void addFieldBy(FiMeta fiMeta, Object value) {
//    addField(fiMeta.getTxKey(), value);
//  }

  public void addFim(FiMeta fiMeta, String value) {
    addField(fiMeta.getTxKey(), value);
  }

  /**
   * getFieldName
   *
   * @return
   */
  public String getFnm() {
    return getFimValueAsString(FimFiCol.fcTxFieldName());
  }

  public String getFicValueAsString(FiCol fiCol) {
    return getValueAsString(fiCol.getFcTxFieldName());
  }


}
