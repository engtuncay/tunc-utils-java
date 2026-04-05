package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.ficols.FimFiCol;
import ozpasyazilim.utils.fidborm.IFiTableMeta;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.table.FiCol;

import java.math.BigDecimal;
import java.util.*;

/**
 * Map [String,Object] türünde HashMap tipidir.
 * <p>
 * Kullanımları :
 * <p>
 * Sql Sorgularında named parametrelere bind etmek için kullanılır.
 * <p>
 * Bir nevi key-object tipinde array dir.
 */
public class FkbMap extends LinkedHashMap<String, FiKeybean> {

  /**
   * FiCol olarak eklenenleri saklamak için
   */
  List<FiCol> listFiCol;

  /**
   * sorgular için alanların ilgili olduğu tabloyu gösterir
   */
  String txTableName;

  IFiTableMeta iFiTableMeta;

  HashMap<String, FiCol> mapFiCol;


  public FkbMap() {
  }

  public FkbMap(Map<? extends String, FiKeybean> m) {
    super(m);
  }

  public FkbMap(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public FkbMap(int initialCapacity) {
    super(initialCapacity);
  }

  public FkbMap(int initialCapacity, float loadFactor, boolean accessOrder) {
    super(initialCapacity, loadFactor, accessOrder);
  }

  public static FkbMap bui() {
    return new FkbMap();
  }

  // üst sınıftan alamaz, üst sınıf FiKeyBean dönüyor
  public FkbMap buiPut(Object fieldName, FiKeybean value) {
    if (fieldName == null) return this;

    this.put(fieldName.toString(), value);
    return this;
  }

  public FkbMap buildPutIfNotNull(Object fieldName, FiKeybean value) {
    if (fieldName == null) return this;
    if (value == null) return this;

    this.put(fieldName.toString(), value);
    return this;
  }

  public FkbMap bind(Object key, FiKeybean value) {
    if (key == null) return this;
    put(key.toString(), value);
    return this;
  }

//  public FkbMap putIfNotEmpty(Object fieldName, Object value, Boolean addPercentage) {
//    if (fieldName == null) return this;
//    if (FiType.isEmptyObj(value)) return this;
//
//    if (FiBool.isTrue(addPercentage)) {
//      if (value instanceof String) {
//        this.put(fieldName.toString(), "%" + value + "%");
//      } else {
//        this.put(fieldName.toString(), value);
//      }
//    } else {
//      this.put(fieldName.toString(), value);
//    }
//    return this;
//  }

  /**
   * Key alanının Tos metodu ile ekleme yapar
   *
   * @param key
   * @param value
   * @return
   */
  public FkbMap putKeyTos(Object key, FiKeybean value) {
    this.put(key.toString(), value);
    return this;
  }

  public FkbMap addFiCol(FiCol fiCol, FiKeybean value) {
    this.put(fiCol.toString(), value);
    getListFiColInit().add(fiCol);
    getMapFiColInit().put(fiCol.getFcTxFieldName(), fiCol);
    return this;
  }

  public FkbMap putField(FiCol fiCol, FiKeybean value) {
    this.put(fiCol.getFcTxFieldName(), value);
    return this;
  }

//  public Integer getFullParamCount() {
//    Collection<Object> values = this.values();
//    Integer lnCount = 0;
//    for (Object value : values) {
//      if (value != null) lnCount++;
//    }
//    return lnCount;
//  }

  public void add(Object field, FiKeybean value) {
    this.put(field.toString(), value);
  }

//  public void addWithPercentage(Object field, Object value) {
//    if (value instanceof String) {
//      String txValue = (String) value;
//      if (!FiString.isEmptyTrim(txValue)) {
//        this.put(field.toString(), value = "%" + value.toString() + "%");
//      }
//    }
//  }

  public Object getAsObj(Object txKey) {
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

  public Integer getFicAsIntOrMinusOne(FiCol fiCol) {
    return FiNumber.orMinusOne(getFicAsInt(fiCol));
  }

  public Integer getFicAsInt(FiCol fiCol) {

    if (fiCol == null) return null;

    return getAsInt(fiCol.getFcTxFieldName());
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

  public Double getFicAsDoubleOrZero(FiCol fiCol) {
    return FiNumber.orZero(getFicAsDouble(fiCol));
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


  public Date getFicAsDate(FiCol fiCol) {
    if (fiCol == null || FiString.isEmpty(fiCol.getFcTxFieldName())) return null;

    if (containsKey(fiCol.getFcTxFieldName())) {
      //return (Date) get(fiCol.getFieldName());
      Object objValue = get(fiCol.getFcTxFieldName());

      //Loghelper.get(getClass()).debug("Obje Türü" + objValue.getClass().getName());

      if (objValue == null) return null;

      if (objValue instanceof Date) {
        return (Date) objValue;
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

//  public void logParams() {
//    Loghelper.get(getClass()).debug("FiKeyBean.logParams called");
//    Loghelper.get(getClass()).debug(FiConsole.textFiKeyBean(this));
//  }

  public void logFiCols() {

    Loghelper.get(getClass()).debug("FiKeyBean listFiCol elemanları");
    for (FiCol fiCol : getListFiColInit()) {
      Loghelper.get(getClass()).debug(fiCol.toString());
    }

  }

  public List<FiCol> getListFiCol() {
    return listFiCol;
  }

  public List<FiCol> getListFiColInit() {
    if (listFiCol == null) {
      listFiCol = new ArrayList<>();
    }
    return listFiCol;
  }

  public void setListFiCol(List<FiCol> listFiCol) {
    this.listFiCol = listFiCol;
  }

  public String getCombineTireByFiCol(FiCol fiCol, FiCol fiCol2) {
    return FiString.orEmpty(get(fiCol.getFcTxFieldName())) + "-" + FiString.orEmpty(get(fiCol2.getFcTxFieldName()));
  }

  public Object getFimValue(FiMeta fiCol) {
    return getFieldValue(fiCol.getFtTxKey());
  }

  public String getFimValueAsString(FiMeta fiCol) {
    return getValueAsString(fiCol.getFtTxKey());
  }

  public String getFkcValueAsString(FkbMap fkbCol) {
    return getValueAsString(fkbCol.getFnm());
  }

  public String getFimValueAsStringNtn(FiMeta fiCol) {
    return getValueAsStringNtn(fiCol.getFtTxKey());
  }

  private Object getFieldValue(String txKey) {
    return get(txKey);
  }

  public String getTxTableName() {
    return txTableName;
  }

  public void setTxTableName(String txTableName) {
    this.txTableName = txTableName;
  }

  public IFiTableMeta getiFiTableMeta() {
    return iFiTableMeta;
  }

  public void setiFiTableMeta(IFiTableMeta iFiTableMeta) {
    this.iFiTableMeta = iFiTableMeta;
  }

  public void appendFiCol(FiCol fiCol) {
    getListFiColInit().add(fiCol);
    getMapFiColInit().put(fiCol.getFcTxFieldName(), fiCol);
  }

  @Override
  public FiKeybean get(Object key) {
    return super.get(key);
  }

  public Optional<Class<?>> getAsClass(Object key) {
    Object value = get(key);
    if (value instanceof Class<?>) {
      return Optional.of((Class<?>) value);
    }
    return Optional.empty();
  }


  public HashMap<String, FiCol> getMapFiCol() {
    return mapFiCol;
  }

  public HashMap<String, FiCol> getMapFiColInit() {
    if (mapFiCol == null) {
      mapFiCol = new LinkedHashMap<>();
    }
    return mapFiCol;
  }

  public void setMapFiCol(LinkedHashMap<String, FiCol> mapFiCol) {
    this.mapFiCol = mapFiCol;
  }

  public Object getTos(Object fiCol) {
    if (fiCol == null) return null;

    return get(fiCol.toString());
  }

  public Object getFicVal(FiCol fiCol) {
    return get(fiCol.getFcTxFieldName());
  }

//  public FiListString getFullKeys() {
//    FiListString fiListString = new FiListString();
//    for (Map.Entry<String, Object> entry : entrySet()) {
//      //System.out.println(entry.getKey() + "/" + entry.getValue());
//      if (!FiObjects.isEmpty(entry.getValue())) {
//        fiListString.add(entry.getKey());
//      }
//    }
//    return fiListString;
//  }

  public void addFieldDb(FiCol fiCol, FiKeybean value) {
    addField(fiCol.getTxDbFieldNameOrFieldName(), value);
  }

  public void addFieldBy(FiCol fiCol, FiKeybean value) {
    addField(fiCol.getFcTxFieldName(), value);
  }

  public void addFieldBy(FiMeta fiMeta, FiKeybean value) {
    addField(fiMeta.getFtTxKey(), value);
  }

  public void addFieldFic(FiCol fiCol, FiKeybean value) {
    addField(fiCol.getFcTxFieldName(), value);
  }

  public void addFic(FiCol fiCol, FiKeybean value) {
    addField(fiCol.getFcTxFieldName(), value);
  }

  public void addField(String txKey, FiKeybean value) {
    add(txKey, value);
  }

  public void addFicIfNotExist(FiCol fiCol, FiKeybean value) {
    if (fiCol == null || FiString.isEmpty(fiCol.getFcTxFieldName())) return;

    addFieldIfNotExist(fiCol.getFcTxFieldName(), value);
  }

  public void addFieldIfNotExist(String txKey, FiKeybean value) {
    if (!containsKey(txKey)) {
      add(txKey, value);
    }
  }

//  public void addFieldBy(FiMeta fiMeta, Object value) {
//    addField(fiMeta.getTxKey(), value);
//  }

  public void addFim(FiMeta fiMeta, FiKeybean value) {
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

  public String getFicAsString(FiCol fiCol) {
    return getValueAsString(fiCol.getFcTxFieldName());
  }
}