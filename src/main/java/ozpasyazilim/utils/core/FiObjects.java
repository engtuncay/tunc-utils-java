package ozpasyazilim.utils.core;

import ozpasyazilim.utils.log.Loghelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FiObjects {

	/**
	 * If both are null, result is false
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(Object a, Object b) {
		return (a!=null && (a == b)) || (a != null && a.equals(b));
	}
}
