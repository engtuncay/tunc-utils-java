package ozpasyazilim.utils.entitysql;

import ozpasyazilim.utils.fidbanno.FiTable;

@FiTable
public class SysObjects {

	String name;
	Integer id;
	String xtype;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getXtype() {
		return xtype;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
	}
}
