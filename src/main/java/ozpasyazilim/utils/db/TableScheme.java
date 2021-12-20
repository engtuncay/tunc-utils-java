package ozpasyazilim.utils.db;

public class TableScheme {

	String DATA_TYPE;
	String COLUMN_NAME;
	String IS_NULLABLE;
	String IsPartOfPrimaryKey;
	String CHARACTER_MAXIMUM_LENGTH;
	String NUMERIC_PRECISION;
	String NUMERIC_SCALE;

	public String getDATA_TYPE() {
		return DATA_TYPE;
	}

	public void setDATA_TYPE(String DATA_TYPE) {
		this.DATA_TYPE = DATA_TYPE;
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

	public String getIsPartOfPrimaryKey() {
		return IsPartOfPrimaryKey;
	}

	public void setIsPartOfPrimaryKey(String isPartOfPrimaryKey) {
		IsPartOfPrimaryKey = isPartOfPrimaryKey;
	}

	public String getCHARACTER_MAXIMUM_LENGTH() {
		return CHARACTER_MAXIMUM_LENGTH;
	}

	public void setCHARACTER_MAXIMUM_LENGTH(String CHARACTER_MAXIMUM_LENGTH) {
		this.CHARACTER_MAXIMUM_LENGTH = CHARACTER_MAXIMUM_LENGTH;
	}

	public String getNUMERIC_PRECISION() {
		return NUMERIC_PRECISION;
	}

	public void setNUMERIC_PRECISION(String NUMERIC_PRECISION) {
		this.NUMERIC_PRECISION = NUMERIC_PRECISION;
	}

	public String getNUMERIC_SCALE() {
		return NUMERIC_SCALE;
	}

	public void setNUMERIC_SCALE(String NUMERIC_SCALE) {
		this.NUMERIC_SCALE = NUMERIC_SCALE;
	}
}
