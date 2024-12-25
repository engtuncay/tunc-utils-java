package ozpasyazilim.utils.core;

import javafx.util.Pair;

import org.apache.commons.beanutils.PropertyUtils;
import ozpasyazilim.utils.datatypes.FiInspect;
import ozpasyazilim.utils.datatypes.FiMeta;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.datatypes.PersonEntityTest;
import ozpasyazilim.utils.table.OzColType;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FiCollection {

    public static void main(String[] args) {

        PersonEntityTest pt = new PersonEntityTest("Def", "Defdef");
        PersonEntityTest pt2 = new PersonEntityTest("Zur", "Zurref");

        List<PersonEntityTest> personTests = Arrays.asList(pt, pt2);

        Map<String, List<PersonEntityTest>> stringListMap = FiCollection.groupByFieldToMap(personTests, PersonEntityTest::getName);

        System.out.println("Size:" + stringListMap.size());


    }

    /**
     * predicareFunc fonksiyonuna göre filtrelenmiş yeni bir list döndürür. True ise ekler, false ise eklemez.
     *
     * @param listtum
     * @param predicateFunc
     * @return
     */
    public static <E> List<E> listFilter(List<E> listtum, Predicate<E> predicateFunc) {

        List<E> listfiltered = new ArrayList<>();

        for (Iterator iterator = listtum.iterator(); iterator.hasNext(); ) {
            E e = (E) iterator.next();

            if (predicateFunc.test(e)) {
                listfiltered.add(e);
            }

        }

        return listfiltered;
    }

    /**
     * 0 index , filterin List döner
     * 1 index , filterout List döner
     * predicareFunc fonksiyonuna göre filtre uyan(in) ve uymayan (out) iki list döndürür
     *
     * @param listtum
     * @param predicateFunc
     * @return
     */
    public static <E> List<List<E>> listFilters(List<E> listtum, Predicate<E> predicateFunc) {

        List<E> listfilterin = new ArrayList<>();
        List<E> listfilterout = new ArrayList<>();

        for (Iterator iterator = listtum.iterator(); iterator.hasNext(); ) {
            E e = (E) iterator.next();

            if (predicateFunc.test(e)) {
                listfilterin.add(e);
            } else {
                listfilterout.add(e);
            }

        }

        List<List<E>> listAll = new ArrayList<>();
        listAll.add(listfilterin);
        listAll.add(listfilterout);
        return listAll;

    }

    /**
     * Left (key), Listenin predicate den false değeri dönen değerleri alır
     * Right (value),  Listenin predicate den true değeri dönen değerleri alır
     * <p>
     * predicareFunc fonksiyonuna göre filtre uyan(Right) ve uymayan (Left) list çifti dönderir
     *
     * @param listData
     * @param predicateFunc
     * @return
     */
    public static <E> Pair<List<E>, List<E>> listFilterPair(List<E> listData, Predicate<E> predicateFunc) {

        List<E> listfilterTrue = new ArrayList<>();
        List<E> listfilterFalse = new ArrayList<>();

        for (Iterator iterator = listData.iterator(); iterator.hasNext(); ) {
            E e = (E) iterator.next();

            if (predicateFunc.test(e)) {
                listfilterTrue.add(e);
            } else {
                listfilterFalse.add(e);
            }

        }

        Pair<List<E>, List<E>> listPair = new Pair<>(listfilterFalse, listfilterTrue);

        return listPair;

    }

    // T input: chh
    // R result : integer

    /**
     * List<T> yi alarak
     * fnKeyGetter fonksiyonu ile map e key olacak değer return edilir.
     * <p>
     * Bir değere karşılık, sadece bir entity bağlanır.
     *
     * @param listData    Data Listesi
     * @param fnKeyGetter Key değerini döndüren fonksiyon
     * @param <KeyVal>    Key value
     * @param <T>         Entity object
     * @return
     */
    public static <KeyVal, T> Map<KeyVal, T> listToMapSingle(List<T> listData, Function<T, KeyVal> fnKeyGetter) {

        Map<KeyVal, T> mapList = new HashMap<>();

        for (Iterator iterator = listData.iterator(); iterator.hasNext(); ) {
            T t = (T) iterator.next();
            KeyVal keyVal = fnKeyGetter.apply(t);
            if (keyVal == null) return null;
            mapList.put(keyVal, t);
        }

        return mapList;
    }

    /**
     * fnKeyGetter ve fnKeyValueGetter ile map oluşturur.
     *
     * @param listData
     * @param fnKeyGetter
     * @param fnKeyValueGetter
     * @param <KeyVal>
     * @param <T>
     * @param <KeyValue>
     * @return
     */
    public static <KeyVal, T, KeyValue> Map<KeyVal, KeyValue> listToMapManual(List<T> listData, Function<T, KeyVal> fnKeyGetter, Function<T, KeyValue> fnKeyValueGetter) {

        Map<KeyVal, KeyValue> mapList = new HashMap<>();

        for (Iterator iterator = listData.iterator(); iterator.hasNext(); ) {
            T t = (T) iterator.next();
            KeyVal keyVal = fnKeyGetter.apply(t);
            if (keyVal == null) continue;

            KeyValue keyValue = fnKeyValueGetter.apply(t);
            mapList.put(keyVal, keyValue);
        }

        return mapList;
    }

    public static <KeyVal, T> Map<KeyVal, T> listToMapSingleIgnoreNullKey(List<T> listData, Function<T, KeyVal> fnKeyGetter) {

        Map<KeyVal, T> mapList = new HashMap<>();

        for (Iterator iterator = listData.iterator(); iterator.hasNext(); ) {
            T t = (T) iterator.next();
            KeyVal keyVal = fnKeyGetter.apply(t);
            if (keyVal == null) continue;
            mapList.put(keyVal, t);
        }
        return mapList;
    }

    /**
     * null değerli keyler map e dahil edilmez
     *
     * @param listData
     * @param fnKey
     * @param fnCombineWhenEqual prm1: ana kayıt (güncellenir), prm2 birleştirilecek yeni kayıt
     * @param <KeyVal>
     * @return
     */
    public static <KeyVal, EntClazz> Map<KeyVal, EntClazz> listToMapSingleCombine(List<EntClazz> listData, Function<EntClazz, KeyVal> fnKey, BiConsumer<EntClazz, EntClazz> fnCombineWhenEqual) {

        Map<KeyVal, EntClazz> mapList = new HashMap<>();

        for (Iterator iterator = listData.iterator(); iterator.hasNext(); ) {

            EntClazz entClazz = (EntClazz) iterator.next();
            KeyVal keyVal = fnKey.apply(entClazz);

            // null değerli keyler map e dahil edilmez
            if (keyVal == null) continue;

            if (mapList.containsKey(keyVal)) {
                fnCombineWhenEqual.accept(mapList.get(keyVal), entClazz);
            } else {
                mapList.put(keyVal, entClazz);
            }

        }

        return mapList;

    }

    /**
     * Key null olursa dahil etmez
     *
     * @param listData
     * @param fnKey
     * @param fnValue
     * @param <KeyType>
     * @param <ValueType>
     * @param <PrmEntClass>
     * @return
     */
    public static <KeyType, ValueType, PrmEntClass> Map<KeyType, ValueType> listToMapKeyValue(List<PrmEntClass> listData, Function<PrmEntClass, KeyType> fnKey, Function<PrmEntClass, ValueType> fnValue) {

        Map<KeyType, ValueType> mapList = new HashMap<>();

        for (Iterator iterator = listData.iterator(); iterator.hasNext(); ) {

            PrmEntClass prmEntClass = (PrmEntClass) iterator.next();

            KeyType key = fnKey.apply(prmEntClass);
            ValueType value = fnValue.apply(prmEntClass);

            if (key == null) continue;

            mapList.put(key, value);

        }

        return mapList;

    }

    /**
     * null keyler dahil edilmez
     *
     * @param listData
     * @param fnKey
     * @param <KeyType>
     * @param <PrmEntClass>
     * @return
     */
    public static <KeyType, PrmEntClass> Set<KeyType> listToSetByKeyValue(List<PrmEntClass> listData, Function<PrmEntClass, KeyType> fnKey) {

        Set<KeyType> setNew = new HashSet<>();

        for (Iterator iterator = listData.iterator(); iterator.hasNext(); ) {

            PrmEntClass prmEntClass = (PrmEntClass) iterator.next();
            KeyType key = fnKey.apply(prmEntClass);

            // null key dahil edilmez
            if (key == null) continue;

            setNew.add(key);
        }

        return setNew;
    }

    /**
     * Verilen Listedeki elemanları belli bir key ye (fnGetId den dönen değer) göre bir listede toplar, bunu map'a ekler. map, key:id value:listEntity şeklinde olur.
     * <p>
     * id null olursa atlar
     * <p>
     * Fav1
     *
     * @param listtum
     * @param fnGetId
     * @param <T>
     * @param <KeyVal>
     * @return
     */

    public static <T, KeyVal> Map<KeyVal, List<T>> listToMapMulti(Collection<T> listtum, Function<T, KeyVal> fnGetId) {

        Map<KeyVal, List<T>> mapList = new HashMap<>();

        for (Iterator iterator = listtum.iterator(); iterator.hasNext(); ) {

            T t = (T) iterator.next();

            KeyVal keyValValue = fnGetId.apply(t);

            if (keyValValue == null) continue;

            if (!mapList.containsKey(keyValValue)) mapList.put(keyValValue, new ArrayList());

            mapList.get(keyValValue).add(t);

        }

        return mapList;

    }

    public static <T, KeyVal> Map<KeyVal, List<T>> listToLinkedMapMulti(Collection<T> listtum, Function<T, KeyVal> fnGetId) {

        Map<KeyVal, List<T>> mapList = new LinkedHashMap<>();

        for (Iterator iterator = listtum.iterator(); iterator.hasNext(); ) {

            T t = (T) iterator.next();

            KeyVal keyValValue = fnGetId.apply(t);

            if (keyValValue == null) continue;

            if (!mapList.containsKey(keyValValue)) mapList.put(keyValValue, new ArrayList());

            mapList.get(keyValValue).add(t);

        }

        return mapList;

    }

    /**
     * Verilen Listedeki elemanları belli bir key ye (fnGetId den dönen değer) göre toplar map<id,list<entity>> şeklinde oluşur
     * <p>
     * id null olursa empty key içine atar
     * <p>
     * Fav1
     *
     * @param listtum
     * @param fnGetId
     * @param <T>
     * @param <KeyVal>
     * @return
     */

    public static <T, KeyVal> Map<KeyVal, List<T>> listToMapMulti(List<T> listtum, Function<T, KeyVal> fnGetId, KeyVal nullKey) {

        Map<KeyVal, List<T>> mapList = new HashMap<>();

        for (Iterator iterator = listtum.iterator(); iterator.hasNext(); ) {

            T t = (T) iterator.next();

            KeyVal keyValValue = fnGetId.apply(t);

            if (keyValValue == null) keyValValue = nullKey;

            if (!mapList.containsKey(keyValValue)) mapList.put(keyValValue, new ArrayList());

            mapList.get(keyValValue).add(t);

        }

        return mapList;

    }

    /**
     * index 0 , list1 kalanlar
     * index 1 , list2 kalanlar
     *
     * @param list1
     * @param list2
     * @param functionKey
     * @return
     */
    public static <R, T> List<List<T>> listCompare(List<T> list1, List<T> list2, Function<T, R> functionKey) {

        Map<R, T> mapList1 = FiCollection.listToMapSingle(list1, functionKey);

        Map<R, T> mapList2 = FiCollection.listToMapSingle(list2, functionKey);

        for (Iterator iterator = list1.iterator(); iterator.hasNext(); ) {

            T t = (T) iterator.next();

            R key = functionKey.apply(t);

            if (mapList2.containsKey(key)) {
                mapList1.remove(key);
                mapList2.remove(key);
            }

        }

        List<T> listres1 = new ArrayList<>();
        for (Entry<R, T> entry : mapList1.entrySet()) {
            R key = entry.getKey();
            T value = entry.getValue();
            listres1.add(value);
        }

        List<T> listres2 = new ArrayList<>();
        for (Entry<R, T> entry : mapList2.entrySet()) {
            R key = entry.getKey();
            T value = entry.getValue();
            listres2.add(value);
        }

        List<List<T>> listResAll = new ArrayList<>();

        listResAll.add(listres1);
        listResAll.add(listres2);

        // List<Map<R, T>> list = new ArrayList<>();
        // list.add(mapList1);
        // list.add(mapList2);

        return listResAll;

    }

    /**
     * @param list1
     * @param list2
     * @param functionCompare Karşılaştırma fonksiyonu
     * @return
     */
    public static <R, T1, T2> Map<T1, T2> listCompareAndPointByCompareFunction(List<T1> list1, List<T2> list2, BiFunction<T1, T2, Boolean> functionCompare
            , BiConsumer<T1, Boolean> functPoint1, BiConsumer<T2, Boolean> functPoint2) {

        Map<T1, T2> mapEslesen = new HashMap<>();

        for (Iterator iterator = list1.iterator(); iterator.hasNext(); ) {

            T1 t1 = (T1) iterator.next();

            for (Iterator iterator2 = list2.iterator(); iterator.hasNext(); ) {

                T2 t2 = (T2) iterator2.next();

                if (functionCompare.apply(t1, t2)) {
                    if (functPoint1 != null) functPoint1.accept(t1, true);
                    if (functPoint2 != null) functPoint2.accept(t2, true);
                    mapEslesen.put(t1, t2);
                    break;

                }
            }
        }

        return mapEslesen;

    }

    /**
     * index 0 , list1 kalanlar
     * index 1 , list2 kalanlar
     *
     * @param list1
     * @param list2
     * @param functionID   eşitliği belirleyecek alan, primary key (id) alanı
     * @param predFunction
     * @return
     */
    public static <R, T> List<List<T>> listCompare2(List<T> list1, List<T> list2, Function<T, R> functionID,
                                                    BiPredicate<T, T> predFunction) {

        Map<R, T> mapList1 = FiCollection.listToMapSingle(list1, functionID);

        Map<R, T> mapList2 = FiCollection.listToMapSingle(list2, functionID);

        for (Iterator iterator = list1.iterator(); iterator.hasNext(); ) {

            T t1 = (T) iterator.next();

            R key = functionID.apply(t1);

            if (mapList2.containsKey(key)) {

                T t2 = mapList2.get(key);

                if (predFunction.test(t1, t2)) {
                    mapList1.remove(key);
                    mapList2.remove(key);
                }

            }

        }

        // map ler , tekrar liste çevrilir

        List<T> listres1 = new ArrayList<>();

        for (Entry<R, T> entry : mapList1.entrySet()) {
            R key = entry.getKey();
            T value = entry.getValue();
            listres1.add(value);
        }

        List<T> listres2 = new ArrayList<>();
        for (Entry<R, T> entry : mapList2.entrySet()) {
            R key = entry.getKey();
            T value = entry.getValue();
            listres2.add(value);
        }

        List<List<T>> listResAll = new ArrayList<>();

        listResAll.add(listres1);
        listResAll.add(listres2);

        // List<Map<R, T>> list = new ArrayList<>();
        // list.add(mapList1);
        // list.add(mapList2);

        return listResAll;

    }


    /**
     * taslak devam edilmedi , copy paste edildi
     * index 0 , list1 kalanlar
     * index 1 , list2 kalanlar
     * index 2 , list1,list2 olup, predfunc fonksiyonunu sağlamayanlar
     *
     * @param list1
     * @param list2
     * @param functionID   eşitliği belirleyecek alan, primary key (id) alanı
     * @param predFunction
     * @return
     */
    public static <R, T> Pair<List<List<T>>, Map<T, T>> listCompare3(List<T> list1, List<T> list2, Function<T, R> functionID,
                                                                     BiPredicate<T, T> predFunction) {

        Map<R, T> mapList1 = FiCollection.listToMapSingle(list1, functionID);

        Map<R, T> mapList2 = FiCollection.listToMapSingle(list2, functionID);

        Map<T, T> mapList3 = new HashMap<>();

        for (Iterator iterator = list1.iterator(); iterator.hasNext(); ) {

            T t1 = (T) iterator.next();

            R key = functionID.apply(t1);


            if (mapList2.containsKey(key)) { // list 2 de kayıt var

                T t2 = mapList2.get(key);
                // id ler eşitse koleksiyondan çıkar
                mapList1.remove(key);
                mapList2.remove(key);

                if (!predFunction.test(t1, t2)) { // ikinci şartı yerine getirmiyorsa ayrı koleksiyona yaz
                    mapList3.put(t1, t2);
                }

            }

        }

        // map ler , tekrar liste çevrilir

        List<T> listres1 = new ArrayList<>();

        for (Entry<R, T> entry : mapList1.entrySet()) {
            R key = entry.getKey();
            T value = entry.getValue();
            listres1.add(value);
        }

        List<T> listres2 = new ArrayList<>();
        for (Entry<R, T> entry : mapList2.entrySet()) {
            R key = entry.getKey();
            T value = entry.getValue();
            listres2.add(value);
        }

        List<List<T>> listResAll = new ArrayList<>();

        listResAll.add(listres1);
        listResAll.add(listres2);


        // List<Map<R, T>> list = new ArrayList<>();
        // list.add(mapList1);
        // list.add(mapList2);

        return new Pair<>(listResAll, mapList3);

    }

    public static <E> void printList(List<E> listObjects) {

        for (Iterator iterator = listObjects.iterator(); iterator.hasNext(); ) {
            E e = (E) iterator.next();
            System.out.println(e);
        }

    }

    public static <E, V> Map<String, List> listcomparebi(List<E> listdata1, List<V> listdata2,
                                                         BiPredicate<E, V> funcPredicate) {

        List<E> listdata1kalan = new ArrayList<>(listdata1);
        List<V> listdata2kalan = new ArrayList<>(listdata2);
        List<E> listeslesenler1 = new ArrayList<>();
        List<V> listeslesenler2 = new ArrayList<>();

        for (Iterator iterator = listdata1kalan.iterator(); iterator.hasNext(); ) {
            E entity1 = (E) iterator.next();

            Boolean found = false;

            for (Iterator iterator2 = listdata2kalan.iterator(); iterator2.hasNext(); ) {
                V entity2 = (V) iterator2.next();

                if (funcPredicate.test(entity1, entity2)) {
                    listeslesenler1.add(entity1);
                    listeslesenler2.add(entity2);
                    iterator2.remove();
                    found = true;
                    break;
                }

            }

            if (found) {
                iterator.remove();
            }

        }

        Map<String, List> returnlist = new HashMap<>();

        returnlist.put("list1kalan", listdata1kalan);
        returnlist.put("list2kalan", listdata2kalan);
        returnlist.put("list1eslesen", listeslesenler1);
        returnlist.put("list2eslesen", listeslesenler2);

        return returnlist;

    }

    public static <E> Map<String, List> listcompare(List<E> listdata1, List<E> listdata2,
                                                    BiPredicate<E, E> funcPredicate) {

        List<E> listdata1kalan = new ArrayList<>(listdata1);
        List<E> listdata2kalan = new ArrayList<>(listdata2);
        List<E> listeslesenler = new ArrayList<>();

        for (Iterator iterator = listdata1kalan.iterator(); iterator.hasNext(); ) {
            E entity1 = (E) iterator.next();

            Boolean found = false;

            for (Iterator iterator2 = listdata2kalan.iterator(); iterator2.hasNext(); ) {
                E entity2 = (E) iterator2.next();

                if (funcPredicate.test(entity1, entity2)) {
                    listeslesenler.add(entity1);
                    iterator2.remove();
                    found = true;
                    break;
                }

            }

            if (found) {
                iterator.remove();
            }

        }

        Map<String, List> returnlist = new HashMap<>();

        returnlist.put("list1kalan", listdata1kalan);
        returnlist.put("list2kalan", listdata2kalan);
        returnlist.put("listeslesen", listeslesenler);

        return returnlist;

    }

    public static <E, V> Map<String, List> listcomparebi_noclone(List<E> listdata1, List<V> listdata2,
                                                                 BiPredicate<E, V> funcPredicate) {

        List<E> listdata1kalan = new ArrayList<>();
        List<V> listdata2kalan = new ArrayList<>();
        List<E> listeslesenler1 = new ArrayList<>();
        List<V> listeslesenler2 = new ArrayList<>();

        Map<Integer, Boolean> mapBulunanlar_index_boolean = new HashMap<>();

        for (Iterator iterator = listdata1.iterator(); iterator.hasNext(); ) {
            E entity1 = (E) iterator.next();

            Boolean found = false;
            int index2 = 0;
            for (Iterator iterator2 = listdata2.iterator(); iterator2.hasNext(); ) {
                V entity2 = (V) iterator2.next();

                if (funcPredicate.test(entity1, entity2)) {
                    listeslesenler1.add(entity1);
                    listeslesenler2.add(entity2);
                    mapBulunanlar_index_boolean.put(index2, true);
                    found = true;
                    break;
                }

                index2++;
            }

            if (found == false) {
                listdata1kalan.add(entity1);
            }

        }

        for (int i = 0; i < listdata2.size(); i++) {
            if (!mapBulunanlar_index_boolean.containsKey(i)) {
                listdata2kalan.add(listdata2.get(i));
            }

        }

        Map<String, List> returnlist = new HashMap<>();

        returnlist.put("list1kalan", listdata1kalan);
        returnlist.put("list2kalan", listdata2kalan);
        returnlist.put("list1eslesen", listeslesenler1);
        returnlist.put("list2eslesen", listeslesenler2);

        return returnlist;

    }

    /**
     * Finds the index of all entries in the list that matches the regex
     *
     * @param list  The list of strings to check
     * @param regex The regular expression to use
     * @return list containing the indexes of all matching entries
     */
    public static List<Integer> getMatchingIndexes(List<String> list, String regex) {

        ListIterator<String> li = list.listIterator();

        List<Integer> indexes = new ArrayList<Integer>();

        while (li.hasNext()) {
            int i = li.nextIndex();
            String next = li.next();
            if (Pattern.matches(regex, next)) {
                indexes.add(i);
            }
        }

        return indexes;
    }

    public static <T, R> Optional<R> mapcheckGet(Map<T, R> mapData, T key) {

        if (mapData.containsKey(key)) {
            return Optional.ofNullable(mapData.get(key));
        }
        return Optional.ofNullable(null);
    }

    public static Boolean isEmpty(Collection collection) {
        if (collection == null) return true;
        if (collection.isEmpty()) return true;
        return false;
    }

    public static Boolean isNotEmpty(Collection collection) {

        if (collection != null && collection.size() > 0) return true;
        return false;
    }

    public static Boolean isEmptyKey(Map mapData, String key) {

        if (!mapData.containsKey(key)) return true;
        if (mapData.get(key) == null) return true;

        return false;
    }

    public static Boolean isNotEmptyKey(Map mapData, String key) {

        if (mapData.containsKey(key) && mapData.get(key) != null) return true;
        return false;
    }

    public static <T> List<T> groupByField(List<T> listData, String field, List<IFiCol> listCol, Class<T> clazz) {

        Map<String, List<T>> mapData = new HashMap<>();

        // map list e çevrildi
        for (T entity : listData) {

            String key = FiReflection.getProperty(entity, field).toString();

            if (!mapData.containsKey(key)) mapData.put(key, new ArrayList<>());

            mapData.get(key).add(entity);
        }

        List<T> listFiltered = new ArrayList<>();

        // gruplanarak grup özeti çıkarılır
        for (String keyData : mapData.keySet()) {

            List<T> listDataByKey = mapData.get(keyData);

            T filteredEntity = new FiReflection().generateObject(clazz);

            for (IFiCol oztablecol : listCol) {

                T exampleObj = listDataByKey.get(0);

                Object objectColValue = FiReflection.getProperty(exampleObj, oztablecol.getOfcTxFieldName());

                if (oztablecol.getColType() == null || oztablecol.getColType() == OzColType.String) {
                    new FiReflection().setter(filteredEntity, oztablecol, objectColValue);
                }

                if (objectColValue instanceof Double) {

                    Double sumDouble = FiNumberToText.sumValuesDouble(listDataByKey, ent -> {
                        Object objectByField = FiReflection.getProperty(ent, oztablecol.getOfcTxFieldName());
                        if (objectByField == null) return 0d;
                        return (Double) objectByField;
                    });

                    new FiReflection().setter(filteredEntity, oztablecol, sumDouble);

                }

            }

            listFiltered.add(filteredEntity);

        }


        return listFiltered;
    }

    public static <T> List<T> groupByField(List<T> listData, List<String> listGroupbyfields, List<IFiCol> listCol, Class<T> clazz) {

        if (FiCollection.isEmpty(listGroupbyfields)) return null;

        Map<String, List<T>> mapData = new HashMap<>();

        String field = listGroupbyfields.get(0);

        // map list e çevrildi
        for (T entity : listData) {

            String key = FiReflection.getProperty(entity, field).toString();

            if (!mapData.containsKey(key)) mapData.put(key, new ArrayList<>());

            mapData.get(key).add(entity);
        }

        List<T> listFiltered = new ArrayList<>();

        // gruplanarak grup özeti çıkarılır
        for (String keyData : mapData.keySet()) {

            List<T> listDataByKey = mapData.get(keyData);

            T filteredEntity = new FiReflection().generateObject(clazz);

            T exampleObj = listDataByKey.get(0);

            for (String fieldGrouped : listGroupbyfields) {
                Object objectColValue = FiReflection.getProperty(exampleObj, fieldGrouped);
                new FiReflection().setter(filteredEntity, fieldGrouped, objectColValue);
            }

            for (IFiCol oztablecol : listCol) {

                Object objectColValue = FiReflection.getProperty(exampleObj, oztablecol.getOfcTxFieldName());

                // yukarıda yapıldığı için atlanır
                if (listGroupbyfields.contains(oztablecol.getOfcTxFieldName())) {
                    continue;
                    //new FiProperty().setter(filteredEntity, oztablecol, objectColValue);
                }

                if (objectColValue instanceof Double) {

                    Double sumDouble = FiNumberToText.sumValuesDouble(listDataByKey, ent -> {
                        Object objectByField = FiReflection.getProperty(ent, oztablecol.getOfcTxFieldName());
                        if (objectByField == null) return 0d;
                        return (Double) objectByField;
                    });

                    new FiReflection().setter(filteredEntity, oztablecol, sumDouble);
                }

            }

            listFiltered.add(filteredEntity);

        }


        return listFiltered;
    }

    public static <T> Map<String, List<T>> groupByFieldToMap(List<T> listData, String field) {

        if (field == null) return null;

        Map<String, List<T>> mapData = new HashMap<>();

        // map list e çevrildi
        for (T entity : listData) {

            String key = FiReflection.getProperty(entity, field).toString();

            if (!mapData.containsKey(key)) mapData.put(key, new ArrayList<>());

            mapData.get(key).add(entity);
        }

        return mapData;
    }

    public static <T> Map<String, List<T>> groupByFieldToMap(List<T> listData, Function<T, ?> getterFunc) {

        if (getterFunc == null) return null;

        //Arrays.asList(1,2).forEach();

        Map<String, List<T>> mapData = new HashMap<>();

        // map list e çevrildi
        for (T entity : listData) {

            String key = getterFunc.apply(entity).toString();

            if (!mapData.containsKey(key)) mapData.put(key, new ArrayList<>());

            mapData.get(key).add(entity);
        }

        return mapData;
    }

    public static <T, R> Map<R, List<T>> groupByFieldToMap2(List<T> listData, Function<T, R> getterFunc) {

        if (getterFunc == null) return null;

        //Arrays.asList(1,2).forEach();

        Map<R, List<T>> mapData = new HashMap<>();

        // map list e çevrildi
        for (T entity : listData) {

            R key = getterFunc.apply(entity);

            if (!mapData.containsKey(key)) mapData.put(key, new ArrayList<>());

            mapData.get(key).add(entity);
        }

        return mapData;
    }


    public static <T, R> List<R> mapToList(Map<T, R> mapData) {

        List<R> list = new ArrayList<>();
        mapData.values().forEach(ent -> list.add(ent));

        return list;


    }

    /**
     * Predicate'den True dönen elemanları siler.
     *
     * @param listData
     * @param predRemove
     * @param <E>
     */
    public static <E> void removeListItems(List<E> listData, Predicate<E> predRemove) {

        if (listData == null || predRemove == null) return;

        for (ListIterator<E> iter = listData.listIterator(); iter.hasNext(); ) {

            E element = iter.next();

            if (predRemove.test(element)) iter.remove();

            // 1 - can call methods of element
            // 2 - can use iter.remove() to remove the current element from the list
            // 3 - can use iter.add(...) to insert a new element into the list
            //     between element and iter->next()
            // 4 - can use iter.set(...) to replace the current element

            // ...

        }


    }

    public static void addIfNotNull(Map<String, Object> mapParam, String key, Object value) {
        if (value != null) mapParam.put(key, value);
    }

    /**
     * list içindeki değerleri fonksiyona göre string olarak birleştirir (deparsing)
     *
     * @param listSelected
     * @param fnTxValue
     * @param <E>
     * @return
     */
    public static <E> String commaSeperatedDeParseMain(List<E> listSelected, Function<E, Object> fnTxValue) {
        if (listSelected == null || listSelected.size() == 0) return "";

        StringBuilder result = new StringBuilder();
        int index = 0;
        for (E entity : listSelected) {
            Object txValue = fnTxValue.apply(entity);
            if (txValue == null) continue;

            index++;
            if (index != 1) {
                result.append(",");
            }

            result.append(txValue.toString());

        }

        return result.toString();
    }

    public static String commaSeperatedDeParseWithKesmeWoutEmpty(List<String> listSelected) {
        if (listSelected == null || listSelected.size() == 0) return "";

        StringBuilder result = new StringBuilder();
        int index = 0;
        for (String txValue : listSelected) {
            if (FiString.isEmpty(txValue)) continue;

            index++;
            if (index != 1) {
                result.append(",");
            }
            result.append("'" + txValue.toString() + "'");
        }
        return result.toString();
    }

    public static String commaSeperatedDeParseIntList(List<Integer> listSelected) {
        return commaSeperatedDeParseMain(listSelected, lnValue -> lnValue);
    }

    public static String commaSeperatedDeParseStringWoutEmpty(List<String> listSelected) {
        if (listSelected == null || listSelected.size() == 0) return "";

        StringBuilder result = new StringBuilder();
        int index = 0;
        for (String txValue : listSelected) {
            if (FiString.isEmpty(txValue)) continue;

            index++;
            if (index != 1) {
                result.append(",");
            }
            result.append(txValue.toString());
        }

        return result.toString();
    }


    /**
     * string değeri virgüle göre değerlere ayrıştırır(parsing). Değerleri listeye ekler.
     *
     * @param txCommaSeperated
     * @return
     */
    public static List<String> commaSeperatedParseToStrList(String txCommaSeperated) {

        if (txCommaSeperated == null) return new ArrayList<>();

        List<String> listData = new ArrayList<>();

        String[] splitData = txCommaSeperated.split(",");

        for (String item : splitData) {
            if (!FiString.isEmpty(item)) {
                listData.add(item);
            }
        }

        return listData;

    }

    public static List<FiMeta> commaSeperatedParseToStrListMeta(String txCommaSeperated) {

        if (txCommaSeperated == null) return new ArrayList<>();

        List<FiMeta> listData = new ArrayList<>();

        String[] splitData = txCommaSeperated.split(",");

        for (String item : splitData) {
            if (!FiString.isEmpty(item)) {
                listData.add(FiMeta.bui(item));
            }
        }

        return listData;

    }

    public static List<Integer> commaSeperatedParseToIntList(String txCommaSeperated) {

        if (txCommaSeperated == null) return new ArrayList<>();

        List<Integer> listData = new ArrayList<>();

        String[] splitData = txCommaSeperated.split(",");

        for (String item : splitData) {
            if (!FiString.isEmpty(item)) {
                try {
                    Integer parsed = Integer.parseInt(item);
                    listData.add(parsed);
                } catch (Exception ex) {
                    Loghelper.debugException(FiCollection.class, ex);
                }
            }
        }

        return listData;

    }

    /**
     * Null değerler eklenmez
     *
     * @param listSelected
     * @param fnTxValue
     * @param <E>
     * @return
     */
    public static <E> List<String> listToListString(List<E> listSelected, Function<E, String> fnTxValue) {
        if (listSelected == null) return null;
        if (listSelected.size() == 0) return new ArrayList<>();

        List<String> list = new ArrayList<>();

        for (E entity : listSelected) {

            String txValue = fnTxValue.apply(entity);

            if (txValue != null) {
                list.add(txValue);
            }

        }

        return list;

    }

    /**
     * Null Değerler eklenmez
     *
     * @param listSelected
     * @param fnTxValue
     * @param <E>
     * @return
     */
    public static <E> List<Integer> listToListInt(List<E> listSelected, Function<E, Integer> fnTxValue) {

        if (listSelected == null) return null;
        if (listSelected.size() == 0) return new ArrayList<>();

        List<Integer> list = new ArrayList<>();

        for (E entity : listSelected) {

            Integer txValue = fnTxValue.apply(entity);

            if (txValue != null) {
                list.add(txValue);
            }

        }

        return list;

    }

    public static <E> Integer[] listToArrayInt(List<E> listSelected, Function<E, Integer> fnTxValue) {

        if (listSelected == null) return null;
        if (listSelected.size() == 0) return new Integer[0];

        Integer[] list = new Integer[listSelected.size()];

        int index = -1;
        for (E entity : listSelected) {

            Integer txValue = fnTxValue.apply(entity);

            if (txValue != null) {
                index++;
                list[index] = txValue;
            }

        }

        return list;

    }

    public static <A> List<A> getItemsByBoField(List<A> listData, String boolField) {

        List<A> listSelect = new ArrayList<>();

        listData.forEach(ent -> {

            try {
                Boolean aBoolean = FiBool.convertBooleanElseFalse(PropertyUtils.getNestedProperty(ent, boolField));
                if (aBoolean) {
                    listSelect.add(ent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        return listSelect;
    }


    public static <NewClazz, OldClazz> List<NewClazz> convertEntity(Collection<OldClazz> listDataOld, Function<OldClazz, NewClazz> convertFunction) {

        List<NewClazz> listNew = new ArrayList<>();

        for (OldClazz oldClazz : listDataOld) {
            listNew.add(convertFunction.apply(oldClazz));
        }

        return listNew;
    }

    public static String joinList(List listData, String txJoin) {

        StringBuilder joinString = new StringBuilder("");
        int i = 1;
        for (Object listDatum : listData) {
            if (listDatum == null) continue;
            if (i != 1) joinString.append(txJoin);
            joinString.append(listDatum.toString());
            i++;
        }

        return joinString.toString();
    }

    public static <PrmEnt> Double sumDouble(Collection<PrmEnt> itemsCheckedByBoSecimAsList, Function<PrmEnt, Double> fnValue) {

        Double dbSum = 0d;

        for (PrmEnt prmEnt : itemsCheckedByBoSecimAsList) {
            Double value = fnValue.apply(prmEnt);
            if (value == null) value = 0d;
            dbSum += value;
        }

        return dbSum;
    }

    public static <PrmEnt> Integer findMinInt(Collection<PrmEnt> hizmetHareketList, Function<PrmEnt, Integer> fnGetInteger) {

        Integer min = null;

        for (PrmEnt prmEnt : hizmetHareketList) {

            Integer tempValue = fnGetInteger.apply(prmEnt);
            if (tempValue == null) continue;

            if (min == null) {
                min = tempValue;
                continue;
            }

            if (tempValue < min) {
                min = tempValue;
            }

        }

        return min;
    }

    public static <PrmEnt> Date findMinDate(Collection<PrmEnt> hizmetHareketList, Function<PrmEnt, Date> fnGetInteger) {

        Date min = null;
        for (PrmEnt prmEnt : hizmetHareketList) {

            Date tempValue = fnGetInteger.apply(prmEnt);
            if (tempValue == null) continue;

            if (min == null) {
                min = tempValue;
                continue;
            }

            if (tempValue.compareTo(min) < 0) {
                min = tempValue;
            }
        }

        return min;
    }

    public static <PrmEnt> Date findMaxDate(Collection<PrmEnt> hizmetHareketList, Function<PrmEnt, Date> fnGetInteger) {

        Date max = null;

        for (PrmEnt prmEnt : hizmetHareketList) {

            Date tempValue = fnGetInteger.apply(prmEnt);
            if (tempValue == null) continue;

            if (max == null) {
                max = tempValue;
                continue;
            }

            if (tempValue.compareTo(max) > 0) {
                max = tempValue;
            }
        }

        return max;
    }

    public static <PrmEnt> PrmEnt findMinDateEntity(Collection<PrmEnt> hizmetHareketList, Function<PrmEnt, Date> fnGetInteger) {

        Date min = null;
        PrmEnt minPrmEnt = null;
        for (PrmEnt prmEnt : hizmetHareketList) {

            Date tempValue = fnGetInteger.apply(prmEnt);
            if (tempValue == null) continue;

            if (min == null) {
                min = tempValue;
                minPrmEnt = prmEnt;
                continue;
            }

            if (tempValue.compareTo(min) < 0) {
                min = tempValue;
                minPrmEnt = prmEnt;
            }
        }

        return minPrmEnt;
    }

    public static boolean isEmptyMap(Map mapData) {
        if (mapData == null || mapData.size() == 0) return true;
        return false;
    }

    /**
     * Sıralanmış yeni bir liste döndürü, orijinal list'de değişiklik yapmaz.
     * <p>
     * Örnek 1 (Comparator ile örnek)
     * <p>
     * List sortedListCariHar  = FiCollection.sort(cariEvrak.getListCariHareketInit(), Comparator.comparing(cariHar -> FiNumber.orZero(cariHar.getCha_satir_no())));
     *
     * @param listData
     * @param comparator varsayılan olarak küçükten büyüğe sıralar. Örneğin -1,0,1 gibi..., tam tersine çevirmek için -1 ile çarpılır.
     * @param <E>
     * @return
     */
    public static <E> List<E> sort(List<E> listData, Comparator<E> comparator) {
        return listData.stream().sorted(comparator).collect(Collectors.toList());

    }

    public static List orValue(List value, List orValue) {
        if (value == null) return orValue;
        return value;
    }

    public static <E> List<E> toList(E... entObject) {
        List<E> list = new ArrayList<>();
        for (E tempObj : entObject) {
            list.add(tempObj);
        }
        return list;
    }

    public static <PrmEnt> Boolean containsEmptyString(List<PrmEnt> value, Function<PrmEnt, String> getApcTxValue) {

        Boolean boEmptyfound = false;

        for (PrmEnt prmEnt : value) {
            if (FiString.isEmpty(getApcTxValue.apply(prmEnt))) {
                boEmptyfound = true;
                break;
            }
        }

        return boEmptyfound;
    }

    public static boolean contains(Map<String, String> mapStHaricInit, Set<String> setStKod) {
        if (mapStHaricInit.isEmpty()) return false;
        if (setStKod.isEmpty()) return false;

        for (String txValue : setStKod) {
            if (mapStHaricInit.containsKey(txValue)) {
                return true;
            }

        }
        return false;
    }

    /**
     * @param listDbEntAppConfig
     * @param allMapByGuid
     * @param fnKey
     * @param fnAddingExtraInfoDbAndMeta param1 : DbObject, param2 : MetaObject
     * @param <PrmEntity>
     */
    public static <PrmEntity> void completeListByMetaList(List<PrmEntity> listDbEntAppConfig, Map<String, PrmEntity> allMapByGuid, Function<PrmEntity, String> fnKey
            , BiConsumer<PrmEntity, PrmEntity> fnAddingExtraInfoDbAndMeta
            , Consumer<PrmEntity> fnAddingExtraInfoForMeta) {

        // Db config objesine, ek bilgilerin eklenmesi (ayar adı vs)
        for (PrmEntity prmEntity : listDbEntAppConfig) {
            if (allMapByGuid.containsKey(fnKey.apply(prmEntity))) {
                PrmEntity metaPrmEntity = allMapByGuid.get(fnKey.apply(prmEntity));

                if (fnAddingExtraInfoDbAndMeta != null) {
                    fnAddingExtraInfoDbAndMeta.accept(prmEntity, metaPrmEntity);
                }

            }
        }

        // vt'de olmayan ayarların eklenmesi
        Set<String> setDb = FiCollection.listToSetByKeyValue(listDbEntAppConfig, fnKey);

        for (PrmEntity prmEntity : allMapByGuid.values()) {
            if (!setDb.contains(fnKey.apply(prmEntity))) {
                // MetaObjesine eklenecek bilgiler
                if (fnAddingExtraInfoForMeta != null) {
                    fnAddingExtraInfoForMeta.accept(prmEntity);
                }

                listDbEntAppConfig.add(prmEntity);
            }
        }
    }

    public static Integer getSizeOrZero(Collection collection) {
        if(collection==null) return 0;
        return collection.size();
    }

    public static <Mt> void addInspect(String txKey, Mt object, Map<String, FiInspect<Mt>> mapBelgeler) {

        FiInspect<Mt> fiInspect = null;

        if (mapBelgeler.containsKey(txKey)) {
            fiInspect = mapBelgeler.get(txKey);
            fiInspect.incCount1();
            fiInspect.getListEntityInit().add(object);
        }else{
            fiInspect = new FiInspect();
            fiInspect.incCount1();
            fiInspect.getListEntityInit().add(object);
            mapBelgeler.put(txKey, fiInspect);
        }

    }

}
