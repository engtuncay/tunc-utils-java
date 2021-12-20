package ozpasyazilim.utils.gui.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ozpasyazilim.utils.datacontainers.OzTableColumn;
import ozpasyazilim.utils.functions.FourConsumer;
import ozpasyazilim.utils.functions.TriFunction;
import ozpasyazilim.utils.table.TableColStructure2;



/**
 * 
 * @author TUNC
 *
 * @param <E>
 */
public class TablemodelFunctional<E> extends AbstractTableModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    String specialfield1;
    List<E> listdata = new ArrayList<>();
    private Map<Integer, OzTableColumn> mapColsOrder;
    private Map<String, OzTableColumn> mapColsAlias;
    JTable tablecomponent;
    TriFunction<TablemodelFunctional<E>,Integer,Integer,Object> funcGetValue;
    FourConsumer<TablemodelFunctional<E>,Integer,Integer,Object> funcSetValue;


    private boolean editDefault = false;

    private TableColStructure2 tableColStructure;

    public void setData(List<E> listdata) {
	this.listdata = listdata;
	fireTableDataChanged();
    }


    public E getRowByModel(Integer index) {
	return getListdata().get(index);
    }

    /**
     * Tablodaki indexine göre list koleksiyondan satırı(item) verir. ( JTable set edilmiş olmalı)
     * 
     * @param indexintable
     * @return
     */
    public E getItemByTableIndex(Integer indexintable) {
	if ( getTablecomponent() == null) return null;
	Integer index = getTablecomponent().convertRowIndexToModel(indexintable);
	return getListdata().get(index);
    }

    // column methods

    @Override
    public int getColumnCount() {
	if(tableColStructure==null)return 0;
	return tableColStructure.getColCount();
    }

    @Override
    public String getColumnName(int colindex) {

	if (getTableColStructure() == null) return null;
	return getTableColStructure().getColNameByIndex(colindex);

    }

    @Override
    public boolean isCellEditable(int row, int col) {

	if(tableColStructure==null)return editDefault;

	return tableColStructure.getColEditable(col);

    }

    // row methods

    @Override
    public int getRowCount() {
	if (this.listdata == null) return 0;
	return this.listdata.size();
    }




    @Override
    public Class<?> getColumnClass(int columnIndex) {

	if(getTableColStructure()!=null) {

	    Class<?> classtype = getTableColStructure().getColClass(columnIndex);
	    //Loghelperr.getInstance(getClass()).debug("class"+ classtype.getTypeName() );
	    if(classtype!=null) return getTableColStructure().getColClass(columnIndex);

	}

	// sütunun tüm satırlarını tarar, bir değer bulursa onun class nı döndürür
	if (this.listdata != null) {
	    for (int i = 0; i < listdata.size(); i++) {
		Object p = getValueAt(i, columnIndex);
		if (p != null) {
		    return p.getClass();
		}
	    }
	}

	//	if(this.listdata.size()>0) {
	//	    Object o = getValueAt(0, columnIndex);
	//	    if (o == null) return Object.class;
	//	    return o.getClass();
	//	}

	return super.getColumnClass(columnIndex);

    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

	if(getFuncGetValue()==null)return null;
	return getFuncGetValue().apply(this,rowIndex, columnIndex);

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	if(getFuncSetValue()==null)return;
	getFuncSetValue().consume(this,rowIndex, columnIndex,aValue);
    }

    public List<E> getListdata() {
	return listdata;
    }

    public void addItem(E lastrow) {
	this.getListdata().add(lastrow);
	this.fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
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

    public void removeItem(E removeobject) {
	this.listdata.remove(removeobject);
	fireTableDataChanged();
    }

    public void removeItem(int index) {
	this.listdata.remove(index);
	fireTableRowsDeleted(index, index);
    }

    public void setListdata(List<E> listdata) {
	this.listdata = listdata;
	fireTableDataChanged();
    }

    public void setColStructure(TableColStructure2 tableColStructure) {
	this.tableColStructure = tableColStructure;
    }


    public List<E> getSelectedRows() {

	if(tablecomponent==null) return null;

	List<E> list = new ArrayList<>();
	int[] selectedRowsByTableIndex= tablecomponent.getSelectedRows();

	for (int i : selectedRowsByTableIndex) {
	    list.add(getItemByTableIndex(i));
	}

	return list;
    }


	public String getSpecialfield1() {
		return specialfield1;
	}


	public void setSpecialfield1(String specialfield1) {
		this.specialfield1 = specialfield1;
	}


	public Map<Integer, OzTableColumn> getMapColsOrder() {
		return mapColsOrder;
	}


	public void setMapColsOrder(Map<Integer, OzTableColumn> mapColsOrder) {
		this.mapColsOrder = mapColsOrder;
	}


	public JTable getTablecomponent() {
		return tablecomponent;
	}


	public void setTablecomponent(JTable tablecomponent) {
		this.tablecomponent = tablecomponent;
	}


	public TriFunction<TablemodelFunctional<E>, Integer, Integer, Object> getFuncGetValue() {
		return funcGetValue;
	}


	public void setFuncGetValue(TriFunction<TablemodelFunctional<E>, Integer, Integer, Object> funcGetValue) {
		this.funcGetValue = funcGetValue;
	}


	public FourConsumer<TablemodelFunctional<E>, Integer, Integer, Object> getFuncSetValue() {
		return funcSetValue;
	}


	public void setFuncSetValue(FourConsumer<TablemodelFunctional<E>, Integer, Integer, Object> funcSetValue) {
		this.funcSetValue = funcSetValue;
	}


	public boolean isEditDefault() {
		return editDefault;
	}


	public void setEditDefault(boolean editDefault) {
		this.editDefault = editDefault;
	}


	public TableColStructure2 getTableColStructure() {
		return tableColStructure;
	}


	public void setTableColStructure(TableColStructure2 tableColStructure) {
		this.tableColStructure = tableColStructure;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
