package ozpasyazilim.utils.core;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.datatypes.FkbList;
import ozpasyazilim.utils.datatypes.FiListKeyString;
import ozpasyazilim.utils.datatypes.FiKeyString;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.table.FiCol;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiFunction;

public class FiConsole {

    private static Set<String> logSingleSet;
    private static Map<String, Integer> logCountMap;

    public static <T> void printTable(List<T> listobjects, BiFunction<T, Integer, String> funcPrint,
                                      Integer numberofcol) {

        for (Iterator iterator = listobjects.iterator(); iterator.hasNext(); ) {

            T t = (T) iterator.next();

            for (int i = 1; i <= numberofcol; i++) {
                String colvalue = funcPrint.apply(t, i);
                System.out.print(colvalue != null ? colvalue + "\t" : "null" + "\t");
            }
            System.out.println("");

        }

    }

    public static <T> void printTable(T t, BiFunction<T, Integer, String> funcPrint, Integer numberofcol) {

        for (int i = 1; i <= numberofcol; i++) {
            String colvalue = funcPrint.apply(t, i);
            System.out.print(colvalue != null ? colvalue + "\t" : "null" + "\t");
        }
        System.out.println("");

    }

    public static void printTable(List<String> listString) {

        for (Iterator iterator = listString.iterator(); iterator.hasNext(); ) {
            String value = (String) iterator.next();
            System.out.println(value != null ? value : "null" + "\t");
        }

    }

    public static <E> void printList(List<E> list) {

        for (int i = 0; i < list.size(); i++) {
            E e = list.get(i);
            if (e == null) {
                //System.out.println("Satır " + i +" : null object ");
                Loghelper.get(FiConsole.class).debug("Satır" + i + " : null object");
                continue;
            }
            Loghelper.get(FiConsole.class).debug("Satır" + i + " :" + e.toString());

        }
    }

    public static void printSet(Set<String> setData) {

        for (String setDatum : setData) {
            System.out.println(setDatum);
        }

    }

    public static void printListOfObjects(List<Object> list) {

        for (int i = 0; i < list.size(); i++) {
            Object e = list.get(i);
            if (e == null) {
                //System.out.println("Satır " + i +" : null object ");
                Loghelper.get(FiConsole.class).debug("Satır" + i + " : null object -- Simple Type Name :" + e.getClass().getSimpleName());
                continue;
            }
            Loghelper.get(FiConsole.class).debug("Satır" + i + " :" + e.toString());

        }
    }

    public static <E> void debugListObjectsNotNull(List<E> list, Class clazz) {
        Loghelper.get(FiConsole.class).debug(String.format("List Detail (Not Null) - Debug Class: %s\n\n%s", clazz.getSimpleName(), textListObjectsNotNullFields(list)));
        //Loghelper.debugLog(clazz, String.format("List Detail (Not Null)\n\n%s", textListObjectsNotNullFields(list)));
    }

    public static <E> String textDebugListObjectsNotNull(List<E> list, Class clazz) {
        return String.format("List Detail (Not Null)\n\n%s", textListObjectsNotNullFields(list));
    }

    public static <E> void debugCollectionObjectsNotNull(Collection<E> collection, Class clazz) {
        Loghelper.debugLog(clazz, String.format("List Detail (Not Null)\n\n%s", textCollection(collection)));
    }

    public static void debugMapNotNull(Map map, Class clazz) {
        Loghelper.get(FiConsole.class).debug(String.format("Map Detail (Not Null) - Debug Class: %s\n\n%s", clazz.getSimpleName(), textMapNotNull(map)));
    }

    public static void debugListMap(FiListKeyString listMap, Class clazz, Boolean boShowNulls) {
        if (listMap == null) {
            Loghelper.debugLog(clazz, String.format("Map Null"));
            return;
        }
        for (FiKeyString fiKeyString : listMap) {
            if (FiBool.isTrue(boShowNulls)) {
                Loghelper.debugLog(clazz, String.format("Map Detail (Not Null)\n\n%s", textFiKeyString(fiKeyString)));
            } else {
                Loghelper.debugLog(clazz, String.format("Map Detail (Not Null)\n\n%s", textMapNotNull(fiKeyString)));
            }

        }

    }

