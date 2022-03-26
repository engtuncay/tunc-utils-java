package ozpasyazilim.utils.entitysql;

import ozpasyazilim.utils.fidbanno.FiTable;
import ozpasyazilim.utils.fidbanno.FiTransient;

@FiTable(name = "INFORMATION_SCHEMA.TABLES")
public class SqlTable {

	String TABLE_CATALOG;
	String TABLE_SCHEMA;
	String TABLE_NAME;
	String TABLE_TYPE;
	@FiTransient
	Boolean boSecim;
	@FiTransient
	Integer lnCount;

	public String getTABLE_CATALOG() {
		return TABLE_CATALOG;
	}

	public void setTABLE_CATALOG(String TABLE_CATALOG) {
		this.TABLE_CATALOG = TABLE_CATALOG;
	}

	public String getTABLE_SCHEMA() {
		return TABLE_SCHEMA;
	}

	public void setTABLE_SCHEMA(String TABLE_SCHEMA) {
		this.TABLE_SCHEMA = TABLE_SCHEMA;
	}

	public String getTABLE_NAME() {
		return TABLE_NAME;
	}

	public void setTABLE_NAME(String TABLE_NAME) {
		this.TABLE_NAME = TABLE_NAME;
	}

	public String getTABLE_TYPE() {
		return TABLE_TYPE;
	}

	public void setTABLE_TYPE(String TABLE_TYPE) {
		this.TABLE_TYPE = TABLE_TYPE;
	}

	public Boolean getBoSecim() {
		return boSecim;
	}

	public void setBoSecim(Boolean boSecim) {
		this.boSecim = boSecim;
	}

	public Integer getLnCount() {
		return lnCount;
	}

	public void setLnCount(Integer lnCount) {
		this.lnCount = lnCount;
	}

}
