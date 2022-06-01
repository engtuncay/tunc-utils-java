package ozpasyazilim.utils.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FiColList extends ArrayList<FiCol>{
	Map<String, FiCol> mapCols;

	public static FiColList build() {
		FiColList fiColList = new FiColList();
		return fiColList;
	}

	public FiColList addField(Object field) {
		FiCol fiTableCol = new FiCol(field.toString());
		add(fiTableCol);
		return this;
	}

	public FiColList addFieldfh(Object field, Object header) {
		FiCol fiTableCol = new FiCol(field.toString(),header.toString());
		add(fiTableCol);
		return this;
	}

	public FiColList addFields(Object... field) {
		for (Object fieldName : field) {
			FiCol fiTableCol = new FiCol(fieldName.toString());
			add(fiTableCol);
		}
		return this;
	}

	public FiColList buildAdd(FiCol fiTableCol) {
		add(fiTableCol);
		return this;
	}

	public FiColList buildAdd(Object fieldName, String headerName) {
		if(fieldName==null) return this;
		FiCol fiTableCol = new FiCol(fieldName.toString(), headerName);
		add(fiTableCol);
		return this;
	}

	@Override
	public boolean add(FiCol fiTableCol) {
		getMapCols().put(fiTableCol.getFieldName(), fiTableCol);
		return super.add(fiTableCol);
	}

	public Map<String, FiCol> getMapCols() {
		if (mapCols == null) {
			mapCols = new HashMap<>();
		}
		return mapCols;
	}

	public void setMapCols(Map<String, FiCol> mapCols) {
		this.mapCols = mapCols;
	}
}