    public static <E> void debugStringArray(String[] arrString, Class clazz) {
        Loghelper.get(FiConsole.class).debug(String.format("Array Detail \n\n%s - Class:%s", textStringArray(arrString), clazz.getSimpleName()));
    }

    public static <E> void debugStringArray(String[] arrString) {
        Loghelper.get(FiConsole.class).debug(String.format("Array Detail \n\n%s", textStringArray(arrString)));
    }

    public static <E> void debugString(String arrString, Class clazz) {
        Loghelper.get(FiConsole.class).debug(String.format("%s - Class:%s", arrString, clazz.getSimpleName()));
    }

    public static <E> void debugStringList(List<String> listString, Class clazz) {
        Loghelper.debugLog(clazz, String.format("Array Detail \n\n%s", textCollectionOfString(listString)));
    }

    public static <E> void debugStringList(Collection<String> stringCollection) {
        Loghelper.debugLog(FiConsole.class, String.format("Array Detail \n\n%s", textCollectionOfString(stringCollection)));
    }

    public static <E> void debugListObjectsToString(List<E> list, Class clazz) {
        Loghelper.debugLog(clazz, String.format("List Detail (Not Null)\n\n%s", textListObjectsToString(list)));
    }

    public static <E> void printListObjectsNotNull(List<E> list) {
        System.out.println(textListObjectsNotNullFields(list));
    }

    public static <E> String textListObjectsNotNullFields(List<E> list) {

        StringBuilder result = new StringBuilder("");
        if (list == null || list.size() == 0) {
            result.append("Boş Liste");
            return result.toString();
        }

        result.append("Print List with Detail : Size:" + list.size() + "\n");
        for (E e : list) {
            result.append(textObjectFieldsPlain(e, null) + "\n");
        }

        return result.toString();

    }


    /**
     * Objelerin not null olan alanlarını gösterir
     *
     * @param collection
     * @return
     * @param <E>
     */
    public static <E> String textCollection(Collection<E> collection) {
        return textCollectionOfObject(collection, false);
    }

    public static <E> String textCollectionOfObject(Collection<E> collection, Boolean boShowNullFields) {

        StringBuilder result = new StringBuilder("");

        if (FiCollection.isEmpty(collection)) {
            result.append("Boş Liste");
            return result.toString();
        }

        result.append("Print Collection Objects(Not full Fields) Col.Size:").append(collection.size()).append("\n");

        for (E e : collection) {
            result.append(textObjectFieldsPlain(e, boShowNullFields)).append("\n");
        }

        return result.toString();
    }

    public static <E, T> String textMapNotNull(Map<E, T> map) {

        StringBuilder result = new StringBuilder("");

        if (map == null || map.size() == 0) {
            result.append("Boş Map");
            return result.toString();
        }

        result.append("Print Map with Detail : Size:" + map.size() + "\n");
        for (E e : map.keySet()) {
            result.append("Key:" + e.toString() + "\n");
            result.append("Value:" + FiString.toStringOrEmpty(map.get(e)) + "\n");
        }

        return result.toString();

    }

    public static String textStringArray(String[] list) {

        StringBuilder result = new StringBuilder("");

        if (list == null || list.length == 0) {
            result.append("Boş Dizi");
            return result.toString();
        }

        result.append("Print List with Detail : Size:" + list.length + "\n");

        for (String e : list) {
            if (e == null) {
                result.append("null\n");
            } else {
                result.append(e.toString()).append("\n");
            }
        }

        return result.toString();

    }

    public static String textCollectionOfString(Collection<String> list) {

        StringBuilder result = new StringBuilder("");

        if (list == null || list.size() == 0) {
            result.append("Boş Liste");
            return result.toString();
        }

        result.append("Print List with Detail : Size:" + list.size() + "\n");

        for (String e : list) {
            if (e == null) {
                result.append("null\n");
            } else {
                result.append(e).append("\n");
            }
        }

        return result.toString();
    }

    public static <E> String textListObjectsToString(List<E> list) {

        StringBuilder result = new StringBuilder("");
        if (list == null || list.size() == 0) {
            result.append("Boş Liste");
            return result.toString();
        }

        result.append("Print List with Detail : Size:" + list.size() + "\n");
        for (E e : list) {
            result.append(e.toString() + "\n");
        }

        return result.toString();

    }

