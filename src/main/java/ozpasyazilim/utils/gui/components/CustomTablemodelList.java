package ozpasyazilim.utils.gui.components;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class CustomTablemodelList<E> extends AbstractTableModel {
	public List<E> listdata;
	private String[] columnNames = null;

	public void setData(List listdata) {
		this.listdata = listdata;
		fireTableDataChanged();
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	@Override
	public String getColumnName(int column) {
		return this.columnNames == null ? null : this.columnNames[column];
	}

	@Override
	public int getRowCount() {
		return this.listdata == null ? 0 : this.listdata.size();
	}

	@Override
	public int getColumnCount() {
		return this.columnNames == null ? 0 : this.columnNames.length;
	}

	@Override
	public Class getColumnClass(int columnIndex) {

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
		if (listdata == null) {
			listdata = new ArrayList<>();
		}
		return listdata;
	}

	public void setListdata(List<E> listdata) {
		this.listdata = listdata;
	}

}
