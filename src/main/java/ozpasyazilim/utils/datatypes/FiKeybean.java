
package ozpasyazilim.utils.datatypes;

import javafx.beans.property.StringProperty;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.ficols.FimFiCol;
import ozpasyazilim.utils.fidborm.FiReflectClass;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.fidborm.Fiqt;
import ozpasyazilim.utils.fidborm.IFiTableMeta;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.table.FiCol;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Map [String,Object] türünde HashMap tipidir.
 * <p>
 * Kullanımları :
 * <p>
 * Sql Sorgularında named parametrelere bind etmek için kullanılır.
 * <p>
 * Bir nevi key-object tipinde array dir.
 */
public class FiKeybean extends LinkedHashMap<String, Object> {

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


  public FiKeybean() {
  }

  public FiKeybean(Map<? extends String, ?> m) {
    super(m);
  }

  public FiKeybean(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public FiKeybean(int initialCapacity) {
    super(initialCapacity);
  }

  public FiKeybean(int initialCapacity, float loadFactor, boolean accessOrder) {
    super(initialCapacity, loadFactor, accessOrder);
  }

  public static FiKeybean bui() {
    return new FiKeybean();
  }

  // üst sınıftan alamaz, üst sınıf FiKeyBean dönüyor
  public FiKeybean buiPut(Object fieldName, Object value) {
    if (fieldName == null) return this;

    this.put(fieldName.toString(), value);
    return this;
  }

  public FiKeybean buildPutIfNotNull(Object fieldName, Object value) {
    if (fieldName == null) return this;
    if (value == null) return this;

    this.put(fieldName.toString(), value);
    return this;
  }

  public FiKeybean bind(Object key, Object value) {
    if (key == null) return this;
    put(key.toString(), value);
    return this;
  }

  public FiKeybean putIfNotEmpty(Object fieldName, Object value, Boolean addPercentage) {
    if (fieldName == null) return this;
    if (FiType.isEmptyObj(value)) return this;

    if (FiBool.isTrue(addPercentage)) {
      if (value instanceof String) {
        this.put(fieldName.toString(), "%" + value + "%");
      } else {
        this.put(fieldName.toString(), value);
      }
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
  public FiKeybean putKeyTos(Object key, Object value) {
    this.put(key.toString(), value);
    return this;
  }

  public FiKeybean addFiCol(FiCol fiCol, Object value) {
    this.put(fiCol.toString(), value);
    getListFiColInit().add(fiCol);
    getMapFiColInit().put(fiCol.getFcTxFieldName(), fiCol);
    return this;
  }

  public FiKeybean putField(FiCol fiCol, Object value) {
    this.put(fiCol.getFcTxFieldName(), value);
    return this;
  }


  /**
   * String boş olmamalı
   * <p>
   * Collection size 0 olmamalı
   * <p>
   * Diger objeler null olmamalı
   * <p>
   * FiQuery kullan
   *
   * @param objKey
   * @param value
   * @param sql
   * @return
   */
  @Deprecated
  public FiKeybean bindAndActivateIfNotEmpty(Object objKey, Object value, StringProperty sql) {
    bindAndActivateIfNotEmpty(objKey, value, sql, null);
    return this;
  }

  /**
   * Use StringProperty
   *
   * @param objKey
   * @param value
   * @param sql
   */
  @Deprecated
  public void bindAndActivateIfNotEmpty(Object objKey, Object value, AtomicReference<String> sql) {
    if (value != null) {
      this.put(objKey.toString(), value);
      sql.set(Fiqt.activateOptParamMain(sql.get(), objKey.toString()));
    }
  }

  /**
   * String boş olmamalı
   * <p>
   * Collection size 0 olmamalı
   * <p>
   * Diger objeler null olmamalı
   * <p>
   * Use FiQuery
   *
   * @param objKey
   * @param value
   * @param sqlProp
   * @param addPercentage
   */
  @Deprecated
  public FiKeybean bindAndActivateIfNotEmpty(Object objKey, Object value, StringProperty sqlProp, Boolean addPercentage) {
    if (value != null) {
      if (value instanceof String) {
        if (FiString.isEmpty((String) value)) {
          Loghelper.get(getClass()).debug("Aktive edilmedi Param:" + objKey.toString());
          return this;
        }
        if (FiBool.isTrue(addPercentage)) value = "%" + value.toString() + "%";
      }

      if (value instanceof Collection) {
        if (FiCollection.isEmpty((Collection) value)) return this;
      }

//			Loghelper.get(getClass()).debug("Aktive edildi Param:" + objKey.toString());
      this.put(objKey.toString(), value);
      Fiqt.activateAndUpdateOptParam(sqlProp, objKey.toString());

    } else {
//			Loghelper.get(getClass()).debug("Aktive edilmedi Param:" + objKey.toString());
    }
    return this;
  }

  /**
   * Generate FiKeybean From Entity Fields (include not null fields and transient)
   *
   * @param entity
   * @param clazz
   * @return
   */
  public FiKeybean genFkbFromEntity(Object entity, Class clazz) {

    FiKeybean fiKeyBean = new FiKeybean();
    Field[] fields = clazz.getDeclaredFields(); // returns all members including private members but not inherited members.

    for (Field field : fields) {

//			if (field.isAnnotationPresent(Transient.class)) continue;
//			if (field.isAnnotationPresent(FiTransient.class)) continue;
      // Static alanlar alınmaz
      if (Modifier.isStatic(field.getModifiers())) continue;

      Object fieldValue = FiReflection.getProperty(entity, field.getName());

      if (fieldValue != null) {
        FiField fiField = FiReflectClass.setupFiFieldBasic(field, null);

        if (FiBool.isTrue(fiField.getFcBoFilterLike())) {
          String txValue = (String) fieldValue;
          txValue = "%" + txValue + "%";
          fiKeyBean.add(fiField.getFcTxDbField(), txValue);
        } else {
          fiKeyBean.add(fiField.getFcTxDbField(), fieldValue);
        }


      }

    }

    return fiKeyBean;
  }

  public Integer getFullParamCount() {
    Collection<Object> values = this.values();
    Integer lnCount = 0;
    for (Object value : values) {
      if (value != null) lnCount++;
    }
    return lnCount;
  }

  public void add(Object field, Object value) {
    this.put(field.toString(), value);
  }

  public void addWithPercentage(Object field, Object value) {
    if (value instanceof String) {
      String txValue = (String) value;
      if (!FiString.isEmptyTrim(txValue)) {
        this.put(field.toString(), value = "%" + value.toString() + "%");
      }
    }
  }

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

  public Boolean getFicAsBool(FiCol fiCol) {
    if (fiCol == null || FiString.isEmpty(fiCol.getFcTxFieldName())) return null;

    if (containsKey(fiCol.getFcTxFieldName())) {
      if (get(fiCol.getFcTxFieldName()) instanceof Boolean) {
        return (Boolean) get(fiCol.getFcTxFieldName());
      }
    }

    return null;
  }

  public Boolean getAsBool(String txKey) {
    return getValueAsBool(txKey);
  }

  public Boolean getValueAsBool(String txKey) {
    if (FiString.isEmpty(txKey)) return null;

    if (containsKey(txKey)) {
      if (get(txKey) instanceof Boolean) {
        return (Boolean) get(txKey);
      }
    }

    return null;
  }

  public Boolean getValueAsBoolV2(String txKey) {
    if (FiString.isEmpty(txKey)) return null;

    if (containsKey(txKey)) {
      if (get(txKey) instanceof Boolean) {
        return (Boolean) get(txKey);
      }
      if (get(txKey) instanceof Integer) {
        Integer intValue = (Integer) get(txKey);
        return FiBool.intToBool(intValue);
      }
      if (get(txKey) instanceof String) {
        String txValue = (String) get(txKey);
        return FiString.stringToBool(txValue);
      }
    }

    return null;
  }

  public int getFimValueAsBoolBit(FiMeta fiMeta) {
    return getValAsBoolBit(fiMeta.getFtTxKey());
  }

  public int getValAsBoolBit(String txKey) {
    if (FiString.isEmpty(txKey)) return -1;

    if (containsKey(txKey) && get(txKey) instanceof Boolean) {
      return ((Boolean) get(txKey)) ? 1 : 0;
    }

    return -1;
  }

  public int getValAsBoolBitV2(String txKey) {
    if (FiString.isEmpty(txKey)) return -1;

    Boolean valueAsBoolV2 = getValueAsBoolV2(txKey);

    if (valueAsBoolV2 == null) return -1;

    return valueAsBoolV2 ? 1 : 0;
  }


  public void copyParamByFiColOrEmptyStr(FiCol fiColFrom, FiCol fiColTo) {
    putKeyTos(fiColTo, getOrDefault(fiColFrom.toString(), ""));
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
    Loghelper.get(getClass()).debug(FiConsole.textFiKeyBean(this));
  }

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
  public Object get(Object key) {
    return super.get(key);
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

  public Object getFiCol(FiCol fiCol) {
    return get(fiCol.getFcTxFieldName());
  }

  public FiListString getFullKeys() {
    FiListString fiListString = new FiListString();
    for (Map.Entry<String, Object> entry : entrySet()) {
      //System.out.println(entry.getKey() + "/" + entry.getValue());
      if (!FiObjects.isEmpty(entry.getValue())) {
        fiListString.add(entry.getKey());
      }
    }
    return fiListString;
  }

  public void addFieldDb(FiCol fiCol, Object value) {
    addField(fiCol.getTxDbFieldNameOrFieldName(), value);
  }

  public void addFieldBy(FiCol fiCol, Object value) {
    addField(fiCol.getFcTxFieldName(), value);
  }

  public void addFieldBy(FiMeta fiMeta, Object value) {
    addField(fiMeta.getFtTxKey(), value);
  }

  public void addFieldFic(FiCol fiCol, Object value) {
    addField(fiCol.getFcTxFieldName(), value);
  }

  public void addFic(FiCol fiCol, Object value) {
    addField(fiCol.getFcTxFieldName(), value);
  }

  public void addField(String txKey, Object value) {
    add(txKey, value);
  }

  public void addFicIfNotExist(FiCol fiCol, Object value) {
    if (fiCol == null || FiString.isEmpty(fiCol.getFcTxFieldName())) return;

    addFieldIfNotExist(fiCol.getFcTxFieldName(), value);
  }

  public void addFieldIfNotExist(String txKey, Object value) {
    if (!containsKey(txKey)) {
      add(txKey, value);
    }
  }

//  public void addFieldBy(FiMeta fiMeta, Object value) {
//    addField(fiMeta.getTxKey(), value);
//  }

  public void addFim(FiMeta fiMeta, Object value) {
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