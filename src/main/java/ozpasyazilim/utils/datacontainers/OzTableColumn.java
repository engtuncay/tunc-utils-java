package ozpasyazilim.utils.datacontainers;

import ozpasyazilim.utils.datatypes.EmDataType;
import ozpasyazilim.utils.datatypes.EnmColAlign;

public class OzTableColumn {

    String colName;
    String colId;
    Integer length;
    Integer colOrder;
    Boolean editable = false;
    EnmColAlign colalign;
    EmDataType coltype;
    Class<?> classtype;

    public OzTableColumn() {

    }

    public OzTableColumn(String name) {
	this.colName = name;
    }

    public OzTableColumn(String name, Integer ordernum) {
	this.colName = name;
	this.colOrder = ordernum;
    }

    public OzTableColumn(String name, Integer colOrder, Integer length) {
	this.colName = name;
	this.colOrder = colOrder;
	this.length = length;
    }

    public OzTableColumn(Integer length, String name) {
	this.colName = name;
	this.length = length;
    }

    public OzTableColumn(String name, Integer colOrder, Boolean biteditable) {
	this.colName = name;
	this.colOrder = colOrder;
	this.editable = biteditable;
    }

    public OzTableColumn(String name, Integer colOrder, Integer length, Boolean biteditable) {
	this.colName = name;
	this.colOrder = colOrder;
	this.editable = biteditable;
	this.length = length;
    }

    public OzTableColumn(String name, EnmColAlign enumColType) {
	this.colName = name;
	this.colalign = enumColType;
    }

    public OzTableColumn(Integer collength, String colname, EmDataType emType) {
	this.colName = colname;
	this.length = collength;
	this.coltype = emType;
    }

    public OzTableColumn(Integer collength, String colname, Class classType) {
	this.colName = colname;
	this.length = collength;
	this.classtype = classType;
    }

    public String getName() {
	return colName;
    }

    public void setName(String name) {
	this.colName = name;
    }

    public Integer getLength() {
	return length;
    }

    public void setLength(Integer length) {
	this.length = length;
    }

    public Integer getColOrder() {
	return colOrder;
    }

    public void setColOrder(Integer colOrder) {
	this.colOrder = colOrder;
    }

    public Boolean getEditable() {
	return editable;
    }

    public void setEditable(Boolean editable) {
	this.editable = editable;
    }

    public String getAlias() {
	return colId;
    }

    public void setAlias(String alias) {
	this.colId = alias;
    }

    public EnmColAlign getColtype() {
	return colalign;
    }

    public void setColtype(EnmColAlign coltype) {
	this.colalign = coltype;
    }

    public void setColtype(EmDataType coltype) {
	this.coltype = coltype;
    }

    public Class getClasstype() {
	return classtype;
    }

    public void setClasstype(Class classtype) {
	this.classtype = classtype;
    }

}
