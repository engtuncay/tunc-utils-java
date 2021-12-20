package ozpasyazilim.utils.gui.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ozpasyazilim.utils.datacontainers.OzTableColumn;

public abstract class CustomTablemodelList4<E> extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String specialfield1;
	private List<E> listdata = new ArrayList<>();
	private Map<Integer, OzTableColumn> mapColsOrder;
	private Map<String, OzTableColumn> mapColsAlias;
	private JTable tablecomp;

	private boolean editDefault = false;

	public void setData(List<E> listdata) {
		this.listdata = listdata;
		fireTableDataChanged();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		if (mapColsOrder.get(col) != null) {
			return mapColsOrder.get(col).getEditable();
		}
		return editDefault;
	}

	public E getItem(Integer indexbyModel) {
		return getListdata().get(indexbyModel);
	}

	/**
	 * Tablodaki indexine göre list koleksiyondan item ı verir
	 * 
	 * @param indexintable
	 * @return
	 */
	public E getItemfromTableIndex(Integer indexintable) {
		if (tablecomp == null) return null;
		Integer index = tablecomp.convertRowIndexToModel(indexintable);
		return getListdata().get(index);
	}

	@Override
	public String getColumnName(int colindex) {
		// Logmain.info("colindex:" + colindex);
		// Logmain.info("mapColsOrder.get(colindex) isnull:"
		// + (getMapColsOrder().get(colindex) == null));

		if (getMapColsOrder() == null) {
			return null;
		}
		if (getMapColsOrder().get(colindex) == null) {
			return null;
		}
		return getMapColsOrder().get(colindex).getName();
	}

	@Override
	public int getRowCount() {
		if (this.listdata == null) {
			return 0;
		}
		return this.listdata.size();

	}

	public void setColumnNamesMap(Map<Integer, OzTableColumn> mapColOrder) {
		this.mapColsOrder = mapColOrder;
	}

	@Override
	public int getColumnCount() {
		return this.mapColsOrder == null ? 0 : this.mapColsOrder.size();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		// sütunun tüm satırlarını tarar, bir değer bulursa onun class nı döndürür
		if (this.listdata != null) {
			for (int i = 0; i < listdata.size(); i++) {
				Object p = getValueAt(i, columnIndex);
				if (p != null) {
					return p.getClass();
				}
			}
		}

		Object o = getValueAt(0, columnIndex);
		if (o == null) {
			return Object.class;
		}
		return o.getClass();

	}

	@Override
	public abstract Object getValueAt(int rowIndex, int columnIndex);

	public List<E> getListdata() {
		return listdata;
	}

	public void addItemAndFireInserted(E lastrow) {
		this.getListdata().add(lastrow);
		this.fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
	}
	
	public void addItem(E lastrow) {
		this.getListdata().add(lastrow);
	}

	public Map<String, OzTableColumn> getMapColsAlias() {
		return mapColsAlias;
	}

	public void setMapColsAlias(Map<String, OzTableColumn> mapcolsAlias) {
		this.mapColsAlias = mapcolsAlias;

		Map<Integer, OzTableColumn> mapcolsorder = new HashMap<>();

		for (String key : mapcolsAlias.keySet()) {

			OzTableColumn col = mapcolsAlias.get(key);
			col.setAlias(key);
			// colorder yoksa atla
			if (mapcolsAlias.get(key).getColOrder() == null) {
				// Loghelper.getInstance(this.getClass()).info("Sütun Sırası tanımlanmamış:" + key);
				continue;
			}
			mapcolsorder.put(col.getColOrder(), col);
		}

		this.mapColsOrder = mapcolsorder;

	}

	public Map<Integer, OzTableColumn> getMapColsOrder() {
		return mapColsOrder;
	}

	public void setMapColsOrder(Map<Integer, OzTableColumn> mapColsOrder) {
		this.mapColsOrder = mapColsOrder;
	}

	public void removeItem(E removeobject) {
		this.listdata.remove(removeobject);
		fireTableDataChanged();
	}

	public void removeItem(int modelindex) {
		this.listdata.remove(modelindex);
		//fireTableRowsDeleted(modelindex, modelindex);
	}
	
	public String getSpecfield1() {
		return specialfield1;
	}

	public void setSpecfield1(String specfield1) {
		this.specialfield1 = specfield1;
	}

	public void setListdata(List<E> listdata) {
		this.listdata = listdata;
		fireTableDataChanged();
	}

	public String getSpecialfield1() {
		return specialfield1;
	}

	public void setSpecialfield1(String specialfield1) {
		this.specialfield1 = specialfield1;
	}

	public JTable getTablecomp() {
		return tablecomp;
	}

	public void setTablecomp(JTable tablecomp) {
		this.tablecomp = tablecomp;
	}

}
