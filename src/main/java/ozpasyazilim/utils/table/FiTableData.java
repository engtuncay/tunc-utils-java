package ozpasyazilim.utils.table;

import java.util.List;
import java.util.Map;

public class FiTableData {

	List<FiCol> listColumn;
	Map<String,String> mapStyle;    // Table stili


	public List<FiCol> getListColumn() {
		return listColumn;
	}

	public void setListColumn(List<FiCol> listColumn) {
		this.listColumn = listColumn;
	}

	public Map<String, String> getMapStyle() {
		return mapStyle;
	}

	public void setMapStyle(Map<String, String> mapStyle) {
		this.mapStyle = mapStyle;
	}



}
