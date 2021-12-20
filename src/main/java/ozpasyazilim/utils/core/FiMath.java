package ozpasyazilim.utils.core;

public class FiMath {

	public static Double dblSubtract(Double d1, Double d2, Double multiplier) {

		Double dd1 = d1 * multiplier;
		Integer intd1 = dd1.intValue();

		Double dd2 = d2 * multiplier;
		Integer intd2 = dd2.intValue();

		Double result = (Double) ((intd1 - intd2) / multiplier);
		// result = result / multiplier;
		return result;

	}

	public static Double dblAdd(Double d1, Double d2, Double multiplier) {

		Double dd1 = d1 * multiplier;
		Integer intd1 = dd1.intValue();

		Double dd2 = d2 * multiplier;
		Integer intd2 = dd2.intValue();

		Double result = (Double) ((intd1 + intd2) / multiplier);
		// result = result / multiplier;
		return result;

	}

}
