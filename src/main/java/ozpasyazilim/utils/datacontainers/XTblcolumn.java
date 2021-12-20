package ozpasyazilim.utils.datacontainers;

import ozpasyazilim.utils.datatypes.EnmColAlign;

public class XTblcolumn {
	String name;
	String alias;
	Integer length;
	Integer colOrder;
	Boolean editable = false;
	EnmColAlign coltype;

	public XTblcolumn(String name) {
		this.name = name;
	}

	public XTblcolumn(String name, Integer ordernum) {
		this.name = name;
		this.colOrder = ordernum;
	}

	public XTblcolumn(String name, Integer colOrder, Integer length) {
		this.name = name;
		this.colOrder = colOrder;
		this.length = length;
	}

	public XTblcolumn(String name, Integer colOrder, Boolean biteditable) {
		this.name = name;
		this.colOrder = colOrder;
		this.editable = biteditable;
	}

	public XTblcolumn(String name, Integer colOrder, Integer length, Boolean biteditable) {
		this.name = name;
		this.colOrder = colOrder;
		this.editable = biteditable;
		this.length = length;
	}

	public XTblcolumn(String name, EnmColAlign enumColType) {
		this.name = name;
		this.coltype = enumColType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public EnmColAlign getColtype() {
		return coltype;
	}

	public void setColtype(EnmColAlign coltype) {
		this.coltype = coltype;
	}

}
