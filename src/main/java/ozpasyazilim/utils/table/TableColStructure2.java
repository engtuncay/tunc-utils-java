package ozpasyazilim.utils.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ozpasyazilim.utils.datacontainers.OzTableColumn;

public class TableColStructure2 {

    // colkey lerin sıralaması
    Map<Integer, String> mapColKeyOrder = new HashMap<>();

    // colkey vs oztablecolumn
    Map<String, OzTableColumn> mapColDef = new HashMap<>();

    private Integer count = 0;
    List<String> cols = new ArrayList<>();

    public void addCol(String colkey) {
    	mapColKeyOrder.put(count, colkey);
    	count++;
    }

    public void addCol(String colkey, OzTableColumn coldef) {
    	
    	this.mapColKeyOrder.put(count, colkey);
    	coldef.setColOrder(count);
    	this.mapColDef.put(colkey, coldef);
    	count++;

    }

    // public void addCol(String colkey, String colname) {
    // mapColNamevsNumber.put(colkey, count);
    // mapColNumbervsKey.put(count, colkey);
    // mapColNames.put(colkey, colname);
    // count++;
    // }

    public void setColDef(String colkey, OzTableColumn coldef) {
	this.mapColDef.put(colkey, coldef);
    }

    // public Integer getColNumber(String colkey) {
    //
    // if (mapColNamevsNumber.containsKey(colkey)) {
    // return mapColNamevsNumber.get(colkey);
    // }
    // return null;
    // }

    // public void setMapColNames(Map<String, String> mapcolnames2) {
    // this.mapColNames = mapcolnames2;
    // }
    //
    // public Map<String, String> getMapColNames() {
    // return mapColNames;
    //}

    // public Map<Integer, String> getMapNumberColName() {
    //
    // Map<Integer, String> mapNumbervscolname = new HashMap<>();
    //
    // for (Entry<String, Integer> entry : mapColNamevsNumber.entrySet()) {
    // String key = entry.getKey();
    // Integer value = entry.getValue();
    // String colname = "";
    // if (mapColNames.containsKey(key)) colname = mapColNames.get(key);
    // mapNumbervscolname.put(value, colname);
    // }
    //
    // return mapNumbervscolname;
    //
    // }

    // public Map<Integer, String> getMapNumberColKey() {
    //
    // Map<Integer, String> mapNumbervscolkey = new HashMap<>();
    //
    // for (Entry<String, Integer> entry : mapColNamevsNumber.entrySet()) {
    // String key = entry.getKey();
    // Integer value = entry.getValue();
    // mapNumbervscolkey.put(value, key);
    // }
    // return mapNumbervscolkey;
    //
    // }

    public String getColKey(Integer colNumber) {
	// Map<Integer, String> mapNumbervscolkey = getMapNumberColKey();
	// if (mapNumbervscolkey.containsKey(colNumber)) return
	// mapNumbervscolkey.get(colNumber);
	if (getMapColNumbervsKey().containsKey(colNumber))
	    return getMapColNumbervsKey().get(colNumber);
	return null;
    }

    // public Map<String, Integer> getMapColNamevsNumber() {
    // return mapColNamevsNumber;
    // }
    //
    // public void setMapColNamevsNumber(Map<String, Integer> mapColNamevsNumber) {
    // this.mapColNamevsNumber = mapColNamevsNumber;
    // }

    public Map<Integer, String> getMapColNumbervsKey() {
	return mapColKeyOrder;
    }

    public void setMapColNumbervsKey(Map<Integer, String> mapColNumbervsKey) {
	this.mapColKeyOrder = mapColNumbervsKey;
    }

    public Integer getCount() {
	return count;
    }

    public void setCount(Integer count) {
	this.count = count;
    }

    public List<String> getCols() {
	return cols;
    }

    public void setCols(List<String> cols) {
	this.cols = cols;
    }

    public String getColNameByIndex(int colindex) {

	if(mapColKeyOrder.get(colindex)==null)return null;

	String colkey = mapColKeyOrder.get(colindex);
	OzTableColumn coldef = mapColDef.get(colkey);

	return coldef.getName();
    }

    public Integer getColCount() {
	return mapColKeyOrder.size();
    }

    public boolean getColEditable(int colindex) {

	if(mapColKeyOrder.get(colindex)==null)return false;

	String colkey = mapColKeyOrder.get(colindex);
	OzTableColumn coldef = mapColDef.get(colkey);

	if(coldef.getEditable()==null)return false;

	return coldef.getEditable();

    }

    public OzTableColumn getColDefByKey(String colkey) {
	if(mapColDef==null)return null;
	return mapColDef.get(colkey);

    }

    public void setAllColEditable() {

	for (Entry<String, OzTableColumn> entry : mapColDef.entrySet()) {
	    String key = entry.getKey();
	    OzTableColumn value = entry.getValue();
	    value.setEditable(true);
	}

    }

    public Class<?> getColClass(int colindex) {

	if(mapColKeyOrder.get(colindex)==null)return null;

	String colkey = mapColKeyOrder.get(colindex);
	OzTableColumn coldef = mapColDef.get(colkey);
	return coldef.getClasstype();

    }
}
