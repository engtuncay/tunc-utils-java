package ozpasyazilim.utils.datacontainers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ozpasyazilim.utils.core.FiString;

public class DataTableOz {

	List<String> listcolname;
	List<List<String>> listrows;
	Map<String, Integer> mapColtoIndex;
	Map<Integer, String> mapIndextoCol;
	List<Map<String, String>> listMapColNametoVal;


	public List<List<String>> getListrows() {
		if (listrows == null) {
			listrows = new ArrayList<>();
		}
		return listrows;
	}

	public void setListrows(List<List<String>> listrows) {
		this.listrows = listrows;
	}

	public Map<String, Integer> getMapColtoIndex() {
		if (mapColtoIndex == null) {
			mapColtoIndex = new HashMap<>();
		}

		return mapColtoIndex;
	}

	public void setMapColtoIndex(Map<String, Integer> mapColtoIndex) {
		this.mapColtoIndex = mapColtoIndex;
	}

	public List<Map<String, String>> getListMapColNametoVal() {
		if (listMapColNametoVal == null) {
			listMapColNametoVal = new ArrayList<>();
		}
		return listMapColNametoVal;
	}

	public void setListMapColNametoVal(List<Map<String, String>> listMapColNametoVal) {
		this.listMapColNametoVal = listMapColNametoVal;
	}

	public Map<Integer, String> getMapIndextoCol() {

		if (mapIndextoCol == null) {
			mapIndextoCol = new HashMap<>();
		}

		return mapIndextoCol;
	}

	public void setMapIndextoCol(Map<Integer, String> mapIndextoCol) {
		this.mapIndextoCol = mapIndextoCol;
	}

	public List<String> getListcolname() {
		if (listcolname == null) {
			listcolname = new ArrayList<>();
		}
		return listcolname;
	}

	public void setListcolname(List<String> listcolname) {
		this.listcolname = listcolname;
	}

	public void generateMapsColumn() {

		Map<String, Integer> mapColtoIndex = new HashMap<>();
		Map<Integer, String> mapIndextoCol = new HashMap<>();

		Integer index = 0;
		// FIXME bütün elemanların dönmemiz lazım
		for (Iterator iterator = getListcolname().iterator(); iterator.hasNext();) {
			String colname = (String) iterator.next();
			mapColtoIndex.put(colname, index);
			mapIndextoCol.put(index, colname);
			index++;
		}

		setMapColtoIndex(mapColtoIndex);
		setMapIndextoCol(mapIndextoCol);

	}

	public Map<String, String> getRowMap(Integer index) {
		return getListMapColNametoVal().get(index);
	}

	public List<String> getRowList(Integer index) {
		return getListrows().get(index);
	}

	public String getColName(Integer index) {
		return getMapIndextoCol().get(index);
	}

	public Integer getColIndex(String colname) {
		return getMapColtoIndex().get(colname);
	}

	public static void printDataTable(DataTableOz dataTableOz) {

		// print column names

		for (Iterator iterator = dataTableOz.getListcolname().iterator(); iterator.hasNext();) {
			String colname = (String) iterator.next();
			System.out.print(colname + " - ");
		}

		System.out.println("");


		for (Iterator iterator = dataTableOz.getListMapColNametoVal().iterator(); iterator.hasNext();) {
			Map<String, String> mapcolnametoval = (Map<String, String>) iterator.next();

			for (Iterator iterator2 = dataTableOz.getListcolname().iterator(); iterator2.hasNext();) {
				String colname = (String) iterator2.next();
				String value = mapcolnametoval.get(colname);
				System.out.print(colname + ":" + value + " - ");
			}

			System.out.println("");


			// for (Entry<String, String> entry : mapcolnametoval.entrySet()) {
			// String key = entry.getKey();
			// String value = entry.getValue();
			//
			// System.out.print(key + ":" + value + " - ");
			// }
			// System.out.println("");


		}

	}

	public boolean containsCol(Integer colindex) {
		return getMapIndextoCol().containsKey(colindex);
	}

	public boolean containsCol(String colname) {
		return getMapColtoIndex().containsKey(colname);
	}

	public void generateMapColNametoVal() {

		List<Map<String, String>> listmapRows = new ArrayList<>();

		for (Iterator iterator = listrows.iterator(); iterator.hasNext();) {

			List<String> listrow = (List<String>) iterator.next();

			Map<String, String> maprow = new HashMap<>();
			for (Integer colindex = 0; colindex < listrow.size(); colindex++) {
				String celldataText = listrow.get(colindex);

				String colname = "col" + colindex.toString();
				if (containsCol(colindex)) {
					colname = getColName(colindex);
				}
				maprow.put(colname, celldataText);
			}
			listmapRows.add(maprow);

		}

		setListMapColNametoVal(listmapRows);

	}

	public static void printDataTableCols(DataTableOz myDataTable) {

		for (Iterator iterator = myDataTable.getListcolname().iterator(); iterator.hasNext();) {
			String colname = (String) iterator.next();
			String colnamevar = colname.replaceAll("\\s", "");
			colnamevar = colnamevar.replaceAll("\\.", "");
			colnamevar = colnamevar.replaceAll("\\\\", "");
			colnamevar = colnamevar.replaceAll("\\/", "");
			colnamevar = FiString.replaceTurkishCharacterstoLatin(colnamevar);
			System.out.println("String col" + colnamevar + "=\"" + colname + "\";");
		}


	}

	public static <E> List<E> bindEntity(DataTableOz datatable, Function<Map<String, String>, E> fnBindMaptoEntity) {

		List<E> list = new ArrayList<>();

		for (Iterator iterator = datatable.getListMapColNametoVal().iterator(); iterator.hasNext();) {

			Map<String, String> mapRow = (Map<String, String>) iterator.next();

			E e = fnBindMaptoEntity.apply(mapRow);

			if (e != null) list.add(e);

		}

		return list;
	}


}
