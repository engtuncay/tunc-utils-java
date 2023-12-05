package ozpasyazilim.utils.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FiColList extends ArrayList<FiCol>{
	Map<String, FiCol> mapCols;

	public FiColList() {
	}

	public FiColList(Collection<? extends FiCol> c) {
		super(c);
	}

	public static FiColList build() {
		FiColList fiColList = new FiColList();
		return fiColList;
	}

	public FiColList addField(Object field) {
		FiCol fiTableCol = new FiCol(field.toString());
		add(fiTableCol);
		return this;
	}

	public FiColList addFieldManual(Object field, Object header) {
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
		getMapColsInit().put(fiTableCol.getFieldName(), fiTableCol);
		return super.add(fiTableCol);
	}

	// URFIX alan üzerinden degil, istenildiğinde oluşturulan bir map yapılmalı (alan kaldırılmalı)
	public Map<String, FiCol> getMapColsInit() {
		if (mapCols == null) {
			mapCols = new HashMap<>();
		}
		return mapCols;
	}

	public Map<String, FiCol> formMapCols() {
		Map<String, FiCol> mapCols = new HashMap<>();
		this.forEach(fiCol -> mapCols.put(fiCol.getFieldName(),fiCol));
		return mapCols;
	}

	public void setMapCols(Map<String, FiCol> mapCols) {
		this.mapCols = mapCols;
	}

//	public void equalsKey(String txKey, Object value) {
//
//		Map<String, FiCol> fiColMap = formMapCols();
//
//		if (fiColMap.containsKey(txKey)) {
//			FiCol fiCol = fiColMap.get(txKey);
//
//
//		}
//
//	}

//	public void addWitHeader(FiCol fiCol, String txHeaderName) {
//
//	}

}
