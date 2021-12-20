package ozpasyazilim.utils.gui.components;

import java.lang.reflect.Field;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class CustomTablemodelBinding extends AbstractTableModel {
	List<?> listdata;
	List<Customitem<String>> listcolumns;
	Class<?> dataclass;

	public Class<?> getDataclass() {
		return dataclass;
	}

	public void setDataclass(Class<?> dataclass) {
		this.dataclass = dataclass;
	}

	public void setData(List listdata) {
		this.listdata = listdata;
		fireTableDataChanged();
	}

	public String getColumnName(int column) {
		return this.listcolumns == null ? null : listcolumns.get(column).getDescription();
	}

	public int getRowCount() {
		return this.listdata == null ? 0 : this.listdata.size();
	}

	public int getColumnCount() {
		return this.listcolumns == null ? 0 : this.listcolumns.size();
	}

	public Class<?> getColumnClass(int columnIndex) {
		Object o = getValueAt(0, columnIndex);
		if (o == null) {
			return Object.class;
		}
		return o.getClass();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {

		Field field;
		try {
			field = getDataclass().getField(listcolumns.get(columnIndex).getValue());
			Object height = field.get(listdata.get(rowIndex));

		} catch (NoSuchFieldException e) {
			// Loghelper.getInstance(this.getClass()).info("Hata :" + UtilModel.exceptiontostring(e));
		} catch (SecurityException e) {
			// Loghelper.getInstance(this.getClass()).info("Hata :" + UtilModel.exceptiontostring(e));
		} catch (IllegalArgumentException | IllegalAccessException e) {

		}

		return null;
	};

	public List<?> getListdata() {
		return listdata;
	}

	public void setListdata(List<?> listdata) {
		this.listdata = listdata;
	}

	public List<Customitem<String>> getListcolumns() {
		return listcolumns;
	}

	public void setListcolumns(List<Customitem<String>> listcolumns) {
		this.listcolumns = listcolumns;
	}

}
