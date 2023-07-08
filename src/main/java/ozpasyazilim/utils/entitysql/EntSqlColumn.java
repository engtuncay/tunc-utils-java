package ozpasyazilim.utils.entitysql;

public class EntSqlColumn {

	String TABLE_CATALOG;
	String TABLE_SCHEMA;
	String TABLE_NAME;
	String COLUMN_NAME;
	String IS_NULLABLE;
	String DATA_TYPE;
//	String NUMERIC_PRECISION;
//	String NUMERIC_PRECISION_RADIX;
	String TX_KEY_TYPE;

	// Getter and Setters


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

	public String getCOLUMN_NAME() {
		return COLUMN_NAME;
	}

	public void setCOLUMN_NAME(String COLUMN_NAME) {
		this.COLUMN_NAME = COLUMN_NAME;
	}

	public String getIS_NULLABLE() {
		return IS_NULLABLE;
	}

	public void setIS_NULLABLE(String IS_NULLABLE) {
		this.IS_NULLABLE = IS_NULLABLE;
	}

	public String getDATA_TYPE() {
		return DATA_TYPE;
	}

	public void setDATA_TYPE(String DATA_TYPE) {
		this.DATA_TYPE = DATA_TYPE;
	}

	public String getTX_KEY_TYPE() {
		return TX_KEY_TYPE;
	}

	public void setTX_KEY_TYPE(String TX_KEY_TYPE) {
		this.TX_KEY_TYPE = TX_KEY_TYPE;
	}
}
