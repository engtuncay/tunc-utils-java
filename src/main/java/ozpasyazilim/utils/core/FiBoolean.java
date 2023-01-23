package ozpasyazilim.utils.core;

public class FiBoolean {

	public static boolean isTrue(Boolean boolVar) {
		if (boolVar == null) return false;
		return boolVar;
	}

	public static boolean isFalse(Boolean boValue) {
		if (boValue == null) return false;
		return !boValue; // false ise true , true ise false gönderir
	}

	public static Boolean convertBoolean(String txValue) {
		if (txValue == null) return null;

		// false değerler
		if (txValue.equalsIgnoreCase("false")) return false;
		if (txValue.equalsIgnoreCase("0")) return false;
		if (txValue.equalsIgnoreCase("x")) return false;
		if (txValue.equalsIgnoreCase("-")) return false;

		// true değerler
		if (txValue.equalsIgnoreCase("true")) return true;
		if (txValue.equalsIgnoreCase("1")) return true;
		if (txValue.equalsIgnoreCase("ok")) return true;
		if (txValue.equalsIgnoreCase("+")) return true;

		return null;
	}

	public static Boolean convertBooleanElseValue(Object objValue, Boolean elseValue) {
		if (objValue == null) return elseValue;

		if (objValue instanceof Boolean) return (Boolean) objValue;

		String txValue = objValue.toString();
		// false değerler
		if (txValue.equalsIgnoreCase("false")) return false;
		if (txValue.equalsIgnoreCase("0")) return false;
		if (txValue.equalsIgnoreCase("x")) return false;
		if (txValue.equalsIgnoreCase("-")) return false;
		// true değerler
		if (txValue.equalsIgnoreCase("true")) return true;
		if (txValue.equalsIgnoreCase("1")) return true;
		if (txValue.equalsIgnoreCase("ok")) return true;
		if (txValue.equalsIgnoreCase("+")) return true;
		return elseValue;

	}

	public static boolean isFalse(String txValue) {
		if (txValue == null) return false;
		if (txValue.equalsIgnoreCase("false")) return true;
		if (txValue.equalsIgnoreCase("0")) return true;
		return false;
	}

	public static boolean isTrue(String txValue) {
		if (txValue == null) return false;
		if (txValue.equalsIgnoreCase("true")) return true;
		if (txValue.equalsIgnoreCase("1")) return true;
		return false;
	}

	public static boolean isNullOrFalse(Boolean boolVar) {
		if (boolVar == null || boolVar == false) return true;
		return false;
	}

	public static boolean isNullOrTrue(Boolean boolVar) {
		if (boolVar == null) return true;
		return boolVar;
	}

	public static Boolean convertDbResultGteZero(Integer result) {
		if (result == null) return false;
		if (result >= 0) return true;
		return false;
	}


	public static Boolean convertBooleanElseFalse(Object objCellValue) {

		if (objCellValue == null) return false;

		Boolean boolResult = false;
		try {
			boolResult = (Boolean) objCellValue;
			return boolResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boolResult;
	}

	public static boolean isNotTrue(Boolean value) {
		if (value == null) return true;
		return !value;

	}

	public static Boolean toggle(Boolean item) {
		if (item == null) return true;
		return !item;
	}

	public static Boolean toggleObj(Object item) {
		if (item == null) return true;
		Boolean itemm = (Boolean) item;
		return !itemm;
	}

	public static boolean getBoolean(Object item) {
		return (boolean) item;
	}

	public static boolean getBooleanIfNullFalse(Object item) {
		if (item == null) return false;
		return (boolean) item;
	}

	public static Boolean or(Boolean boValue, Boolean orValue) {
		if (boValue == null) return orValue;
		return boValue;
	}

	public static String convertBooleanToStr(Boolean boKilitli) {
		if (boKilitli == null) return "NULL";
		if (boKilitli) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * Null ise değer , orValue değerini döner
	 *
	 * @param boValue
	 * @param orValue
	 * @return
	 */
	public static Boolean isNullOr(Boolean boValue, boolean orValue) {
		if(boValue==null)return orValue;
		return boValue;
	}
}
