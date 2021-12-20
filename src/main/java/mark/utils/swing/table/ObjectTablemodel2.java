package mark.utils.swing.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;

import mark.utils.collections.CollectionsUtilByMark;
import mark.utils.collections.filter.Filter;
import mark.utils.el.FieldDefResolver;
import mark.utils.el.annotation.AnnotationResolver;

/**
 * A TableModel based on reflection.   ( obj tbl model 2 - editable özelliği değiştirildi )
 * 
 * to : Formatter is closed by default. useFormmatter field should be true in the Fieldhander, Methodhandler classes. <br>
 * 
 *  	Example  <br>
 *  	String fields = "fieldname1:Colname1,fieldname2:ColName2";
 *		AnnotationResolver annotationResolver = new AnnotationResolver(EntDummy.class); 
 *		ObjectTablemodel<EntDummy> tblmodelDemo = new ObjectTablemodel<>(annotationResolver, fields);
 *
 * @author Marcos Vasconcelos
 */
public class ObjectTablemodel2<T> extends AbstractTableModel {
	private List<T> data = new ArrayList<>();
	private FieldDefResolver fieldsResolverArr[];
	private boolean editDefault = false;
	
	private Map<String, Boolean> colsEditableMap = new HashMap<>();
	private int extracolumn = 0;
	private String modelname;
	

	public ObjectTablemodel2(AnnotationResolver resolver, String cols) {
		// TODO : incele
		// data = new ArrayList<T>();
		// resolver.setFormatterEnabled(getfo);
		this.fieldsResolverArr = resolver.resolve(cols).clone();
	}

	public ObjectTablemodel2(FieldDefResolver fields[]) {
		data = new ArrayList<>();
		this.fieldsResolverArr = fields.clone();
	}

	public void setEditableDefault(boolean editable) {
		editDefault = editable;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		
		String colname = fieldsResolverArr[col].getFieldName();
		
		if (colsEditableMap.get(colname) != null) {
			return colsEditableMap.get(colname);
		}
		
		return editDefault;
	}

	public Map<String, Boolean> getColumnseditable() {
		return colsEditableMap;
	}

	@Override
	public int getColumnCount() {
		return fieldsResolverArr.length + extracolumn;
	}

	@Override
	public int getRowCount() {
		// Logmain.info("row count: "+data.size());
		return data.size();

	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		// < tabloya extra column eklenmesi için yapıldı
		if (arg1 >= fieldsResolverArr.length) {
			return "" + arg0;
		}
		// > extra col

		try {
			Object obj = data.get(arg0);
			return fieldsResolverArr[arg1].getValue(obj);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void setValueAt(Object value, int arg0, int arg1) {
		if (arg1 >= fieldsResolverArr.length) {
			return;
		}
		try {
			Object obj = data.get(arg0);
			fieldsResolverArr[arg1].setValue(obj, value);
			fireTableCellUpdated(arg0, arg1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public T getValue(int modelindex) {
		return data.get(modelindex);
	}
	
	public T getRow(int modelindex) {
		return data.get(modelindex);
	}

	@Override
	public String getColumnName(int row) {
		// tabloya extra column eklenmişse hata vermemsi için eklendi - harici sütun
		if (row >= fieldsResolverArr.length) {
			return "";
		}
		
		return fieldsResolverArr[row].getName();
	}

	public void add(T obj) {
		data.add(obj);
		fireTableDataChanged();
	}

	public void clean() {
		data = new ArrayList<>();
		fireTableDataChanged();
	}

	public void setData(List<T> data) {
		// Loghelper.getInstance(this.getClass()).info("setdate objectmodel:size:" + data.size());
		if(data==null)data= new ArrayList<>();
		this.data = data;
		fireTableDataChanged();
	}

	public void remove(int row) {
		data.remove(row);
		fireTableDataChanged();
	}

	public List<T> getData() {
		return new ArrayList<>(data);
	}

	public void remove(int idx[]) {
		for (int i : idx) {
			remove(i);
		}
	}

	public void remove(List<T> objs) {
		for (T t : objs) {
			remove(indexOf(t));
		}
	}

	public void remove(T obj) {
		remove(indexOf(obj));
	}

	public void addAll(Collection<T> coll) {
		for (T t : coll) {
			add(t);
		}
	}

	public List<T> getList(int idx[]) {
		List<T> list = new ArrayList<>();
		int size = idx.length;
		for (int j = 0; j < size; j++) {
			list.add(getValue(idx[j]));
		}

		return list;
	}

	public int indexOf(T obj) {
		return data.indexOf(obj);
	}

	private int indexOf(int col, Filter<T> filter) {
		return CollectionsUtilByMark.firstIndexOf(data, filter);
	}

	private int[] indexesOf(int col, Filter<T> filter) {
		Integer[] indexes = CollectionsUtilByMark.allMatchIndex(data, filter);
		int[] result = new int[indexes.length];
		for (int i = 0; i < indexes.length; i++) {
			result[i] = indexes[i];
		}
		return result;
	}

	public FieldDefResolver getColumnResolver(int colIndex) {
		return fieldsResolverArr[colIndex];
	}

	// to ekledi
	@Override
	public Class getColumnClass(int columnIndex) {
		Object o = getValueAt(0, columnIndex);
		if (o == null) {
			return fieldsResolverArr[columnIndex].getFieldType();
		}
		return o.getClass();
	}

	public int getExtracolumn() {
		return extracolumn;
	}

	public void setExtracolumn(int extracolumn) {
		this.extracolumn = extracolumn;
	}

	public String getModelname() {
		return modelname;
	}

	public void setModelname(String modelname) {
		this.modelname = modelname;
	}

	public Map<String, Boolean> getColsEditableMap() {
		return colsEditableMap;
	}

	public void setColsEditableMap(Map<String, Boolean> colsEditableMap) {
		this.colsEditableMap = colsEditableMap;
	}
	
}