package ozpasyazilim.utils.gui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public abstract class CustomTablemodelList2<E> extends AbstractTableModel {
	public List<E> listdata = new ArrayList<E>();
	public E totalrow;
	public Map<Integer, String> mapColumns;
	private Object[] longValues;

	public void setData(List<E> listdata) {
		this.listdata = listdata;
		fireTableDataChanged();
	}

	public void setColumnNames(Map<Integer, String> columnsRutaekleme) {
		this.mapColumns = columnsRutaekleme;
	}

	public String getColumnName(int colindex) {
		return this.mapColumns == null ? null : this.mapColumns.get(colindex);
	}

	public int getRowCount() {
		if (this.listdata == null) return 0;
		if (this.totalrow != null) return this.listdata.size(); // +1 'e gerek
																// yok her
																// nedense
																// Logmain.info(" "+ this.listdata.size());
		return this.listdata.size();

	}

	public int getColumnCount() {
		return this.mapColumns == null ? 0 : this.mapColumns.size();
	}

	public Class getColumnClass(int columnIndex) {
		Object o = getValueAt(0, columnIndex);
		if (o == null) {
			return Object.class;
		}
		return o.getClass();
	}

	@Override
	public abstract Object getValueAt(int rowIndex, int columnIndex);

	public Object[] getLongValues() {
		return longValues;
	}

	public void setLongValues(Object[] longValues) {
		this.longValues = longValues;
	}

	public List<E> getListdata() {
		return listdata;
	}

	public E getTotalrow() {
		return totalrow;
	}

	public void setTotalrow(E lastrow) {
		this.totalrow = lastrow;
	}

	public void addItem(E lastrow) {
		this.getListdata().add(lastrow);
		this.fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
	}

}
