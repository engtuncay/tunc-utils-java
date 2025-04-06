
package ozpasyazilim.utils.datatypes;

import javafx.beans.property.StringProperty;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.fidborm.FiReflectClass;
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
 * Map [String,Entity] türünde HashMap tipidir.
 */
public class FiKeyEntity<E> extends LinkedHashMap<String, E> {

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


    public FiKeyEntity() {
    }

    public FiKeyEntity(Map<? extends String, E> m) {
        super(m);
    }

    public FiKeyEntity(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public FiKeyEntity(int initialCapacity) {
        super(initialCapacity);
    }

    public FiKeyEntity(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public static FiKeyEntity bui() {
        return new FiKeyEntity();
    }

    // üst sınıftan alamaz, üst sınıf FiKeyBean dönüyor
    public FiKeyEntity buiPut(Object fieldName, E value) {
        if (fieldName == null) return this;

        this.put(fieldName.toString(), value);
        return this;
    }

    public FiKeyEntity buildPutIfNotNull(Object fieldName, E value) {
        if (fieldName == null) return this;
        if (value == null) return this;

        this.put(fieldName.toString(), value);
        return this;
    }

    public FiKeyEntity bind(Object key, E value) {
        if (key == null) return this;
        put(key.toString(), value);
        return this;
    }

//    public FiKeyEntity putIfNotEmpty(Object fieldName, E value, Boolean addPercentage) {
//        if (fieldName == null) return this;
//        if (FiType.isEmptyGen(value)) return this;
//
//        if (FiBool.isTrue(addPercentage)) {
//            if (value instanceof String) {
//                this.put(fieldName.toString(), "%" + value + "%");
//            } else {
//                this.put(fieldName.toString(), value);
//            }
//        } else {
//            this.put(fieldName.toString(), value);
//        }
//        return this;
//    }

    /**
     * Key alanının Tos metodu ile ekleme yapar
     *
     * @param key
     * @param value
     * @return
     */
    public FiKeyEntity putKeyTos(Object key, E value) {
        this.put(key.toString(), value);
        return this;
    }

    public FiKeyEntity putFiCol(FiCol fiCol, E value) {
        this.put(fiCol.toString(), value);
        getListFiColInit().add(fiCol);
        getMapFiColInit().put(fiCol.getOfcTxFieldName(), fiCol);
        return this;
    }

    public FiKeyEntity putField(FiCol fiCol, E value) {
        this.put(fiCol.getOfcTxFieldName(), value);
        return this;
    }


//    /**
//     * String boş olmamalı
//     * <p>
//     * Collection size 0 olmamalı
//     * <p>
//     * Diger objeler null olmamalı
//     * <p>
//     * FiQuery kullan
//     *
//     * @param objKey
//     * @param value
//     * @param sql
//     * @return
//     */
//    @Deprecated
//    public FiKeyEntity bindAndActivateIfNotEmpty(Object objKey, Object value, StringProperty sql) {
//        bindAndActivateIfNotEmpty(objKey, value, sql, null);
//        return this;
//    }

    /**
     * Use StringProperty
     *
     * @param objKey
     * @param value
     * @param sql
     */
    @Deprecated
    public void bindAndActivateIfNotEmpty(Object objKey, E value, AtomicReference<String> sql) {
        if (value != null) {
            this.put(objKey.toString(), value);
            sql.set(Fiqt.activateOptParamMain(sql.get(), objKey.toString()));
        }
    }

//    /**
//     * String boş olmamalı
//     * <p>
//     * Collection size 0 olmamalı
//     * <p>
//     * Diger objeler null olmamalı
//     * <p>
//     * Use FiQuery
//     *
//     * @param objKey
//     * @param value
//     * @param sqlProp
//     * @param addPercentage
//     */
//    @Deprecated
//    public FiKeyEntity bindAndActivateIfNotEmpty(Object objKey, E value, StringProperty sqlProp, Boolean addPercentage) {
//        if (value != null) {
//            if (value instanceof String) {
//                if (FiString.isEmpty((String) value)) {
//                    Loghelper.get(getClass()).debug("Aktive edilmedi Param:" + objKey.toString());
//                    return this;
//                }
//                if (FiBool.isTrue(addPercentage)) value = "%" + value.toString() + "%";
//            }
//
//            if (value instanceof Collection) {
//                if (FiCollection.isEmpty((Collection) value)) return this;
//            }
//
////			Loghelper.get(getClass()).debug("Aktive edildi Param:" + objKey.toString());
//            this.put(objKey.toString(), value);
//            Fiqt.activateAndUpdateOptParam(sqlProp, objKey.toString());
//
//        } else {
////			Loghelper.get(getClass()).debug("Aktive edilmedi Param:" + objKey.toString());
//        }
//        return this;
//    }

    /**
     * Generate FiKeybean From Entity Fields (include not null fields and transient)
     *
     * @param entity
     * @param clazz
     * @return
     */
    public FiKeyEntity genFkbFromEntity(Object entity, Class clazz) {

        FiKeyEntity fiKeyBean = new FiKeyEntity();
        Field[] fields = clazz.getDeclaredFields(); // returns all members including private members but not inherited members.

        for (Field field : fields) {

//			if (field.isAnnotationPresent(Transient.class)) continue;
//			if (field.isAnnotationPresent(FiTransient.class)) continue;
            // Static alanlar alınmaz
            if (Modifier.isStatic(field.getModifiers())) continue;

            Object fieldValue = FiReflection.getProperty(entity, field.getName());

            if (fieldValue != null) {
                FiField fiField = FiReflectClass.setupFiFieldBasic(field, null);

                if (FiBool.isTrue(fiField.getOfcBoFilterLike())) {
                    String txValue = (String) fieldValue;
                    txValue = "%" + txValue + "%";
                    fiKeyBean.add(fiField.getOfcTxDbField(), txValue);
                } else {
                    fiKeyBean.add(fiField.getOfcTxDbField(), fieldValue);
                }


            }

        }

        return fiKeyBean;
    }

//    public Integer getFullParamCount() {
//        Collection<Object> values = this.values();
//        Integer lnCount = 0;
//        for (Object value : values) {
//            if (value != null) lnCount++;
//        }
//        return lnCount;
//    }

    public void add(Object field, E value) {
        this.put(field.toString(), value);
    }

//    public void addWithPercentage(Object field, Object value) {
//        if (value instanceof String) {
//            String txValue = (String) value;
//            if (!FiString.isEmptyTrim(txValue)) {
//                this.put(field.toString(), value = "%" + value.toString() + "%");
//            }
//        }
//    }

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
        if (fiCol == null || FiString.isEmpty(fiCol.getOfcTxFieldName())) return null;

        if (containsKey(fiCol.getOfcTxFieldName())) {
            Object objValue = get(fiCol.getOfcTxFieldName());

            if (objValue == null) return null;

            return (String) objValue;
        }

        return null;
    }

    public Integer getAsIntOrMinusOne(FiCol fiCol) {
        return FiNumber.orMinusOne(getAsInt(fiCol));
    }

    public Integer getAsInt(FiCol fiCol) {

        if (fiCol == null || FiString.isEmpty(fiCol.getOfcTxFieldName())) return null;

        if (containsKey(fiCol.getOfcTxFieldName())) {
            Object objValue = get(fiCol.getOfcTxFieldName());

            if (objValue == null) return null;

            if (objValue instanceof Integer) {
                return (Integer) objValue;
            }

        }

        return null;
    }

    public Double getAsDouble(FiCol fiCol) {

        if (fiCol == null || FiString.isEmpty(fiCol.getOfcTxFieldName())) return null;

        if (containsKey(fiCol.getOfcTxFieldName())) {
            Object objValue = get(fiCol.getOfcTxFieldName());

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

    public Double getAsDoubleOrZero(IFiCol fiCol) {
        return FiNumber.orZero(getAsDouble(fiCol));
    }


    public Double getAsDouble(IFiCol fiCol) {

        if (fiCol == null || FiString.isEmpty(fiCol.getOfcTxFieldName())) return null;

        if (containsKey(fiCol.getOfcTxFieldName())) {
            Object objValue = get(fiCol.getOfcTxFieldName());

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

    public Date getAsDate(FiCol fiCol) {
        if (fiCol == null || FiString.isEmpty(fiCol.getOfcTxFieldName())) return null;

        if (containsKey(fiCol.getOfcTxFieldName())) {
            //return (Date) get(fiCol.getFieldName());
            Object objValue = get(fiCol.getOfcTxFieldName());

            if (objValue == null) return null;

            if (objValue instanceof Date) {
                return (Date) objValue;
            }

        }

        return null;
    }

    public Boolean getAsBoolean(FiCol fiCol) {
        if (fiCol == null || FiString.isEmpty(fiCol.getOfcTxFieldName())) return null;

        if (containsKey(fiCol.getOfcTxFieldName())) {
            if (get(fiCol.getOfcTxFieldName()) instanceof Boolean) {
                return (Boolean) get(fiCol.getOfcTxFieldName());
            }
        }

        return null;
    }

    public Boolean getAsBoolean(String key) {
        if (FiString.isEmpty(key)) return null;

        if (containsKey(key)) {
            if (get(key) instanceof Boolean) {
                return (Boolean) get(key);
            }
        }

        return null;
    }


//    public void copyParamByFiColOrEmptyStr(FiCol fiColFrom, FiCol fiColTo) {
//        putKeyTos(fiColTo, getOrDefault(fiColFrom.toString(), ""));
//    }

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

    public void logParams() {
        Loghelper.get(getClass()).debug("FiKeyBean.logParams called");
        Loghelper.get(getClass()).debug(FiConsole.textObjectFields(this));
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
        return FiString.orEmpty(get(fiCol.getOfcTxFieldName())) + "-" + FiString.orEmpty(get(fiCol2.getOfcTxFieldName()));
    }

    public Object getByFiCol(FiCol fiCol) {
        return get(fiCol.getOfcTxFieldName());
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
        getMapFiColInit().put(fiCol.getOfcTxFieldName(), fiCol);
    }

    @Override
    public E get(Object key) {
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
        return get(fiCol.getOfcTxFieldName());
    }

    public FiListString getFullKeys() {
        FiListString fiListString = new FiListString();
        for (Map.Entry<String, E> entry : entrySet()) {
            //System.out.println(entry.getKey() + "/" + entry.getValue());
            if (!FiObjects.isEmpty(entry.getValue())) {
                fiListString.add(entry.getKey());
            }
        }
        return fiListString;
    }
}