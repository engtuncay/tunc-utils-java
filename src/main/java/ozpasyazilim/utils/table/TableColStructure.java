package ozpasyazilim.utils.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ozpasyazilim.utils.datacontainers.OzTableColumn;

public class TableColStructure {

	// key alanlar için double index
	// colkey , colnumber - az kullanılır
	Map<String, Integer> mapColNamevsNumber = new HashMap<>();
	// colorder vs colkey   // daha çok kullanılıyor
	Map<Integer, String> mapColNumbervsKey = new HashMap<>();

	// colkey vs colname
	Map<String, String> mapColNames = new HashMap<>();

	// colkey vs oztablecolumn
	Map<String, OzTableColumn> mapColDef = new HashMap<>();

	private Integer count = 1;

	List<String> cols = new ArrayList<>();

	public void addCol(String colkey) {
		mapColNamevsNumber.put(colkey, count);
		mapColNumbervsKey.put(count, colkey);
		count++;
	}

	public void addCol(String colkey, String colname) {
		mapColNamevsNumber.put(colkey, count);
		mapColNumbervsKey.put(count, colkey);
		mapColNames.put(colkey, colname);
		count++;
	}

	public Integer getColNumber(String colkey) {

		if (mapColNamevsNumber.containsKey(colkey)) {
			return mapColNamevsNumber.get(colkey);
		}
		return null;
	}

	public void setMapColNames(Map<String, String> mapcolnames2) {
		this.mapColNames = mapcolnames2;
	}

	public Map<String, String> getMapColNames() {
		return mapColNames;
	}

	public Map<Integer, String> getMapFromNumberToColName() {

		Map<Integer, String> mapNumbervscolname = new HashMap<>();

		for (Entry<String, Integer> entry : mapColNamevsNumber.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			String colname = "";
			if (mapColNames.containsKey(key)) colname = mapColNames.get(key);
			mapNumbervscolname.put(value, colname);
		}

		return mapNumbervscolname;

	}

	//	public Map<Integer, String> getMapNumberColKey() {
	//
	//		Map<Integer, String> mapNumbervscolkey = new HashMap<>();
	//
	//		for (Entry<String, Integer> entry : mapColNamevsNumber.entrySet()) {
	//			String key = entry.getKey();
	//			Integer value = entry.getValue();
	//			mapNumbervscolkey.put(value, key);
	//		}
	//		return mapNumbervscolkey;
	//
	//	}

	public String getColKey(Integer colNumber) {
		//Map<Integer, String> mapNumbervscolkey = getMapNumberColKey();
		//if (mapNumbervscolkey.containsKey(colNumber)) return mapNumbervscolkey.get(colNumber);
		if (getMapColNumbervsKey().containsKey(colNumber)) return getMapColNumbervsKey().get(colNumber);
		return null;
	}

	public Map<String, Integer> getMapColNamevsNumber() {
		return mapColNamevsNumber;
	}

	public void setMapColNamevsNumber(Map<String, Integer> mapColNamevsNumber) {
		this.mapColNamevsNumber = mapColNamevsNumber;
	}

	public Map<Integer, String> getMapColNumbervsKey() {
		return mapColNumbervsKey;
	}

	public void setMapColNumbervsKey(Map<Integer, String> mapColNumbervsKey) {
		this.mapColNumbervsKey = mapColNumbervsKey;
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
}
