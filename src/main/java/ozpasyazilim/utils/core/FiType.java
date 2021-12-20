package ozpasyazilim.utils.core;

import javafx.scene.Node;
import ozpasyazilim.utils.returntypes.FnResult;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class FiType {

	// orEmpty vs getOrEmpty vs ifNullEmpty
	public static String orEmpty(String value) {
		if (value == null) return "";
		return value;
	}


	public static String ifNullThenPercentage(String value) {
		if (value == null) return "%";
		return "%" + value + "%";
	}

	/**
	 * Empty value = 0
	 *
	 * @param value
	 * @return
	 */
	public static Integer orEmpty(Integer value) {
		if (value == null) return 0;
		return value;
	}

	/**
	 * if Value is null , Then return elseValue
	 *
	 * @param value
	 * @param elseValue
	 * @return
	 */
	public static String ifNullThenElse(String value, String elseValue) {
		if (value == null) return elseValue;
		return value;
	}

	public static Integer ifNullThenElse(Integer value, Integer elseValue) {
		if (value == null) return elseValue;
		return value;
	}

	public static Date ifNullThenElse(Date value, Date elseValue) {
		if (value == null) return elseValue;
		return value;
	}


	/**
	 * use FiString
	 *
	 * @param s
	 * @return
	 */
	@Deprecated
	public static boolean checkEmptyorNull(final String s) {
		// Null-safe, short-circuit evaluation.
		return s == null || s.trim().isEmpty();
	}

	public static Boolean isTrue(FnResult resultDialog) {
		if (resultDialog == null || resultDialog.getBoResult() == null) return false;
		return resultDialog.getBoResult();
	}

	public static Boolean isTrue(Boolean value) {
		if (value == null) return false;
		return value;
	}

	public static <C> Optional<C> optional(C c) {
		return Optional.ofNullable(c);
	}

	/**
	 * Null veya
	 * <p>
	 * Trim yapıldıktan sonra boş mu ?
	 *
	 * @param strValue
	 * @return
	 */
	public static boolean isEmptyWithTrim(String strValue) {
		if (strValue == null || strValue.trim().equals("")) return true;
		return false;
	}

	public static boolean isEmpty(String strValue) {
		if (strValue == null || strValue.equals("")) return true;
		return false;
	}

	public static boolean isEmpty(Date dateValue) {
		if (dateValue == null) return true;
		return false;
	}

	public static boolean isEmpty(Integer intValue) {
		if (intValue == null) return true;
		return false;
	}

	public static Double orEmpty(Double dbValue) {
		if (dbValue == null) return 0d;
		return dbValue;
	}

	public static String ToStringOrEmpty(Object objValue) {
		if (objValue == null) return "";
		return objValue.toString();
	}

	public static String ifNullEmptyString(Integer value) {
		if (value == null) return "";
		return value.toString();
	}

	public static boolean isEmpty(List<String> list) {
		if (list == null || list.size() == 0) return true;
		return false;
	}


	public static void ifNullObjectElse(Boolean kkoBoOnay, boolean b) {

	}

	public static Boolean ifNullThenElse(Boolean boValue, Boolean elseValue) {
		if (boValue == null) return elseValue;
		return boValue;
	}

	public static boolean equalsInt(Integer value, Integer valueCheck) {
		if (value == null) return false;
		return value.equals(valueCheck);
	}

	public static boolean equals(Object value, Object valueCheck) {
		if (value == null) return false;
		return value.equals(valueCheck);
	}

	public static Double ifNullThenElse(Double dblValue1, Double dblValue2) {
		if (dblValue1 != null) return dblValue1;
		return dblValue2;
	}

	public static Double ifNullThenElse(Double dblValue1, Double dblValue2, Double dblValue3) {
		if (dblValue1 != null) return dblValue1;
		if (dblValue2 != null) return dblValue2;
		return dblValue3;
	}

	public static Double ifNullOrZeroThenElse(Double dblValue1, Double dblValue2, Double dblValue3) {
		if (dblValue1 != null && dblValue1 != 0d) return dblValue1;
		if (dblValue2 != null && dblValue2 != 0d) return dblValue2;
		return dblValue3;
	}

	public static Double ifNullOrZeroThenElseByThreshold(Double threshold, Double dblValue1, Double dblValue2, Double dblValue3) {
		if (dblValue1 != null && !isZero(dblValue1, threshold)) return dblValue1;
		if (dblValue2 != null && !isZero(dblValue2, threshold)) return dblValue2;
		return dblValue3;
	}

	public static boolean isZero(double value, double threshold) {
		return value >= -threshold && value <= threshold;
	}

	/**
	 * Biri dolu, bir boş mu kontrolü <br>
	 * Empty String null kabul edildi , yani boş kabul edildi.
	 *
	 * @param val1
	 * @param val2
	 * @return
	 */
	public static Boolean checkOneOfTwoEmpty(Object val1, Object val2) {

		if (val1 instanceof String) {
			if (FiString.isEmpty((String) val1)) {
				val1 = null;
			}
		}

		if (val2 instanceof String) {
			if (FiString.isEmpty((String) val2)) {
				val2 = null;
			}
		}

		if (val1 == null && val2 != null) return true;
		if (val2 == null && val1 != null) return true;
		return false;
	}

	public static <PrmEnt> PrmEnt orElse(PrmEnt entity, PrmEnt elseValue) {
		if (entity == null) return elseValue;
		return entity;
	}

	public static boolean isNullOfOneThem(Integer... lnVals) {
		for (Integer lnVal : lnVals) {
			if(lnVal==null){
				return true;
			}
		}
		return false;
	}

	public static boolean isNullSome(Object... lnVals) {
		for (Object lnVal : lnVals) {
			if(lnVal==null){
				return true;
			}
		}
		return false;
	}

	public static boolean isEmptyGen(Object value) {
		if(value==null) return true;

		if(value instanceof String){
			return FiString.isEmpty((String)value);
		}

		return false;
	}

	public static Boolean checkNull(Object... values) {

		for (Object value : values) {
			if(value==null){
				return true;
			}
		}

		return false;
	}

	public static String orEmpty(Node nodeComponent) {
		if(nodeComponent==null) return "null Node";
		return nodeComponent.getId();
	}
}