    public static void printFieldsNotNullSingle(Object obj, Class clazz) {

        Boolean boExist = getLogSingleSet().contains(clazz.getSimpleName());

        if (!boExist) {
            Loghelper.debugLog(clazz, "FiConsole is Called");
            printObjectFieldsNotNull(obj);
            getLogSingleSet().add(clazz.getSimpleName());
        }


    }

    public static void printFields(Object obj) {
        if (obj == null) {
            System.out.println("Obje null");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {

            if (!Modifier.isStatic(f.getModifiers())) {

                f.setAccessible(true);

                Object value = null;

                try {
                    value = f.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    buffer.append("N/A");
                }

                buffer.append(f.getType().getName());
                buffer.append(" ");
                buffer.append(f.getName());
                buffer.append("=");
                buffer.append("" + value);
                buffer.append("\n");
            }
        }

        System.out.println(buffer.toString());
    }

    public static void printObjectFieldsNotNull(Object obj) {
        System.out.println(textObjectFieldsPlain(obj, false));
    }

    public static void printObjectFields(Object obj) {
        System.out.println(textObjectFieldsPlain(obj, true));
    }

    public static void debug(Object obj, Class clazz) {

        if (obj instanceof List) {
            debugListObjectsNotNull((List) obj, clazz);
            return;
        }

        Loghelper.debugLog(clazz, textObjectFieldsPlain(obj, null));
    }

    public static void debugObjectNotNull(Object obj, Class clazz) {
        Loghelper.debugLog(clazz, textObjectFieldsPlain(obj, false));
    }

    public static void debug(Object obj, Class clazz, Class clazzObj) {

        if (obj instanceof List) {
            debugListObjectsNotNull((List) obj, clazz);
            return;
        }

        Loghelper.debugLog(clazz, textObjectFieldsByClassMain(obj, false, clazzObj));
    }

    public static void debugWithNullFields(Object obj, Class clazz, Class clazzObj) {

        if (obj instanceof List) {
            debugListObjectsNotNull((List) obj, clazz);
            return;
        }

        Loghelper.get(clazz).debug(textObjectFieldsByClassMain(obj, true, clazzObj));
    }

    public static String getLogWithNullFields(Object obj, Class clazz, Class clazzObj) {

        if (obj instanceof List) {
            return textDebugListObjectsNotNull((List) obj, clazz);
        }

        return textObjectFieldsByClassMain(obj, true, clazzObj);
    }

    public static void debug(String message, Object obj) {
        Loghelper.get(getClassi()).debug(message);
        debug(obj);
    }

    private static Class<FiConsole> getClassi() {
        return FiConsole.class;
    }

    public static void debug(Object obj) {

        Loghelper.get(FiConsole.class).debug("FiConsole.debug method");

        if (obj == null) {
            Loghelper.debugLog(FiConsole.class, "Null Değişken");
            return;
        }

        if (obj instanceof List) {

            List list = (List) obj;

            if (list.size() > 0 && list.get(0) instanceof String) {
                debugStringList((List<String>) list);
                return;
            }

            debugListObjectsNotNull(list, FiConsole.class);
            return;
        }

        if (obj instanceof Collection) {
            debugCollectionObjectsNotNull((Collection) obj, FiConsole.class);
            return;
        }

        if (obj instanceof String[]) {
            debugStringArray((String[]) obj, FiConsole.class);
            return;
        }

        if (obj instanceof String) {
            debugString((String) obj, FiConsole.class);
            return;
        }

        if (obj instanceof HashMap) {
            debugMapNotNull((Map) obj, FiConsole.class);
        }

        Loghelper.debugLog(FiConsole.class, textObjectFieldsPlain(obj, null));
    }

    public static String textLogMain(Object obj) {

        if (obj == null) {
            return "Null Obje";
        }

        if (obj instanceof List) {

            List list = (List) obj;

            if (list.size() > 0 && list.get(0) instanceof String) {
                return String.format("Array Detail \n\n%s", textCollectionOfString((list)));
            }

            debugListObjectsNotNull(list, FiConsole.class);
            return String.format("List Detail (Not Null)\n\n%s", textListObjectsNotNullFields(list));
        }

        if (obj instanceof Collection) {
            Collection collection = (Collection) obj;
            return String.format("Collection Detail (Not Null Fields)\n\n%s", textCollection(collection));
        }

        if (obj instanceof String[]) {
            String[] stArray = (String[]) obj;
            return String.format("Array Detail \n\n%s", textStringArray(stArray));
        }

        if (obj instanceof String) {
            String stObj = (String) obj;
            return stObj;
        }

        if (obj instanceof HashMap) {
            HashMap map = (HashMap) obj;
            return String.format("Map Detail (Not Null)\n\n%s", textMapNotNull(map));
        }

        return textObjectFieldsPlain(obj, null);
    }

    public static String textObjectFieldsPlain(Object obj, Boolean boShowNull) {
        FiConsoleConfig fiConsoleConfig = new FiConsoleConfig();
        fiConsoleConfig.setBoShowNull(boShowNull);
        return textObjectFieldsMain(obj, fiConsoleConfig);
    }

    public static String textObjectFieldsOnlyValues(Object obj, Boolean boShowNull) {

        FiConsoleConfig fiConsoleConfig = new FiConsoleConfig();
        fiConsoleConfig.setBoShowNull(boShowNull);
        fiConsoleConfig.setBoTypeHide(true);
        fiConsoleConfig.setBoEqualSignHide(true);
        fiConsoleConfig.setBoFieldNameHide(true);

        return textObjectFieldsMain(obj, fiConsoleConfig);
    }

    /**
     * Without Equal Sign and Type
     *
     * @param obj
     * @param boShowNull
     * @return
     */
    public static String textObjectFieldsSimple(Object obj, Boolean boShowNull) {

        FiConsoleConfig fiConsoleConfig = new FiConsoleConfig();
        fiConsoleConfig.setBoShowNull(boShowNull);
        fiConsoleConfig.setBoEqualSignHide(true);
        fiConsoleConfig.setBoTypeHide(true);

        return textObjectFieldsMain(obj, fiConsoleConfig);
    }

    /**
     * Object alanlarının değerlerini gösterir.
     *
     * @param obj
     * @return
     */
    public static String textObjectFields(Object obj) {

        FiConsoleConfig fiConsoleConfig = new FiConsoleConfig();
        fiConsoleConfig.setBoShowNull(true);
        fiConsoleConfig.setBoEqualSignHide(true);
        fiConsoleConfig.setBoTypeHide(true);

        return textObjectFieldsMain(obj, fiConsoleConfig);
    }

    public static String textObjectFieldsNtn(Object obj) {

        FiConsoleConfig fiConsoleConfig = new FiConsoleConfig();
        fiConsoleConfig.setBoShowNull(false);
        fiConsoleConfig.setBoEqualSignHide(true);
        fiConsoleConfig.setBoTypeHide(true);

        return textObjectFieldsMain(obj, fiConsoleConfig);
    }

    public static String textObjectFieldsHp2ForExcel(Object obj, Boolean boShowNull) {

        FiConsoleConfig fiConsoleConfig = new FiConsoleConfig();
        fiConsoleConfig.setBoShowNull(boShowNull);
        fiConsoleConfig.setBoEqualSignHide(true);

        return textObjectFieldsMain(obj, fiConsoleConfig);
    }

    public static String textObjectFieldsMain(Object obj, FiConsoleConfig fiConsoleConfig) {

        if (fiConsoleConfig == null) fiConsoleConfig = new FiConsoleConfig();

        StringBuffer strBuffer = new StringBuffer();

        if (obj == null) {
            strBuffer.append("Obje null\n");
            return strBuffer.toString();
        }

        strBuffer.append("\n\n" + obj.getClass().getSimpleName() + " Sınıfının Objesinin Tanımlı Alanları " + (FiBool.isTrue(fiConsoleConfig.getBoShowNull()) ? "(With Null)" : "(Without Null)") + "\n\n");

        Field[] fields = obj.getClass().getDeclaredFields(); // declared idi

        for (Field f : fields) {

            if (!Modifier.isStatic(f.getModifiers())) {

                f.setAccessible(true);

                Object value = null;

                try {
                    value = f.get(obj);
                    if (value == null && !FiBool.isTrue(fiConsoleConfig.getBoShowNull())) {
                        continue;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    strBuffer.append("N/A");
                }

                if (!FiBool.isTrue(fiConsoleConfig.getBoTypeHide())) {
                    strBuffer.append(f.getType().getSimpleName());
                    strBuffer.append("\t");
                }

                if (!FiBool.isTrue(fiConsoleConfig.getBoFieldNameHide())) {
                    strBuffer.append(f.getName());
                    strBuffer.append("\t");
                }

                if (FiBool.isNotTrue(fiConsoleConfig.getBoEqualSignHide())) {
                    strBuffer.append("=\t");
                }

                strBuffer.append(value);
                strBuffer.append("\n");
            }
        }

        // System.out.println(strBuffer.toString());
        return strBuffer.toString();
    }

    public static String textObjectFieldsByClassMain(Object obj, Boolean boShowNullFields, Class clazz) {

        StringBuffer buffer = new StringBuffer();

        if (obj == null) {
            buffer.append("Obje null\n");
            return buffer.toString();
        }

        buffer.append("\n\n" + clazz.getSimpleName() + " Sınıfının Objesinin Tanımlı Alanları (Not Null)\n");

        Field[] fields = clazz.getDeclaredFields(); // declared idi

        for (Field f : fields) {

            if (!Modifier.isStatic(f.getModifiers())) {

                f.setAccessible(true);

                Object value = null;

                try {
                    value = f.get(obj);
                    if (value == null && !FiBool.isTrue(boShowNullFields)) continue;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    buffer.append("N/A");
                }

                buffer.append(f.getType().getSimpleName() + "\t");
                buffer.append(f.getName() + "\t");
                //buffer.append("=\t");
                buffer.append(value);
                buffer.append("\n");
            }
        }

//		System.out.println(buffer.toString());
        return buffer.toString();
    }


    public static String toString(Object result) {
        if (result == null) return "null";
        return result.toString();
    }

    public static void printNullCheck(String message, Object objectToCheck, Class clazz) {

        Loghelper.get(clazz).debug(message + " Null? : " + (objectToCheck == null ? "True" : "False"));

    }

    public static void printObjectDefiniton(Object item, String itemName) {
        printObjectDefiniton(item, itemName, FiConsole.class);
    }

    public static void printObjectDefiniton(Object item, String itemName, Class clazz) {

        if (item == null) {
            Loghelper.get(clazz).debug("Object Null");
            return;
        }

        Loghelper.get(clazz).debug(String.format("%s %s = %s", item.getClass().getSimpleName(), itemName, item.toString()));

    }

    public static void printObjectDefinitonLimityByClass(Object item, String itemName, Class clazz) {

        Integer lnExist = getLogCountMap().getOrDefault(clazz.getSimpleName(), 0);

        if (lnExist < getLnLimitLog()) {

            String simpleName = item.getClass().getSimpleName();

            if (item == null) {
                item = "null";
                simpleName = "N/A";
            }

            Loghelper.get(clazz).debug(String.format("%s %s = %s", simpleName, itemName, item.toString()));

            getLogCountMap().put(clazz.getSimpleName(), ++lnExist);

        }

    }

    private static Integer getLnLimitLog() {
        return 5;
    }

    public static void printBounds(Bounds bound) {

        if (bound == null) {
            Loghelper.get(FiConsole.class).debug("Bounds null");
            return;
        }

        Loghelper.get(FiConsole.class).debug(String.format("Min x : %.1f --- Max x : %.1f --- Min y : %.1f --- Max y : %.1f"
                , bound.getMinX(), bound.getMaxY(), bound.getMinY(), bound.getMaxY()));


    }

    public static void printBounds(Point2D point2D) {

        if (point2D == null) {
            Loghelper.get(FiConsole.class).debug("point2d null");
            return;
        }

        Loghelper.get(FiConsole.class).debug(String.format(" x : %.2f --- y : %.2f"
                , point2D.getX(), point2D.getY()));
    }

    public static Set<String> getLogSingleSet() {
        if (logSingleSet == null) {
            logSingleSet = new HashSet<>();
        }
        return logSingleSet;
    }

    public static void setLogSingleSet(Set<String> prmSetLogSingle) {
        logSingleSet = prmSetLogSingle;
    }

    public static Map<String, Integer> getLogCountMap() {
        if (logCountMap == null) {
            logCountMap = new HashMap<>();
        }
        return logCountMap;
    }

    public static void setLogCountMap(Map<String, Integer> logCountMap) {
        FiConsole.logCountMap = logCountMap;
    }

    public static <E> void printListObjectsNotNull(Set<E> setToPrint) {

        if (setToPrint == null || setToPrint.size() == 0) {
            System.out.println("Boş Liste");
            return;
        }

        System.out.println("Print List with Detail : Size:" + setToPrint.size());
        for (E entity : setToPrint) {
            printObjectFieldsNotNull(entity);
            System.out.println("");
        }

    }

    public static void printList(Integer[] intData) {
        for (int intDatum : intData) {
            Loghelper.get(FiConsole.class).debug("Data:" + intDatum);
        }
    }

    public static void printMap(Map<String, String> mapData) {
        System.out.println(textFiKeyString(mapData));
    }

    public static void printMap2(Map<String, Object> mapData) {
        System.out.println(textMapStringObject(mapData));
    }

    public static void printMapFi(FiKeyBean mapData) {
        System.out.println(textFiKeyBean(mapData));
    }

    public static String textFiMapString(Map<String, String> appMap) {
        return textFiKeyString(appMap);
    }

    public static String textFiKeyString(Map<String, String> appMap) {

        StringBuilder log = new StringBuilder("");
        for (Map.Entry<String, String> entry : appMap.entrySet()) {
            log.append(entry.getKey() + " : " + entry.getValue() + "\n"); //System.out.println(entry.getKey() + "/" + entry.getValue());
        }

        return log.toString();
    }

    public static String textMapStringObject(Map<String, Object> appMap) {

        StringBuilder log = new StringBuilder("");
        for (Map.Entry<String, Object> entry : appMap.entrySet()) {
            log.append(entry.getKey() + " : " + entry.getValue() + "\n");
        }
        return log.toString();
    }


    public static String textFiKeyBean(FiKeyBean fiKeyBean) {
        StringBuilder log = new StringBuilder("FiKeyBean İçeriği:");
        log.append("\n");
        for (Map.Entry<String, Object> entry : fiKeyBean.entrySet()) {
            log.append(entry.getKey()).append(" : ").append(FiString.ToStrOrEmpty(entry.getValue())).append("\n");
        }
        return log.toString();
    }

    public static String textFiCols(List<FiCol> listFiColInit) {
        StringBuilder log = new StringBuilder("FiCol List İçeriği:");
        log.append("\n");

        for (FiCol entry : listFiColInit) {
            log.append(entry.getOfcTxFieldName()).append(" : ").append(FiString.ToStrOrEmpty(entry.getColValue())).append("\n");
        }

        return log.toString();
    }

    public static void printStringCollection(Collection<String> strings) {
        System.out.println(textCollectionOfString(strings));
    }

    public static String textListFiKeyBean(FkbList fkbList) {
        StringBuilder sbOutput = new StringBuilder("");
        for (FiKeyBean fiKeyBean : fkbList) {
            sbOutput.append(textFiKeyBean(fiKeyBean) + "\n");
        }
        return sbOutput.toString();
    }

    public static void debugListFkb(List<FiKeyBean> listFkb) {
        if (listFkb == null) {
            Loghelper.get(FiConsole.class).debug("List Fkb null");
            return;

        }

        for (FiKeyBean fiKeyBean : listFkb) {
            debugFkb(fiKeyBean);
        }
    }

    public static void debugFkb(FiKeyBean fiKeyBean) {

        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        fiKeyBean.forEach((s, o) -> {
            sb.append(s).append(" : ").append(FiString.orEmpty(o));
            sb.append("\n");
        });

        Loghelper.get(FiConsole.class).debug("FiKeybean Content \n");
        Loghelper.get(FiConsole.class).debug(sb.toString());

    }

    public static void logTextFiKeyBean(FiKeyBean fkb) {
        Loghelper.get(FiConsole.class).debug(FiConsole.textFiKeyBean(fkb));
    }

    public static void logFiCols(List<FiCol> listFiColInit) {
        Loghelper.get(FiConsole.class).debug(FiConsole.textFiCols(listFiColInit));
    }


    public static void logFiListKeyString(FiListKeyString fiListKeyString) {
        StringBuilder sbLog = new StringBuilder();

        sbLog.append("FiListKeyString Content\n\n");

        for (FiKeyString fiKeyString : fiListKeyString) {
            sbLog.append(FiConsole.textFiKeyString(fiKeyString)).append("\n");
        }

        Loghelper.get(FiConsole.class).debug(sbLog.toString());
    }
}
