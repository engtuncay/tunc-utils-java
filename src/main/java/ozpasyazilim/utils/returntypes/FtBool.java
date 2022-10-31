package ozpasyazilim.utils.returntypes;

/**
 *
 * Ft : Fi Type
 *
 */
public class FtBool {
	Boolean value;
	Boolean boDefault;
	Integer lnCount;

	public FtBool() {
	}

	public FtBool(Boolean value) {
		setValue(value);
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}

	public Boolean getBoResultOrDefault(){
		if (getValue()==null) {
			return getBoDefault();
		}
		return getValue();
	}

	public Boolean getBoDefault() {
		return boDefault;
	}

	public Boolean getValueNotNull() {
		if(value==null) return false;
		return value;
	}

	public void setBoDefault(Boolean boDefault) {
		this.boDefault = boDefault;
	}

	/**
	 * Null + x = x
	 * <p>
	 * True + x = x
	 * <p>
	 * False + 0x = False
	 *
	 * @param boResult
	 */
	public void and(Boolean boResult) {

		if (getValue() == null) {
			setValue(boResult);
			return;
		}

		if (getValue()) {
			setValue(boResult);
		}

//		if(!getBoResult()){
//			return;
//		}

	}

	public Integer getLnCount() {
		return lnCount;
	}

	public Integer getIndexNotNull() {
		if(lnCount ==null) return 0;
		return lnCount;
	}

	public void setLnCount(Integer lnCount) {
		this.lnCount = lnCount;
	}

	public Integer addIndex(){
		setLnCount(getIndexNotNull()+1);
		return getLnCount();
	}

	/**
	 *
	 * Bir True ,sonucu True yapar
	 *
	 * @param boOrValue
	 */
	public void or(Boolean boOrValue) {
		if(getValue()==null){
			setValue(boOrValue);
			return;
		}

		// true ise atamaya yapmayacak
		if(getValue()==true) return;

		setValue(boOrValue);
	}
}
