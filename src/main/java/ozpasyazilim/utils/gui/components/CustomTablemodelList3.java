package ozpasyazilim.utils.gui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import ozpasyazilim.utils.gui.utils.CollectionsUtil;
import ozpasyazilim.utils.gui.utils.Filter;

public abstract class CustomTablemodelList3<T> extends AbstractTableModel {
    List<T> data = new ArrayList<T>();
    String[] columnNames;
    private Map<Integer, Boolean> columnseditable = new HashMap<Integer, Boolean>();
    private boolean editDefault = false;

    public void setData(List<T> listdata) {
	this.data = listdata;
	fireTableDataChanged();
    }

    public void setColumnNames(String[] columnNames) {
	this.columnNames = columnNames;
    }

    public String getColumnName(int colindex) {
	return this.columnNames == null ? null : this.columnNames[colindex];
    }

    public int getRowCount() {
	if (this.data == null) return 0;
	return this.data.size();
    }

    public int getColumnCount() {
	return this.columnNames == null ? 0 : this.columnNames.length;
    }

    public Class getColumnClass(int columnIndex) {
	Object o = getValueAt(0, columnIndex);
	if (o == null) {
	    return Object.class;
	}
	return o.getClass();
    }

    // listten get etmek için
    @Override
    public abstract Object getValueAt(int rowIndex, int columnIndex);

    // liste set edilmeli , getvalue ile yapısı aynı olmalı
    @Override
    public abstract void setValueAt(Object aValue, int rowIndex, int columnIndex);

    public T getListitem(int rowIndex) {
	return data.get(rowIndex);
    }

    public List<T> getListdata() {
	return data;
    }

    public List<T> getDataCloned() {
	return new ArrayList<T>(data);
    }

    public boolean isCellEditable(int i, int k) {
	if (columnseditable.get(k) != null) return columnseditable.get(k);
	return editDefault;
    }

    public void clean() {
	data = new ArrayList<T>();
	fireTableDataChanged();
    }

    public void remove(int row) {
	data.remove(row);
	fireTableDataChanged();
    }

    public void remove(int idx[]) {
	for (int i : idx)
	    remove(i);
    }

    public void remove(List<T> objs) {
	for (T t : objs)
	    remove(indexOf(t));
    }

    public void remove(T obj) {
	remove(indexOf(obj));
    }

    public void addAll(Collection<T> coll) {
	for (T t : coll)
	    add(t);
    }

    public void add(T obj) {
	data.add(obj);
	fireTableDataChanged();
    }

    public List<T> getList(int idx[]) {
	List<T> list = new ArrayList<T>();
	int size = idx.length;
	for (int j = 0; j < size; j++)
	    list.add(getListitem(idx[j]));

	return list;
    }

    public int indexOf(T obj) {
	return data.indexOf(obj);
    }

    // XIM ?
    private int indexOf(int col, Filter<T> filter) {
	return CollectionsUtil.firstIndexOf(data, filter);
    }

    // XIM ?
    private int[] indexesOf(int col, Filter<T> filter) {
	Integer[] indexes = CollectionsUtil.allMatchIndex(data, filter);
	int[] result = new int[indexes.length];
	for (int i = 0; i < indexes.length; i++)
	    result[i] = indexes[i];
	return result;
    }

}
