package ozpasyazilim.utils.datatypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FiListMap extends ArrayList<HashMap<String,Object>> {

	Map<String,Object> mapData;

	public void put(String key,Object value){
		this.getMapData().put(key,value);
	}

	public Map<String, Object> getMapData() {
		if (mapData == null) {
			mapData=new HashMap<>();
		}
		return mapData;
	}

	public void setMapData(Map<String, Object> mapData) {
		this.mapData = mapData;
	}
}
